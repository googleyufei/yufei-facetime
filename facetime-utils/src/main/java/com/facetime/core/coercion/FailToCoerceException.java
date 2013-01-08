package com.facetime.core.coercion;

@SuppressWarnings("serial")
public class FailToCoerceException extends RuntimeException {

    public FailToCoerceException(String message) {
        super(message);
    }

    public FailToCoerceException(String message, Throwable cause) {
        super(message, cause);
    }

}
