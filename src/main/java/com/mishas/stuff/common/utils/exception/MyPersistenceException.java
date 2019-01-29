package com.mishas.stuff.common.utils.exception;


public final class MyPersistenceException extends RuntimeException {

    public MyPersistenceException() {
        super();
    }

    public MyPersistenceException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public MyPersistenceException(final String message) {
        super(message);
    }

    public MyPersistenceException(final Throwable cause) {
        super(cause);
    }
}
