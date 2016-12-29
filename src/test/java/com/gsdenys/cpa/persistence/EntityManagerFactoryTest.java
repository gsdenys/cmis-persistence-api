package com.gsdenys.cpa.persistence;

import com.github.gsdenys.CmisInMemoryRunner;
import com.github.gsdenys.Configure;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.*;

/**
 * Created by gsdenys on 14/12/16.
 */
@RunWith(CmisInMemoryRunner.class)
@Configure(port = 8080)
public class EntityManagerFactoryTest {
    EntityManagerFactory emf;

    @Before
    public void setUp() throws Exception {
        emf = Persistence.createEntityManagerFactory("sample");
    }

    @Test
    public void getEntityManager() throws Exception {
        EntityManager em = emf.getEntityManager();
        Assert.assertNotNull("The EntityManagerImpl shoud no be null", em);
    }

    @Test
    public void getEntityManagerString() throws Exception {
        EntityManager em = emf.getEntityManager();
        EntityManager em1 = emf.getEntityManager("A1");

        Assert.assertNotNull("The EntityManagerImpl shoud no be null", em1);
        Assert.assertNotNull("The EntityManagerImpl shoud no be null", em);
        Assert.assertSame("The entities shoul be the same", em, em1);
    }


    @Test
    public void listRepositoriesId() throws Exception {
        List<String> reposId = emf.listRepositoriesId();

        Assert.assertNotNull("The repositories list should not be null", reposId);
        Assert.assertFalse("The repositories list shoul not be empty", reposId.isEmpty());
        Assert.assertEquals("The repository list should have just one repository", reposId.size(),1);
    }

    @Test
    public void setMappedRepository() throws Exception {
        try {
            Map<String, String> repos = new HashMap<>();
            emf.setMappedRepository(repos);
        }catch (Exception e) {
            Assert.assertFalse("This method should not return error", true);
        }
    }
}