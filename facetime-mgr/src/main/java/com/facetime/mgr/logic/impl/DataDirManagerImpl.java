package com.facetime.mgr.logic.impl;

import com.facetime.mgr.common.BusnDataDir;
import com.facetime.mgr.domain.SysDatadir;
import com.facetime.mgr.logic.DataDirManager;
import com.facetime.spring.logic.LogicImpl;
import com.facetime.spring.support.Limitable.OrderBy;
import com.facetime.spring.support.LogicUtils;
import com.facetime.spring.support.PMLO;
import com.facetime.spring.support.QueryFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DataDirManagerImpl extends LogicImpl implements DataDirManager {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int addItem(SysDatadir dataDir) {
		int count = countByKey(dataDir.getParentid(), dataDir.getKey());
		if (count > 0) {
			return 0;
		}

		dataDir.setChildnum(0);
		dataDir.setOrder(getMaxOrderNum(dataDir.getParentid()));
		this.save(dataDir);

		// 更新父节点的孩子数
		refreshChildCount(dataDir.getParentid());
		return 1;
	}

	/*** {@inheritDoc} */
	@Override
	public int delAll(String[] itemIds) {
		int iCount = 0;
		for (String dirId : itemIds) {
			SysDatadir dataDir = this.findById(SysDatadir.class, dirId);
			String[] aryDelIds = StringUtils.split(queryIdList(dirId), ",");
			List<SysDatadir> list = this.findList(SysDatadir.class, LogicUtils.filterby("id", PMLO.IN, aryDelIds));
			iCount += list.size();
			this.delete(list);
			refreshChildCount(dataDir.getParentid());
		}
		return iCount;
	}

	@Override
	public SysDatadir findByPath(String path) {
		String[] keys = StringUtils.split(path, BusnDataDir.ITEM_SEPARATOR);
		SysDatadir dir = null;
		String parentId = BusnDataDir.TOP_PARENT_ID;
		String currentKey = null;
		for (String key : keys) {
			currentKey = key;
			dir = this.findUnique(SysDatadir.class, LogicUtils.filterby("parentid", parentId), LogicUtils.filterby("key", currentKey));
			if (dir == null) {
				return null;
			}
			parentId = dir.getId();
		}
		return dir;
	}

	@Override
	public Map<String, String> getChildValueMap(String path) {
		Map<String, String> childMap = new HashMap<String, String>();
		List<SysDatadir> childList = queryChildByPath(path);
		for (SysDatadir dir : childList) {
			childMap.put(dir.getValue(), dir.getNote());
		}
		return childMap;
	}

	/**
	 * 根据项目全路径识别项目并返回项目直属子节点XML格式信息
	 * 
	 * <pre>
	 * 例如： <?xml version=\"1.0\" encoding=\"UTF-8\" ?>
	 * <page> <ROWSET> <ROW> <NOTE>学生</NOTE> <VALUE>1</VALUE> <ID>1</ID>
	 * </ROW> <ROW> <NOTE>老师</NOTE> <VALUE>2</VALUE> <ID>2</ID> </ROW>
	 * </ROWSET> </page>
	 * </pre>
	 */
	@Override
	public String getChildXMLByPath(String path) {
		StringBuilder sbfXml = new StringBuilder();
		sbfXml.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><page><ROWSET>");
		List<SysDatadir> childList = queryChildByPath(path);
		for (SysDatadir dir : childList) {
			sbfXml.append("<ROW><NOTE>");
			sbfXml.append(StringEscapeUtils.escapeXml(dir.getNote()));
			sbfXml.append("</NOTE>");
			sbfXml.append("<VALUE>");
			if (dir.getValue() != null) {
				sbfXml.append(StringEscapeUtils.escapeXml(dir.getValue()));
			}
			sbfXml.append("</VALUE><ID>");
			sbfXml.append(dir.getId());
			sbfXml.append("</ID></ROW>");
		}
		sbfXml.append("</ROWSET></page>");
		return sbfXml.toString();
	}

	@Override
	public Map<String, String> getKeyNoteMap(String path) {
		HashMap<String, String> childMap = new HashMap<String, String>();
		List<SysDatadir> childList = queryChildByPath(path);
		for (SysDatadir dir : childList) {
			childMap.put(dir.getKey(), dir.getNote());
		}
		return childMap;
	}

	@Override
	public Map<String, String> getKeyValueMap(String path) {
		HashMap<String, String> childMap = new HashMap<String, String>();
		List<SysDatadir> childList = queryChildByPath(path);
		for (SysDatadir dir : childList) {
			childMap.put(dir.getKey(), dir.getValue());
		}
		return childMap;
	}

	@Override
	public String getNavigation(String itemId) {
		List<SysDatadir> dirList = queryFatherList(itemId);
		int size = dirList.size();
		if (size == 0) {
			return BusnDataDir.ROOT;
		}

		StringBuilder nagigation = new StringBuilder();
		for (int i = size - 1; i > 0; i--) {
			SysDatadir dataDir = dirList.get(i);
			nagigation.append("<a href=\"javascript:ComeIn('");
			nagigation.append(dataDir.getId());
			nagigation.append("')\">");
			nagigation.append(StringEscapeUtils.escapeHtml(dataDir.getNote()));
			nagigation.append("</a><b>-&gt;</b>");
		}
		SysDatadir dataDir = dirList.get(0);
		nagigation.append("<a href=\"javascript:ComeIn('");
		nagigation.append(dataDir.getId());
		nagigation.append("')\">");
		nagigation.append(StringEscapeUtils.escapeHtml(dataDir.getNote()));
		nagigation.append("</a>");
		return nagigation.toString();
	}

	@Override
	public String getOptionByPath(String path) {
		StringBuilder sbfXml = new StringBuilder();
		List<SysDatadir> childList = queryChildByPath(path);
		for (SysDatadir dir : childList) {
			sbfXml.append("<option ");
			sbfXml.append("value='");
			if (dir.getValue() != null) {
				sbfXml.append(StringEscapeUtils.escapeXml(dir.getValue()));
			}
			sbfXml.append("'>");
			sbfXml.append(StringEscapeUtils.escapeXml(dir.getNote()));
			sbfXml.append("</option>");
		}
		return sbfXml.toString();
	}

	@Override
	public String getParentId(String id) {
		SysDatadir dir = this.findById(SysDatadir.class, id);
		return dir != null ? dir.getParentid() : null;
	}

	@Override
	public String getPath(String itemId) {
		List<SysDatadir> dirList = queryFatherList(itemId);
		ArrayList<String> pathList = new ArrayList<String>();
		for (SysDatadir dir : dirList) {
			pathList.add(dir.getKey());
		}
		if (pathList.size() == 0) {
			return "";
		}

		StringBuilder sbfPath = new StringBuilder();
		for (int i = pathList.size() - 1; i > 0; i--) {
			sbfPath.append(pathList.get(i));
			sbfPath.append(BusnDataDir.ITEM_SEPARATOR);
		}
		sbfPath.append(pathList.get(0));
		return sbfPath.toString();
	}

	@Override
	public String getValueByPath(String path) {
		SysDatadir dir = findByPath(path);
		if (dir == null) {
			return null;
		}
		return dir.getValue();
	}

	@Override
	public List<SysDatadir> queryChildList(String parentId) {
		return this.findList(SysDatadir.class, new QueryFilter("parentid", parentId), OrderBy.asc("order"),
				LogicUtils.orderDesc("childnum"));
	}

	@Override
	public int reorderItems(String idList) {
		String[] childId = com.facetime.core.utils.StringUtils.toArray(idList, ",");
		if (childId == null) {
			return 0;
		}
		for (int i = 0; i < childId.length; i++) {
			SysDatadir dirinfo = this.findById(SysDatadir.class, childId[i]);
			dirinfo.setOrder(i + 1);
			this.update(dirinfo);
		}
		return childId.length;
	}

	/**
	 * @return -1:项目已经不存在 , 0：存在相同KEY的兄弟节点 , 1：保存成功
	 */
	@Override
	public int updateItem(SysDatadir dataDir) {
		SysDatadir oriDir = this.findById(SysDatadir.class, dataDir.getId());
		if (oriDir == null) {
			return -1;
		}
		if (!oriDir.getKey().equals(dataDir.getKey())) {
			if (countByKey(dataDir.getParentid(), dataDir.getKey()) > 0) {
				return 0;
			}
		}

		oriDir.setKey(dataDir.getKey());
		oriDir.setNote(dataDir.getNote());
		oriDir.setNoteEn(dataDir.getNoteEn());
		oriDir.setValue(dataDir.getValue());
		this.update(oriDir);
		return 1;
	}

	/**
	 * 计算同一父节点下具有相同key的节点数
	 * @parentId
	 * @key
	 */
	private int countByKey(String parentId, String key) {
		return (int) this.count(SysDatadir.class, new QueryFilter[] { new QueryFilter("parentid", parentId),
				new QueryFilter("key", key) });
	}

	/**
	 * 返回得子节点数目
	 */
	private int getChildCount(String parentId) {
		return (int) this.count(SysDatadir.class, new QueryFilter("parentid", parentId));
	}

	/**
	 * 取得父节点目前子节点最大的序号
	 * @parentId 父ID
	 */
	private int getMaxOrderNum(String parentId) {
		List<Integer> list = this.findHQL("select max(dir.order) FROM SysDatadir as dir WHERE dir.parentid=? ",
				new Object[] { parentId });
		if (list.isEmpty()) {
			return 0;
		}

		if (list.get(0) == null) {
			return 0;
		}
		return list.get(0).intValue();
	}

	/**
	 * 根据该项目的全路径识别该项目ID，并返回该项目下属子节点 如果该路径不存在，返回空的列表对象
	 */
	private List<SysDatadir> queryChildByPath(String path) {
		if (path.equals(BusnDataDir.ROOT)) {
			return queryChildList(BusnDataDir.TOP_PARENT_ID);
		}
		SysDatadir dir = findByPath(path);
		if (dir == null) {
			return new ArrayList<SysDatadir>(0);
		}
		return queryChildList(dir.getId());
	}

	/**
	 * 返回该节点及其所有父节点的集合
	 * @param itemId
	 * @return
	 */
	private List<SysDatadir> queryFatherList(String itemId) {
		List<SysDatadir> dirList = new ArrayList<SysDatadir>();
		SysDatadir dataDir = this.findById(SysDatadir.class, itemId);
		while (dataDir != null) {
			dirList.add(dataDir);
			if (dataDir.getParentid().equals(BusnDataDir.TOP_PARENT_ID)) {
				break;
			}
			dataDir = this.findById(SysDatadir.class, dataDir.getParentid());
		}
		return dirList;
	}

	/**
	 * 返回当前项目ID和下属所有子项目ID，ID之间用','分隔
	 * @itemId 要查询的项目ID
	 */
	private String queryIdList(String itemId) {
		StringBuilder sb = new StringBuilder();
		sb.append(itemId);
		List<SysDatadir> childList = queryChildList(itemId);
		for (SysDatadir dir : childList) {
			sb.append(",").append(queryIdList(dir.getId()));
		}
		return sb.toString();
	}

	/**
	 * 更新该项目父节点的childNum值
	 */
	private void refreshChildCount(String parentId) {
		SysDatadir parentDir = this.findById(SysDatadir.class, parentId);
		if (parentDir != null) {
			parentDir.setChildnum(getChildCount(parentId));
			this.update(parentDir);
		}
	}
}
