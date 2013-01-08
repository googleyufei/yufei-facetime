package com.facetime.core.http;

public interface ErrorType {
	String ERR_404 = "ERROR_404";
	String ERR_500 = "ERROR_500";
	/** MQ丢失连接*/
	String ERR_MQ_DISCONNTECTED = "ERROR_MQ_DISCONNTECTED";
	String OK = "OK";
	String ERR_CHECK_TOKEN = "ERR_CHECK_TOKEN";
	/**
	 * 系统错误
	 */
	String ERR_SYS_ERR = "ERR_SYS_ERR";
	/** 请求的参数是空的 */
	String ERR_REQ_PARAM_EMPTY = "ERR_REQ_PARAM_EMPTY";
}
