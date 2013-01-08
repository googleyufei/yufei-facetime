package com.facetime.communication.bean;

import com.facetime.core.bean.BusinessBean;

public class SimpleUserInfoDTO implements BusinessBean {

	private static final long serialVersionUID = 1L;

	private long userId;

	private String userName;

	public SimpleUserInfoDTO() {
	}

	public SimpleUserInfoDTO(long userId, String userName) {
		this.userId = userId;
		this.userName = userName;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
