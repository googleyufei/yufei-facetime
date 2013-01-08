package com.facetime.mgr.bean;

import com.facetime.mgr.common.BaseForm;

public class MenuInfoForm extends BaseForm {

	/***/
	private static final long serialVersionUID = 1L;

	protected String actionto;

	protected String childnum;

	protected String floor;

	protected String image;

	protected String menuid;

	protected String menuitem;

	protected String menuitemBak;

	protected String menuitemEn;

	protected String note;

	protected String order;

	protected String parentid;

	protected String target;

	/** Default empty constructor. */
	public MenuInfoForm() {
	}

	public String getActionto() {
		return actionto;
	}

	public String getChildnum() {
		return childnum;
	}

	public String getFloor() {
		return floor;
	}

	public String getImage() {
		return image;
	}

	public String getMenuid() {
		return menuid;
	}

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

	public String getOrder() {
		return order;
	}

	public String getParentid() {
		return parentid;
	}

	public String getTarget() {
		return target;
	}

	public void setActionto(String actionto) {
		this.actionto = actionto;
	}

	public void setChildnum(String childnum) {
		this.childnum = childnum;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void setMenuid(String menuid) {
		this.menuid = menuid;
	}

	/**
	 * @struts.validator type="required"
	 */

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

	public void setOrder(String order) {
		this.order = order;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public void setTarget(String target) {
		this.target = target;
	}

}
