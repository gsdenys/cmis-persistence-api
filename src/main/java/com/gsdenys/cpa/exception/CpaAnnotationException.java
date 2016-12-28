package com.gsdenys.cpa.exception;

import com.gsdenys.cpa.annotations.DocumentType;
import com.gsdenys.cpa.annotations.SecondaryType;

/**
 * A CPA annotation exception.
 *
 * This Error just will returned when the parser found any annotation error at any of the follow annotations
 * <ul>
 *     <li>
 *         {@link CpaConnection}
 *     </li>
 *     <li>
 *         {@link DocumentType}
 *     </li>
 *     <li>
 *         {@link SecondaryType}
 *     </li>
 *     <li>
 *         {@link java.beans.MetaData}
 *     </li>
 * </ul>
 *
 * @author Denys G. Santos (gsdenys@gmail.com)
 * @version 0.0.1
 * @since 0.0.1
 */
public class CpaAnnotationException extends Exception {
    public CpaAnnotationException() {
        super();
    }

    public CpaAnnotationException(String message) {
        super(message);
    }

    public CpaAnnotationException(String message, Throwable cause) {
        super(message, cause);
    }

    public CpaAnnotationException(Throwable cause) {
        super(cause);
    }

    public CpaAnnotationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}