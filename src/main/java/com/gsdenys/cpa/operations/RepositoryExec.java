package com.gsdenys.cpa.operations;

import com.gsdenys.cpa.RootFolder;
import com.gsdenys.cpa.exception.CpaAnnotationException;
import com.gsdenys.cpa.exception.CpaRuntimeException;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.exceptions.CmisConnectionException;

import java.util.List;


public class RepositoryExec {

    protected CmisExec cmisExec;

    /**
     * Default builder
     *
     * @param cmisExec the CMIS Executor
     */
    RepositoryExec(CmisExec cmisExec) {
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


    RootFolder getRootFolder() throws CpaRuntimeException, CpaAnnotationException {
        Session session = this.cmisExec.getSession();
        Folder folder = session.getRootFolder();

        RootFolder rf = new RootFolder();
        rf.setId(folder.getId());
        rf.setName(folder.getName());

        this.cmisExec.getPersistExec().refresh(rf);

        return rf;
    }
}
