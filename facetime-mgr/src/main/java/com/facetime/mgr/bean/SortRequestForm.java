package com.facetime.mgr.bean;

import com.facetime.mgr.common.BaseForm;

public class SortRequestForm extends BaseForm {
	/***/
	private static final long serialVersionUID = 1L;

	private String idList;

	private String menuid;

	private String parentid;

	public SortRequestForm() {
	}

	public String getIdList() {
		return idList;
	}

	public String getMenuid() {
		return menuid;
	}

	public String getParentid() {
		return parentid;
	}

	public void setIdList(String idList) {
		this.idList = idList;
	}

	public void setMenuid(String menuid) {
		this.menuid = menuid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

}
