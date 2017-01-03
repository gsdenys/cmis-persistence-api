package com.gsdenys.cpa.persistence;

import com.github.gsdenys.CmisInMemoryRunner;
import com.github.gsdenys.Configure;
import com.gsdenys.cpa.utils.PropertiesConnection;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;


@RunWith(CmisInMemoryRunner.class)
@Configure
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
        Assert.assertEquals("The repository list should have just one repository", reposId.size(), 1);
    }

    @Test
    public void setMappedRepository() throws Exception {
        try {
            Map<String, String> repos = new HashMap<>();
            emf.setMappedRepository(repos);
        } catch (Exception e) {
            Assert.assertFalse("This method should not return error", true);
        }
    }

    @Test
    public void EntityManagerFactory() throws Exception {
        PropertiesConnection pCon = new PropertiesConnection();
        Properties p = pCon.loadPropertiesFile("sample");

        EntityManagerFactory factory = new EntityManagerFactory(
                p.getProperty(Persistence.PROP_URL),
                p.getProperty(Persistence.PROP_USER),
                p.getProperty(Persistence.PROP_PASSWRD),
                null
        );

        EntityManager em = factory.getEntityManager();
        Assert.assertNotNull("The EntityManagerImpl shoud no be null", em);
    }
}
