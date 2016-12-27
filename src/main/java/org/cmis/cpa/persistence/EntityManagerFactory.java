package org.cmis.cpa.persistence;

import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.commons.exceptions.CmisConnectionException;
import org.cmis.cpa.exception.CpaConnectionException;
import org.cmis.cpa.exception.CpaRuntimeException;
import org.cmis.cpa.operations.CmisExec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is a factory of {@link EntityManager}, which aims to manage the creation of {@link EntityManager} to each
 * repository existent in content management
 *
 * @author
 * @version 0.0.0
 * @since 0.0.0
 */
public class EntityManagerFactory {

    private Map<String, EntityManager> entityManagerMap = new HashMap<>();

    /**
     * This CMIS field just has a basic values that can be used just to execute a search by repositories
     */
    private CmisExec cmisExec;


    private Map<String, String> mappedRepository;


    /**
     * method to clone {@link CmisExec} object
     *
     * @return CmisExec the {@link CmisExec} object
     * @throws CpaRuntimeException the error to be throw when the clone go wrong
     */
    private CmisExec cloneCmisExec() throws CpaRuntimeException{
        CmisExec cmisExec;

        try {
            cmisExec = this.cmisExec.clone();
        } catch (CloneNotSupportedException e) {
            throw new CpaRuntimeException("Error when try to clone object.", e.getCause());
        }

        return  cmisExec;
    }


    /**
     * Builder expose just as package-protected
     *
     * @param url          the url of cmis
     * @param user         the user that will be used as performer
     * @param password     the user password
     * @param repositoryId the repository ID
     */
    protected EntityManagerFactory(String url, String user, String password, String repositoryId)
            throws CpaConnectionException, CpaRuntimeException {

        this.cmisExec = new CmisExec(url, user, password);

        CmisExec exec = this.cloneCmisExec();
        exec.setRepositoryId(repositoryId);

        this.validateConnection(exec);

        EntityManager em = new EntityManager(exec);

        if (repositoryId != null) {
            if (this.entityManagerMap.isEmpty()) {
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
     * The {@link EntityManager}
     */
    public EntityManager getEntityManager() {
        return this.entityManagerMap.get(EntityManager.CPA_DEFAULT_REPOSITORY);
    }

    /**
     * Get the {@link EntityManager} based on repository ID
     *
     * @param repositoryId the repository ID
     * @return EntityManager
     * the {@link EntityManager}
     */
    public EntityManager getEntityManager(String repositoryId) throws CpaRuntimeException, CpaConnectionException {
        EntityManager em = this.entityManagerMap.get(repositoryId);

        if (em != null) {
            return em;
        }

        CmisExec exec = this.cloneCmisExec();
        exec.setRepositoryId(repositoryId);

        this.validateConnection(exec);

        em = new EntityManager(exec);

        this.entityManagerMap.put(repositoryId, em);

        return em;
    }

    /**
     * List all repositories present at the ECM.
     *
     * @return List of IDs of repositories
     */
    public List<String> listRepositoriesId() {
        List<Repository> repo = cmisExec.listRepositories();
        List<String> strings = new ArrayList<>();

        repo.forEach(repository -> {
            strings.add(repository.getId());
        });

        return  strings;
    }

    /**
     * Validate if the connection with content manager is ok and if the repository is accessible
     *
     * @param cmisExec cmis executor
     * @return boolean
     * if CMIS is accessible or not
     */
    private boolean validateConnection(CmisExec cmisExec) throws CpaConnectionException {

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

    /**
     * set the mapped repositories
     *
     * @param mappedRepository
     */
    public void setMappedRepository(Map<String, String> mappedRepository) {
        this.mappedRepository = mappedRepository;
    }
}
