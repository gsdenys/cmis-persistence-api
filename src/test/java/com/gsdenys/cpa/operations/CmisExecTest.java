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

import com.github.gsdenys.CmisInMemoryRunner;
import com.github.gsdenys.Configure;
import com.gsdenys.cpa.entities.Document;
import com.gsdenys.cpa.operations.parser.EntityParser;
import com.gsdenys.cpa.persistence.Persistence;
import com.gsdenys.cpa.utils.PropertiesConnection;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
    public void cmisExec1() throws Exception {
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
    public void cmisExec2() throws Exception {
        PropertiesConnection pcon = new PropertiesConnection();
        Properties properties = pcon.loadPropertiesFile("sample");

        CmisExec exec = new CmisExec(
                properties.getProperty(Persistence.PROP_URL),
                properties.getProperty(Persistence.PROP_USER),
                properties.getProperty(Persistence.PROP_PASSWRD),
                properties.getProperty(Persistence.PROP_REPOSITORY_ID)
        );

        Assert.assertNotNull("The exec should not be null", exec);

        //test fail when try to create a CMIS EXEC with repository id = null
        try {
            new CmisExec(
                    properties.getProperty(Persistence.PROP_URL),
                    properties.getProperty(Persistence.PROP_USER),
                    properties.getProperty(Persistence.PROP_PASSWRD),
                    null
            );

            Assert.fail("The execution should throws an error: the repositoryId = null");
        } catch (Exception e) {
            //nothing to do here
        }
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

    @Test
    public void createFactory() throws Exception {
        Method method = CmisExec.class.getDeclaredMethod("createFactory");
        method.setAccessible(true);
        method.invoke(this.cmisExec);

        Field field = CmisExec.class.getDeclaredField("factory");
        field.setAccessible(true);
        SessionFactory factory = (SessionFactory) field.get(this.cmisExec);

        Assert.assertNotNull("The Session factory should be null", factory);
    }

    @Test
    public void hashCode1() throws Exception {
        this.cmisExec.getFactory();
        int h1 = this.cmisExec.hashCode();

        //change the factory
        Field field = CmisExec.class.getDeclaredField("factory");
        field.setAccessible(true);
        field.set(this.cmisExec, null);

        int h2 = this.cmisExec.hashCode();

        Assert.assertNotEquals("The hash codes should be different", h1, h2);
    }

    @Test
    public void getDocParser() throws Exception {
        EntityParser parser = this.cmisExec.getEntityParser(Document.class);

        Assert.assertNotNull("The parser should not be null", parser);
    }

    @Test
    public void getLockExec() throws Exception {
        LockExec exec = this.cmisExec.getLockExec();

        Assert.assertNotNull(exec);
    }
}
