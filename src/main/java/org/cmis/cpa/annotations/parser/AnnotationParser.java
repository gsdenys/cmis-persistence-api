package org.cmis.cpa.annotations.parser;

import org.cmis.cpa.exception.CpaAnnotationException;

/**
 * AnnotationParser interface for annotations
 *
 * @param <T>
 */
public interface AnnotationParser<T> {

    void validate(T classBean) throws CpaAnnotationException;


}
