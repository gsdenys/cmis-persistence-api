package org.cmis.cpa.persistence;

import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.commons.exceptions.CmisConnectionException;
import org.cmis.cpa.annotations.DocumentType;
import org.cmis.cpa.annotations.parser.DocumentTypeParser;
import org.cmis.cpa.annotations.parser.MetadataParser;
import org.cmis.cpa.exception.CpaAnnotationException;
import org.cmis.cpa.exception.CpaConnectionException;
import org.cmis.cpa.exception.CpaPersistenceException;
import org.cmis.cpa.operations.CmisExec;

import java.io.Serializable;
import java.util.*;

/**
 * This is a factory of {@link EntityManager}, which aims to manage the creation of {@link EntityManager} to each
 * repository existent in content management
 *
 * @author
 * @version 0.0.0
 * @since 0.0.0
 */
public class EntityManagerFactory {

    private Map<String, EntityManager> entityManagerMap;

    private String user;
    private String password;
    private String url;

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
    protected EntityManagerFactory(String url, String user, String password, String repositoryId)
            throws CpaConnectionException {

        CmisExec cmisExec = new CmisExec(url, user, password, repositoryId);
        EntityManager em = new EntityManager(cmisExec);

        validateConnection(cmisExec);

        if (repositoryId != null) {
            if(this.entityManagerMap.isEmpty()) {
                this.entityManagerMap.put(EntityManager.CPA_DEFAULT_REPOSITORY, em);
            }

            this.entityManagerMap.put(repositoryId, em);
        } else {
            this.entityManagerMap.put(EntityManager.CPA_DEFAULT_REPOSITORY, em);
        }
    }

    /**
     * Get the {@link EntityManager}
     *
     * @return EntityManager
     *          The {@link EntityManager}
     */
    public EntityManager getEntityManager() {
        return this.entityManagerMap.get(EntityManager.CPA_DEFAULT_REPOSITORY);
    }

    /**
     * Get the {@link EntityManager} based on repository ID
     *
     * @param repositoryId
     *          the repository ID
     * @return EntityManager
     *          the {@link EntityManager}
     */
    public EntityManager getEntityManager(String repositoryId) {
        EntityManager em = this.entityManagerMap.get(repositoryId);

        if(em != null) {
            return em;
        }

        CmisExec cmisExec = new CmisExec(this.url, this.user, this.password, repositoryId);
        em = new EntityManager(cmisExec);

        this.entityManagerMap.put(repositoryId, em);

        return em;
    }


    /**
     * Validate if the connection with content manager is ok and if the repository is accessible
     *
     * @param cmisExec
     *          cmis executor
     * @return boolean
     *          if CMIS is accessible or not
     */
    private boolean validateConnection(CmisExec cmisExec ) throws CpaConnectionException {

        List<Repository> repo;

        try {
            repo = cmisExec.listRepositories();
        } catch (CmisConnectionException cmisConnectionException) {
            throw new CpaConnectionException(
                    "The CMIS service is not accessible. Check the URL, user and password at the properties file",
                    cmisConnectionException
            );
        }

        if (repo.isEmpty()) {
            return false;
        }

        return true;
    }
}
