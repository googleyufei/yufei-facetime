package com.shop.logic.bean;


public class ProductTypeBean extends BaseBean {

	private Integer typeid;
	private String name;
	private String note;
	private Integer parentid;
	private static final long serialVersionUID = 19864394230117628L;

	public String getName() {
		return name;
	}

	public String getNote() {
		return note;
	}

	public Integer getParentid() {
		return parentid;
	}

	public Integer getTypeid() {
		return typeid;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void setParentid(Integer parentid) {
		this.parentid = parentid;
	}

	public void setTypeid(Integer typeid) {
		this.typeid = typeid;
	}
}
