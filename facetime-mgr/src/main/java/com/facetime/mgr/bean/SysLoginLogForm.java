package com.facetime.mgr.bean;

import com.facetime.mgr.common.BaseForm;

public class SysLoginLogForm extends BaseForm {

	private static final long serialVersionUID = 1L;

	protected String hostip;

	protected String loginid;

	protected String logintime;

	protected String logouttime;

	protected String note;

	protected String result;

	protected String userid;

	protected String username;

	private String loginbegintime;

	private String loginendtime;

	private String logoutbegintime;

	private String logoutendtime;

	public String getHostip() {
		return hostip;
	}

	public String getLoginbegintime() {
		return loginbegintime;
	}

	public String getLoginendtime() {
		return loginendtime;
	}

	public String getLoginid() {
		return loginid;
	}

	public String getLogintime() {
		return logintime;
	}

	public String getLogoutbegintime() {
		return logoutbegintime;
	}

	public String getLogoutendtime() {
		return logoutendtime;
	}

	public String getLogouttime() {
		return logouttime;
	}

	public String getNote() {
		return note;
	}

	public String getResult() {
		return result;
	}

	public String getUserid() {
		return userid;
	}

	public String getUsername() {
		return username;
	}

	public void setHostip(String hostip) {
		this.hostip = hostip;
	}

	public void setLoginbegintime(String loginbegintime) {
		this.loginbegintime = loginbegintime;
	}

	public void setLoginendtime(String loginendtime) {
		this.loginendtime = loginendtime;
	}

	public void setLoginid(String loginid) {
		this.loginid = loginid;
	}

	public void setLogintime(String logintime) {
		this.logintime = logintime;
	}

	public void setLogoutbegintime(String logoutbegintime) {
		this.logoutbegintime = logoutbegintime;
	}

	public void setLogoutendtime(String logoutendtime) {
		this.logoutendtime = logoutendtime;
	}

	public void setLogouttime(String logouttime) {
		this.logouttime = logouttime;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
