package com.shop.domain.privilege;

import com.facetime.core.bean.BusinessObject;
import com.facetime.core.utils.IdGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

/**
 * 系统权限
 */
@Entity
public class SystemPrivilege implements BusinessObject {
	private static final long serialVersionUID = 4119217381330493698L;
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;

	private SystemPrivilegePK permission;
	@Column(length = 20, nullable = false)
	private String name;

	public SystemPrivilege() {
	}

	public SystemPrivilege(String module, String privilege, String name) {
		id = IdGenerator.strId();
		permission = new SystemPrivilegePK(module, privilege);
		this.name = name;
	}

	public SystemPrivilege(SystemPrivilegePK permission) {
		this.permission = permission;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		SystemPrivilege other = (SystemPrivilege) obj;
		if (permission == null) {
			if (other.permission != null) {
				return false;
			}
		} else if (!permission.equals(other.permission)) {
			return false;
		}
		return true;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public SystemPrivilegePK getPermission() {
		return permission;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (permission == null ? 0 : permission.hashCode());
		return result;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPermission(SystemPrivilegePK permission) {
		this.permission = permission;
	}

	@Override
	public String toString() {
		return "SystemPrivilege {module:" + permission.getModule() + ", privilege:" + permission.getPrivilege() + "]";
	}

}
