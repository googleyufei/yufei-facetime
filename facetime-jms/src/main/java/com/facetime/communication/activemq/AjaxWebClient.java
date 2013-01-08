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

import com.facetime.communication.bean.MessageDTO;
import com.facetime.communication.bean.MessageType;
import com.facetime.communication.bean.SimpleUserInfoDTO;
import com.facetime.communication.bean.SimpleUserInfosDTO;
import com.facetime.communication.utils.CookieConstants;
import com.facetime.core.bean.UserToken;
import com.facetime.core.http.HttpConstants;
import com.facetime.core.http.HttpSender;
import com.facetime.core.http.PojoMapper;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.activemq.MessageAvailableConsumer;

/*
 * Collection of all data needed to fulfill requests from a single web client.
 */
public class AjaxWebClient extends AmqConsumer {
	private final static String serverId = UUID.randomUUID().toString();

	private static long index = 0;

	// an instance which has not been accessed in this many milliseconds can be
	// removed.
	final long expireAfter = 60 * 1000;

	Map<MessageAvailableConsumer, String> idMap;
	Map<MessageAvailableConsumer, String> destinationNameMap;
	AjaxListener listener;
	Long lastAccessed;

	// client token
	private String token;

	private String serverProxyId = serverId + "-" + (index++);

	public AjaxWebClient(HttpServletRequest request, long maximumReadTimeout) {
		// 'id' meaning the first argument to the JavaScript addListener()
		// function.
		// used to indicate which JS callback should handle a given message.
		this.idMap = new HashMap<MessageAvailableConsumer, String>();

		// map consumers to destinations like topic://test, etc.
		this.destinationNameMap = new HashMap<MessageAvailableConsumer, String>();

		this.listener = new AjaxListener(this, maximumReadTimeout);

		this.lastAccessed = this.getNow();

		token = extractToken(request);
	}

	/**
	 * Get token from request, if exist return null
	 * 
	 * @param request
	 * @return
	 */
	public String extractToken(HttpServletRequest request) {
		String token = null;

		// get token from parameter
		token = request.getParameter(CookieConstants.COOKIE_USER_TOKEN);
		// from header
		if (token == null) {
			token = request.getHeader(CookieConstants.COOKIE_USER_TOKEN);
		}

		if (token == null) {
			token = request.getParameter(HttpConstants.KEY_USER_TOKEN);
		}

		if (token == null) {
			token = request.getHeader(HttpConstants.KEY_USER_TOKEN);
		}

		if (token != null) {
			try {
				token = URLDecoder.decode(token, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		if (token != null) {
			this.token = token;
		}

		return token;
	}

	public Map<MessageAvailableConsumer, String> getIdMap() {
		return this.idMap;
	}

	public Map<MessageAvailableConsumer, String> getDestinationNameMap() {
		return this.destinationNameMap;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		facetimeLogger.info("set token, old token=" + this.token + ", new token=" + token);
		this.token = token;
	}

	public AjaxListener getListener() {
		return this.listener;
	}

	public long getMillisSinceLastAccessed() {
		return this.getNow() - this.lastAccessed;
	}

	public void updateLastAccessed() {
		this.lastAccessed = this.getNow();
		listener.access();
	}

	public boolean closeIfExpired() {
		boolean returnVal = false;
		if (this.getMillisSinceLastAccessed() > this.expireAfter) {
			// 退出系统，发送消息通知
			if (token != null) {
				try {
					synchronized (this) {
						new SignOutThread(new UserToken(token)).start();
					}
				} catch (Exception e) {
				}
			}

			this.close();
			returnVal = true;
		}
		return returnVal;
	}

	protected long getNow() {
		return System.currentTimeMillis();
	}

	public String getServerProxyId() {
		return serverProxyId;
	}

	private static HttpSender proxy = HttpSender.get();

	/**
	 * 检查token是否最后有效token
	 * 
	 * @param token
	 * @return
	 */
	public static boolean checkToken(UserToken token) {
		// TODO
		String result = proxy.postToUrl(HttpConstants.checkToken, token.getUserTokenStr(), token.getUserTokenStr());
		return Boolean.valueOf(result);
	}

	/**
	 * 检查token是否最后有效token
	 * 
	 * @param token
	 * @return
	 */
	public static boolean checkToken(String token) {
		try {
			UserToken clientToken = new UserToken(token);
			return checkToken(clientToken);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 用户退出系统线程
	 */
	private static class SignOutThread extends Thread {
		private UserToken clientToken;

		public SignOutThread(UserToken clientToken) {
			this.clientToken = clientToken;
		}

		@Override
		public void run() {
			super.run();

			boolean isTheLastEffective = checkToken(clientToken);

			facetimeLogger.info("client token=" + (clientToken != null ? clientToken : "null")
					+ " try logout, isTheLastEffective=" + isTheLastEffective);

			if (isTheLastEffective) {
				/**
				 * sign out
				 */
				//				proxy.postToUrl(cloudServiceUrl(HttpConstants.logout), clientToken.getUserIdAndToken(),
				//						clientToken.getUserIdAsString());
				final long logOutUserId = clientToken.getUserId();
				try {
					//					String usersJson = proxy.postToUrl(cloudServiceUrl(HttpConstants.getOnlineBuddyAndColleague),
					//							clientToken.getUserIdAndToken(), String.valueOf(logOutUserId));
					String usersJson = null;
					SimpleUserInfosDTO userInfosDTO = PojoMapper.getObject(usersJson, SimpleUserInfosDTO.class);
					if (userInfosDTO != null) {
						List<SimpleUserInfoDTO> userInfos = userInfosDTO.getUserList();
						if (userInfos != null) {
							/**
							 * send message(type : UserLeave) inform user's
							 * buddies and colleagues
							 */
							// setup message
							MessageDTO messageDTO = new MessageDTO();
							messageDTO.setSender(logOutUserId);
							messageDTO.setMessageType(MessageType.UserLeave);
							messageDTO.setSendDate(new Date());
							for (SimpleUserInfoDTO userInfo : userInfos) {
								messageDTO.setReceiver(userInfo.getUserId());
								// send message
								AmqProducer.getInstance().sendMessage(messageDTO);
							}
						}
					}
				} catch (Exception e) {
					facetimeLogger.warn(e);
				}
			}
		}
	}
}
