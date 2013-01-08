package com.facetime.communication.bean;

import com.facetime.communication.utils.UserAgent;

/**
 * 用户登录信息
 */
public class LoginDTO extends NonceDTO {
	private static final long serialVersionUID = 1L;

	// account， 必须
	public String account;
	// 是否保存用户信息,
	public int keepUserInfo;
	// 用户密码（密文 MD5）， 必须
	public String password;

	// 用户使用的登录设备， 必须
	public String agent = UserAgent.Web;
	// 标识客户端
	public String clientId;

	/**
	 * 用户登录终端IP地址
	 */
	private String ip;

	public LoginDTO() {
	}

	public LoginDTO(String account, String nonce, String password) {
		this.account = account;
		this.nonce = nonce;
		this.password = password;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public int getKeepUserInfo() {
		return keepUserInfo;
	}

	public void setKeepUserInfo(int keepUserInfo) {
		this.keepUserInfo = keepUserInfo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String hashPassword) {
		this.password = hashPassword;
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

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

}
