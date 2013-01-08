package com.facetime.mgr.bean;

import com.facetime.mgr.common.BaseForm;

/**
 * @author wzhuo
 * 
 */
public class OrgUnionForm extends BaseForm {
	/***/
	private static final long serialVersionUID = 1L;

	protected String address;

	protected String childnum;

	protected String contact;

	protected String level;

	protected String order;

	protected String parentid;

	protected String tel;

	protected String unionorgfullname;

	protected String unionorgid;

	protected String unionorgname;

	protected String unionorgnameEn;

	private String areaid;

	/** Default empty constructor. */
	public OrgUnionForm() {
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

	public String getLevel() {
		return level;
	}

	public String getOrder() {
		return order;
	}

	public String getParentid() {
		return parentid;
	}

	public String getTel() {
		return tel;
	}

	public String getUnionorgfullname() {
		return unionorgfullname;
	}

	@Override
	public String getUnionorgid() {
		return unionorgid;
	}

	public String getUnionorgname() {
		return unionorgname;
	}

	public String getUnionorgnameEn() {
		return unionorgnameEn;
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

	public void setLevel(String level) {
		this.level = level;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public void setUnionorgfullname(String unionorgfullname) {
		this.unionorgfullname = unionorgfullname;
	}

	@Override
	public void setUnionorgid(String unionorgid) {
		this.unionorgid = unionorgid;
	}

	public void setUnionorgname(String unionorgname) {
		this.unionorgname = unionorgname;
	}

	public void setUnionorgnameEn(String unionorgnameEn) {
		this.unionorgnameEn = unionorgnameEn;
	}
}
