package com.shop.domain.privilege;

import com.facetime.core.bean.BusinessObject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

/**
 * 权限与权限组的中间表
 */
@Entity
public class SystemPrivilegeGroup implements BusinessObject {

	/** */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;
	private String privilegeId;
	private String groupId;

	public String getGroupId() {
		return groupId;
	}

	public String getId() {
		return id;
	}

	public String getPrivilegeId() {
		return privilegeId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setPrivilegeId(String privilegeId) {
		this.privilegeId = privilegeId;
	}

}
