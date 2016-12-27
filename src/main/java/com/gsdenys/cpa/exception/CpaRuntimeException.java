package com.gsdenys.cpa.exception;

/**
 * Created by gsdenys on 27/12/16.
 */
public class CpaRuntimeException extends Exception {
    public CpaRuntimeException() {
        super();
    }

    public CpaRuntimeException(String message) {
        super(message);
    }

    public CpaRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public CpaRuntimeException(Throwable cause) {
        super(cause);
    }

    protected CpaRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
