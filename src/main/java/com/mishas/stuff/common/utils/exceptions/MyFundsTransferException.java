package com.mishas.stuff.common.utils.exceptions;

public class MyFundsTransferException extends RuntimeException {

    public MyFundsTransferException() {
        super();
    }

    public MyFundsTransferException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public MyFundsTransferException(final String message) {
        super(message);
    }

    public MyFundsTransferException(final Throwable cause) {
        super(cause);
    }
}
