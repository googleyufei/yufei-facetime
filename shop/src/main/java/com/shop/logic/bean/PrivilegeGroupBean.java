package com.shop.logic.bean;


public class PrivilegeGroupBean extends BaseBean {
	private static final long serialVersionUID = -7958462491358329450L;

	private String name;
	private String groupid;
	private String[] privilegeIds;

	public String getGroupid() {
		return groupid;
	}

	public String getName() {
		return name;
	}

	public String[] getPrivilegeIds() {
		return privilegeIds;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPrivilegeIds(String[] privilegeIds) {
		this.privilegeIds = privilegeIds;
	}

}
