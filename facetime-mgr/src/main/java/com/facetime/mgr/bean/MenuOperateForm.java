package com.facetime.mgr.bean;

import com.facetime.mgr.common.BaseForm;

public class MenuOperateForm extends BaseForm {

	/***/
	private static final long serialVersionUID = 1L;

	private String clickname;

	private String keys;

	private String operid;

	private String opername;

	private String picpath;

	private String types;

	/** Default empty constructor. */
	public MenuOperateForm() {
	}

	public String getClickname() {
		return clickname;
	}

	public String getKeys() {
		return keys;
	}

	public String getOperid() {
		return operid;
	}

	public String getOpername() {
		return opername;
	}

	public String getPicpath() {
		return picpath;
	}

	public String getTypes() {
		return types;
	}

	public void setClickname(String clickname) {
		this.clickname = clickname;
	}

	public void setKeys(String keys) {
		this.keys = keys;
	}

	public void setOperid(String operid) {
		this.operid = operid;
	}

	public void setOpername(String opername) {
		this.opername = opername;
	}

	public void setPicpath(String picpath) {
		this.picpath = picpath;
	}

	public void setTypes(String types) {
		this.types = types;
	}

}
