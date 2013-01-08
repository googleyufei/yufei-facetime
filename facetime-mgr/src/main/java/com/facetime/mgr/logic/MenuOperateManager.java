package com.facetime.mgr.logic;

import com.facetime.mgr.domain.MenuOperate;
import com.facetime.spring.logic.Logic;
import com.facetime.spring.support.Page;

import java.util.Map;

/**
 * 菜单功能点信息操作接口
 */
public interface MenuOperateManager extends Logic {

	/**
	 * 删除项目
	 *
	 * @param idList要删除的项目ID，不同ID用,分隔
	 * @return 删除的项目数
	 */
	void delAll(String idList);

	/**
	 * @return 返回以<operid,opername>为键值对的MAP
	 */
	Map<String, String> getMenuOperateMap();

	/**
	 * @return 生成操作功能点的下拉列表，在操作日志中用于查询
	 */
	String getMenuOperID();

	String getNameByID(String operid);

	/**
	 * 把功能按钮的id、name保存于Map中
	 */
	Map<String, String> getOperMap();

	Page<MenuOperate> getPage(int pageNum, int pageSize);
}
