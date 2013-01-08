package com.shop.web.support;

import com.facetime.core.security.Base64;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.shop.domain.user.Buyer;
import com.shop.util.WebUtil;

public class BuyerLogicFilter implements Filter {

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		Buyer buyer = WebUtil.getBuyer(request);
		if (buyer == null) {
			String url = WebUtil.getRequestURIWithParam(request);
			String directUrl = new String(Base64.encodeToByte(url.getBytes(),
					false));
			HttpServletResponse response = (HttpServletResponse) res;
			response.sendRedirect("/shop/user/login.do?directUrl=" + directUrl);
		} else {
			chain.doFilter(req, res);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

}
