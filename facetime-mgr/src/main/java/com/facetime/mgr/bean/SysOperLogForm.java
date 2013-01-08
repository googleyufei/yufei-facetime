package com.facetime.mgr.bean;

import com.facetime.mgr.common.BaseForm;

public class SysOperLogForm extends BaseForm {

	private static final long serialVersionUID = 1L;

	protected String id;

	protected String logtime;

	protected String menuid;

	protected String note;

	protected String operid;

	protected String result;

	protected String userid;

	protected String username;

	private String logbegintime;

	private String logendtime;

	/** Default empty constructor. */
	public SysOperLogForm() {
	}

	public String getId() {
		return id;
	}

	public String getLogbegintime() {
		return logbegintime;
	}

	public String getLogendtime() {
		return logendtime;
	}

	public String getLogtime() {
		return logtime;
	}

	public String getMenuid() {
		return menuid;
	}

	public String getNote() {
		return note;
	}

	public String getOperid() {
		return operid;
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

	public void setId(String id) {
		this.id = id;
	}

	public void setLogbegintime(String logbegintime) {
		this.logbegintime = logbegintime;
	}

	public void setLogendtime(String logendtime) {
		this.logendtime = logendtime;
	}

	public void setLogtime(String logtime) {
		this.logtime = logtime;
	}

	public void setMenuid(String menuid) {
		this.menuid = menuid;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void setOperid(String operid) {
		this.operid = operid;
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
