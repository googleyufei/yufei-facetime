package com.facetime.mgr.domain;

import com.facetime.core.bean.BusinessObject;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
public class AreaInfo implements BusinessObject {

	private static final long serialVersionUID = 1L;

	@Id
	private String areaid;
	private int childnum;
	private int level;
	private String name;
	private String nameEn;
	private String note;
	private int order;
	private String parentid;

	public String getAreaid() {
		return areaid;
	}

	public int getChildnum() {
		return childnum;
	}

	public int getLevel() {
		return level;
	}

	/** @struts.validator type="required" */
	public String getName() {
		return name;
	}

	public String getNameEn() {
		return nameEn;
	}

	public String getNote() {
		return note;
	}

	public int getOrder() {
		return order;
	}

	public String getParentid() {
		return parentid;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getAreaid()).toHashCode();
	}

	public void setAreaid(String areaid) {
		this.areaid = areaid;
	}

	public void setChildnum(int childnum) {
		this.childnum = childnum;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("areaid", getAreaid()).toString();
	}
}
