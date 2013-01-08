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
public class UsrUsrGrp implements BusinessObject {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;
	private String grpcode;
	private String userid;

	public UsrUsrGrp() {
	}

	/** full constructor */
	public UsrUsrGrp(String id, String userid, String grpcode) {
		this.id = id;
		this.userid = userid;
		this.grpcode = grpcode;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof UsrUsrGrp))
			return false;
		UsrUsrGrp castOther = (UsrUsrGrp) other;
		return new EqualsBuilder().append(getId(), castOther.getId()).isEquals();
	}

	public String getGrpcode() {
		return grpcode;
	}

	public String getId() {
		return id;
	}

	public String getUserid() {
		return userid;
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

	public void setUserid(String userid) {
		this.userid = userid;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("id", getId()).toString();
	}

}
