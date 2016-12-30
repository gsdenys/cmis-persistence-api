package com.gsdenys.cpa.persistence;

import com.github.gsdenys.CmisInMemoryRunner;
import com.github.gsdenys.Configure;
import com.gsdenys.cpa.annotations.DocumentType;
import com.gsdenys.cpa.annotations.ID;
import com.gsdenys.cpa.annotations.Metadata;
import com.gsdenys.cpa.annotations.Parent;
import com.gsdenys.cpa.exception.CpaRuntimeException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.fail;

/**
 * Created by gsdenys on 28/12/16.
 */
@RunWith(CmisInMemoryRunner.class)
@Configure
public class EntityManagerImplTest {

    EntityManagerFactory factory;
    EntityManager entity;

    @Before
    public void setUp() throws Exception {
        this.factory = Persistence.createEntityManagerFactory("sample");
        this.entity = this.factory.getEntityManager();
    }

    @Test
    public void persist() throws Exception {

    }

    @Test
    public void persist1() throws Exception {

    }

    @Test
    public void refresh() throws Exception {

    }

    @Test
    public void refresh1() throws Exception {

    }

    @Test
    public void lock() throws Exception {

    }

    @Test
    public void isLocked() throws Exception {

    }

    @Test
    public void isLockedBy() throws Exception {

    }

    @Test
    public void lockedBy() throws Exception {

    }

    @Test
    public void remove() throws Exception {

    }

    @Test
    public void move() throws Exception {

    }

    @Test
    public void copy() throws Exception {

    }

    @Test
    public void getProperties() throws Exception {

    }

    @Test
    public void getProperties1() throws Exception {

    }

    @Test
    public void setProperties() throws Exception {
        Map<String, Object> properties = new HashMap<>();
        properties.put("name", "TESTE-CASE-SET-PROPERTIES");

        Document document = new Document();

        entity.setProperties(document, properties);

        Assert.assertNotNull("the property name should be null", document.name);
        Assert.assertEquals(
                "The property name should be 'TESTE-CASE-SET-PROPERTIES'",
                document.name,
                "TESTE-CASE-SET-PROPERTIES"
        );

        //a fail case
        properties.put("teste", "TEST-CASE-SER-PROPERTIES-ERROR");
        try {
            entity.setProperties(document, properties);
            fail("The set action at test properties shoul throw CpaRuntimeException");
        } catch (CpaRuntimeException e) {
            //nothing to do
        }
    }
}

//Object Metadata

@DocumentType(name = "cmis:document")
class Document {

    @ID
    String id;

    @Parent
    String parentId;

    @Metadata(name = "cmis:name", mandatory = true)
    String name;
}