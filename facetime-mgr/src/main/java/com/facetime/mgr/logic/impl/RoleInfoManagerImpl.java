package com.facetime.mgr.logic.impl;

import com.facetime.mgr.bean.MenuFuncVO;
import com.facetime.mgr.bean.RolefuncVO;
import com.facetime.mgr.domain.MenuInfo;
import com.facetime.mgr.domain.UsrGrpRole;
import com.facetime.mgr.domain.UsrRole;
import com.facetime.mgr.domain.UsrRoleFunction;
import com.facetime.mgr.logic.RoleInfoManager;
import com.facetime.spring.logic.LogicImpl;
import com.facetime.spring.support.QueryFilter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleInfoManagerImpl extends LogicImpl implements RoleInfoManager {

	@Override
	public void deleteRole(String rolecode) {
		this.delete(UsrRoleFunction.class, new QueryFilter("rolecode", rolecode));
		this.delete(UsrGrpRole.class, new QueryFilter("rolecode", rolecode));
		this.deleteById(UsrRole.class, rolecode);
	}

	// 返回最底层菜单的功能点列表
	public List<Object[]> getMenuFuncLst(String menuid) {
		String hql = "select mf.funcid,mf.operid,mo.opername,mf.menuid,menu.menuitem from MenuFunction as mf,"
				+ "MenuOperate as mo,MenuInfo as menu where mf.menuid='" + menuid
				+ "' and mf.menuid=menu.menuid and mo.operid=mf.operid";
		@SuppressWarnings("unchecked")
		List<Object[]> funcLst = getHibernateTemplate().find(hql);
		return funcLst;
	}

	// 获得整理后的VOList
	@Override
	public List<RolefuncVO> getMenuTree() {
		List<MenuInfo> menuList = getChildMenu("0");
		List<RolefuncVO> newMenuTree = new ArrayList<RolefuncVO>();
		for (MenuInfo menu : menuList) {
			RolefuncVO rf = getRolefuncVO(menu.getMenuid());
			if (rf != null) {
				newMenuTree.add(rf);
			}
		}
		return newMenuTree;
	}

	/** {@inheritDoc} */
	@Override
	public List<UsrRoleFunction> getRoleFuncs(String rolecode) {
		return this.findList(UsrRoleFunction.class, new QueryFilter("rolecode", rolecode));
	}

	// 返回一个菜单的所有功能点
	public RolefuncVO getRolefuncVO(String menuid) {
		RolefuncVO mfVO = new RolefuncVO();
		// 得到这个菜单所有的功能点

		List<Object[]> mfLst = getMenuFuncLst(menuid);
		if (mfLst == null || mfLst.size() == 0) {
			mfVO = null;
			return mfVO;
		}

		List<MenuFuncVO> voLst = new ArrayList<MenuFuncVO>();
		for (int i = 0; i < mfLst.size(); i++) {
			Object[] mf = mfLst.get(i);
			MenuFuncVO vo = new MenuFuncVO();
			vo.setFuncid((String) mf[0]);
			vo.setOperid((String) mf[1]);
			vo.setOpername((String) mf[2]);
			vo.setMenuid((String) mf[3]);
			vo.setMenuname((String) mf[4]);
			voLst.add(vo);
		}
		mfVO.setOperLst(voLst);
		MenuInfo menu = findById(MenuInfo.class, menuid);
		mfVO.setMenuid(menuid);
		mfVO.setMenunname(menu.getMenuitem());
		mfVO.setParentMenu(getParentMenu(menu));
		return mfVO;
	}

	@Override
	public List<UsrRole> getRoles() {
		List<UsrRole> lst = this.findList(UsrRole.class);
		if (lst.isEmpty()) {
			lst = new ArrayList<UsrRole>(0);
		}
		return lst;
	}

	@Override
	public void updateRoleFunc(String rolecode, List<String> roleFuncids) {
		this.delete(UsrRoleFunction.class, new QueryFilter("rolecode", rolecode));
		if (roleFuncids == null || roleFuncids.size() == 0) {
			return;
		}
		for (int i = 0; i < roleFuncids.size(); i++) {
			UsrRoleFunction rf = new UsrRoleFunction();
			String funcid = roleFuncids.get(i);
			rf.setFuncid(funcid);
			rf.setRolecode(rolecode);
			this.save(rf);
		}
	}

	private List<MenuInfo> getChildMenu(String menuid) {
		List<MenuInfo> returnList = new ArrayList<MenuInfo>();
		List<MenuInfo> childList = new ArrayList<MenuInfo>();

		List<MenuInfo> menus = this.findList(MenuInfo.class, new QueryFilter("parentid", menuid));
		for (MenuInfo po : menus) {
			returnList.add(po);
			childList = getChildMenu(po.getMenuid());
			if (childList.size() > 0) {
				returnList.addAll(childList);
			}
		}
		return returnList;
	}

	private String getParentMenu(MenuInfo menu) {
		List<String> parentModule = new ArrayList<String>();
		MenuInfo paremenu = menu;
		while (!paremenu.getParentid().equals("0")) {
			paremenu = findById(MenuInfo.class, paremenu.getParentid());
			parentModule.add(paremenu.getMenuitem());

		}
		String parentmenuname = new String();
		for (int i = parentModule.size() - 1; i >= 0; i--) {
			String menuname = parentModule.get(i);
			parentmenuname += menuname;
			parentmenuname += " ";
		}
		return parentmenuname.toString();
	}
}
