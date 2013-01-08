package com.facetime.mgr.bean;

import com.facetime.mgr.common.BaseForm;

public class RoleInfoForm extends BaseForm {

	private static final long serialVersionUID = 1L;

	private String rolecode;

	private String rolename;

	public String getRolecode() {
		return rolecode;
	}

	public String getRolename() {
		return rolename;
	}

	public void setRolecode(String rolecode) {
		this.rolecode = rolecode;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

}
