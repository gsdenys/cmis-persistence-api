package org.cmis.cpa.persistence;

import org.cmis.cpa.annotations.parser.DocumentTypeParser;
import org.cmis.cpa.exception.CpaAnnotationException;
import org.cmis.cpa.exception.CpaPersistenceException;

/**
 * org.cmis.cpa.persistence
 *
 * @author gsdenys
 * @version 0.0.0
 * @since 0.0.0
 */
public class EntityManager {

    private String url;
    private String user;
    private String password;
    private String repositoryId;

    /**
     * Builder expose just as package-protected
     *
     * @param url
     *          the url of cmis
     * @param user
     *          the user that will be used as performer
     * @param password
     *          the user password
     * @param repositoryId
     *          the repository ID
     */
    EntityManager(String url, String user, String password, String repositoryId) {
        this.user = user;
        this.url = url;
        this.password = password;
        this.repositoryId = repositoryId;
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
