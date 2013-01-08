package com.facetime.communication.bean;

import com.facetime.core.bean.BusinessBean;

import java.util.List;

public class SimpleUserInfosDTO implements BusinessBean {

	private static final long serialVersionUID = 1L;

	private List<SimpleUserInfoDTO> userList;

	public List<SimpleUserInfoDTO> getUserList() {
		return userList;
	}

	public void setUserList(List<SimpleUserInfoDTO> userList) {
		this.userList = userList;
	}

}
