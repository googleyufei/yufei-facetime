package com.facetime.core.utils;

@SuppressWarnings("serial")
public class FailToGetValueException extends RuntimeException {

	public FailToGetValueException(String message, Object... args) {
		super(String.format(message, args));
	}

	public FailToGetValueException(Throwable e, String message, Object... args) {
		super(String.format(message, args), e);
	}

}
