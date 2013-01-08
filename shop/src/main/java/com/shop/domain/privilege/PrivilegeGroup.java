package com.shop.domain.privilege;

import com.facetime.core.bean.BusinessObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

/**
 * 权限组实体
 */
@Entity
public class PrivilegeGroup implements BusinessObject {
	private static final long serialVersionUID = 8002609912155683600L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String groupid;
	private String name;

	public PrivilegeGroup() {
	}

	public PrivilegeGroup(String groupid) {
		this.groupid = groupid;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final PrivilegeGroup other = (PrivilegeGroup) obj;
		if (groupid == null) {
			if (other.groupid != null)
				return false;
		} else if (!groupid.equals(other.groupid))
			return false;
		return true;
	}

	public String getGroupid() {
		return groupid;
	}

	@Column(length = 20, nullable = false)
	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (groupid == null ? 0 : groupid.hashCode());
		return result;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public void setName(String name) {
		this.name = name;
	}
}
