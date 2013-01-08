package com.facetime.mgr.bean;

import com.facetime.mgr.common.BaseForm;

public class AreaInfoForm extends BaseForm {

	private static final long serialVersionUID = 1L;

	protected String areaid;

	protected String childnum;

	protected String level;

	protected String name;

	protected String nameEn;

	protected String note;

	protected String order;

	protected String parentid;

	/** Default empty constructor. */
	public AreaInfoForm() {
	}

	public String getAreaid() {
		return areaid;
	}

	public String getChildnum() {
		return childnum;
	}

	public String getLevel() {
		return level;
	}

	public String getName() {
		return name;
	}

	public String getNameEn() {
		return nameEn;
	}

	public String getNote() {
		return note;
	}

	public String getOrder() {
		return order;
	}

	public String getParentid() {
		return parentid;
	}

	public void setAreaid(String areaid) {
		this.areaid = areaid;
	}

	public void setChildnum(String childnum) {
		this.childnum = childnum;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

}
