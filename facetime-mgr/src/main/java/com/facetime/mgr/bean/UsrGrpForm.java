package com.facetime.mgr.bean;

import com.facetime.mgr.common.BaseForm;

public class UsrGrpForm extends BaseForm {

	private static final long serialVersionUID = 1L;

	private String checked;

	private String grpcode;

	private String grpname;

	private String userid;

	public String getChecked() {
		return checked;
	}

	public String getGrpcode() {
		return grpcode;
	}

	public String getGrpname() {
		return grpname;
	}

	public String getUserid() {
		return userid;
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

	public void setUserid(String userid) {
		this.userid = userid;
	}
}
