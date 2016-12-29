package com.gsdenys.cpa.operations;

import com.sun.istack.NotNull;
import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.exceptions.CmisConnectionException;
import com.gsdenys.cpa.exception.CpaRuntimeException;
import com.gsdenys.cpa.persistence.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * org.cmis.cpa.operations
 *
 * @author gsdenys
 * @version 0.0.0
 * @since 0.0.0
 */
public class CmisExec implements Cloneable {

    private Map<String, String> parameter = new HashMap<>();

    private String repositoryId;

    private SessionFactory factory;

    //executors
    private RepositoryExec repositoryExec;
    private PersistExec persistExec;

    /**
     * Default Builder
     *
     * @param url          the url of cmis
     * @param user         the user that will be used as performer
     * @param password     the user password
     * @param repositoryId the repository ID
     */
    public CmisExec(String url, String user, String password, @NotNull String repositoryId) throws CpaRuntimeException {
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

    private void createExecutors() {
        this.repositoryExec = new RepositoryExec(this);
        this.persistExec = new PersistExec(this);
    }

    @Override
    public CmisExec clone() throws CloneNotSupportedException {
        return (CmisExec) super.clone();
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
    protected Session getSession() {
        this.createFactory();

        if (parameter.get(SessionParameter.REPOSITORY_ID) == null) {
            if (this.repositoryId == null) {
                this.repositoryId = this.repositoryExec.listRepositories().get(0).getId();
            }

            this.parameter.put(SessionParameter.REPOSITORY_ID, this.repositoryId);
        }

        return this.factory.createSession(this.parameter);
    }


    protected Map<String, String> getParameter() {
        return parameter;
    }

    protected String getRepositoryId() {
        return repositoryId;
    }

    protected SessionFactory getFactory() {
        if(this.factory != null) {
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CmisExec exec = (CmisExec) o;

        if (!parameter.equals(exec.parameter)) return false;
        if (repositoryId != null ? !repositoryId.equals(exec.repositoryId) : exec.repositoryId != null) return false;
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
