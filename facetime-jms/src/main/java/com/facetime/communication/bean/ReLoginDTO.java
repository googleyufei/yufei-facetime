package com.facetime.communication.bean;

import com.facetime.communication.utils.UserAgent;
import com.facetime.core.bean.BusinessBean;

/**
 * 重新登录
 */
public class ReLoginDTO implements BusinessBean {

	private static final long serialVersionUID = 1L;

	/*
	 * 用户token
	 */
	private String token;

	// 用户使用的登录设备
	private String agent = UserAgent.Web;
	//标识客户端
	private String clientId;

	public ReLoginDTO() {
	}

	public ReLoginDTO(String token, String agent) {
		this.token = token;
		this.agent = agent;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

}
