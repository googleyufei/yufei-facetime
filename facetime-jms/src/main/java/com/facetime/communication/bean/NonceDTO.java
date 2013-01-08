package com.facetime.communication.bean;

import com.facetime.core.bean.BusinessBean;

public class NonceDTO implements BusinessBean {

	private static final long serialVersionUID = 1L;

	// 随机串
	public String nonce;
	// 用户验证码
	public String hashKey;

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	public String getHashKey() {
		return hashKey;
	}

	public void setHashKey(String hashKey) {
		this.hashKey = hashKey;
	}

}
