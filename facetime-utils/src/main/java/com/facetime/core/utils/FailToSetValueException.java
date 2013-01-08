package com.facetime.core.utils;

@SuppressWarnings("serial")
public class FailToSetValueException extends RuntimeException {

	public FailToSetValueException(String message, Object... objects) {
		super(String.format(message, objects));
	}

	public FailToSetValueException(Throwable e, String message, Object... objects) {
		super(String.format(message, objects), e);
	}

}
