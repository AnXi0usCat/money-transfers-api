package com.mishas.stuff.common.utils.exceptions;

public final class MyMissingResourceException extends RuntimeException {

    public MyMissingResourceException() {
        super();
    }

    public MyMissingResourceException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public MyMissingResourceException(final String message) {
        super(message);
    }

    public MyMissingResourceException(final Throwable cause) {
        super(cause);
    }
}
