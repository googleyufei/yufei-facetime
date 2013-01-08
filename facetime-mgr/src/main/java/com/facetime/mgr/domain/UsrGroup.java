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
public class UsrGroup implements BusinessObject {

	/***/
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String grpcode;

	private String grpname;

	/** default constructor */
	public UsrGroup() {
	}

	/** full constructor */
	public UsrGroup(String grpname) {
		this.grpname = grpname;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof UsrGroup)) {
			return false;
		}
		UsrGroup castOther = (UsrGroup) other;
		return new EqualsBuilder().append(getGrpcode(), castOther.getGrpcode()).isEquals();
	}

	public String getGrpcode() {
		return grpcode;
	}

	public String getGrpname() {
		return grpname;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getGrpcode()).toHashCode();
	}

	public void setGrpcode(String grpcode) {
		this.grpcode = grpcode;
	}

	public void setGrpname(String grpname) {
		this.grpname = grpname;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("grpcode", getGrpcode()).toString();
	}

}
