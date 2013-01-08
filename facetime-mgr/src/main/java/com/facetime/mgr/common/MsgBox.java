package com.facetime.mgr.common;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

public class MsgBox {

	public static final String BUTTON_CLOSE = "CLOSE";// 关闭窗口

	public static final String BUTTON_NONE = "NONE";// 在消息框中不显示命令按钮

	// 消息框中命令button的类型取值：
	public static final String BUTTON_OK = "OK";// 跳转到backUrl所指示的页面

	public static final String BUTTON_RETURN = "RETURN";// 返回原来的页面

	/**
	 * 用来把消息对象保存在request属性中的key查找索引
	 */
	public static final String KEY = "MSGBOX.KEY";

	// 指示返回操作要跳转的目标框架（父框架，本身所在框架，最顶层框架）
	public static final String TARGET_PARENT = "PARENT";

	public static final String TARGET_SELF = "SELF";

	public static final String TARGET_TOP = "TOP";

	private String backUrl = "/";// 点击命令按钮要激发跳转的URL信息，可不包括参数

	private String buttonType = BUTTON_RETURN;// 默认返回上一页面

	private Exception errorObj;// 记录异常对象

	private String msgInfo;// 要显示的消息

	private String target = TARGET_SELF;// 默认本身所在框架
	/**保存跳转URL中要传递的参数名和参数值*/
	private Map<String, String> urlParameters;

	/**
	 * 显示定义在全局i18n资源文件中的信息
	 *
	 * @param request
	 * @param msgKey
	 *            资源文件中的key
	 */
	public MsgBox(HttpServletRequest request, String msgKey) {
		msgInfo = MsgResources.message(request, msgKey);
		save(request);
	}

	/**
	 * 显示全局资源文件中的信息，可以传入参数
	 *
	 * @param request
	 * @param msgKey
	 *            资源文件中的key
	 * @param msgArgs
	 *            显示信息中的参数
	 */
	public MsgBox(HttpServletRequest request, String msgKey, Object[] msgArgs) {
		msgInfo = MsgResources.message(request, msgKey, msgArgs);
		save(request);
	}

	/**
	 * 返回根据backUrl和urlParameters拼凑成的url,例如/datadir.do?id=1&menuId=2
	 *
	 * @return
	 */
	public String getBackUrl() {
		if (urlParameters != null) {
			StringBuffer sbf = new StringBuffer();
			if (backUrl.indexOf("?") > 0) {
				sbf.append(backUrl).append("&0=0");
			} else {
				sbf.append(backUrl).append("?0=0");
			}

			Set<String> keySet = urlParameters.keySet();
			for (String key : keySet) {
				sbf.append("&").append(key).append("=").append(urlParameters.get(key).toString());
			}
			return sbf.toString();
		}

		return backUrl;
	}

	public String getButtonType() {
		return buttonType;
	}

	public Exception getErrorObj() {
		return errorObj;
	}

	public String getErrorStack() {

		if (errorObj == null) {
			return null;
		}

		try {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			errorObj.printStackTrace(pw);
			return sw.toString();
		} catch (Exception e2) {
			return "(bad stack2string)".concat(errorObj.getMessage());
		}
	}

	/**
	 * 返回要显示的消息
	 */
	public String getMsgInfo() {
		return msgInfo;
	}

	public String getTarget() {
		return target;
	}

	public Map<String, String> getUrlParameters() {
		return urlParameters;
	}

	public void setBackUrl(String backUrl) {
		this.backUrl = backUrl;
	}

	public void setButtonType(String buttonType) {
		this.buttonType = buttonType.toUpperCase();
	}

	public void setErrorObj(Exception errorObj) {
		this.errorObj = errorObj;
	}

	public void setTarget(String target) {
		this.target = target.toUpperCase();
	}

	/**
	 * 复制request的参数设置
	 *
	 * @param request
	 */
	public void setUrlParameters(HttpServletRequest request) {
		Enumeration<?> em = request.getParameterNames();
		Map<String, String> hm = new HashMap<String, String>();
		String key;
		while (em.hasMoreElements()) {
			key = em.nextElement().toString();
			hm.put(key, request.getParameter(key));
		}
		urlParameters = hm;
	}

	/**
	 * 设置要传递的参数
	 *
	 * @param urlParameters
	 */
	public void setUrlParameters(Map<String, String> urlParameters) {
		this.urlParameters = urlParameters;
	}

	private void save(HttpServletRequest request) {
		// 保存当前的消息对话框对象
		request.setAttribute(KEY, this);
	}
}
