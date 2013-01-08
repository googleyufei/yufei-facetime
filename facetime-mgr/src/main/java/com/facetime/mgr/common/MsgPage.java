package com.facetime.mgr.common;

import com.facetime.core.utils.StringUtils;
import com.facetime.spring.support.SpringContextUtils;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.ModelMap;

/**
 *
 */
public class MsgPage {

	public static String view(ModelMap model, String urladdress, String message, Object... params) {
		if (StringUtils.isValid(message))
			model.addAttribute("message", SpringContextUtils.message(message, params));
		if (StringUtils.isValid(urladdress))
			model.addAttribute("urladdress", urladdress);
		return JspHelper.MESSAGE;
	}

	public static String view(HttpServletRequest request, String urladdress, String message, Object... params) {
		request.setAttribute("message", SpringContextUtils.message(message, params));
		request.setAttribute("urladdress", urladdress);
		return JspHelper.MESSAGE;
	}
}
