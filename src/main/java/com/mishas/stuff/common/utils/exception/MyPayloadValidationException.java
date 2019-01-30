package com.mishas.stuff.common.utils.exception;

public class MyPayloadValidationException extends RuntimeException {

    public MyPayloadValidationException() {
        super();
    }

    public MyPayloadValidationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public MyPayloadValidationException(final String message) {
        super(message);
    }

    public MyPayloadValidationException(final Throwable cause) {
        super(cause);
    }
}
