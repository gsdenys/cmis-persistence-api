package com.github.gsdenys.cpa.operations;

import com.github.gsdenys.CmisInMemoryRunner;
import com.github.gsdenys.Configure;
import com.github.gsdenys.cpa.persistence.EntityManagerFactory;
import com.github.gsdenys.cpa.persistence.EntityManagerImpl;
import com.github.gsdenys.cpa.persistence.Persistence;
import org.apache.chemistry.opencmis.client.api.Repository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by gsdenys on 29/12/16.
 */
@RunWith(CmisInMemoryRunner.class)
@Configure
public class RepositoryExecTest {

    private RepositoryExec repositoryExec;

    @Before
    public void setUp() throws Exception {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("sample");
        EntityManagerImpl emi = (EntityManagerImpl) factory.getEntityManager();

        Field f = EntityManagerImpl.class.getDeclaredField("cmisExec");
        f.setAccessible(true);
        CmisExec cmisExec = (CmisExec) f.get(emi);

        this.repositoryExec = cmisExec.getRepositoryExec();
    }

    @Test
    public void listRepositories() throws Exception {
        List<Repository> repositories = this.repositoryExec.listRepositories();

        Assert.assertNotNull("The repository list should not be null", repositories);
        Assert.assertFalse("The repository list should not be empty", repositories.isEmpty());
        Assert.assertEquals("The repository list should be just one element", repositories.size(), 1);
    }

}