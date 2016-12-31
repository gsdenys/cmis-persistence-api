package com.gsdenys.cpa.operations.parser;

import com.gsdenys.cpa.annotations.BaseType;
import com.gsdenys.cpa.annotations.VersioningType;
import com.gsdenys.cpa.entities.Document;
import org.apache.cxf.helpers.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Test case for {@link TypeParser}
 *
 * @author Denys G. Santos
 * @author 0.0.1
 * @since 0.0.1
 */
public class TypeParserTest {
    private TypeParser<Document> parser;
    private Document doc;

    @Before
    public void setUp() throws Exception {
        this.parser = new TypeParser<>(Document.class);
        this.doc = new Document();

        this.doc.setContent(new ByteArrayInputStream("test".getBytes()));
        this.doc.setParent("1234");
        this.doc.setName("test");
        this.doc.setId("2345");
    }

    @Test
    public void getDocTypeName() throws Exception {
        String cmisType = parser.getDocTypeName();

        Assert.assertNotNull("The CmisName should not be null", cmisType);
        Assert.assertEquals("The docType should be cmis:document", cmisType, "cmis:document");
    }

    @Test
    public void getId() throws Exception {
        String id = this.parser.getId(this.doc);

        Assert.assertNotNull("The id should exists", id);
        Assert.assertEquals("The id should be '2345'", id, "2345");
    }

    @Test
    public void getParentId() throws Exception {
        String id = this.parser.getParentId(this.doc);

        Assert.assertNotNull("The parent id should exists", id);
        Assert.assertEquals("The parent id should be '1234'", id, "1234");
    }

    @Test
    public void getContent() throws Exception {
        InputStream stream = this.parser.getContent(this.doc);

        Assert.assertNotNull("The content stream id should exists", stream);

        String str = IOUtils.toString(stream);

        Assert.assertEquals("The content should be 'test'", str, "test");
    }

    @Test
    public void getProperties() throws Exception {
        Map<String, ?> prop = this.parser.getProperties(this.doc);

        Assert.assertNotNull("The property id should exists", prop);
        Assert.assertEquals("The property should have 2 elements", prop.size(), 2);
    }

    @Test
    public void getBaseType() throws Exception {
        BaseType baseType = this.parser.getBaseType();

        Assert.assertNotNull("The base type should not be null", baseType);
        Assert.assertEquals("The base type should be Document", baseType, BaseType.DOCUMENT);
    }

    @Test
    public void getEncode() throws Exception {
        String encode = this.parser.getEncode(this.doc);

        Assert.assertNotNull("The encode should exists", encode);
        Assert.assertEquals("The encode should be 'UTF-8'", encode, "UTF-8");
    }

    @Test
    public void getVersioning() throws Exception {
        VersioningType versioning = this.parser.getVersioning(this.doc);

        Assert.assertNotNull("The Versioning should exists", versioning);
        Assert.assertEquals("The versioning should be 'MAJOR'", versioning, VersioningType.MAJOR);
    }

    @Test
    public void getEntity() throws Exception {
        Map<String, String> prop = new HashMap<>();
        prop.put("cmis:name", "test");

        String id = "2345";
        String parentId = "1234";

        InputStream is = new ByteArrayInputStream("test".getBytes());

        Document document = this.parser.getEntity(prop, is, parentId, "UTF-8", id, VersioningType.MAJOR);

        Assert.assertNotNull("The document shoul not be null", document);
        Assert.assertEquals("The entities should be equals", document, this.doc);
    }
}