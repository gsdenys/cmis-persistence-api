package org.cmis.cpa.persistence;

import com.github.gsdenys.CmisInMemoryRunner;
import com.github.gsdenys.Configure;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Properties;

import static org.junit.Assert.*;

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
        Assert.assertNotNull("The EntityManager shoud no be null", em);
    }

    @Test
    public void getEntityManager1() throws Exception {
        Properties prp = new Properties();
        prp.load(
                this.getClass().getClassLoader().getResourceAsStream(
                        "sample-cmis.properties"
                )
        );

        String repo = prp.getProperty("org.cmis.cpa.repository");

        EntityManager em = emf.getEntityManager();
        EntityManager em1 = emf.getEntityManager(repo);

        Assert.assertNotNull("The EntityManager shoud no be null", em1);
        Assert.assertNotNull("The EntityManager shoud no be null", em);
        Assert.assertSame("The entities shoul be the same", em, em1);
    }
}