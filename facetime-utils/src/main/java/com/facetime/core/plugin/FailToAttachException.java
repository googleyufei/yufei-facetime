package com.facetime.core.plugin;


public class FailToAttachException extends PluginException {

    public FailToAttachException(Throwable throwable) {
        super(throwable);
    }

    public FailToAttachException(String message) {
        super(message);
    }

    public FailToAttachException(Throwable throwable, String message) {
        super(throwable, message);
    }
}
