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
package com.gsdenys.cpa.operations;

import com.gsdenys.cpa.exception.CpaAnnotationException;
import com.gsdenys.cpa.exception.CpaRuntimeException;
import com.gsdenys.cpa.operations.parser.EntityParser;
import com.sun.istack.NotNull;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;

import java.util.HashMap;
import java.util.Map;

/**
 * org.cmis.cpa.operations
 *
 * @author gsdenys
 * @version 0.0.0
 * @since 0.0.0
 */
public class CmisExec implements Cloneable {

    private static Map<Class, EntityParser> docParserStore;

    static {
        if (docParserStore == null) {
            docParserStore = new HashMap<>();
        }
    }

    private Map<String, String> parameter = new HashMap<>();
    private String repositoryId;
    private SessionFactory factory;
    //executors
    private RepositoryExec repositoryExec;
    private PersistExec persistExec;
    private LockExec lockExec;
    private LocationExec locationExec;

    /**
     * Default Builder
     *
     * @param url          the url of cmis
     * @param user         the user that will be used as performer
     * @param password     the user password
     * @param repositoryId the repository ID
     * @throws CpaRuntimeException any error at runtime
     */
    CmisExec(@NotNull String url, @NotNull  String user, @NotNull String password, @NotNull String repositoryId)
            throws CpaRuntimeException {
        parameter.put(SessionParameter.USER, user);
        parameter.put(SessionParameter.PASSWORD, password);
        parameter.put(SessionParameter.ATOMPUB_URL, url);
        parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());

        if (repositoryId == null) {
            throw new CpaRuntimeException(
                    "The repository ID is required and not null when this method is invoked"
            );
        }

        this.repositoryId = repositoryId;

        this.createExecutors();
    }

    /**
     * Builder
     *
     * @param url      the url of cmis
     * @param user     the user that will be used as performer
     * @param password the user password
     */
    public CmisExec(String url, String user, String password) {
        parameter.put(SessionParameter.USER, user);
        parameter.put(SessionParameter.PASSWORD, password);
        parameter.put(SessionParameter.ATOMPUB_URL, url);
        parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());

        this.createExecutors();
    }

    /**
     * Create executors. This methods is only called by the builders
     */
    private void createExecutors() {
        this.repositoryExec = new RepositoryExec(this);
        this.persistExec = new PersistExec(this);
        this.lockExec = new LockExec(this);
        this.locationExec = new LocationExec(this);
    }

    /**
     * clone override implementation
     *
     * @return CmisExec the clone of object
     * @throws CloneNotSupportedException case the clone was not supported
     */
    @Override
    public CmisExec clone() throws CloneNotSupportedException {
        return (CmisExec) super.clone();
    }

    /**
     * Create a new session factory if has no one created
     */
    private void createFactory() {
        if (this.factory == null) {
            this.factory = SessionFactoryImpl.newInstance();
        }
    }

    /**
     * Get CMIS Session
     *
     * @return Session
     * the cmis session
     */
    Session getSession() {
        this.createFactory();

        if (parameter.get(SessionParameter.REPOSITORY_ID) == null) {
            if (this.repositoryId == null) {
                this.repositoryId = this.repositoryExec.listRepositories().get(0).getId();
            }

            this.parameter.put(SessionParameter.REPOSITORY_ID, this.repositoryId);
        }

        return this.factory.createSession(this.parameter);
    }

    Map<String, String> getParameter() {
        return parameter;
    }

    String getRepositoryId() {
        return repositoryId;
    }

    /**
     * Set the repository ID.
     * <p>
     * This method set the repository ID case it's not already set, else an {@link CpaRuntimeException} will be returned
     *
     * @param repositoryId the repository id to be setted
     * @throws CpaRuntimeException the exception that will occur when the id is already set.
     */
    public void setRepositoryId(String repositoryId) throws CpaRuntimeException {

        if (this.repositoryId != null) {
            throw new CpaRuntimeException(
                    "The repository ID just can be set once, and it'd already set."
            );
        }

        this.repositoryId = repositoryId;
    }

    SessionFactory getFactory() {
        if (this.factory != null) {
            return this.factory;
        }

        this.createFactory();
        return this.factory;
    }

    /**
     * Get executor for repository methods
     *
     * @return RepositoryExec the executor for the repository
     */
    public RepositoryExec getRepositoryExec() {
        return repositoryExec;
    }

    public PersistExec getPersistExec() {
        return persistExec;
    }

    public LockExec getLockExec() {
        return lockExec;
    }

    public LocationExec getLocationExec() {
        return locationExec;
    }

    /**
     * get the document type parser
     *
     * @param clazz the class of the parser
     * @return EntityParser the parser
     * @throws CpaAnnotationException case any annotation was not correctly applied
     * @throws CpaRuntimeException    any error during runtime
     */
    EntityParser getEntityParser(Class clazz) throws CpaAnnotationException, CpaRuntimeException {
        if (docParserStore.containsKey(clazz)) {
            return docParserStore.get(clazz);
        }

        EntityParser parser = new EntityParser(clazz);
        docParserStore.put(clazz, parser);

        return parser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CmisExec exec = (CmisExec) o;

        if (!parameter.equals(exec.parameter)) return false;
        if (repositoryId != null ? !repositoryId.equals(exec.repositoryId) : exec.repositoryId != null)
            return false;
        return factory != null ? factory.equals(exec.factory) : exec.factory == null;
    }

    @Override
    public int hashCode() {
        int result = parameter.hashCode();
        result = 31 * result + (repositoryId != null ? repositoryId.hashCode() : 0);
        result = 31 * result + (factory != null ? factory.hashCode() : 0);
        return result;
    }
}
