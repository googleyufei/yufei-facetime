package com.facetime.mgr.logic.impl;

import com.facetime.mgr.common.BusnDataDir;
import com.facetime.mgr.domain.OrgInfo;
import com.facetime.mgr.logic.OrgInfoManager;
import com.facetime.spring.logic.LogicImpl;
import com.facetime.spring.support.LogicUtils;
import com.facetime.spring.support.PMLO;
import com.facetime.spring.support.QueryFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrgInfoManagerImpl extends LogicImpl implements OrgInfoManager {

	@Override
	public List<OrgInfo> findByAreaid(String[] areaids) {
		return findList(OrgInfo.class, new QueryFilter("areaid", PMLO.IN, areaids));
	}

	/**
	 * 根据机构ID取得机构名称
	 */
	@Override
	public String findOrgname(String orgid) {
		List<OrgInfo> list = this.findList(OrgInfo.class, new QueryFilter("orgid", orgid));
		return list.size() > 0 ? list.get(0).getOrgname() : "";
	}

	/**
	 * 根据机构ID查找其父ID
	 */
	@Override
	public String findParentid(String orgid) {
		List<OrgInfo> list = this.findList(OrgInfo.class, new QueryFilter("orgid", orgid));
		return list.size() > 0 ? list.get(0).getParentid() : "";
	}

	/** {@inheritDoc} */
	@Override
	public String findParentName(String parentid) {
		String orgname = "";
		List<String> result = new ArrayList<String>();
		if (parentid.equals(BusnDataDir.TOP_PARENTID)) {
			result.add(findParentOrgName(BusnDataDir.TOP_PARENTID));
		} else {
			while (!parentid.equals(BusnDataDir.TOP_PARENTID)) {
				List<OrgInfo> list = this.findList(OrgInfo.class, new QueryFilter("orgid", parentid));
				for (OrgInfo info : list) {
					result.add(info.getOrgname());
				}
				parentid = findParentid(parentid);
			}
		}
		for (int i = result.size(); i > 0; i--) {
			if (i == result.size()) {
				orgname += result.get(i - 1).toString();
			} else {
				orgname += "->" + result.get(i - 1);
			}
		}
		return orgname;
	}

	@Override
	public String getAllChildOrgid(String orgid) {
		StringBuilder allOrgid = new StringBuilder();
		allOrgid.append(orgid);
		findChildOrgid(orgid, allOrgid);
		return allOrgid.toString();
	}

	@Override
	public List<OrgInfo> getAllChildren(String parentid) {
		String orgids = getAllChildOrgid(parentid);
		StringTokenizer st = new StringTokenizer(orgids, ",");
		List<OrgInfo> orgList = new ArrayList<OrgInfo>();
		while (st.hasMoreTokens()) {
			String orgid = st.nextToken();
			OrgInfo orgInfo = this.findById(OrgInfo.class, orgid);
			orgList.add(orgInfo);
		}
		return orgList;
	}

	@Override
	public int getChildLevel(OrgInfo orgInfo) {
		if (orgInfo.getParentid().equals(BusnDataDir.TOP_PARENTID)) {
			return 0;
		}
		List<OrgInfo> list = this.findList(OrgInfo.class, new QueryFilter("orgid", orgInfo.getParentid()));
		return list.size() > 0 ? list.get(0).getLevel() + 1 : 0;
	}

	@Override
	public int getChildNum(String parentid) {
		List<Integer> list = this.findPart(OrgInfo.class, new QueryFilter("orgid", parentid),
				new String[] { "childnum" });
		return list.size() > 0 ? list.get(0).intValue() : 0;
	}

	@Override
	public int getChildOrder(OrgInfo orgInfo) {
		List<Integer> list = this.findHQL("select max(info.order) from OrgInfo info where info.parentid=?",
				new Object[] { orgInfo.getParentid() });
		return list.size() > 0 ? list.get(0).intValue() + 1 : 1;
	}

	@Override
	public int getCountChild(String parentid) {
		return (int) this.count(OrgInfo.class, new QueryFilter("parentid", parentid));
	}

	@Override
	public List<OrgInfo> getListByHql(String orgid, String parentid) {
		return this.findList(OrgInfo.class,
				new QueryFilter[] { new QueryFilter("orgid", PMLO.IN, StringUtils.split(getAllChildOrgid(orgid), ",")),
						LogicUtils.filterby("parentid", parentid) }, LogicUtils.orderAsc("orgid"));
	}

	@Override
	public List<OrgInfo> getOrgInfoList(String parentid) {
		return this.findList(OrgInfo.class, new QueryFilter("parentid", parentid), LogicUtils.orderAsc("orgid"));
	}

	@Override
	public Map<String, String> getOrgInfoMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<OrgInfo> list = findList(OrgInfo.class);
		for (OrgInfo info : list) {
			map.put(info.getOrgid(), info.getOrgname());
		}
		return map;
	}

	/**
	 * 生成对应机构及下属机构的option选项的html代码
	 * @param orgId
	 * @return
	 */
	@Override
	public String getOrgSelTree(String orgId) {
		List<OrgInfo> list = null;
		if (orgId.equals(BusnDataDir.TOP_PARENTID)) {
			list = this.findList(OrgInfo.class, new QueryFilter("parentid", orgId), LogicUtils.orderAsc("order"));
		} else {
			list = this.findList(OrgInfo.class, new QueryFilter("orgid", orgId));
		}
		int level = 0;
		StringBuilder sbfXml = new StringBuilder();
		for (OrgInfo info : list) {
			sbfXml.append("<option ");
			sbfXml.append("value='");
			if (info.getOrgid() != null) {
				sbfXml.append(StringEscapeUtils.escapeXml(info.getOrgid()));
			}
			sbfXml.append("'>");
			sbfXml.append(info.getOrgname());
			sbfXml.append("</option>");
			if (info.getChildnum() > 0) {
				AppendChildSel(info.getOrgid(), sbfXml, level);
			}
		}
		return sbfXml.toString();
	}

	@Override
	public String getOrgSelTreeAll(String orgId) {
		List<OrgInfo> list = null;
		if (orgId.equals(BusnDataDir.TOP_PARENTID)) {
			list = this.findList(OrgInfo.class, new QueryFilter("parentid", orgId), LogicUtils.orderAsc("order"));
		} else {
			list = this.findList(OrgInfo.class, new QueryFilter("orgid", orgId));
		}
		int level = 0;
		StringBuilder sbfXml = new StringBuilder();
		for (OrgInfo info : list) {
			sbfXml.append("<option ");
			sbfXml.append("value='");
			if (info.getOrgid() != null) {
				sbfXml.append(StringEscapeUtils.escapeXml(info.getOrgid()));
			}
			sbfXml.append("'>");
			sbfXml.append(info.getOrgname());
			sbfXml.append("</option>");

			if (info.getChildnum() > 0) {
				AppendAllChildSel(info.getOrgid(), sbfXml, level);
			}
		}
		return sbfXml.toString();
	}

	@Override
	public String getUnderChildOrgid(String orgid) {
		List<String> list = this.findPart(OrgInfo.class, new QueryFilter("parentid", orgid), new String[] { "orgid" },
				LogicUtils.orderAsc("orgid"));
		String result = "";
		for (String fundOrgid : list) {
			result = result + fundOrgid + ",";
		}
		result = result.substring(result.length() - 1, result.length());
		return result;
	}

	/**
	 * 判断机构ID是否唯一
	 */
	@Override
	public boolean isOnlyOrgid(OrgInfo orgInfo) {
		List<OrgInfo> list = this.findList(OrgInfo.class, new QueryFilter("orgid", orgInfo.getOrgid()));
		return list.isEmpty();
	}

	/**
	 * 根据机构ID查询一个对象
	 */
	@Override
	public List<OrgInfo> querybyoneorgInfo(String orgid) {
		return this.findList(OrgInfo.class, new QueryFilter("orgid", orgid), LogicUtils.orderAsc("orgid"));
	}

	/**
	 * 更新父节点的子节点数
	 */
	@Override
	public void updateChildNum(String orgid, int i) {
		List<OrgInfo> list = querybyoneorgInfo(orgid);
		if (!list.isEmpty()) {
			OrgInfo info = list.get(0);
			if (i >= 0) {
				info.setChildnum(i);
			}
			getHibernateTemplate().update(info);
		} else {
			System.out.println("更新失败");
		}
	}

	private void AppendAllChildSel(String orgid, StringBuilder sbfXml, int level) {
		level++;
		List<OrgInfo> topList = getOrgInfoList(orgid);
		for (OrgInfo info : topList) {
			sbfXml.append("<option ");
			sbfXml.append("value='");
			sbfXml.append(StringEscapeUtils.escapeXml(info.getOrgid()));
			sbfXml.append("'>");
			sbfXml.append(StringUtils.repeat("&nbsp;&nbsp;", level));
			sbfXml.append("|--");
			sbfXml.append(info.getOrgname());
			sbfXml.append("</option>");

			if (info.getChildnum() > 0) {
				AppendAllChildSel(info.getOrgid(), sbfXml, level);
			}

		}
	}

	private void AppendChildSel(String orgid, StringBuilder sbfXml, int level) {
		level++;
		List<OrgInfo> topList = getOrgInfoList(orgid);
		for (OrgInfo info : topList) {
			sbfXml.append("<option ");
			sbfXml.append("value='");
			sbfXml.append(StringEscapeUtils.escapeXml(info.getOrgid()));
			sbfXml.append("'>");
			sbfXml.append(StringUtils.repeat("&nbsp;&nbsp;", level));
			sbfXml.append("|--");
			sbfXml.append(info.getOrgname());
			sbfXml.append("</option>");

			/*
			 * 根据袁爱红的要求,屏蔽掉分行级以下机构 by lichuang if (info.getChildnum() > 0)
			 * AppendAllChildSel(info.getOrgid(), sbfXml, level);
			 */
		}
	}

	private void findChildOrgid(String orgid, StringBuilder allOrgid) {
		// 找出orgid的下一级所有子机构信息
		List<OrgInfo> topList = getOrgInfoList(orgid);
		for (OrgInfo info : topList) {
			allOrgid.append(",");
			allOrgid.append(info.getOrgid());
			if (info.getChildnum() > 0) {
				findChildOrgid(info.getOrgid(), allOrgid);
			}
		}
	}

	/**
	 * 根据机构父ID取得机构名称
	 */
	private String findParentOrgName(String parentid) {
		List<OrgInfo> list = this.findList(OrgInfo.class, new QueryFilter("parentid", parentid));
		return list.size() > 0 ? list.get(0).getOrgname() : "";
	}

}
