package com.facetime.mgr.bean;

import com.facetime.mgr.common.BaseForm;

import java.util.List;

public class RolefuncVO extends BaseForm {

	private static final long serialVersionUID = 1L;

	private String menuid;

	private String menunname;

	// 操作列表funcVO 对象的列表
	private List<MenuFuncVO> operLst;

	// 父模块名称
	private String parentMenu;

	private String roleid;

	public String getMenuid() {
		return menuid;
	}

	public String getMenunname() {
		return menunname;
	}

	public List<MenuFuncVO> getOperLst() {
		return operLst;
	}

	public String getParentMenu() {
		return parentMenu;
	}

	public String getRoleid() {
		return roleid;
	}

	public void setMenuid(String menuid) {
		this.menuid = menuid;
	}

	public void setMenunname(String menunname) {
		this.menunname = menunname;
	}

	public void setOperLst(List<MenuFuncVO> operLst) {
		this.operLst = operLst;
	}

	public void setParentMenu(String parentMenu) {
		this.parentMenu = parentMenu;
	}

	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}
}
