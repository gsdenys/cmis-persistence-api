package com.gsdenys.cpa.operations;

import com.github.gsdenys.CmisInMemoryRunner;
import com.github.gsdenys.Configure;
import com.gsdenys.cpa.entities.Document;
import com.gsdenys.cpa.operations.parser.FieldChecker;
import com.gsdenys.cpa.persistence.EntityManager;
import com.gsdenys.cpa.persistence.EntityManagerFactory;
import com.gsdenys.cpa.persistence.EntityManagerImpl;
import com.gsdenys.cpa.persistence.Persistence;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;

import static org.junit.Assert.*;

/**
 * Created by gsdenys on 02/01/17.
 */
@RunWith(CmisInMemoryRunner.class)
@Configure
public class AbstPersistExecTest {

    private PersistExec persistExec;

    @Before
    public void setUp() throws Exception {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("sample");
        EntityManager em = emf.getEntityManager();

        Field field = EntityManagerImpl.class.getDeclaredField("cmisExec");
        field.setAccessible(true);

        CmisExec exec = (CmisExec) field.get(em);
        this.persistExec = exec.getPersistExec();
    }

    @Test
    public void update() throws Exception {

    }

    @Test
    public void create() throws Exception {
        Document document = new Document();
        document.setName("test - " + System.currentTimeMillis());
        document.setContent(new ByteArrayInputStream("teste".getBytes()));
        document.setParent(persistExec.cmisExec.getRepositoryExec().getRootFolder().getId());

        this.persistExec.persist(document);

        Assert.assertNotNull("The id should not be null", document.getId());
    }

    @Test
    public void updateDocument() throws Exception {

    }

    @Test
    public void getContent() throws Exception {

    }

    @Test
    public void getDocument() throws Exception {

    }
}
