package com.shop.domain.privilege;

import com.facetime.core.bean.BusinessObject;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SystemPrivilegePK implements BusinessObject {
	private static final long serialVersionUID = -8263022050978731613L;

	/* 模块 */
	@Column(length = 20, name = "module")
	private String module;
	/* 权限值 */
	@Column(length = 20, name = "privilege")
	private String privilege;

	public SystemPrivilegePK() {
	}

	public SystemPrivilegePK(String module, String privilege) {
		this.module = module;
		this.privilege = privilege;
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
		final SystemPrivilegePK other = (SystemPrivilegePK) obj;
		if (module == null) {
			if (other.module != null) {
				return false;
			}
		} else if (!module.equals(other.module)) {
			return false;
		}
		if (privilege == null) {
			if (other.privilege != null) {
				return false;
			}
		} else if (!privilege.equals(other.privilege)) {
			return false;
		}
		return true;
	}

	public String getModule() {
		return module;
	}

	public String getPrivilege() {
		return privilege;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (module == null ? 0 : module.hashCode());
		result = prime * result + (privilege == null ? 0 : privilege.hashCode());
		return result;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public void setPrivilege(String privilege) {
		this.privilege = privilege;
	}

}
