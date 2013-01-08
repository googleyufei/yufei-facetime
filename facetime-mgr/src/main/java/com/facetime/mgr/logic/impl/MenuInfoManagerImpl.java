package com.facetime.mgr.logic.impl;

import com.facetime.mgr.bean.MenuFunctionForm;
import com.facetime.mgr.common.BusnDataDir;
import com.facetime.mgr.domain.MenuFunction;
import com.facetime.mgr.domain.MenuInfo;
import com.facetime.mgr.domain.MenuOperate;
import com.facetime.mgr.domain.UsrRoleFunction;
import com.facetime.mgr.logic.MenuInfoManager;
import com.facetime.spring.logic.LogicImpl;
import com.facetime.spring.support.LogicUtils;
import com.facetime.spring.support.PMLO;
import com.facetime.spring.support.QueryFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * 菜单管理操作实现
 */
@Service
@Transactional
public class MenuInfoManagerImpl extends LogicImpl implements MenuInfoManager {

	/** {@inheritDoc} */
	@Override
	public int addItem(MenuInfo menuInfo) {
		menuInfo.setChildnum(0);
		menuInfo.setOrder(getMaxOrderNum(menuInfo.getParentid()));
		this.save(menuInfo);

		// 更新父节点的孩子数
		refreshChildCount(menuInfo.getParentid());
		return 1;
	}

	/**
	 * 删除项目
	 * @param idList要删除的项目ID，不同ID用,分隔
	 * @return 删除的项目数
	 */
	@Override
	public int delAll(String[] ids) {
		int delnum = 0;
		for (String menuid : ids) {
			String[] chd = StringUtils.split(queryChildIdList(menuid), ",");

			// 更新父项目的孩子数
			MenuInfo menuParent = findById(findById(menuid).getParentid());
			if (menuParent != null) {
				menuParent.setChildnum(menuParent.getChildnum() - 1);
				this.update(menuParent);
			}

			delnum = chd.length;
			// 先删除对应的权限表USR_RULEFUNC,MENU_FUNCTION
			String[] funcids = findIdArray(MenuFunction.class, new QueryFilter("menuid", PMLO.IN, chd));
			if (funcids.length > 0)
				this.delete(UsrRoleFunction.class, new QueryFilter("funcid", PMLO.IN, funcids));
			this.delete(MenuFunction.class, new QueryFilter("menuid", PMLO.IN, chd));
			this.deleteByIds(MenuInfo.class, chd);
		}
		return delnum;
	}

	@Override
	public MenuInfo findById(String id) {
		return this.findById(MenuInfo.class, id);
	}

	/**
	 * 根据路径返回项目信息
	 * @path:从父节点一直到该项目的关键字排列，项目关键字间用ITEM_SEPARATOR分隔
	 */
	public MenuInfo findByPath(String path) {
		String[] keys = StringUtils.split(path, BusnDataDir.ITEM_SEPARATOR);
		String parentid = BusnDataDir.TOP_PARENT_ID;
		MenuInfo menuInfo = null;
		for (String key : keys) {
			menuInfo = this.findUnique(MenuInfo.class, new QueryFilter[] { new QueryFilter("parentid", parentid),
					new QueryFilter("key", key) });
			if (menuInfo == null) {
				return null;
			}
			parentid = menuInfo.getParentid();
		}
		return menuInfo;
	}

	@Override
	public MenuFunction get(String menuid, String operid) {
		return findUnique(MenuFunction.class, new QueryFilter[] { new QueryFilter("menuid", menuid),
				new QueryFilter("operid", operid) });
	}

	/**
	 * 取得该用户所拥有的菜单中所有叶子节点的信息
	 * @return List
	 */
	@Override
	public List<MenuInfo> getAllMenuLeafInfo(String userid) {
		StringBuilder hql = new StringBuilder();
		hql.append("FROM MenuInfo menuinfo WHERE");
		hql.append(" (menuinfo.menuid in");
		hql.append(" (select menufun.menuid from MenuFunction menufun where menufun.funcid in");
		hql.append(" (select rolefun.funcid from UsrRolefunc rolefun where rolefun.rolecode in");
		hql.append(" (select grprole.rolecode from UsrGrprole grprole where grprole.grpcode in");
		hql.append(" (select usrgrp.grpcode from UsrUsrgrp usrgrp where usrgrp.userid=?");
		hql.append(")))))");
		hql.append(" and menuinfo.childnum=?");
		return this.findHQL(hql.toString(), new Object[] { userid, "0" });
	}

	/**
	 * 取得子节点数目
	 */
	public int getChildCount(String parentId) {
		return (int) this.count(MenuInfo.class, new QueryFilter("parentid", parentId));
	}

	/**
	 * 根据项目全路径识别项目并返回项目直属子节点XML格式信息，例如： <?xml version=\"1.0\" encoding=\"UTF-8\" ?> <page> <ROWSET>
	 * <ROW> <NOTE>学生</NOTE> <VALUE>1</VALUE> <ID>1</ID> </ROW> <ROW> <NOTE>老师</NOTE>
	 * <VALUE>2</VALUE> <ID>2</ID> </ROW> </ROWSET> </page>
	 */
	@Override
	public String getChildXMLByPath(String path) {
		StringBuilder sbfXml = new StringBuilder();
		sbfXml.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><page><ROWSET>");
		List<MenuInfo> childList = queryChildByPath(path);
		for (MenuInfo menu : childList) {
			sbfXml.append("<ROW><NOTE>");
			sbfXml.append(StringEscapeUtils.escapeXml(menu.getMenuitem()));
			sbfXml.append("</NOTE>");
			sbfXml.append("<VALUE>");
			if (menu.getActionto() != null) {
				sbfXml.append(StringEscapeUtils.escapeXml(menu.getActionto()));
			}
			sbfXml.append("</VALUE><ID>");
			sbfXml.append(menu.getMenuid());
			sbfXml.append("</ID></ROW>");
		}
		sbfXml.append("</ROWSET></page>");
		return sbfXml.toString();
	}

	@Override
	public List<MenuInfo> getList() {
		return this.findList(MenuInfo.class);
	}

	@Override
	public Map<String, String> getMenuIdRelation() {
		List<Object[]> menuIdList = this.findPart(MenuInfo.class, new String[] { "menuid", "parentid" },
				LogicUtils.orderAsc("parentid"));
		Map<String, String> menuIdMap = new HashMap<String, String>();
		for (Object[] arry : menuIdList) {
			menuIdMap.put(arry[0].toString(), arry[1].toString());
		}
		return menuIdMap;
	}

	/**
	 * 按菜单名称查询到此菜单所在的最顶级父节点的ID
	 * @param menuName 菜单名称
	 * @return
	 */
	@Override
	public String getMenuParentId(String menuName) {
		MenuInfo dir = queryParentId(menuName);
		while (!dir.getParentid().equals("0")) {
			dir = this.findById(MenuInfo.class, dir.getParentid());
			System.out.println(dir.getMenuitem() + "  " + dir.getParentid());
		}
		return dir.getMenuid();
	}

	/**
	 * 返回网页导航条
	 */
	@Override
	public String getNavigation(String itemId) {
		ArrayList<MenuInfo> aryPath = new ArrayList<MenuInfo>();
		MenuInfo menuInfo = findById(itemId);
		while (menuInfo != null) {
			aryPath.add(menuInfo);
			if (menuInfo.getParentid().equals(BusnDataDir.TOP_PARENT_ID)) {
				break;
			}
			menuInfo = findById(menuInfo.getParentid());
		}
		int iSize = aryPath.size();
		if (iSize == 0) {
			return BusnDataDir.ROOT;
		}

		StringBuilder sbfPath = new StringBuilder();
		for (int i = iSize - 1; i > 0; i--) {
			menuInfo = aryPath.get(i);
			sbfPath.append("<a href=\"javascript:ComeIn('");
			sbfPath.append(menuInfo.getMenuid());
			sbfPath.append("')\">");
			sbfPath.append(StringEscapeUtils.escapeHtml(menuInfo.getMenuitem()));
			sbfPath.append("</a><b>-&gt;</b>");
		}
		menuInfo = aryPath.get(0);
		sbfPath.append("<a href=\"javascript:ComeIn('");
		sbfPath.append(menuInfo.getMenuid());
		sbfPath.append("')\">");
		sbfPath.append(StringEscapeUtils.escapeHtml(menuInfo.getMenuitem()));
		sbfPath.append("</a>");
		return sbfPath.toString();
	}

	@Override
	public String getParentId(String id) {
		MenuInfo dir = this.findById(id);
		if (dir != null) {
			return dir.getParentid();
		}
		return null;
	}

	/**
	 * 获得一级父节点信息
	 * @return
	 */
	@Override
	public List<MenuInfo> getParentMenu() {
		return findList(MenuInfo.class, new QueryFilter("floor", 1));
	}

	/**
	 * 返回该项目的关键字路径
	 */
	@Override
	public String getPath(String itemId) {
		ArrayList<String> aryPath = new ArrayList<String>();
		MenuInfo menuInfo = findById(itemId);
		while (menuInfo != null) {
			aryPath.add(menuInfo.getMenuid());
			if (menuInfo.getParentid().equals(BusnDataDir.TOP_PARENT_ID)) {
				break;
			}
			menuInfo = findById(menuInfo.getParentid());
		}

		int iSize = aryPath.size();
		if (iSize == 0) {
			return "";
		}

		StringBuilder sbfPath = new StringBuilder();
		for (int i = iSize - 1; i > 0; i--) {
			sbfPath.append(aryPath.get(i));
			sbfPath.append(BusnDataDir.ITEM_SEPARATOR);
		}
		sbfPath.append(aryPath.get(0));
		return sbfPath.toString();
	}

	/**
	 * 返回当前项目ID和下属所有子项目ID，ID之间用,分隔
	 * @itemId 要查询的项目ID
	 */
	public String queryChildIdList(String itemId) {
		StringBuilder sbfIds = new StringBuilder();
		sbfIds.append(itemId);
		List<MenuInfo> childList = queryChildList(itemId);
		for (Object element : childList) {
			MenuInfo menuInfo = (MenuInfo) element;
			sbfIds.append(",").append(queryChildIdList(menuInfo.getMenuid()));
		}
		return sbfIds.toString();
	}

	/**
	 * 返回父ID符合要求的直属子项目集合
	 * @parentId 父项目ID
	 */
	@Override
	public List<MenuInfo> queryChildList(String parentId) {
		return this.findList(MenuInfo.class, new QueryFilter("parentid", parentId), LogicUtils.orderAsc("order"));
	}

	@Override
	public int queryChildnum(String parentId) {
		return getChildCount(parentId);
	}

	/**
	 * 获取菜单功能点列表
	 */
	@Override
	public List<MenuFunctionForm> queryMenuOperate(String menuid) {
		List<MenuOperate> operateLst = this.findList(MenuOperate.class);
		List<MenuFunction> functionList = queryMenuFun(menuid);
		List<MenuFunctionForm> menuFunFormLst = new ArrayList<MenuFunctionForm>();
		for (MenuOperate group : operateLst) {
			MenuFunctionForm form = new MenuFunctionForm();
			// 给form的rolename,rolecode赋值
			try {
				BeanUtils.copyProperties(form, group);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 给form的isCheck,menuid赋值

			for (MenuFunction menuOper : functionList) {
				if (menuOper.getOperid().equals(form.getOperid())) {
					form.setChecked("checked");
				}
			}

			form.setMenuid(menuid);
			menuFunFormLst.add(form);
		}

		return menuFunFormLst;
	}

	@Override
	public int reorderItems(String idList) {
		String[] childId = com.facetime.core.utils.StringUtils.toArray(idList, ",");
		if (childId != null) {
			for (int i = 0; i < childId.length; i++) {
				MenuInfo menuinfo = findById(childId[i]);
				menuinfo.setOrder(i + 1);
				this.update(menuinfo);
			}
			return childId.length;
		} else {
			return 0;
		}
	}

	/**
	 * @return -1:项目已经不存在 0：存在相同KEY的兄弟节点 1：保存成功
	 */
	@Override
	public int updateItem(MenuInfo menuInfo) {
		MenuInfo ordInfo = findById(menuInfo.getMenuid());
		// 判断是否存在
		if (ordInfo == null) {
			return -1;
		}
		// 判断主键值是否重复
		if (!ordInfo.getMenuid().equals(menuInfo.getMenuid())) {
			int iCount = countChildNum(menuInfo.getParentid(), menuInfo.getMenuid());
			if (iCount > 0) {
				return 0;
			}
		}

		ordInfo.setMenuid(menuInfo.getMenuid());
		ordInfo.setMenuitem(menuInfo.getMenuitem());
		ordInfo.setTarget(menuInfo.getTarget());
		ordInfo.setActionto(menuInfo.getActionto());
		ordInfo.setFloor(ordInfo.getFloor());
		ordInfo.setMenuitemEn(menuInfo.getMenuitemEn());
		ordInfo.setImage(menuInfo.getImage());

		int childnum = getChildCount(menuInfo.getMenuid());

		ordInfo.setChildnum(childnum);
		ordInfo.setOrder(ordInfo.getOrder());
		ordInfo.setParentid(ordInfo.getParentid());
		this.update(ordInfo);

		return 1;
	}

	/**
	 * 更新菜单功能点
	 */
	@Override
	public void updateMenuOper(String menuid, List<MenuFunction> functionList) {
		this.delete(MenuFunction.class, new QueryFilter("menuid", menuid));
		this.save(functionList);
	}

	/**
	 * 计算同一父节点下具有相同key的节点数
	 * @parentId
	 * @key
	 */

	private int countChildNum(String parentId, String menuid) {
		Assert.hasLength(menuid);
		Assert.hasLength(parentId);
		return (int) this.count(MenuInfo.class, new QueryFilter[] { new QueryFilter("parentid", parentId),
				new QueryFilter("menuid", menuid) });
	}

	/**
	 * 取得父节点目前子节点最大的序号
	 * @parentId 父ID
	 */
	private int getMaxOrderNum(String parentId) {
		List<Integer> list = this.findHQL("select max(menu.order) FROM MenuInfo as menu WHERE menu.parentid=?",
				new Object[] { parentId });
		if (list.isEmpty() || list.get(0) == null) {
			return 0;
		}
		return list.get(0).intValue();
	}

	/**
	 * 根据该项目的全路径识别该项目ID，并返回该项目下属子节点 如果该路径不存在，返回空的列表对象
	 */
	private List<MenuInfo> queryChildByPath(String path) {
		if (path.equals(BusnDataDir.ROOT)) {
			return queryChildList(BusnDataDir.TOP_PARENT_ID);
		}

		MenuInfo menuInfo = findByPath(path);
		if (menuInfo == null) {
			return new ArrayList<MenuInfo>(0);
		}
		return queryChildList(menuInfo.getMenuid());
	}

	private List<MenuFunction> queryMenuFun(String menuid) {
		return this.findList(MenuFunction.class, new QueryFilter("menuid", menuid), LogicUtils.orderAsc("operid"));
	}

	/**
	 * 按菜单名称查询父节点的ID
	 * @param menuName 菜单名称
	 * @return
	 */
	private MenuInfo queryParentId(String menuName) {
		String hql = "FROM MenuInfo as menu WHERE trim(menu.menuitem) = ? ORDER BY menu.order asc";
		List<MenuInfo> childList = this.findHQL(hql, new Object[] { menuName });
		if (childList != null) {
			return childList.get(0);
		}
		return null;
	}

	/**
	 * 更新该项目的childNum值
	 */
	private void refreshChildCount(String itemId) {
		MenuInfo parentMenu = findById(itemId);
		if (parentMenu != null) {
			parentMenu.setChildnum(getChildCount(parentMenu.getMenuid()));
			this.update(parentMenu);
		}
	}
}
