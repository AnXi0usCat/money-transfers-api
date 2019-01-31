package com.mishas.stuff.common.utils.exceptions;

public final class MyInputValidationException extends RuntimeException {

    public MyInputValidationException() {
        super();
    }

    public MyInputValidationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public MyInputValidationException(final String message) {
        super(message);
    }

    public MyInputValidationException(final Throwable cause) {
        super(cause);
    }
}




