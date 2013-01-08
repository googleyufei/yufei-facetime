/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.facetime.communication.activemq;

import static com.facetime.core.conf.SysLogger.facetimeLogger;

import com.facetime.communication.utils.AmqMsgType;
import com.facetime.communication.utils.ParamKey;
import com.facetime.communication.utils.Result;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.activemq.MessageAvailableConsumer;
import org.eclipse.jetty.continuation.Continuation;
import org.eclipse.jetty.continuation.ContinuationSupport;

/**
 * A servlet for sending and receiving messages to/from JMS destinations using
 * HTTP POST for sending and HTTP GET for receiving.
 * <p/>
 * You can specify the destination and whether it is a topic or queue via
 * configuration details on the servlet or as request parameters.
 * <p/>
 * For reading messages you can specify a readTimeout parameter to determine how
 * long the servlet should block for. The servlet can be configured with the
 * following init parameters:
 * <dl>
 * <dt>defaultReadTimeout</dt>
 * <dd>The default time in ms to wait for messages. May be overridden by a
 * request using the 'timeout' parameter</dd>
 * <dt>maximumReadTimeout</dt>
 * <dd>The maximum value a request may specify for the 'timeout' parameter</dd>
 * <dt>maximumMessages</dt>
 * <dd>maximum messages to send per response</dd>
 * <dt></dt>
 * <dd></dd>
 * </dl>
 * 
 * @version $Revision: 1.1.1.1 $
 */
public class MessageListenerServlet extends MessageServletSupport {
	private static final long serialVersionUID = 7743318784473549238L;

	private String readTimeoutParameter = ParamKey.TIMEOUT;
	private long defaultReadTimeout = -1;
	public static long maximumReadTimeout = 25000;
	private int maximumMessages = 100;
	private Timer clientCleanupTimer = new Timer();

	public void init() throws ServletException {
		ServletConfig servletConfig = getServletConfig();
		String name = servletConfig.getInitParameter("defaultReadTimeout");
		if (name != null) {
			defaultReadTimeout = asLong(name);
		}
		name = servletConfig.getInitParameter("maximumReadTimeout");
		if (name != null) {
			maximumReadTimeout = asLong(name);
		}
		name = servletConfig.getInitParameter("maximumMessages");
		if (name != null) {
			maximumMessages = (int) asLong(name);
		}
		clientCleanupTimer.schedule(new ClientCleaner(), 5000, 120000);
	}

	/**
	 * Sends a message to a destination or manage subscriptions. If the the
	 * content type of the POST is
	 * <code>application/x-www-form-urlencoded</code>, then the form parameters
	 * "destination", "message" and "type" are used to pass a message or a
	 * subscription. If multiple messages or subscriptions are passed in a
	 * single post, then additional parameters are shortened to "dN", "mN" and
	 * "tN" where N is an index starting from 1. The type is either "send",
	 * "listen" or "unlisten". For send types, the message is the text of the
	 * TextMessage, otherwise it is the ID to be used for the subscription. If
	 * the content type is not <code>application/x-www-form-urlencoded</code>,
	 * then the body of the post is sent as the message to a destination that is
	 * derived from a query parameter, the URL or the default destination.
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {

		// lets turn the HTTP post into a JMS Message
		AjaxWebClient client = ClientSource.getAjaxWebClient(request);

		response.setContentType("text/plain");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter writer = response.getWriter();

		String result = Result.Success.toString();

		if (client != null) {
			synchronized (client) {
				client.extractToken(request);

				result = doJms(request, response, client);

				if ("true".equals(request.getParameter("poll"))) {
					try {
						doMessages(client, request, response);
					} catch (JMSException e) {
						result = Result.ErrorMqException.toString();
					}
				}
			}
		} else {
			result = "no client";
		}

		writer.print(result);
	}

	private String doJms(HttpServletRequest request, HttpServletResponse response, AjaxWebClient client)
			throws IOException {
		int messages = 0;

		String result = Result.Success.toString();

		String type = request.getParameter("type");

		if (AmqMsgType.SET_PRAMA.equals(type)) {
			client.extractToken(request);
		} else {
			// loop until no more messages
			while (true) {
				// Get the message parameters. Multiple messages are encoded
				// with more compact parameter names.
				String destinationName = request.getParameter(messages == 0 ? "destination" : ("d" + messages));

				if (destinationName == null) {
					destinationName = request.getHeader("destination");
				}

				String message = request.getParameter(messages == 0 ? "message" : ("m" + messages));

				type = request.getParameter(messages == 0 ? "type" : ("t" + messages));

				if (destinationName == null || message == null || type == null) {
					if (messages == 0) {
						result = "destination or message or type is null";
					}
					break;
				}

				try {
					Destination destination = getDestination(client, request, destinationName);

					if (facetimeLogger.isDebugEnabled()) {
						facetimeLogger.debug(messages + " destination=" + destinationName + " message=" + message
								+ " type=" + type);
						facetimeLogger.debug(destination + " is a " + destination.getClass().getName());
					}

					messages++;

					if (AmqMsgType.LISTEN.equals(type)) {
						AjaxListener listener = client.getListener();
						Map<MessageAvailableConsumer, String> consumerIdMap = client.getIdMap();
						Map<MessageAvailableConsumer, String> consumerDestinationNameMap = client
								.getDestinationNameMap();
						client.closeConsumer(destination); // drop any existing
															// consumer.
						MessageAvailableConsumer consumer = (MessageAvailableConsumer) client.getConsumer(destination,
								request.getHeader(AmqConsumer.selectorName));

						consumer.setAvailableListener(listener);
						consumerIdMap.put(consumer, message);
						consumerDestinationNameMap.put(consumer, destinationName);
						facetimeLogger.info("Subscribed: " + consumer + " to " + destination + " id=" + message
								+ " client=" + client);
					} else if (AmqMsgType.UNLISTEN.equals(type)) {
						Map<MessageAvailableConsumer, String> consumerIdMap = client.getIdMap();
						Map<MessageAvailableConsumer, String> consumerDestinationNameMap = client
								.getDestinationNameMap();
						MessageAvailableConsumer consumer = (MessageAvailableConsumer) client.getConsumer(destination,
								request.getHeader(AmqConsumer.selectorName));

						consumer.setAvailableListener(null);
						consumerIdMap.remove(consumer);
						consumerDestinationNameMap.remove(consumer);
						client.closeConsumer(destination);
						if (facetimeLogger.isDebugEnabled()) {
							facetimeLogger.debug("Unsubscribed: " + consumer);
						}
					} else if (AmqMsgType.SEND.equals(type)) {
						TextMessage text = client.getSession().createTextMessage(message);
						appendParametersToMessage(request, text);

						client.send(destination, text);
						if (facetimeLogger.isDebugEnabled()) {
							facetimeLogger.debug("Sent " + message + " to " + destination);
						}
					} else {
						facetimeLogger.warn("unknown type " + type);
						result = "unknown type " + type;
					}
				} catch (JMSException e) {
					facetimeLogger.warn("jms", e);
					result = Result.ErrorMqException.toString();
				}
			}
		}
		return result;
	}

	/**
	 * Supports a HTTP DELETE to be equivlanent of consuming a singe message
	 * from a queue
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			AjaxWebClient client = ClientSource.getAjaxWebClient(request);

			if (client == null) {
				response.getWriter().println("no clientId");
				return;
			}

			if (facetimeLogger.isDebugEnabled()) {
				facetimeLogger.debug("GET client=" + client + " session=" + request.getSession().getId() + " clientId="
						+ request.getParameter("clientId") + " uri=" + request.getRequestURI() + " query="
						+ request.getQueryString());
			}

			client.extractToken(request);

			doMessages(client, request, response);
		} catch (JMSException e) {
			response.getWriter().write("JMS problem: " + e);
			throw new ServletException("JMS problem: " + e, e);
		}
	}

	/**
	 * Reads a message from a destination up to some specific timeout period
	 * 
	 * @param client
	 *            The webclient
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doMessages(AjaxWebClient client, HttpServletRequest request, HttpServletResponse response)
			throws JMSException, IOException {
		int messages = 0;
		// This is a poll for any messages

		long timeout = getReadTimeout(request);

		Message message = null;
		message = (Message) request.getAttribute("message");

		synchronized (client) {
			List<MessageConsumer> consumers = client.getConsumers();

			MessageAvailableConsumer consumer = (MessageAvailableConsumer) request.getAttribute("consumer");

			/**
			 * message 为空代表请求第一次发起或continuation 超时发起，不为空代表这一请求为
			 * {@link Continuation#suspend()}之后 {@link Continuation#resume()}
			 * 发起并且有新消息到达
			 */
			if (message == null) {
				// Look for a message that is ready to go
				for (int i = 0; message == null && i < consumers.size(); i++) {
					consumer = (MessageAvailableConsumer) consumers.get(i);
					if (consumer.getAvailableListener() == null) {
						continue;
					}

					// Look for any available messages
					message = consumer.receive(10);
					// facetimeLogger.info("received " + message + " from " + consumer);
				}
			}

			// prepare the response
			response.setContentType("text/xml;charset=UTF-8");
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader(ParamKey.SERVER_ID, client.getServerProxyId());

			if (message == null) {
				Continuation continuation = ContinuationSupport.getContinuation(request);

				if (continuation.isExpired() || timeout == 0) {
					response.setStatus(HttpServletResponse.SC_OK);
					StringWriter swriter = new StringWriter();
					PrintWriter writer = new PrintWriter(swriter);
					writer.println("<ajax-response>");
					writer.print("</ajax-response>");

					writer.flush();
					String m = swriter.toString();
					response.getWriter().println(m);

					return;
				}

				continuation.setTimeout(timeout);
				continuation.suspend();

				// Fetch the listeners
				AjaxListener listener = client.getListener();

				// register this continuation with our listener.
				listener.setContinuation(continuation);

				return;
			}

			StringWriter swriter = new StringWriter();
			PrintWriter writer = new PrintWriter(swriter);

			Map<MessageAvailableConsumer, String> consumerIdMap = client.getIdMap();
			Map<MessageAvailableConsumer, String> consumerDestinationNameMap = client.getDestinationNameMap();
			response.setStatus(HttpServletResponse.SC_OK);
			writer.println("<ajax-response>");

			// Send any message we already have
			if (message != null) {
				String id = consumerIdMap.get(consumer);
				String destinationName = consumerDestinationNameMap.get(consumer);
				writeMessageResponse(writer, message, id, destinationName);

				messages++;
			}

			// Send the rest of the messages
			for (int i = 0; i < consumers.size() && messages < maximumMessages; i++) {
				consumer = (MessageAvailableConsumer) consumers.get(i);
				if (consumer.getAvailableListener() == null) {
					continue;
				}

				// Look for any available messages
				while (messages < maximumMessages) {
					message = consumer.receiveNoWait();
					if (message == null) {
						break;
					}
					messages++;
					String id = consumerIdMap.get(consumer);
					String destinationName = consumerDestinationNameMap.get(consumer);
					writeMessageResponse(writer, message, id, destinationName);
				}
			}

			writer.print("</ajax-response>");

			writer.flush();
			String m = swriter.toString();
			response.getWriter().println(m);
		}
	}

	protected void writeMessageResponse(PrintWriter writer, Message message, String id, String destinationName)
			throws JMSException, IOException {
		writer.print("<response id='");
		writer.print(id);
		writer.print("'");
		if (destinationName != null) {
			writer.print(" destination='" + destinationName + "' ");
		}
		writer.print(">");
		if (message instanceof TextMessage) {
			TextMessage textMsg = (TextMessage) message;
			String txt = textMsg.getText();
			if (txt.startsWith("<?")) {
				txt = txt.substring(txt.indexOf("?>") + 2);
			}
			writer.print(txt);
		} else if (message instanceof ObjectMessage) {
			ObjectMessage objectMsg = (ObjectMessage) message;
			Object object = objectMsg.getObject();
			writer.print(object.toString());
		}
		writer.println("</response>");
	}

	/**
	 * @return the timeout value for read requests which is always >= 0 and <=
	 *         maximumReadTimeout to avoid DoS attacks
	 */
	protected long getReadTimeout(HttpServletRequest request) {
		long answer = defaultReadTimeout;

		String name = request.getParameter(readTimeoutParameter);
		if (name != null) {
			answer = asLong(name);
		}
		if (answer < 0 || answer > maximumReadTimeout) {
			answer = maximumReadTimeout;
		}
		return answer;
	}

	/*
	 * an instance of this class runs every minute (started in init), to clean
	 * up old web clients & free resources.
	 */
	private class ClientCleaner extends TimerTask {
		public void run() {
			HashMap<String, AjaxWebClient> ajaxWebClients = ClientSource.getAjaxWebClients();

			synchronized (ajaxWebClients) {
				Iterator<Entry<String, AjaxWebClient>> it = ajaxWebClients.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry<String, AjaxWebClient> e = (Map.Entry<String, AjaxWebClient>) it.next();
					String key = e.getKey();
					AjaxWebClient client = e.getValue();

					synchronized (client) {
						// close an expired client and remove it from the
						// ajaxWebClients hash.
						if (client.closeIfExpired()) {
							facetimeLogger.debug("Removing expired AjaxWebClient " + key);
							it.remove();
						}
					}
				}
			}
		}
	}
}
