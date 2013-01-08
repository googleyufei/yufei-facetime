package com.facetime.core.plugin;

@SuppressWarnings("serial")
public class PluginException extends RuntimeException {

	public PluginException(Throwable throwable) {
		super(throwable);
	}
	
	public PluginException(String message) {
		super(message);
	}
	
	public PluginException(Throwable throwable, String message) {
		super(message, throwable);
	}
}
