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

import javax.jms.MessageConsumer;

import org.apache.activemq.MessageAvailableListener;
import org.eclipse.jetty.continuation.Continuation;

/*
 * Listen for available messages and wakeup any continuations.
 */
public class AjaxListener implements MessageAvailableListener {
	private long maximumReadTimeout;
	private AjaxWebClient client;
	private long lastAccess;
	private Continuation continuation;

	AjaxListener(AjaxWebClient client, long maximumReadTimeout) {
		this.client = client;
		this.maximumReadTimeout = maximumReadTimeout;
		lastAccess = System.currentTimeMillis();
	}

	public void access() {
		lastAccess = System.currentTimeMillis();
	}

	public synchronized void setContinuation(Continuation continuation) {
		this.continuation = continuation;
	}

	public synchronized void onMessageAvailable(MessageConsumer consumer) {
		if (continuation != null) {
			/**
			 * 不要在这里接收消息，因为在Firefox浏览器下刷新页面时会继续之前的get请求，将导致混乱，从而导致部分些消息接收不到，
			 * 所以统一在 {@link MessageListenerServlet}下接收消息
			 */
			// try {
			// Message message = consumer.receive(10);
			// continuation.setAttribute("message", message);
			// continuation.setAttribute("consumer", consumer);
			// } catch (Exception e) {
			// LOG.error("Error receiving message " + e, e);
			// }
			continuation.resume();
		}
		/**
		 * 当web访问较大时，当个用户访问延迟可能较长，故不在这里清理consumers
		 */
		// else if (System.currentTimeMillis() - lastAccess > 2 *
		// this.maximumReadTimeout) {
		// new Thread() {
		// public void run() {
		// client.closeConsumers();
		// };
		// }.start();
		// }
	}
}
