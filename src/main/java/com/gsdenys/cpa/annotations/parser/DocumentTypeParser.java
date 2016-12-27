package com.gsdenys.cpa.annotations.parser;

import com.gsdenys.cpa.exception.CpaAnnotationException;

/**
 * Created by gsdenys on 13/12/16.
 */
public class DocumentTypeParser<T> implements AnnotationParser<T> {
    MetadataParser<T> metadataParser = new MetadataParser<T>();

    public void validate(T classBean) throws CpaAnnotationException {
        metadataParser.validate(classBean);

    }
}
