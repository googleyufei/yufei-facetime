package com.facetime.core.i18n.impl;

import java.util.Locale;

/**
 * 消息格式化器
 */
public class MessageFormatter {

	private final String format;
	private final Locale locale;

	public MessageFormatter(String format, Locale locale) {
		this.format = format;
		this.locale = locale;
	}

	/**
	 * Extracts the message from an exception. If the exception's message is
	 * null, returns the exceptions class getName.
	 * 
	 * @param exception
	 *            to extract message from
	 * @return message or class getName
	 */
	public static String extractMessage(Throwable exception) {
		String message = exception.getMessage();
		if (message != null) return message;
		return exception.getClass().getName();
	}

	public String format(Object... args) {
		for (int i = 0; i < args.length; i++) {
			Object arg = args[i];
			if (Throwable.class.isInstance(arg)) {
				args[i] = extractMessage((Throwable) arg);
			}
		}
		// Might be tempting to create a Formatter object and just keep reusing
		// it ... but
		// Formatters are not threadsafe.
		return String.format(locale, format, args);
	}
}
