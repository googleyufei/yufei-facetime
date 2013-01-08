package com.facetime.mgr.bean;

import com.facetime.mgr.common.BaseForm;

public class DataDirForm extends BaseForm {

	private static final long serialVersionUID = 1L;

	private String childnum;

	private String id;

	private String idList;

	private String key;

	private String note;

	private String noteEn;

	private String order;

	private String parentid;
	private String mark;
	private String value;

	/** Default empty constructor. */
	public DataDirForm() {
	}

	public String getChildnum() {
		return childnum;
	}

	public String getId() {
		return id;
	}

	public String getIdList() {
		return idList;
	}

	public String getKey() {
		return key;
	}

	public String getMark() {
		return mark;
	}

	public String getNote() {
		return note;
	}

	public String getNoteEn() {
		return noteEn;
	}

	public String getOrder() {
		return order;
	}

	public String getParentid() {
		return parentid;
	}

	public String getValue() {
		return value;
	}

	public void setChildnum(String childnum) {
		this.childnum = childnum;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setIdList(String idList) {
		this.idList = idList;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void setNoteEn(String noteEn) {
		this.noteEn = noteEn;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
