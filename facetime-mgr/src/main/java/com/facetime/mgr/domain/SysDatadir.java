package com.facetime.mgr.domain;

import com.facetime.core.bean.BusinessObject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class SysDatadir implements BusinessObject {

	/***/
	private static final long serialVersionUID = 1L;

	private int childnum;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;

	private String key;

	private String note;

	private String noteEn;

	private int order;

	private String parentid;

	private String value;

	/** default constructor */
	public SysDatadir() {
	}

	/** minimal constructor */
	public SysDatadir(String parentid, String key, int order, int childnum,
			String note) {
		this.parentid = parentid;
		this.key = key;
		this.order = order;
		this.childnum = childnum;
		this.note = note;
	}

	/** full constructor */
	public SysDatadir(String parentid, String key, String value, int order,
			int childnum, String note, String noteEn) {
		this.parentid = parentid;
		this.key = key;
		this.value = value;
		this.order = order;
		this.childnum = childnum;
		this.note = note;
		this.noteEn = noteEn;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof SysDatadir)) {
			return false;
		}
		SysDatadir castOther = (SysDatadir) other;
		return new EqualsBuilder().append(getId(), castOther.getId())
				.isEquals();
	}

	public int getChildnum() {
		return childnum;
	}

	public String getId() {
		return id;
	}

	/**
	 * @return
	 * @struts.validator type="required"
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @return
	 * @struts.validator type="required"
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @return
	 * @struts.validator type="required"
	 */
	public String getNoteEn() {
		return noteEn;
	}

	public int getOrder() {
		return order;
	}

	public String getParentid() {
		return parentid;
	}

	public String getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getId()).toHashCode();
	}

	public void setChildnum(int childnum) {
		this.childnum = childnum;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void setNoteEn(String noteEn) {
		this.noteEn = noteEn;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public void setValue(String value) {
		this.value = value;
	}
}