package com.facetime.core.resource;

@SuppressWarnings("serial")
public final class ResourceNotFoundException extends RuntimeException {
	/**
	 * Constructor
	 */
	public ResourceNotFoundException() {
		super();
	}
	/**
	 * Constructor
	 * 
	 * @param message
	 *            Description of the problem
	 */
	public ResourceNotFoundException(final String message) {
		super(message);
	}

	/**
	 * Constructor
	 * 
	 * @param cause
	 *            Nested stack trace
	 */
	public ResourceNotFoundException(final Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor
	 * 
	 * @param message
	 *            Description of the problem
	 * @param cause
	 *            Nested stack trace
	 */
	public ResourceNotFoundException(final String message, final Throwable cause) {
		super(message, cause);
	}
}