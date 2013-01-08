package com.facetime.communication.bean;

public class EntpriseLoginDTO extends LoginDTO {

	private static final long serialVersionUID = 1L;

	// 企业名称， 必须
	public String enterpriseName;

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

}
