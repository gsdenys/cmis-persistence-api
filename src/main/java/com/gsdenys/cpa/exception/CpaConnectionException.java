package com.gsdenys.cpa.exception;

/**
 * Created by gsdenys on 14/12/16.
 */
public class CpaConnectionException extends Exception {
    public CpaConnectionException() {
        super();
    }

    public CpaConnectionException(String message) {
        super(message);
    }

    public CpaConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public CpaConnectionException(Throwable cause) {
        super(cause);
    }

    protected CpaConnectionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
