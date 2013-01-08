package com.facetime.mgr.logic;

import com.facetime.mgr.domain.SysDatadir;
import com.facetime.spring.logic.Logic;

import java.util.List;
import java.util.Map;

public interface DataDirManager extends Logic {

	/**
	 * @return 0:表示存在相同KEY的兄弟节点,  1：新增成功
	 */
	int addItem(SysDatadir dataDir);

	/**
	 * 删除项目
	 * @param itemIds 要删除的项目ID，不同ID用,分隔
	 * @return 删除的项目数
	 */
	int delAll(String[] itemIds);

	/**
	 * 根据路径返回项目信息
	 *
	 * @param path - 从父节点一直到该项目的关键字排列，项目关键字间用ITEM_SEPARATOR分隔
	 */
	SysDatadir findByPath(String path);

	/**
	 * 根据路径返回value note对照表 , 以value为Map的key, note为Map的值
	 * @param path
	 * @return
	 */
	Map<String, String> getChildValueMap(String path);

	String getChildXMLByPath(String path);

	/**
	 * 根据路径返回<key,note>对照表, 以key为Map的key, note为Map的值
	 * @param path
	 * @return
	 */
	Map<String, String> getKeyNoteMap(String path);

	/**
	 * 根据路径返回key value对照表,以key为Map的key, value为Map的值
	 * @param path
	 * @return
	 */
	Map<String, String> getKeyValueMap(String path);

	/**
	 * 返回数据字典菜单的导航条, 如'现金尾箱->调账结果'
	 * @param itemId
	 * @return
	 */
	String getNavigation(String itemId);

	/**
	 * 返回html页面中的select元素标签
	 * @param path
	 * @return
	 */
	String getOptionByPath(String path);

	String getParentId(String id);

	String getPath(String itemId);

	/**
	 * 根据路径返回该项目值
	 */
	String getValueByPath(String path);

	List<SysDatadir> queryChildList(String parentId);

	int reorderItems(String idList);

	int updateItem(SysDatadir dataDir);
}
