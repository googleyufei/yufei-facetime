package com.shop.logic.bean;


public class DepartmentBean extends BaseBean {
	private static final long serialVersionUID = -5050762758305205770L;
	private String name;
	private String departmentid;

	public DepartmentBean() {
		super();
	}

	public DepartmentBean(String name) {
		super();
		this.name = name;
	}

	public String getDepartmentid() {
		return departmentid;
	}

	public String getName() {
		return name;
	}

	public void setDepartmentid(String departmentid) {
		this.departmentid = departmentid;
	}

	public void setName(String name) {
		this.name = name;
	}
}
