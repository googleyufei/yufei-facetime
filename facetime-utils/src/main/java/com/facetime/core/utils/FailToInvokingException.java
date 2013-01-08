package com.facetime.core.utils;

@SuppressWarnings("serial")
public class FailToInvokingException extends RuntimeException {

	public FailToInvokingException(String message, Object... objects) {
		super(String.format(message, objects));
	}

	public FailToInvokingException(Throwable e, String message, Object... objects) {
		super(String.format(message, objects), e);
	}
}
