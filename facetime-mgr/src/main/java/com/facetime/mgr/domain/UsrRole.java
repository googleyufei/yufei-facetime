package com.facetime.mgr.domain;

import com.facetime.core.bean.BusinessObject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class UsrRole implements BusinessObject {

	private static final long serialVersionUID = 798747487407403410L;
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String rolecode;
	private String rolename;

	/** default constructor */
	public UsrRole() {
	}

	/** full constructor */
	public UsrRole(String rolename) {
		this.rolename = rolename;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof UsrRole)) {
			return false;
		}
		UsrRole castOther = (UsrRole) other;
		return new EqualsBuilder().append(getRolecode(), castOther.getRolecode()).isEquals();
	}

	public String getRolecode() {
		return rolecode;
	}

	public String getRolename() {
		return rolename;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getRolecode()).toHashCode();
	}

	public void setRolecode(String rolecode) {
		this.rolecode = rolecode;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("rolecode", getRolecode()).toString();
	}

}
