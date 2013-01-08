package com.facetime.mgr.domain;

import com.facetime.core.bean.BusinessObject;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Entity
public class OrgInfo implements BusinessObject {

	private static final long serialVersionUID = 1L;

	private String address;

	private String areaid;

	/** persistent field */
	private int childnum;

	private String contact;

	/** persistent field */
	private int level;

	/** persistent field */
	private int order;

	private String orgcode;

	/** persistent field */
	private String orgfullname;

	@Id
	private String orgid;

	/** persistent field */
	private String orgname;

	private String orgnameEn;

	private String parentid;

	private String tel;

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof OrgInfo)) {
			return false;
		}
		OrgInfo castOther = (OrgInfo) other;
		return new EqualsBuilder().append(getOrgid(), castOther.getOrgid()).isEquals();
	}

	public String getAddress() {
		return address;
	}

	public String getAreaid() {
		return areaid;
	}

	public int getChildnum() {
		return childnum;
	}

	public String getContact() {
		return contact;
	}

	public int getLevel() {
		return level;
	}

	public int getOrder() {
		return order;
	}

	public String getOrgcode() {
		return orgcode;
	}

	public String getOrgfullname() {
		return orgfullname;
	}

	public String getOrgid() {
		return orgid;
	}

	public String getOrgname() {
		return orgname;
	}

	public String getOrgnameEn() {
		return orgnameEn;
	}

	public String getParentid() {
		return parentid;
	}

	public String getTel() {
		return tel;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getOrgid()).toHashCode();
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setAreaid(String areaid) {
		this.areaid = areaid;
	}

	public void setChildnum(int childnum) {
		this.childnum = childnum;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public void setOrgcode(String orgcode) {
		this.orgcode = orgcode;
	}

	public void setOrgfullname(String orgfullname) {
		this.orgfullname = orgfullname;
	}

	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}

	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}

	public void setOrgnameEn(String orgnameEn) {
		this.orgnameEn = orgnameEn;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

}
