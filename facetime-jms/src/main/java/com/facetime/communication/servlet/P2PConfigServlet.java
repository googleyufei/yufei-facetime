package com.facetime.communication.servlet;

import com.facetime.core.conf.ConfigConstants;
import com.facetime.core.conf.ConfigUtils;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * p2p servlet
 * 
 * @mapping /getP2PConfig
 */
public class P2PConfigServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().write(ConfigUtils.getProperty(ConfigConstants.P2P_HOST_KEY));
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		doGet(request, response);
	}

}
