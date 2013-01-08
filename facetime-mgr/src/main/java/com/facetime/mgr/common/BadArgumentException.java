package com.facetime.mgr.common;

public class BadArgumentException extends IllegalArgumentException {
	private static final long serialVersionUID = 5994330433260337288L;

	public BadArgumentException() {
		super();
	}

	public BadArgumentException(String s) {
		super(s);
	}

	public BadArgumentException(String message, Throwable cause) {
		super(message, cause);
	}

	public BadArgumentException(Throwable cause) {
		super(cause);
	}
}
