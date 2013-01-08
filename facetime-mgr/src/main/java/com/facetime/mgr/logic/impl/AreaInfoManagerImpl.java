package com.facetime.mgr.logic.impl;

import com.facetime.mgr.common.BusnDataDir;
import com.facetime.mgr.domain.AreaInfo;
import com.facetime.mgr.domain.OrgInfo;
import com.facetime.mgr.logic.AreaInfoManager;
import com.facetime.mgr.logic.OrgInfoManager;
import com.facetime.spring.logic.LogicImpl;
import com.facetime.spring.support.LogicUtils;
import com.facetime.spring.support.PMLO;
import com.facetime.spring.support.QueryFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AreaInfoManagerImpl extends LogicImpl implements AreaInfoManager {

	@Override
	public int delMapByGeog(String[] areaid) {
		for (String element : areaid) {
			this.deleteById(AreaInfo.class, element);
		}
		return areaid.length;
	}

	/**
	 * 根据区域ID查找区域名称
	 */
	@Override
	public String findAreaName(String areaid) {
		List<AreaInfo> list = this.findList(AreaInfo.class, new QueryFilter("areaid", areaid));
		return list.size() > 0 ? list.get(0).getName() : "";
	}

	/**
	 * 根据区域ID查找其父ID
	 */
	@Override
	public String findParentid(String areaid) {
		List<AreaInfo> list = this.findList(AreaInfo.class, new QueryFilter("areaid", areaid));
		return list.size() > 0 ? list.get(0).getParentid() : "";
	}

	@Override
	public String findParentName(String parentid) {
		String name = "";
		List<String> list = new ArrayList<String>();
		if (parentid.equals(BusnDataDir.TOP_PARENTID)) {
			list.add(findParentidName(BusnDataDir.TOP_PARENTID));
		} else {
			while (!parentid.equals(BusnDataDir.TOP_PARENTID)) {
				list.add(findAreaName(parentid));
				parentid = findParentid(parentid);
			}
		}
		for (int i = list.size(); i > 0; i--) {
			if (i == list.size()) {
				name += list.get(i - 1).toString();
			} else {
				name += "->" + list.get(i - 1);
			}
		}
		return name;
	}

	@Override
	public String[] getAllChildGeogid(String geogid) {
		StringBuffer allGeogid = new StringBuffer();
		String geogids = geogid;
		geogids += findChildGeogid(geogid, allGeogid);
		return StringUtils.split(geogids, ",");
	}

	/**
	 * 同一父节点下的孩子节点的级别
	 */
	@Override
	public int getChildLevel(AreaInfo geogArea) {
		if (!geogArea.getParentid().equals(BusnDataDir.TOP_PARENTID)) {
			List<Integer> list = this.findPart(AreaInfo.class, new QueryFilter("areaid", geogArea.getParentid()),
					new String[] { "level" });
			return list.isEmpty() ? 0 : list.get(0).intValue() + 1;
		}
		return 0;
	}

	@Override
	public int getChildNum(String parentid) {
		List<Integer> list = this.findPart(AreaInfo.class, new QueryFilter("areaid", parentid),
				new String[] { "childnum" });
		return list.size() > 0 ? list.get(0).intValue() : 0;
	}

	@Override
	public int getChildOrder(AreaInfo geogArea) {
		String hql = "select max(area.order)  from GeogArea area where area.parentid=?";
		List<Integer> list = this.findHQL(hql, new Object[] { geogArea.getParentid() });
		return list.size() > 0 ? list.get(0).intValue() + 1 : 1;
	}

	@Override
	public List<AreaInfo> getGeogAreaList(String parentid) {
		return findList(AreaInfo.class, new QueryFilter("parentid", parentid), LogicUtils.orderby("areaid"));
	}

	@Override
	public Map<String, String> getGeogAreaMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<AreaInfo> list = findList(AreaInfo.class);
		for (AreaInfo area : list) {
			map.put(area.getAreaid(), area.getName());
		}
		return map;
	}

	@Override
	public String getGeogAreaTree(String parentid) throws DataAccessException {
		StringBuffer AllChildAreaNode = new StringBuffer();
		AreaInfo area = null;
		if (parentid.equals(BusnDataDir.TOP_PARENTID)) {
			area = this.findUnique(AreaInfo.class, new QueryFilter("parentid", parentid));
		} else {
			area = this.findUnique(AreaInfo.class, new QueryFilter("areaid", parentid));
		}
		AllChildAreaNode.append("<option value=" + area.getAreaid() + ">" + area.getName() + "</option>");
		parentid = area.getAreaid();
		return appendAllChild(parentid, AllChildAreaNode);
	}

	@Override
	public List<AreaInfo> getListByHql(String areaid, String parentid) {
		return findList(AreaInfo.class, new QueryFilter[] { new QueryFilter("parentid", parentid),
				new QueryFilter("areaid", PMLO.IN, getAllChildGeogid(areaid)) }, LogicUtils.orderAsc("areaid"));
	}

	// 判断机构下是否存在终端信息
	@Override
	public boolean isExitingOrg(String[] ids) {
		for (String id2 : ids) {
			String idArray[] = getAllChildGeogid(id2);
			List<OrgInfo> list = locate(OrgInfoManager.class).findByAreaid(idArray);
			if (list.size() > 0) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isOnlyAreaid(String areaid) {
		List<AreaInfo> list = findList(AreaInfo.class, new QueryFilter("areaid", areaid));
		return list.isEmpty();
	}

	@Override
	public List<AreaInfo> querybyoneGeogAreaList(String areaid) {
		return findList(AreaInfo.class, new QueryFilter("areaid", areaid), LogicUtils.orderAsc("areaid"));
	}

	@Override
	public void updateChildNum(String areaid, int i) {
		List<AreaInfo> list = querybyoneGeogAreaList(areaid);
		if (list.size() > 0) {
			AreaInfo area = list.get(0);
			if (i >= 0) {
				area.setChildnum(i);
			}
			this.update(area);
		}
	}

	private String appendAllChild(String orgid, StringBuffer AllChildAreaNode) {
		String space = "&nbsp;";
		List<AreaInfo> topList = getGeogAreaList(orgid);
		for (AreaInfo area : topList) {
			space = "&nbsp;";
			for (int i = 0; i < area.getLevel(); i++) {
				space += space;
			}
			space = space + "|--";
			AllChildAreaNode.append("<option value=" + area.getAreaid() + ">");
			AllChildAreaNode.append(space + area.getName());
			AllChildAreaNode.append("</option>");
			int num = area.getChildnum();
			orgid = area.getAreaid();
			if (num != 0) {
				appendAllChild(orgid, AllChildAreaNode);
			}
		}
		return AllChildAreaNode.toString();
	}

	private String findChildGeogid(String geogid, StringBuffer allGeogid) {
		String space = ",";
		List<AreaInfo> topList = getGeogAreaList(geogid);
		for (AreaInfo area : topList) {
			allGeogid.append(space);
			allGeogid.append(area.getAreaid());
			int num = area.getChildnum();
			geogid = area.getAreaid();
			if (num != 0) {
				findChildGeogid(geogid, allGeogid);
			}
		}
		return allGeogid.toString();
	}

	/**
	 * 根据区域父ID查找区域名称
	 */
	private String findParentidName(String parentid) {
		List<AreaInfo> list = this.findList(AreaInfo.class, new QueryFilter("parentid", parentid));
		return list.size() > 0 ? list.get(0).getName() : "";
	}
}
