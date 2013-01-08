package com.facetime.mgr.domain;

import com.facetime.core.bean.BusinessObject;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
public class MenuOperate implements BusinessObject {

	/***/
	private static final long serialVersionUID = 1L;

	@Id
	private String operid;

	private String clickname;

	private String keys;

	private String opername;

	private String picpath;

	private String types;

	/** default constructor */
	public MenuOperate() {
	}

	/** minimal constructor */
	public MenuOperate(String operid, String opername) {
		this.operid = operid;
		this.opername = opername;
	}

	/** full constructor */
	public MenuOperate(String operid, String opername, String picpath, String clickname, String keys, String types) {
		this.operid = operid;
		this.opername = opername;
		this.picpath = picpath;
		this.clickname = clickname;
		this.keys = keys;
		this.types = types;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof MenuOperate)) {
			return false;
		}
		MenuOperate castOther = (MenuOperate) other;
		return new EqualsBuilder().append(getOperid(), castOther.getOperid()).isEquals();
	}

	public String getClickname() {
		return clickname;
	}

	public String getKeys() {
		return keys;
	}

	public String getOperid() {
		return operid;
	}

	public String getOpername() {
		return opername;
	}

	public String getPicpath() {
		return picpath;
	}

	public String getTypes() {
		return types;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getOperid()).toHashCode();
	}

	public void setClickname(String clickname) {
		this.clickname = clickname;
	}

	public void setKeys(String keys) {
		this.keys = keys;
	}

	public void setOperid(String operid) {
		this.operid = operid;
	}

	public void setOpername(String opername) {
		this.opername = opername;
	}

	public void setPicpath(String picpath) {
		this.picpath = picpath;
	}

	public void setTypes(String types) {
		this.types = types;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("operid", getOperid()).toString();
	}

}
