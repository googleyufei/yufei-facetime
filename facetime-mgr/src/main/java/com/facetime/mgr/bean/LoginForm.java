package com.facetime.mgr.bean;

import com.facetime.mgr.common.BaseForm;

public class LoginForm extends BaseForm {

	private static final long serialVersionUID = -6369647249556782797L;

	protected String userid;

	protected String userpwd;

	public LoginForm() {
	}

	public String getUserid() {
		return userid;
	}

	public String getPassword() {
		return userpwd;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public void setPassword(String userpwd) {
		this.userpwd = userpwd;
	}

}
