package com.facetime.spring.logic;

import com.facetime.core.bean.BusinessBean;

/**
 * 
 * 远程调用业务逻辑接口
 * @author yufei
 * @Create_by 2012-11-29
 * @Design_by eclipse  
 */
public interface RemoteLogic {

	public abstract <T extends BusinessBean> String postToUri(String token, String resturi, T param);

	public abstract <T extends BusinessBean> T postToUri(String token, String resturi, String json, Class<T> clazz);

	public abstract String postToUri(String token, String resturi, String json);

	public abstract <T extends BusinessBean> T getFromUri(String token, String resturi, String param, Class<T> clazz);

	public abstract String getFromUri(String token, String resturi, String param);

	public abstract <T extends BusinessBean> T getFromUri(String token, String resturi, Class<T> clazz);

	public abstract String getFromUri(String token, String resturi);

	public String getBaseUri();

}
