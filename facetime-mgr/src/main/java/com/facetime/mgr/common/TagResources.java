package com.facetime.mgr.common;

/**
 * <p>Title: 自助终端监控系统VIEW3.0.0</p>
 * <p>Description: 封装了struts的读消息资源的类</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: GRGBanking  Co.,Ltd</p>
 * @author lvjm
 * @version 1.0
 */
import com.facetime.spring.support.SpringContextUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

public class TagResources {

	public TagResources() {
	}

	public static String message(PageContext pageContext, String key) {
		return SpringContextUtils.getAppContext().getMessage(key, null, null);
	}

	public static String message(PageContext pageContext, String key, Object arg0) throws JspException {
		return message(pageContext, key, new Object[] { arg0 });
	}

	public static String message(PageContext pageContext, String key, Object args[]) throws JspException {
		return SpringContextUtils.getAppContext().getMessage(key, args, pageContext.getRequest().getLocale());
	}
}