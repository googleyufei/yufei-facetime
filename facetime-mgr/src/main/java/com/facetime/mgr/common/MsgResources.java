package com.facetime.mgr.common;

import com.facetime.spring.support.SpringContextUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 根据struts框架特性，提供读取i18n资源信息的静态方法 只能在struts的action中使用，在普通的jsp中不一定能取到i18n资源对象
 *
 * @author ljming
 *
 */
public class MsgResources {

	/**
	 * 读取全局资源文件信息
	 */
	public static String message(HttpServletRequest request, String key) {
		return SpringContextUtils.getAppContext().getMessage(key, null, request.getLocale());
	}

	/**
	 * 带参数的全局资源文件信息
	 */
	public static String message(HttpServletRequest request, String key, Object args[]) {
		return SpringContextUtils.getAppContext().getMessage(key, args, request.getLocale());
	}
}