package org.cmis.cpa.persistence;

import org.cmis.cpa.annotations.DocumentType;
import org.cmis.cpa.annotations.parser.DocumentTypeParser;
import org.cmis.cpa.annotations.parser.MetadataParser;
import org.cmis.cpa.exception.CpaAnnotationException;
import org.cmis.cpa.exception.CpaPersistenceException;

import java.util.Objects;

/**
 * org.cmis.cpa.persistence
 *
 * @author gsdenys
 * @version 0.0.0
 * @since 0.0.0
 */
public class EntityManagerFactory {

    private EntityManager entityManager;

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
    protected EntityManagerFactory(String url, String user, String password, String repositoryId) {
        this.entityManager = new EntityManager(url, user, password, repositoryId);
    }

    public EntityManager getEntityManager() {
        return this.entityManager;
    }
}
