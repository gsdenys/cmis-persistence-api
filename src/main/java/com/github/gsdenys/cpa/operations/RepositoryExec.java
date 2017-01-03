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
package com.github.gsdenys.cpa.operations;

import com.github.gsdenys.cpa.entity.RootFolder;
import com.github.gsdenys.cpa.exception.CpaAnnotationException;
import com.github.gsdenys.cpa.exception.CpaRuntimeException;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.exceptions.CmisConnectionException;

import java.util.List;

/**
 * Executor to perform the repositories action
 *
 * @author Denys G. Santos (gsdenys@gmail.com)
 * @version 0.0.1
 * @since 0.0.1
 */
public class RepositoryExec {

    protected CmisExec cmisExec;

    /**
     * Default builder
     *
     * @param cmisExec the CMIS Executor
     */
    protected RepositoryExec(CmisExec cmisExec) {
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


    protected RootFolder getRootFolder() throws CpaRuntimeException, CpaAnnotationException {
        Session session = this.cmisExec.getSession();
        Folder folder = session.getRootFolder();

        RootFolder rf = new RootFolder();
        rf.setId(folder.getId());
        rf.setName(folder.getName());

        this.cmisExec.getPersistExec().refresh(rf);

        return rf;
    }
}
