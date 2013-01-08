package com.facetime.spring.logic;

import com.facetime.core.bean.BusinessBean;
import com.facetime.core.conf.SysLogger;
import com.facetime.core.http.HttpSender;
import com.facetime.core.http.PojoMapper;

/**
 * 
 *
 * @author yufei
 * @Create_by 2012-11-29
 * @Design_by eclipse  
 */
public class RemoteLogicImpl implements RemoteLogic {

	private String baseUri;

	public String getBaseUri() {
		return baseUri;
	}

	public void setBaseUri(String baseUri) {
		this.baseUri = baseUri;
	}

	/**
	 * 远程服务请求代理
	 */
	private HttpSender httpSender = HttpSender.get();

	@Override
	public String getFromUri(String token, String resturi) {
		return getFromUri(token, baseUri + resturi, "");
	}

	@Override
	public <T extends BusinessBean> T getFromUri(String token, String resturi, Class<T> clazz) {
		String result = null;
		try {
			result = httpSender.getFromUrl(token, baseUri + resturi, "");
			return PojoMapper.getObject(result, clazz);
		} catch (Exception e) {
			SysLogger.cloudAppLogger.error(e);
			throw new AssertionError(HttpSender.buildGetErrorMsg(token, resturi, "", result));
		}
	}

	@Override
	public String getFromUri(String token, String resturi, String param) {
		String result = null;
		try {
			return result = httpSender.getFromUrl(token, baseUri + resturi, param);
		} catch (Exception e) {
			SysLogger.cloudAppLogger.error(e);
			throw new AssertionError(HttpSender.buildGetErrorMsg(token, resturi, param, result));
		}
	}

	@Override
	public <T extends BusinessBean> T getFromUri(String token, String resturi, String param, Class<T> clazz) {
		String result = null;
		try {
			result = httpSender.getFromUrl(token, baseUri + resturi, param);
			return PojoMapper.getObject(result, clazz);
		} catch (Exception e) {
			SysLogger.cloudAppLogger.error(e);
			throw new AssertionError(HttpSender.buildGetErrorMsg(token, resturi, param, result));
		}
	}

	@Override
	public String postToUri(String token, String resturi, String json) {
		String result = null;
		try {
			result = httpSender.postToUrl(token, baseUri + resturi, json);
			return result;
		} catch (Exception e) {
			SysLogger.cloudAppLogger.error(e);
			throw new AssertionError(HttpSender.buildPostErrorMsg(token, resturi, json, result));
		}
	}

	@Override
	public <T extends BusinessBean> T postToUri(String token, String resturi, String json, Class<T> clazz) {
		String result = null;
		try {
			result = httpSender.postToUrl(token, baseUri + resturi, json);
			return PojoMapper.getObject(json, clazz);
		} catch (Exception e) {
			String error = HttpSender.buildPostErrorMsg(token, resturi, json, result);
			SysLogger.cloudAppLogger.error(error);
			throw new AssertionError("postToUri fail!");
		}
	}

	@Override
	public <T extends BusinessBean> String postToUri(String token, String resturi, T param) {
		return postToUri(token, resturi, PojoMapper.toJson(param));
	}
}
