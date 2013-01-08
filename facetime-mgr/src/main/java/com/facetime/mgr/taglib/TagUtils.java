package com.facetime.mgr.taglib;

import javax.servlet.jsp.PageContext;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.util.Assert;

public class TagUtils {

	/** Constant identifying the page scope */
	public static final String SCOPE_PAGE = "page";

	/** Constant identifying the request scope */
	public static final String SCOPE_REQUEST = "request";

	/** Constant identifying the session scope */
	public static final String SCOPE_SESSION = "session";

	/** Constant identifying the application scope */
	public static final String SCOPE_APPLICATION = "application";

	public static int getScope(String scope) {
		Assert.notNull(scope, "Scope to search for cannot be null");
		if (scope.equals(SCOPE_REQUEST)) {
			return PageContext.REQUEST_SCOPE;
		} else if (scope.equals(SCOPE_SESSION)) {
			return PageContext.SESSION_SCOPE;
		} else if (scope.equals(SCOPE_APPLICATION)) {
			return PageContext.APPLICATION_SCOPE;
		} else {
			return PageContext.PAGE_SCOPE;
		}
	}

	public static Object lookup(PageContext pageContext, String beanName, String attrName, String scope) {
		Object bean = null;
		if (scope != null) {
			bean = pageContext.getAttribute(beanName, getScope(scope));
		} else {
			bean = pageContext.findAttribute(beanName);
		}
		try {
			Object attrValue = PropertyUtils.getProperty(bean, attrName);
			return attrValue;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
