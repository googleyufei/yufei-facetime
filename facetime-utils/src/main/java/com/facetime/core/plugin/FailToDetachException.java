package com.facetime.core.plugin;


@SuppressWarnings("serial")
public class FailToDetachException extends PluginException {

    public FailToDetachException(Throwable throwable) {
        super(throwable);
    }

    public FailToDetachException(String message) {
        super(message);
    }

    public FailToDetachException(Throwable throwable, String message) {
        super(throwable, message);
    }
}
