package com.facetime.spring.support;

/**
 * 逻辑异常
 *
 * @author yufei
 * @Create_by 2012-12-7
 * @Design_by eclipse  
 */
public class LogicException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private String errorMsg;
	private String errorType;

	public LogicException() {
		super();
	}

	public LogicException(String errorType) {
		super(errorType);
		this.errorType = errorType;
	}

	public LogicException(String errorType, String msg) {
		super(errorType);
		this.errorType = errorType;
		this.errorMsg = msg;
	}

	public LogicException(String errorType, Throwable cause) {
		super(errorType, cause);
		this.errorType = errorType;
	}

	public LogicException(Throwable cause) {
		super(cause);
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getErrorType() {
		return errorType;
	}

	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}

}