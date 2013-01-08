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
public class MenuInfo implements BusinessObject {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String menuid;

	private String actionto;

	private int childnum;

	private int floor;

	private String image;

	private String menuitem;

	private String menuitemBak;

	private String menuitemEn;

	private String note;

	private int order;

	private String parentid;

	private int target;

	/** default constructor */
	public MenuInfo() {
	}

	/** minimal constructor */
	public MenuInfo(String parentid, String menuitem, int order, String actionto, int target, int childnum, int floor) {
		this.parentid = parentid;
		this.menuitem = menuitem;
		this.order = order;
		this.actionto = actionto;
		this.target = target;
		this.childnum = childnum;
		this.floor = floor;
	}

	/** full constructor */
	public MenuInfo(String parentid, String menuitem, String menuitemEn, String menuitemBak, int order,
			String actionto, int target, int childnum, int floor, String image, String note) {
		this.parentid = parentid;
		this.menuitem = menuitem;
		this.menuitemEn = menuitemEn;
		this.menuitemBak = menuitemBak;
		this.order = order;
		this.actionto = actionto;
		this.target = target;
		this.childnum = childnum;
		this.floor = floor;
		this.image = image;
		this.note = note;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof MenuInfo)) {
			return false;
		}
		MenuInfo castOther = (MenuInfo) other;
		return new EqualsBuilder().append(getMenuid(), castOther.getMenuid()).isEquals();
	}

	/** @struts.validator type="required"  */
	public String getActionto() {
		return actionto;
	}

	public int getChildnum() {
		return childnum;
	}

	public int getFloor() {
		return floor;
	}

	public String getImage() {
		return image;
	}

	public String getMenuid() {
		return menuid;
	}

	/** @struts.validator type="required"  */
	public String getMenuitem() {
		return menuitem;
	}

	public String getMenuitemBak() {
		return menuitemBak;
	}

	public String getMenuitemEn() {
		return menuitemEn;
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

	public int getTarget() {
		return target;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getMenuid()).toHashCode();
	}

	public void setActionto(String actionto) {
		this.actionto = actionto;
	}

	public void setChildnum(int childnum) {
		this.childnum = childnum;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void setMenuid(String menuid) {
		this.menuid = menuid;
	}

	public void setMenuitem(String menuitem) {
		this.menuitem = menuitem;
	}

	public void setMenuitemBak(String menuitemBak) {
		this.menuitemBak = menuitemBak;
	}

	public void setMenuitemEn(String menuitemEn) {
		this.menuitemEn = menuitemEn;
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

	public void setTarget(int target) {
		this.target = target;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("menuid", getMenuid()).toString();
	}
}
