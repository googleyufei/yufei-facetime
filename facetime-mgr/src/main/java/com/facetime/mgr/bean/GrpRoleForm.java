package com.facetime.mgr.bean;

import com.facetime.mgr.common.BaseForm;

public class GrpRoleForm extends BaseForm {

	private static final long serialVersionUID = 1L;
	private String checked = "";
	private String grpcode;
	private String grpname;
	private String id;
	private String rolecode;

	private String rolename;

	public String getChecked() {
		return checked;
	}

	public String getGrpcode() {
		return grpcode;
	}

	public String getGrpname() {
		return grpname;
	}

	public String getId() {
		return id;
	}

	public String getRolecode() {
		return rolecode;
	}

	public String getRolename() {
		return rolename;
	}

	public void setChecked(String checked) {
		this.checked = checked;
	}

	public void setGrpcode(String grpcode) {
		this.grpcode = grpcode;
	}

	public void setGrpname(String grpname) {
		this.grpname = grpname;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setRolecode(String rolecode) {
		this.rolecode = rolecode;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

}
