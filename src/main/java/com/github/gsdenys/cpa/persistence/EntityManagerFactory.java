/*
 * Copyright 2016 CMIS Persistence API
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.gsdenys.cpa.persistence;

import com.github.gsdenys.cpa.exception.CpaConnectionException;
import com.github.gsdenys.cpa.exception.CpaRuntimeException;
import com.github.gsdenys.cpa.operations.CmisExec;
import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.commons.exceptions.CmisConnectionException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is a factory of {@link EntityManager}, which aims to manage the creation of
 * {@link EntityManager} to each repository existent in content management
 *
 * @author Denys G. Santos (gsdenys@gmail.com)
 * @version 0.0.1
 * @since 0.0.1
 */
public class EntityManagerFactory {

    private Map<String, EntityManager> entityManagerMap = new HashMap<>();

    /**
     * This CMIS field just has a basic values that can be used just to execute a search by repositories
     */
    private CmisExec cmisExec;


    private Map<String, String> mappedRepository;


    /**
     * Builder expose just as package-protected
     *
     * @param url          the url of cmis
     * @param user         the user that will be used as performer
     * @param password     the user password
     * @param repositoryId the repository ID
     * @throws CpaRuntimeException    any error at runtime
     * @throws CpaConnectionException any connection Errors
     */
    protected EntityManagerFactory(String url, String user, String password, String repositoryId)
            throws CpaConnectionException, CpaRuntimeException {

        this.cmisExec = new CmisExec(url, user, password);

        CmisExec exec = this.cloneCmisExec();
        exec.setRepositoryId(repositoryId);

        this.validateConnection(exec);

        EntityManager em = new EntityManagerImpl(exec);

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
     * method to clone {@link CmisExec} object
     *
     * @return CmisExec the {@link CmisExec} object
     * @throws CpaRuntimeException the error to be throw when the clone go wrong
     */
    private CmisExec cloneCmisExec() throws CpaRuntimeException {
        CmisExec cmisExec;

        try {
            cmisExec = this.cmisExec.clone();
        } catch (CloneNotSupportedException e) {
            throw new CpaRuntimeException("Error when try to clone object.", e.getCause());
        }

        return cmisExec;
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
     * @return EntityManager the {@link EntityManager}
     * @throws CpaRuntimeException any error at runtime
     */
    public EntityManager getEntityManager(String repositoryId) throws CpaRuntimeException {
        EntityManager em = this.entityManagerMap.get(repositoryId);

        if (em != null) {
            return em;
        }

        CmisExec exec = this.cloneCmisExec();
        exec.setRepositoryId(repositoryId);

        this.validateConnection(exec);

        em = new EntityManagerImpl(exec);

        this.entityManagerMap.put(repositoryId, em);

        return em;
    }

    /**
     * List all repositories present at the ECM.
     *
     * @return List of IDs of repositories
     */
    public List<String> listRepositoriesId() {
        List<Repository> repo = this.cmisExec.getRepositoryExec().listRepositories();
        List<String> strings = new ArrayList<>();

        repo.forEach(repository -> {
            strings.add(repository.getId());
        });

        return strings;
    }

    /**
     * Validate if the connection with content manager is ok and if the repository is accessible
     *
     * @param exec cmis executor
     * @return boolean if CMIS is accessible or not
     */
    private boolean validateConnection(CmisExec exec) throws CpaConnectionException {
        try {
            exec.getRepositoryExec().listRepositories();
        } catch (CmisConnectionException cmisConnectionException) {
            throw new CpaConnectionException(
                    "The CMIS service is not accessible. Check the url, user and password at the properties file",
                    cmisConnectionException
            );
        }

        return true;
    }

    /**
     * get all repositories already mapped at the repository configuration file
     *
     * @return Map the map containig the repositories map (name, id)
     */
    public Map<String, String> getMappedRepository() {
        return mappedRepository;
    }

    /**
     * set the mapped repositories
     *
     * @param mappedRepository the mapped repository at properties file
     */
    protected void setMappedRepository(Map<String, String> mappedRepository) {
        this.mappedRepository = mappedRepository;
    }
}
