package com.facetime.mgr.bean;

import com.facetime.mgr.common.BaseForm;

public class GrpInfoForm extends BaseForm {
	/***/
	private static final long serialVersionUID = 1L;

	private String grpcode;

	private String grpname;

	public String getGrpcode() {
		return grpcode;
	}

	public String getGrpname() {
		return grpname;
	}

	public void setGrpcode(String grpcode) {
		this.grpcode = grpcode;
	}

	public void setGrpname(String grpname) {
		this.grpname = grpname;
	}

}
