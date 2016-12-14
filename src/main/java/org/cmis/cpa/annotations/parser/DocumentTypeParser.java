package org.cmis.cpa.annotations.parser;

import org.cmis.cpa.annotations.Metadata;
import org.cmis.cpa.exception.CpaAnnotationException;

/**
 * Created by gsdenys on 13/12/16.
 */
public class DocumentTypeParser<T> implements AnnotationParser<T> {
    MetadataParser<T> metadataParser = new MetadataParser<T>();

    @Override
    public void validate(T classBean) throws CpaAnnotationException {

        metadataParser.validate(classBean);
    }
}
