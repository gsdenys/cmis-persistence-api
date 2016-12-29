package com.gsdenys.cpa.operations;

import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.commons.exceptions.CmisConnectionException;

import java.util.List;

/**
 * Created by gsdenys on 29/12/16.
 */
public class RepositoryExec {

    protected CmisExec cmisExec;

    /**
     * Default builder
     *
     * @param cmisExec the CMIS Executor
     */
    public RepositoryExec(CmisExec cmisExec) {
        this.cmisExec = cmisExec;
    }

    /**
     * List all CMIS Repositories
     *
     * @return List
     * a repositories list
     */
    public List<Repository> listRepositories() throws CmisConnectionException {
        return this.cmisExec.getFactory().getRepositories(this.cmisExec.getParameter());
    }
}
