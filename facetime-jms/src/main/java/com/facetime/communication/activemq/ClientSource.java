package com.facetime.communication.activemq;

import com.facetime.core.http.HttpConstants;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 
 * @author jinkerjiang
 * 
 */
public class ClientSource {
	private static HashMap<String, AjaxWebClient> ajaxWebClients = new HashMap<String, AjaxWebClient>();

	public static AjaxWebClient getAjaxWebClient(HttpServletRequest request) {
		HttpSession session = request.getSession(true);

		String clientId = request.getParameter(HttpConstants.QUER_KEY_CLIENT_ID);

		if (clientId == null) {
			clientId = request.getHeader(HttpConstants.QUER_KEY_CLIENT_ID);
		}

		if (clientId == null) {
			Object object = request.getAttribute(HttpConstants.QUER_KEY_CLIENT_ID);
			if (object != null) {
				clientId = (String) object;
			}
		}
		if (clientId == null) {
			return null;
		}

		String sessionKey = session.getId() + '-' + clientId;

		AjaxWebClient client = ajaxWebClients.get(sessionKey);
		synchronized (ajaxWebClients) {
			// create a new AjaxWebClient if one does not already exist for this
			// sessionKey.
			if (client == null) {
				client = new AjaxWebClient(request, MessageListenerServlet.maximumReadTimeout);
				ajaxWebClients.put(sessionKey, client);
			}
			client.updateLastAccessed();
		}
		return client;
	}

	public static HashMap<String, AjaxWebClient> getAjaxWebClients() {
		return ajaxWebClients;
	}

}
