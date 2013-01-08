package com.shop.web.support;

import com.facetime.mgr.bean.UserModel;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.shop.util.SiteUrl;
import com.shop.util.WebUtil;

public class PrivilegeFilter implements Filter {

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
			ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		UserModel employee = WebUtil.getEmployee(request);
		if (employee == null) {
			HttpServletResponse response = (HttpServletResponse) res;
			response.sendRedirect(SiteUrl.readUrl("employee.login.ui"));
		} else {
			chain.doFilter(req, res);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

}
