package com.facetime.mgr.logic;

import com.facetime.mgr.bean.MenuFunctionForm;
import com.facetime.mgr.domain.MenuFunction;
import com.facetime.mgr.domain.MenuInfo;
import com.facetime.spring.logic.Logic;

import java.util.List;
import java.util.Map;

public interface MenuInfoManager extends Logic {

	/**
	 * @return 0:存在相同KEY的兄弟节点, 1：新增成功
	 */
	public int addItem(MenuInfo menuInfo);

	public int delAll(String[] ids);

	public MenuInfo findById(String id);

	public abstract List<MenuInfo> getAllMenuLeafInfo(String userid);

	public String getChildXMLByPath(String path);

	public String getMenuParentId(String menuName);

	public String getNavigation(String itemId);

	public String getParentId(String id);

	public List<MenuInfo> getParentMenu();

	// 业务操作
	public String getPath(String itemId);

	public List<MenuInfo> queryChildList(String parentId);

	public int queryChildnum(String parentId);

	public List<MenuFunctionForm> queryMenuOperate(String menuid);

	public int reorderItems(String idList);

	public int updateItem(MenuInfo menuInfo);

	public void updateMenuOper(String menuid, List<MenuFunction> menuFunLst);

	/**
	 * 返回菜单与功能点的中间实体 
	 * @param menuid 
	 * @param operid
	 * @return
	 */
	MenuFunction get(String menuid, String operid);

	/**
	 * @return 返回全部的菜单信息列表
	 */
	List<MenuInfo> getList();

	/**
	 * @return 返回menuid和parentId的Map
	 */
	Map<String, String> getMenuIdRelation();
}
