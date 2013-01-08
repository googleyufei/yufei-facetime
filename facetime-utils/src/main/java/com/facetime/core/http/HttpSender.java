package com.facetime.core.http;

import static com.facetime.core.conf.SysLogger.facetimeLogger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.impl.conn.SchemeRegistryFactory;
import org.apache.http.util.EntityUtils;

public class HttpSender {

	/**
	 * 连接监控，关闭过期连接
	 * 
	 * @author yang
	 * 
	 */
	private class IdleConnectionEvictor extends Thread {
		private final ClientConnectionManager connMgr;

		public IdleConnectionEvictor(ClientConnectionManager connMgr) {
			this.connMgr = connMgr;
		}

		@Override
		public void run() {
			try {
				while (true) {
					synchronized (this) {
						wait(30000);
						// 关闭过期连接
						connMgr.closeExpiredConnections();
						// 关闭空闲超过40s的连接
						connMgr.closeIdleConnections(40, TimeUnit.SECONDS);
					}
				}
			} catch (Exception ex) {
				facetimeLogger.warn("", ex);
			}
		}
	}

	// 日志
	private static PoolingClientConnectionManager cm;

	public static final String buildGetErrorMsg(String token, String resturl, String search, String result) {
		return buildGetMsg(false, token, resturl, search, result);
	}

	public static final String buildGetMsg(boolean isOK, String token, String resturl, String search, String result) {
		search = search != null ? search : "";
		return String.format("[getFromUrl] %s! [result] is: %s ! [token:%s]-[serviceUri:%s]-[search:%s] ", isOK ? "OK"
				: "Fail", result, token, resturl, search);
	}

	public static final String buildGetOKMsg(String token, String resturl, String search, String result) {
		return buildGetMsg(true, token, resturl, search, result);
	}

	public static final String buildPostErrorMsg(String token, String resturl, String json, String result) {
		return buildPostMsg(false, token, resturl, json, result);
	}

	public static final String buildPostMsg(boolean isOK, String token, String resturl, String json, String result) {
		return String.format("[postToUrl] %s ! [result] is: %s ! [token:%s]-[serviceUri:%s]-[json:%s] ", isOK ? "OK"
				: "Fail", result, token, resturl, json);
	}

	public static final String buildPostOKMsg(String token, String resturl, String json, String result) {
		return buildPostMsg(true, token, resturl, json, result);
	}

	public static HttpSender get() {
		return instance;
	}

	private HttpClient httpClient;

	private static HttpSender instance = new HttpSender();

	private HttpSender() {
		cm = new PoolingClientConnectionManager(SchemeRegistryFactory.createDefault(), 30, TimeUnit.SECONDS);
		// 最大连接数
		cm.setMaxTotal(10000);
		// 每个路由的默认最大连接数
		cm.setDefaultMaxPerRoute(1000);
		// 启动监控线程
		new IdleConnectionEvictor(cm).start();

		httpClient = new DefaultHttpClient(cm);
	}

	/**
	 * get
	 * @param token
	 * @param parameters
	 * @param urlPath
	 * 
	 * @return
	 */
	public HttpEntity get(final String token, final String serviceUri, final String parameters) {
		String getURL = serviceUri;
		if (parameters != null && !parameters.isEmpty()) {
			getURL = getURL + "?" + parameters;
		}
		HttpGet httpGet = new HttpGet(getURL);
		httpGet.setHeader(HttpConstants.KEY_USER_TOKEN, token != null ? token : "");
		httpGet.setHeader("Content-Type", "text/plain; charset=UTF-8");
		HttpEntity entity = null;
		try {
			HttpResponse response = httpClient.execute(httpGet);
			entity = response.getEntity();
		} catch (Exception ex) {
			facetimeLogger.error(buildGetErrorMsg(token, serviceUri, parameters, ""), ex);
		}
		return entity;
	}

	public String getFromUrl(String token, String resturl, final String parameters) {
		String result = "";
		try {
			HttpEntity entity = get(token, resturl, parameters);
			if (entity != null) {
				result = EntityUtils.toString(entity);
			} else {
				result = ErrorType.ERR_404;
			}
		} catch (Exception ex) {
			facetimeLogger.error(buildGetErrorMsg(token, resturl, parameters, result), ex);
			result = ErrorType.ERR_500;
		}
		return result;
	}

	/**
	 * post
	 * 
	 * @param urlPath
	 * @param postData
	 * @param parameters
	 * @param headers
	 * @return
	 */
	public HttpEntity post(final String urlPath, final HttpEntity postData, final String parameters,
			Map<String, String> headers) {
		HttpEntity result = null;
		String postURL = urlPath;
		if (parameters != null && !parameters.isEmpty()) {
			postURL = postURL + "?" + parameters;
		}
		HttpPost httpPost = new HttpPost(postURL);
		for (String key : headers.keySet()) {
			httpPost.setHeader(key, headers.get(key));
		}
		try {
			httpPost.setEntity(postData);
			HttpResponse response = httpClient.execute(httpPost);
			result = response.getEntity();
			httpPost.abort();
		} catch (IOException ex) {
			facetimeLogger.error("url:" + postURL + ", parameters:" + parameters, ex);
		}
		return result;
	}

	/**
	 * post
	 * @param token
	 * @param postData
	 * @param parameters
	 * @param urlPath
	 * 
	 * @return
	 */
	public String post(String token, String resturl, final HttpEntity postData, final String parameters) {
		String result = null;
		String postURL = resturl;
		try {
			Map<String, String> headers = new HashMap<String, String>();
			headers.put(HttpConstants.KEY_USER_TOKEN, token != null ? token : "");
			HttpEntity entity = post(postURL, postData, parameters, headers);
			if (entity != null) {
				result = EntityUtils.toString(entity);
			} else {
				result = ErrorType.ERR_404;
			}
		} catch (IOException ex) {
			facetimeLogger.error(buildPostErrorMsg(token, resturl, "", result), ex);
			result = ErrorType.ERR_500;
		}
		return result;
	}

	/**
	 * 
	 * @param token
	 * @param parameters
	 * @param urlPath
	 * @return
	 */
	public HttpEntity post(String token, String resturl, final String parameters) {
		String getURL = resturl;
		if (parameters != null && !parameters.isEmpty()) {
			getURL = getURL + "?" + parameters;
		}
		HttpPost httpPost = new HttpPost(getURL);
		httpPost.setHeader(HttpConstants.KEY_USER_TOKEN, token != null ? token : "");
		HttpEntity entity = null;
		try {
			HttpResponse response = httpClient.execute(httpPost);
			entity = response.getEntity();
		} catch (Exception ex) {
			facetimeLogger.error(getURL, ex);
		}
		return entity;
	}

	/**
	 * post
	 * 
	 */
	public String postToUrl(String token, String resturl, final String postData) {
		String result = null;
		try {
			HttpEntity postEntity = new StringEntity(postData, "UTF-8");
			Map<String, String> headers = setRequestHead(token);
			HttpEntity entity = post(resturl, postEntity, null, headers);
			if (entity != null) {
				result = EntityUtils.toString(entity);
			} else {
				result = ErrorType.ERR_404;
			}
		} catch (IOException ex) {
			facetimeLogger.error(buildPostErrorMsg(token, resturl, postData, result), ex);
			result = ErrorType.ERR_500;
		}
		return result;
	}

	/**
	 * @param token
	 * @return
	 */
	private Map<String, String> setRequestHead(String token) {
		Map<String, String> headers = new HashMap<String, String>();
		if (token != null) {
			headers.put(HttpConstants.KEY_USER_TOKEN, token);
		} else {
			headers.put(HttpConstants.KEY_USER_TOKEN, "");
		}
		headers.put("Content-Type", "text/plain; charset=UTF-8");
		return headers;
	}
}
