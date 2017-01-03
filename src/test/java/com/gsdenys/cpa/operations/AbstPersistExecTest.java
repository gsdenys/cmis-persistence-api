package com.gsdenys.cpa.operations;

import com.github.gsdenys.CmisInMemoryRunner;
import com.github.gsdenys.Configure;
import com.gsdenys.cpa.entities.Document;
import com.gsdenys.cpa.entities.Folder;
import com.gsdenys.cpa.operations.parser.EntityParser;
import com.gsdenys.cpa.persistence.EntityManager;
import com.gsdenys.cpa.persistence.EntityManagerFactory;
import com.gsdenys.cpa.persistence.EntityManagerImpl;
import com.gsdenys.cpa.persistence.Persistence;
import org.apache.chemistry.opencmis.client.api.ObjectId;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by gsdenys on 02/01/17.
 */
@RunWith(CmisInMemoryRunner.class)
@Configure
public class AbstPersistExecTest {

    private PersistExec persistExec;

    @Before
    public void setUp() throws Exception {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("docs");
        EntityManager em = emf.getEntityManager();

        Field field = EntityManagerImpl.class.getDeclaredField("cmisExec");
        field.setAccessible(true);

        CmisExec exec = (CmisExec) field.get(em);
        this.persistExec = exec.getPersistExec();
    }

    @Test
    public void update() throws Exception {
        //Create
        Document document = new Document();
        document.setName("test - " + System.currentTimeMillis());
        document.setContent(new ByteArrayInputStream("teste".getBytes()));
        document.setParent(persistExec.cmisExec.getRepositoryExec().getRootFolder().getId());
        this.persistExec.persist(document);
        Assert.assertNotNull("The id should not be null", document.getId());

        Folder folder = new Folder();
        folder.setName("test - " + System.currentTimeMillis());
        folder.setParent(persistExec.cmisExec.getRepositoryExec().getRootFolder().getId());
        this.persistExec.persist(folder);
        Assert.assertNotNull("The id should not be null", folder.getId());

        //UPDATE
        document.setName("UPDATE - " + System.currentTimeMillis());
        this.persistExec.persist(document);
        this.persistExec.refresh(document);
        Assert.assertTrue(
                "The Document Name should starts with \"UPDATE\"",
                document.getName().startsWith("UPDATE")
        );

        folder.setName("UPDATE - " + System.currentTimeMillis());
        this.persistExec.persist(folder);
        this.persistExec.refresh(folder);
        Assert.assertTrue(
                "The Folder Name should starts with \"UPDATE\"",
                folder.getName().startsWith("UPDATE")
        );
    }

    @Test
    public void create() throws Exception {
        Document document = new Document();
        document.setName("test - " + System.currentTimeMillis());
        document.setContent(new ByteArrayInputStream("teste".getBytes()));
        document.setParent(persistExec.cmisExec.getRepositoryExec().getRootFolder().getId());

        this.persistExec.persist(document);

        Assert.assertNotNull("The id should not be null", document.getId());

        Folder folder = new Folder();
        folder.setName("test - " + System.currentTimeMillis());
        folder.setParent(persistExec.cmisExec.getRepositoryExec().getRootFolder().getId());

        this.persistExec.persist(folder);
        Assert.assertNotNull("The id should not be null", folder.getId());
    }

    @Test
    public void updateDocument() throws Exception {
        Document document = new Document();
        document.setName("test - " + System.currentTimeMillis());
        document.setContent(new ByteArrayInputStream("teste".getBytes()));
        document.setParent(persistExec.cmisExec.getRepositoryExec().getRootFolder().getId());

        this.persistExec.persist(document);

        Assert.assertNotNull("The id should not be null", document.getId());


        //updateDocument
        EntityParser<Document> parser = new EntityParser<>(Document.class);
        Session session = this.persistExec.cmisExec.getSession();

        Map<String, ?> prop = this.persistExec.getProperties(document);
        ObjectId id = session.createObjectId(parser.getId(document));
        InputStream stream = new ByteArrayInputStream("update-test".getBytes());

        this.persistExec.updateDocument(prop,id,stream, VersioningState.NONE, session);
        this.persistExec.refresh(document);

        stream = document.getContent();

        Writer writer = new StringWriter();
        IOUtils.copy(stream, writer, "UTF-8");
        String str = writer.toString();

        Assert.assertEquals("The content of Document should be \"update-test\"", str, "update-test");
    }

    @Test
    public void getContent() throws Exception {
        ContentStream stream = this.persistExec.getContent(
                new ByteArrayInputStream("teste".getBytes()),
                "teste - " + System.currentTimeMillis()
        );

        Assert.assertNotNull("the stream should not be null", stream);
    }

    @Test
    public void getDocument() throws Exception {
        Document document = new Document();
        document.setName("test - " + System.currentTimeMillis());
        document.setContent(new ByteArrayInputStream("teste".getBytes()));
        document.setParent(persistExec.cmisExec.getRepositoryExec().getRootFolder().getId());

        this.persistExec.persist(document);

        org.apache.chemistry.opencmis.client.api.Document doc =
                this.persistExec.getDocument(document);

        Assert.assertNotNull("The doc should not be null",doc);
    }
}
