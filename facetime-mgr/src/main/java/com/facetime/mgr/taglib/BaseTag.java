package com.facetime.mgr.taglib;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class BaseTag extends TagSupport {
	/** */
	private static final long serialVersionUID = 8470268511556391973L;

	protected String server = null;

	protected String target = null;

	@Override
	public int doStartTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		String serverName = server == null ? request.getServerName() : server;

		String baseTag = renderBaseElement(request.getScheme(), serverName, request.getServerPort(),
				request.getRequestURI());

		JspWriter out = pageContext.getOut();
		try {
			out.write(baseTag);
		} catch (IOException e) {
			pageContext.setAttribute("org.apache.struts.action.EXCEPTION", e, 2);
			throw new JspException(e);
		}

		return 1;
	}

	public String getServer() {
		return server;
	}

	public String getTarget() {
		return target;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public static StringBuffer createServerStringBuffer(String scheme, String server, int port) {
		StringBuffer url = new StringBuffer();
		if (port < 0) {
			port = 80;
		}
		url.append(scheme);
		url.append("://");
		url.append(server);
		if (((scheme.equals("http")) && (port != 80)) || ((scheme.equals("https")) && (port != 443))) {
			url.append(':');
			url.append(port);
		}
		return url;
	}

	public static StringBuffer createServerUriStringBuffer(String scheme, String server, int port, String uri) {
		StringBuffer serverUri = createServerStringBuffer(scheme, server, port);
		serverUri.append(uri);
		return serverUri;
	}

	protected String renderBaseElement(String scheme, String serverName, int port, String uri) {
		StringBuffer tag = new StringBuffer("<base href=\"");
		tag.append(createServerUriStringBuffer(scheme, serverName, port, uri).toString());

		tag.append("\"");

		if (target != null) {
			tag.append(" target=\"");
			tag.append(target);
			tag.append("\"");
		}
		tag.append(" />");
		return tag.toString();
	}
}
