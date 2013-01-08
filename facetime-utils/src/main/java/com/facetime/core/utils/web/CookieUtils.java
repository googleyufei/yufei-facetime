/**
 * cookie的增、删、查工具类
 */
package com.facetime.core.utils.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.facetime.core.conf.ConfigUtils;

public class CookieUtils {

	private static final int MAX_AGE = ConfigUtils.IS_DEV_MODE ? 24 * 3600000 : 7 * 24 * 3600000;

	public static Cookie getCookie(HttpServletRequest request, String name) {
		Cookie cookies[] = request.getCookies();
		if (cookies == null || name == null || name.length() == 0) {
			return null;
		}
		for (int i = 0; i < cookies.length; i++) {
			if (name.equals(cookies[i].getName()) && request.getServerName().equals(cookies[i].getDomain())) {
				return cookies[i];
			}
		}
		return null;
	}

	public static String getCookieValue(HttpServletRequest request, String name) {
		Cookie cookies[] = request.getCookies();
		if (cookies == null || name == null || name.length() == 0) {
			return null;
		}
		for (int i = 0; i < cookies.length; i++) {
			if (name.equals(cookies[i].getName())) {
				return cookies[i].getValue();
			}
		}
		return null;
	}

	public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
		Cookie cookie = getCookie(request, cookieName);
		if (cookie != null) {
			cookie.setPath(getPath(request));
			cookie.setValue("");
			cookie.setMaxAge(0);
			response.addCookie(cookie);
		}
	}

	public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, Cookie cookie) {
		if (cookie != null) {
			cookie.setPath(getPath(request));
			cookie.setValue("");
			cookie.setMaxAge(0);
			response.addCookie(cookie);
		}
	}

	public static void setCookie(HttpServletRequest request, HttpServletResponse response, String name, String value) {
		setCookie(request, response, name, value, MAX_AGE);
	}

	public static void setCookie(HttpServletRequest request, HttpServletResponse response, String name, String value,
			int maxAge) {
		setCookieToPath(response, name, value, getPath(request));
	}

	private static String getPath(HttpServletRequest request) {
		String path = request.getContextPath();
		return (path == null || path.length() == 0) ? "/" : path;
	}

	/**
	 * Set enterprise name into cookie
	 *
	 * @param entName
	 */
	public static void setCookieToRootPath(HttpServletResponse response, String name, String value) {
		setCookieToPath(response, name, value, "/");
	}

	/**
	 * Set enterprise name into cookie
	 *
	 * @param entName
	 */
	public static void setCookieToPath(HttpServletResponse response, String name, String value, String path) {
		Cookie cookie = new Cookie(name, value == null ? "" : value);
		cookie.setMaxAge(MAX_AGE);
		cookie.setPath(path);
		response.addCookie(cookie);
	}

	/**
	 * 删除所有cookie
	 */
	public static void deleteAllCookies(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie _cookie = new Cookie(cookies[i].getName(), "");
				_cookie.setMaxAge(0);
				_cookie.setPath("/");
				response.addCookie(_cookie);
			}
		}
	}
}
