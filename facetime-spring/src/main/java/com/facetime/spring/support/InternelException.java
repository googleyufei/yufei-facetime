package com.facetime.spring.support;

/**
 * 运行时ROOT异常, 不需要声明就可以抛出
 * 
 * @author YUFEI
 * 
 */
public class InternelException extends RuntimeException {

	private static final long serialVersionUID = -6504423119172834150L;

	private int reason;

	public InternelException() {
		super();
	}

	public InternelException(int reason) {
		super();
		this.reason = reason;
	}

	public InternelException(String message) {
		super(message);
	}

	public InternelException(String message, Throwable cause) {
		super(message, cause);
	}

	public InternelException(Throwable cause) {
		super(cause);
	}

	public int getReason() {
		return reason;
	}

	public void setReason(int reason) {
		this.reason = reason;
	}

}
