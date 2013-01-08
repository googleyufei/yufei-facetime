package com.facetime.mgr.logic.impl;

import com.facetime.core.utils.CheckUtil;
import com.facetime.core.utils.StringUtils;
import com.facetime.mgr.bean.EmployeeBean;
import com.facetime.mgr.bean.OperInfoForm;
import com.facetime.mgr.bean.UserModel;
import com.facetime.mgr.common.BadArgumentException;
import com.facetime.mgr.common.HashUtil;
import com.facetime.mgr.domain.IDCard;
import com.facetime.mgr.domain.MenuFunction;
import com.facetime.mgr.domain.MenuInfo;
import com.facetime.mgr.domain.MenuOperate;
import com.facetime.mgr.domain.SysUser;
import com.facetime.mgr.domain.UsrGrpRole;
import com.facetime.mgr.domain.UsrRoleFunction;
import com.facetime.mgr.domain.UsrUsrGrp;
import com.facetime.mgr.logic.LoginManager;
import com.facetime.spring.logic.LogicImpl;
import com.facetime.spring.support.Limitable.PageBy;
import com.facetime.spring.support.LogicUtils;
import com.facetime.spring.support.PMLO;
import com.facetime.spring.support.Page;
import com.facetime.spring.support.QueryFilter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 登陆控制实现
 * 
 * @author arco
 * @version 4.0
 */
@Service
@Transactional
public class LoginManagerImpl extends LogicImpl implements LoginManager {

	@Override
	public String createTree(String menuid, String[] grps, String surl) {
		StringBuffer sbf = new StringBuffer();
		// 第一级菜单节点
		MenuInfo menuinfo = findById(MenuInfo.class, menuid);
		String floors = "";
		for (int j = 0; j < menuinfo.getFloor(); j++) {
			floors += "-";
		}
		sbf.append("addtree(\"" + floors + menuinfo.getMenuitem() + "\");");
		// 获取所有的子节点
		createChildTree(menuinfo, grps, surl, sbf);
		return sbf.toString();
	}

	@Override
	public void delete(String... employeeIds) {
		if (!CheckUtil.isValid(employeeIds)) {
			throw new BadArgumentException(
					"employee id array is null or empty.");
		}
		List<SysUser> list = this.findByIds(SysUser.class, employeeIds);
		for (SysUser employee : list) {
			employee.setVisible(false);
		}
		this.update(list);
	}

	@Override
	public void deleteUser(String[] usernames) {
		this.delete(UsrUsrGrp.class, new QueryFilter("username", PMLO.IN,
				usernames));
		this.delete(SysUser.class, new QueryFilter("username", PMLO.IN,
				usernames));
	}

	@Override
	public void fillBean(EmployeeBean bean) {
		SysUser employee = findById(SysUser.class, bean.getUsername());
		bean.setGender(employee.getGender());
		bean.setEmail(employee.getEmail());
		bean.setPhone(employee.getPhone());
		bean.setRealname(employee.getRealname());
		bean.setSchool(employee.getSchool());

		IDCard idcard = this.findById(IDCard.class, employee.getCardno());
		bean.setCardno(idcard.getCardno());
		bean.setBirthday(idcard.getBirthday());
		bean.setDegree(employee.getDegree());
		bean.setAddress(idcard.getAddress());
		bean.setImagePath(employee.getImagePath());
		bean.setImageName(employee.getImageName());
		if (employee.getDepartmentId() != null) {
			bean.setDepartmentid(employee.getDepartmentId());
		}
	}

	@Override
	public Map<String, List<MenuOperate>> findMenuFuncMap(String userid) {
		Map<String, List<MenuOperate>> hp = new HashMap<String, List<MenuOperate>>();
		List<MenuFunction> functions = findMenuFunction(userid);
		for (MenuFunction function : functions) {
			List<MenuOperate> result = new ArrayList<MenuOperate>();
			for (int i = 0; i < functions.size(); i++) {
				MenuFunction mf = functions.get(i);
				if (mf.getMenuid().equals(function.getMenuid())) {
					result.add(findById(MenuOperate.class, mf.getOperid()));
				}
			}
			hp.put(function.getMenuid(), result);
		}
		return hp;
	}

	@Override
	public List<MenuFunction> findMenuFunction(String userid) {
		String hql = "from MenuFunction mf where mf.funcid in "
				+ "( select rolefun.funcid from UsrRoleFunction rolefun where rolefun.rolecode in "
				+ " ( select grprole.rolecode from UsrGrpRole grprole where grprole.grpcode in "
				+ " ( select usrgrp.grpcode from UsrUsrGrp usrgrp where usrgrp.userid=?))) order by mf.funcid";
		return findHQL(hql, new Object[] { userid });
	}

	@Override
	public Page<SysUser> getEmployeeResult(EmployeeBean employeeBean,
			PageBy pageBy) {
		List<QueryFilter> filters = new ArrayList<QueryFilter>();
		if (employeeBean != null && "true".equals(employeeBean.getQuery())) {
			if (CheckUtil.isValid(employeeBean.getUsername())) {
				filters.add(LogicUtils.filterby("username", PMLO.LIKE,
						"%" + employeeBean.getUsername() + "%"));
			}
			if (CheckUtil.isValid(employeeBean.getRealname())) {
				filters.add(LogicUtils.filterby("realname", PMLO.LIKE,
						"%" + employeeBean.getRealname() + "%"));
			}
			if (CheckUtil.isValid(employeeBean.getDepartmentid())) {
				filters.add(LogicUtils.filterby("departmentId",
						employeeBean.getDepartmentid()));
			}
		}
		return this.findPage(SysUser.class,
				filters.toArray(new QueryFilter[] {}), pageBy);
	}

	@Override
	public void getleafmenu(String menuid, String[] grps,
			List<MenuInfo> leafMenu) {
		List<MenuInfo> mymenu = getMenuByAuth(menuid, grps);
		if (mymenu.size() <= 0) {
			return;
		}
		List<MenuInfo> mysonmenu = getMenuByParentAuth(menuid, grps);
		for (MenuInfo menuinfoson : mysonmenu) {
			if (menuinfoson.getChildnum() > 0) {
				// 有子菜单
				getleafmenu(menuinfoson.getMenuid(), grps, leafMenu);
			} else {
				leafMenu.add(menuinfoson);
			}
		}
	}

	@Override
	public List<MenuInfo> getMenubyGrp(String[] grps) {
		String parentid = "";
		StringBuffer parentids = new StringBuffer();
		List<MenuInfo> modelMenu = findMenubyGrp(grps);// 查询最底层子目录菜单
		for (int i = 0; i < modelMenu.size(); i++) {
			MenuInfo menuinfo = modelMenu.get(i);
			if (!menuinfo.getParentid().equals(parentid)) {
				getParentMenu(menuinfo, parentids);
				parentid = menuinfo.getParentid();
			}
		}
		// 排序数组
		parentid = parentids.toString();
		if (!parentid.equals("")) {
			List<MenuInfo> menulist = this.findHQL(
					"From MenuInfo menu where menu.menuid in ("
							+ parentid.substring(0, parentid.length() - 1)
							+ ") order by menu.order", new Object[] {});
			return menulist;
		}
		return null;
	}

	@Override
	public MenuInfo getMenuInfo(String menuid) {
		return findById(MenuInfo.class, menuid);
	}

	@Override
	public Page<SysUser> getOperInfo1(OperInfoForm form, int pageNum,
			int pageSize) {
		List<QueryFilter> filters = new ArrayList<QueryFilter>();
		filters.add(new QueryFilter("username", PMLO.LIKE, "%"
				+ form.getUsername() + "%", StringUtils.isValid(form
				.getUsername())));
		filters.add(new QueryFilter("realname", PMLO.IN, StringUtils.likeOf(form
				.getRealname()), StringUtils.isValid(form.getRealname())));
		Page<SysUser> page = this.findPage(SysUser.class,
				filters.toArray(new QueryFilter[] {}),
				LogicUtils.pageby(pageNum, pageSize));
		return page;
	}

	@Override
	public Map<String, String> getSysUserMap() {
		List<SysUser> list = this.findList(SysUser.class);
		Map<String, String> hp = new HashMap<String, String>();
		for (SysUser user : list) {
			hp.put(user.getUsername(), user.getUsername());
		}
		return hp;
	}

	/**
	 * 初始化管理员账号
	 */
	@Override
	public void initAdmin() {
		if (this.count(SysUser.class) > 0) {
			return;
		}

		SysUser user = new SysUser();
		user.setUsername("admin");
		user.setPassword(HashUtil.hash("123456"));
		user.setEmail("googleyufei@qq.com");
		user.setCreatedate(new Date());
		user.setModifydate(new Date());
		user.setRealname("余飞");
		user.setGender("0");
		IDCard idcard = new IDCard("213213", "北京", new Date());
		this.save(idcard);
		user.setCardno(idcard.getCardno());
		user.setDegree("1");
		this.save(user);
	}

	@Override
	public int modifyPassword(String username, String oldPwd, String newPwd) {
		SysUser user = this.findById(SysUser.class, username);
		oldPwd = HashUtil.hash(oldPwd);
		newPwd = HashUtil.hash(newPwd);
		if (user.getPassword().equals(oldPwd)) {
			user.setModifydate(new Date());
			user.setPassword(newPwd);
		} else {
			return -1;
		}
		this.update(user);
		return 0;
	}

	@Override
	public void resetPwd(String[] usernames) {
		for (String username : usernames) {
			SysUser user = this.findById(SysUser.class, username);
			user.setPassword(HashUtil.hash("111111"));
			this.update(user);
		}
	}

	@Override
	public void save(EmployeeBean bean) {
		SysUser employee = new SysUser();
		employee.setUsername(bean.getUsername().trim());
		employee.setPassword(bean.getPassword().trim());
		employee.setDegree(bean.getDegree());
		employee.setGender(bean.getGender());
		employee.setEmail(bean.getEmail());
		employee.setPhone(bean.getPhone());
		employee.setRealname(bean.getRealname());
		employee.setSchool(bean.getSchool());
		employee.setImageName(bean.getImageName());

		IDCard idcard = new IDCard(bean.getCardno(), bean.getAddress(),
				bean.getBirthday());
		this.save(idcard);

		employee.setCardno(idcard.getCardno());
		if (StringUtils.isNotEmpty(bean.getDepartmentid())) {
			employee.setDepartmentId(bean.getDepartmentid());
		}
		this.save(employee);
	}

	@Override
	public void savePage(String username, String url) {
		SysUser user = this.findById(SysUser.class, username);
		if (user != null) {
			user.setUrl(url);
			this.update(user);
		}
	}

	@Override
	public String[] searchUsrGrp(String userid) {
		List<UsrUsrGrp> grpList = this.findList(UsrUsrGrp.class,
				LogicUtils.filterby("userid", userid), LogicUtils.orderAsc("grpcode"));
		List<String> usrGrpList = new ArrayList<String>();
		for (UsrUsrGrp grp : grpList) {
			usrGrpList.add(grp.getGrpcode());
		}
		return usrGrpList.toArray(new String[] {});
	}

	@Override
	public boolean setupLoginUser(UserModel userModel) {
		// 设置用户组信息
		userModel.setGroupids(searchUsrGrp(userModel.getUsername()));
		// 设置用户权限
		if (userModel.getGroupids().length > 0) {
			Map<String, String> functionHash = new HashMap<String, String>();
			List<String> funcList = this.findPart(
					UsrRoleFunction.class,
					LogicUtils.filterby("rolecode", PMLO.IN,
							getRoleByGroup(userModel.getGroupids())),
					new String[] { "funcid" });
			for (String funcid : funcList) {
				functionHash.put(funcid, funcid);
			}
			userModel.setOperHash(functionHash);
		}
		return true;
	}

	@Override
	public void unlockPwd(String[] usernames) {
		for (String username : usernames) {
			SysUser user = this.findById(SysUser.class, username);
			user.setModifydate(new Date());
			user.setValid(true);
			this.update(user);
		}
	}

	@Override
	public void update(EmployeeBean bean) {
		if (!CheckUtil.isValid(bean.getUsername())) {
			throw new BadArgumentException(
					"username must be not null or empty.");
		}
		SysUser employee = findById(SysUser.class, bean.getUsername());
		employee.setDegree(bean.getDegree());
		employee.setGender(bean.getGender());
		employee.setEmail(bean.getEmail());
		employee.setPhone(bean.getPhone());
		employee.setRealname(bean.getRealname());
		employee.setSchool(bean.getSchool());
		if (CheckUtil.isValid(bean.getImageName())) {
			employee.setImageName(bean.getImageName());
		}
		IDCard idcard = this.findById(IDCard.class, employee.getCardno());
		idcard.setCardno(bean.getCardno());
		idcard.setAddress(bean.getAddress());
		idcard.setBirthday(bean.getBirthday());
		this.update(idcard);
		if (CheckUtil.isValid(bean.getDepartmentid())) {
			employee.setDepartmentId(bean.getDepartmentid());
		}
		this.update(employee);
	}

	@Override
	public boolean validate(String username, String password) {
		if (!StringUtils.isValid(username)) {
			throw new BadArgumentException("username is null or empty.");
		}
		if (!StringUtils.isValid(password)) {
			throw new BadArgumentException("password is null or empty.");
		}
		long count = this.count(SysUser.class,
				new QueryFilter[] { LogicUtils.filterby("username", username.trim()),
						LogicUtils.filterby("password", password.trim()) });
		return count > 0;
	}

	private void createChildTree(MenuInfo menuinfo, String[] grps, String surl,
			StringBuffer tree) {
		String floorsons = "";
		List<MenuInfo> menuList = getMenuByParentAuth(menuinfo.getMenuid(),
				grps); // 得到下一级子节点
		if (menuList.isEmpty()) {
			// 删除多余项(没有权限的最后一级节点，相应的父节点)
			for (int j = 0; j < menuinfo.getFloor(); j++) {
				floorsons += "-";
			}
			String strTree = "addtree(\"" + floorsons + menuinfo.getMenuitem()
					+ "\");";
			// 如果是一级菜单，且没有子菜单，则不显示 zwqing
			tree.delete(tree.indexOf(strTree),
					tree.indexOf(strTree) + strTree.length());

			// 如果是一级菜单，且没有子菜单，也显示此菜单 zwqing
			String actiontourl = menuinfo.getActionto();
			if (!(actiontourl == null || actiontourl.equals("#") || actiontourl
					.equals(""))) {
				tree.append("addtree(\"" + floorsons + menuinfo.getMenuitem()
						+ "\",\"" + surl + actiontourl);
				if (actiontourl.indexOf("?") > 0) {
					tree.append("&menuno=").append(menuinfo.getMenuid());
				} else {
					tree.append("?menuno=").append(menuinfo.getMenuid());
				}
				tree.append("\");");
			}
		}
		for (MenuInfo myson : menuList) {
			floorsons = "";
			for (int j = 0; j < myson.getFloor(); j++) {
				floorsons += "-";
			}
			if (myson.getChildnum() > 0) {
				// 如果还有子菜单
				tree.append("addtree(\"" + floorsons + myson.getMenuitem()
						+ "\");");
				createChildTree(myson, grps, surl, tree);
			} else {
				String actiontourl = myson.getActionto();
				if (!(actiontourl == null || actiontourl.equals("#") || actiontourl
						.equals(""))) {
					tree.append("addtree(\"" + floorsons + myson.getMenuitem()
							+ "\",\"" + surl + actiontourl);
					if (actiontourl.indexOf("?") > 0) {
						tree.append("&menuno=").append(myson.getMenuid());
					} else {
						tree.append("?menuno=").append(myson.getMenuid());
					}
					tree.append("\");");
				}
			}
		}
	}

	private List<MenuInfo> findMenubyGrp(String[] grps) {
		StringBuilder sb = new StringBuilder();
		List<Object> values = new ArrayList<Object>();
		sb.append("FROM MenuInfo menuinfo WHERE menuinfo.menuid in ");
		sb.append("(select menufun.menuid from MenuFunction menufun, UsrRoleFunction rolefun,UsrGrpRole grprole ");
		sb.append(" where menufun.funcid = rolefun.funcid and rolefun.rolecode = grprole.rolecode ");
		sb.append(" and grprole.grpcode in (");
		for (String group : grps) {
			sb.append("?,");
			values.add(group);
		}
		sb.replace(sb.length() - 1, sb.length(), ")");
		sb.append(") and menuinfo.childnum = 0 ");
		sb.append(" ORDER BY menuinfo.parentid,menuinfo.order desc ");
		return this.findHQL(sb.toString(), values.toArray());
	}

	/**
	 * 查询符合menuid和grps的用户有可视权限的菜单记录
	 */
	private List<MenuInfo> getMenuByAuth(String menuid, String[] grps) {
		StringBuilder sb = new StringBuilder();
		List<Object> values = new ArrayList<Object>();
		sb.append("FROM MenuInfo menuinfo ");
		sb.append("WHERE (menuinfo.menuid in ");
		sb.append(" (select menufun.menuid from MenuFunction menufun, UsrRoleFunction rolefun,UsrGrpRole grprole ");
		sb.append(" where menufun.funcid = rolefun.funcid and rolefun.rolecode = grprole.rolecode ");
		sb.append(" and grprole.grpcode in (");
		for (String group : grps) {
			sb.append("?,");
			values.add(group);
		}
		sb.replace(sb.length() - 1, sb.length(), ")");
		sb.append(") or menuinfo.actionto='#')");
		sb.append(" and menuinfo.menuid = ? ");
		values.add(menuid);
		sb.append(" order by menuinfo.order ");
		return this.findHQL(sb.toString(), values.toArray());
	}

	/**
	 * 查询符合父节点等于menuid和c_userid的用户有可视权限的菜单记录
	 */
	private List<MenuInfo> getMenuByParentAuth(String menuid, String[] grps) {
		StringBuilder sb = new StringBuilder();
		List<Object> values = new ArrayList<Object>();

		sb.append(" FROM MenuInfo menuinfo WHERE (menuinfo.menuid in ");
		sb.append(" (select menufun.menuid from MenuFunction menufun, UsrRoleFunction rolefun,UsrGrpRole grprole ");
		sb.append(" where menufun.funcid = rolefun.funcid and rolefun.rolecode = grprole.rolecode ");
		sb.append(" and grprole.grpcode in (");
		for (String group : grps) {
			sb.append("?,");
			values.add(group);
		}
		sb.replace(sb.length() - 1, sb.length(), ")");
		sb.append(") or menuinfo.actionto='#') and menuinfo.parentid = ? ");
		values.add(menuid);
		sb.append(" order by menuinfo.order ");
		return this.findHQL(sb.toString(), values.toArray());
	}

	private void getParentMenu(MenuInfo menuinfo, StringBuffer parent) {
		String tempParent = parent.toString();
		if (menuinfo.getFloor() == 1
				&& tempParent.indexOf("'" + menuinfo.getMenuid() + "'") == -1) {
			parent.append("'" + menuinfo.getMenuid() + "',");
		} else {
			MenuInfo menuParent = this.findById(MenuInfo.class,
					menuinfo.getParentid());
			if (menuParent != null) {
				if (menuParent.getFloor() == 1
						&& tempParent.indexOf("'" + menuParent.getMenuid()
								+ "'") == -1) {
					parent.append("'" + menuParent.getMenuid() + "',");
				} else {
					getParentMenu(menuParent, parent);
				}
			}
		}
	}

	/**
	 * 根据用户组别信息获取用户的角色信息
	 * 
	 * @param groupIds
	 *            []
	 */
	private String[] getRoleByGroup(String[] groupIds) {
		return this.findPart(UsrGrpRole.class,
				new QueryFilter("grpcode", PMLO.IN, groupIds),
				new String[] { "rolecode" }).toArray(new String[] {});
	}
}
