package com.facetime.mgr.bean;

import com.facetime.mgr.common.BaseForm;

/**
 * @author wzhuo
 * 
 */
public class OrgInfoForm extends BaseForm {
	/***/
	private static final long serialVersionUID = 1L;

	protected String address;

	protected String childnum;

	protected String contact;

	protected String level;

	protected String order;

	protected String orgfullname;

	protected String orgid;

	protected String orgname;

	protected String orgnameEn;

	protected String parentid;

	protected String tel;

	private String areaid;

	private String fcrOrgId;

	private String orgcode;

	private String pan;

	private String paper;

	private String unionorgid;

	/** Default empty constructor. */
	public OrgInfoForm() {
	}

	public String getAddress() {
		return address;
	}

	public String getAreaid() {
		return areaid;
	}

	public String getChildnum() {
		return childnum;
	}

	public String getContact() {
		return contact;
	}

	public String getFcrOrgId() {
		return fcrOrgId;
	}

	public String getLevel() {
		return level;
	}

	public String getOrder() {
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

	public String getPan() {
		return pan;
	}

	public String getPaper() {
		return paper;
	}

	public String getParentid() {
		return parentid;
	}

	public String getTel() {
		return tel;
	}

	@Override
	public String getUnionorgid() {
		return unionorgid;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setAreaid(String areaid) {
		this.areaid = areaid;
	}

	public void setChildnum(String childnum) {
		this.childnum = childnum;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public void setFcrOrgId(String fcrOrgId) {
		this.fcrOrgId = fcrOrgId;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public void setOrgcode(String orgcode) {
		this.orgcode = orgcode;
	}

	public void setOrgfullname(String orgfullname) {
		this.orgfullname = orgfullname;
	}

	/**
	 * @struts.validator type="required"
	 */

	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}

	/**
	 * @struts.validator type="required"
	 */

	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}

	public void setOrgnameEn(String orgnameEn) {
		this.orgnameEn = orgnameEn;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public void setPaper(String paper) {
		this.paper = paper;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	@Override
	public void setUnionorgid(String unionorgid) {
		this.unionorgid = unionorgid;
	}

}
