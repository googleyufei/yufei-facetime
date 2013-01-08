package com.shop.domain.privilege;

import com.facetime.core.bean.BusinessObject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

/**
 * 员工权限组中间表
 */
@Entity
public class EmployeePrivilegeGrp implements BusinessObject {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;
	private String employeeId;
	private String privilegeGrpId;

	public String getEmployeeId() {
		return employeeId;
	}

	public String getId() {
		return id;
	}

	public String getPrivilegeGrpId() {
		return privilegeGrpId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setPrivilegeGrpId(String privilegeGrpId) {
		this.privilegeGrpId = privilegeGrpId;
	}

}
