package com.gsdenys.cpa.operations;

import com.github.gsdenys.CmisInMemoryRunner;
import com.github.gsdenys.Configure;
import com.gsdenys.cpa.persistence.Persistence;
import com.gsdenys.cpa.utils.PropertiesConnection;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.fail;

@RunWith(CmisInMemoryRunner.class)
@Configure
public class CmisExecTest {

    private CmisExec cmisExec;

    @Before
    public void setUp() throws Exception {
        PropertiesConnection pcon = new PropertiesConnection();
        Properties properties = pcon.loadPropertiesFile("sample");

        this.cmisExec = new CmisExec(
                properties.getProperty(Persistence.PROP_URL),
                properties.getProperty(Persistence.PROP_USER),
                properties.getProperty(Persistence.PROP_PASSWRD),
                properties.getProperty(Persistence.PROP_REPOSITORY_ID)
        );
    }

    @Test
    public void CmisExec1() throws Exception {
        PropertiesConnection pcon = new PropertiesConnection();
        Properties properties = pcon.loadPropertiesFile("sample");

        CmisExec exec = new CmisExec(
                properties.getProperty(Persistence.PROP_URL),
                properties.getProperty(Persistence.PROP_USER),
                properties.getProperty(Persistence.PROP_PASSWRD)
        );

        Assert.assertNotNull("The exec should not be null", exec);
    }

    @Test
    public void CmisExec() throws Exception {
        PropertiesConnection pcon = new PropertiesConnection();
        Properties properties = pcon.loadPropertiesFile("sample");

        CmisExec exec = new CmisExec(
                properties.getProperty(Persistence.PROP_URL),
                properties.getProperty(Persistence.PROP_USER),
                properties.getProperty(Persistence.PROP_PASSWRD),
                properties.getProperty(Persistence.PROP_REPOSITORY_ID)
        );

        Assert.assertNotNull("The exec should not be null", exec);
    }

    @Test
    public void cloneTest() throws Exception {
        CmisExec exec = this.cmisExec.clone();

        Assert.assertNotNull("The clone of cmisExec should not be null", exec);
        Assert.assertEquals("The exec and cmisExec should be equals", exec, this.cmisExec);
    }

    @Test
    public void setRepositoryId() throws Exception {
        try {
            this.cmisExec.setRepositoryId("A1");
            fail("The CMIS should not be set once already set");
        } catch (Exception e) {
            //nothing to do
        }

        PropertiesConnection pcon = new PropertiesConnection();
        Properties properties = pcon.loadPropertiesFile("sample");

        CmisExec exec = new CmisExec(
                properties.getProperty(Persistence.PROP_URL),
                properties.getProperty(Persistence.PROP_USER),
                properties.getProperty(Persistence.PROP_PASSWRD)
        );

        Assert.assertNull("The Repository ID should be null", exec.getRepositoryId());

        exec.setRepositoryId("A1");
        Assert.assertEquals("The repository ID should be 'A1'", exec.getRepositoryId(), "A1");
    }

    @Test
    public void getSession() throws Exception {
        Session session = this.cmisExec.getSession();

        Assert.assertNotNull("The Session should not be null", session);
    }

    @Test
    public void getParameter() throws Exception {
        Map<String, String> parameters = this.cmisExec.getParameter();

        Assert.assertNotNull("The parameters should not be null", parameters);
        Assert.assertFalse("The parameters map should not be empty", parameters.isEmpty());
    }

    @Test
    public void getRepositoryId() throws Exception {
        String repositoryId = this.cmisExec.getRepositoryId();

        Assert.assertNotNull("The repository ID should not be null", repositoryId);
        Assert.assertEquals("The repository ID should be 'A1'", repositoryId, "A1");
    }

    @Test
    public void getFactory() throws Exception {
        SessionFactory factory = this.cmisExec.getFactory();

        Assert.assertNotNull("The SessionFactory should not be null", factory);
    }

    @Test
    public void getPersistExec() throws Exception {
        PersistExec persistExec = this.cmisExec.getPersistExec();

        Assert.assertNotNull("The PersistenceExec should not be null", persistExec);
    }

    @Test
    public void getRepositoryExec() throws Exception {
        RepositoryExec repositoryExec = this.cmisExec.getRepositoryExec();

        Assert.assertNotNull("The RepositoryExec should not be null", repositoryExec);
    }

    @Test
    public void equals() throws Exception {
        CmisExec exec = this.cmisExec.clone();

        Assert.assertTrue("The cmisExec and Exec Should Be equals", exec.equals(this.cmisExec));
    }
}