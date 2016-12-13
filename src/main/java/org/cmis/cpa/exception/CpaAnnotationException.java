package org.cmis.cpa.exception;

/**
 * A CPA annotation exception.
 *
 * This Error just will returned when the parser found any annotation error at any of the follow annotations
 * <ul>
 *     <li>
 *         {@link org.cmis.cpa.annotations.CPAConnection}
 *     </li>
 *     <li>
 *         {@link org.cmis.cpa.annotations.DocumentType}
 *     </li>
 *     <li>
 *         {@link org.cmis.cpa.annotations.SecondaryType}
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
    //nothing to do here
}
