package com.gsdenys.cpa.operations;

import com.github.gsdenys.CmisInMemoryRunner;
import com.github.gsdenys.Configure;
import com.gsdenys.cpa.entities.Document;
import com.gsdenys.cpa.persistence.EntityManagerFactory;
import com.gsdenys.cpa.persistence.EntityManagerImpl;
import com.gsdenys.cpa.persistence.Persistence;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


@RunWith(CmisInMemoryRunner.class)
@Configure
public class PersistExecTest {

    private PersistExec persistExec;
    private String folderId;

    private CmisExec exec;

    @Before
    public void setUp() throws Exception {

        //get persistence exec
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("docs");
        EntityManagerImpl em = (EntityManagerImpl) emf.getEntityManager();

        Field fieldCmisExec = EntityManagerImpl.class.getDeclaredField("cmisExec");
        fieldCmisExec.setAccessible(true);
        exec = (CmisExec) fieldCmisExec.get(em);

        Field field = CmisExec.class.getDeclaredField("persistExec");
        field.setAccessible(true);
        this.persistExec = (PersistExec) field.get(exec);

        if (this.folderId == null) {
            //create a simple folder at repository to test propose
            Session session = exec.getSession();
            Map<String, Object> prop = new HashMap<>();
            prop.put(PropertyIds.NAME, "test-cmis-" + System.currentTimeMillis());
            prop.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
            this.folderId = session.getRootFolder().createFolder(prop).getId();
        }
    }

    @Test
    public void persist() throws Exception {
        //create document 01
        Document document = new Document();
        document.setName("test - " + System.currentTimeMillis());
        document.setParent(this.folderId);
        document.setContent(new ByteArrayInputStream("test-cmis".getBytes()));

        this.persistExec.persist(document);

        Assert.assertNotNull("The document shoul not be null", document);
        Assert.assertNotNull("The Id should not be null", document.getId());

        //create doc 02 - no content
        Document document2 = new Document();
        document2.setName("test - " + System.currentTimeMillis());
        document2.setParent(this.folderId);

        this.persistExec.persist(document2);

        Assert.assertNotNull("The document shoul not be null", document2);
        Assert.assertNotNull("The Id should not be null", document2.getId());

        //update
        document.setName("alter - " + System.currentTimeMillis());
        document.setContent(new ByteArrayInputStream("bla".getBytes()));
        this.persistExec.persist(document);

        Session session = this.exec.getSession();

        String query = "select * from cmis:document where cmis:name like 'alter -%'";
        ItemIterable<QueryResult> items = session.query(query, false);

        String name = null;
        String content = null;

        for (Iterator<QueryResult> iterator = items.iterator(); iterator.hasNext(); ) {
            QueryResult item = iterator.next();
            name = (String) item.getPropertyById("cmis:name").getValues().get(0);
            content = (String) item.getPropertyByQueryName("cmis:contentStreamFileName").getFirstValue();
        }

        Assert.assertFalse("the query should return itens", items.getTotalNumItems() != 1);
        Assert.assertEquals(
                "The name of content stream should be equals the name of document",
                document.getName(),
                content
        );
        Assert.assertEquals(
                "The name of document should be equals the document recovery by search",
                document.getName(),
                name
        );
    }
}