package com.facetime.spring.logic;

import com.facetime.core.bean.BusinessBean;

/**
 * 
 * 远程调用业务接口的空实现
 * @author yufei
 * @Create_by 2012-12-7
 * @Design_by eclipse  
 */
public class EmptyRemoteLogic implements RemoteLogic {

	@Override
	public String getBaseUri() {
		return null;
	}

	@Override
	public <T extends BusinessBean> String postToUri(String token, String resturi, T param) {

		return null;
	}

	@Override
	public <T extends BusinessBean> T postToUri(String token, String resturi, String json, Class<T> clazz) {

		return null;
	}

	@Override
	public String postToUri(String token, String resturi, String json) {

		return null;
	}

	@Override
	public <T extends BusinessBean> T getFromUri(String token, String resturi, String param, Class<T> clazz) {

		return null;
	}

	@Override
	public String getFromUri(String token, String resturi, String param) {

		return null;
	}

	@Override
	public <T extends BusinessBean> T getFromUri(String token, String resturi, Class<T> clazz) {

		return null;
	}

	@Override
	public String getFromUri(String token, String resturi) {

		return null;
	}

}
