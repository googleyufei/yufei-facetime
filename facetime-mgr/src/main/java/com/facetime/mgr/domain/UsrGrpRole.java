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
public class UsrGrpRole implements BusinessObject {

	/***/
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;
	private String grpcode;
	private String rolecode;

	/** default constructor */
	public UsrGrpRole() {
	}

	/** full constructor */
	public UsrGrpRole(String id, String grpcode, String rolecode) {
		this.id = id;
		this.grpcode = grpcode;
		this.rolecode = rolecode;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof UsrGrpRole)) {
			return false;
		}
		UsrGrpRole castOther = (UsrGrpRole) other;
		return new EqualsBuilder().append(getId(), castOther.getId()).isEquals();
	}

	public String getGrpcode() {
		return grpcode;
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

	public void setGrpcode(String grpcode) {
		this.grpcode = grpcode;
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
