package com.gsdenys.cpa.persistence;

import com.gsdenys.cpa.annotations.parser.DocumentTypeParser;
import com.gsdenys.cpa.exception.CpaAnnotationException;
import com.gsdenys.cpa.exception.CpaPersistenceException;
import com.gsdenys.cpa.operations.CmisExec;

/**
 * org.cmis.cpa.persistence
 *
 * @author gsdenys
 * @version 0.0.0
 * @since 0.0.0
 */
public class EntityManager {

    public static final String CPA_DEFAULT_REPOSITORY = "cpa-default-repository";

    private CmisExec cmisExec;

    /**
     * Builder expose just as package-protected
     *
     * @param cmisExec
     *          the cmis executor object
     */
    EntityManager(CmisExec cmisExec) {
        this.cmisExec = cmisExec;
    }

    /**
     * Save de obj at content management system through CMIS
     *
     * @param obj
     *          the object to be saved at content Management
     * @param <E>
     * @throws CpaPersistenceException
     * @throws CpaAnnotationException
     */
    public <E> void persist(E obj) throws CpaPersistenceException, CpaAnnotationException {
        DocumentTypeParser<E> typeParser = new DocumentTypeParser<E>();
        typeParser.validate(obj);

    }
}
