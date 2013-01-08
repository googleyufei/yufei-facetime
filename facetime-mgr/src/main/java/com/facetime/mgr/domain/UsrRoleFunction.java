package com.facetime.mgr.domain;

import com.facetime.core.bean.BusinessObject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

/**
 * 用户角色和菜单功能点的中间类
 */
@Entity
public class UsrRoleFunction implements BusinessObject {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;
	private String funcid;
	private String rolecode;

	/** default constructor */
	public UsrRoleFunction() {
	}

	/** full constructor */
	public UsrRoleFunction(String id, String rolecode, String funcid) {
		this.id = id;
		this.rolecode = rolecode;
		this.funcid = funcid;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof UsrRoleFunction)) {
			return false;
		}
		UsrRoleFunction castOther = (UsrRoleFunction) other;
		return new EqualsBuilder().append(getId(), castOther.getId()).isEquals();
	}

	public String getFuncid() {
		return funcid;
	}

	public String getId() {
		return id;
	}

	public String getRolecode() {
		return rolecode;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getId()).toHashCode();
	}

	public void setFuncid(String funcid) {
		this.funcid = funcid;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setRolecode(String rolecode) {
		this.rolecode = rolecode;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("id", getId()).toString();
	}

}
