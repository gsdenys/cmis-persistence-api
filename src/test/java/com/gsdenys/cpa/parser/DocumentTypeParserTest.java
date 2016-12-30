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
package com.gsdenys.cpa.parser;

import com.gsdenys.cpa.annotations.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Base64;
import java.util.Map;

/**
 * Test Case for a Documen class
 *
 * @author Denys G. Santos (gsdenys@gmail.com)
 * @version 0.0.1
 * @since 0.0.1
 */
public class DocumentTypeParserTest {

    private DocumentTypeParser<Document> parser;

    @Before
    public void setUp() throws Exception {
        Document doc = new Document();
        doc.id = "1234";
        doc.name = "Document";
        doc.content = Base64.getEncoder().encodeToString("teste".getBytes());

        Folder folder = new Folder();
        folder.id = "12345";

        doc.parent = folder;

        this.parser = new DocumentTypeParser<>(doc);
    }

    @Test
    public void getIdName() throws Exception {
        String idFieldName = this.parser.getIdName();

        Assert.assertNotNull("The idFieldName Should not be null", idFieldName);
        Assert.assertEquals("The idFieldName should be id", idFieldName, "id");
    }

    @Test
    public void getParentName() throws Exception {
        String parentFieldName = this.parser.getParentName();

        Assert.assertNotNull("The idFieldName Should not be null", parentFieldName);
        Assert.assertEquals("The idFieldName should be parent", parentFieldName, "parent");
    }

    @Test
    public void getDocTypeName() throws Exception {
        String docName = this.parser.getDocTypeName();

        Assert.assertNotNull("The docName Should not be null", docName);
        Assert.assertEquals("The docName should be cmis:document", docName, "cmis:document");
    }

    @Test
    public void getContentName() throws Exception {
        String fieldName = this.parser.getContentName();

        Assert.assertNotNull("The idFieldName Should not be null", fieldName);
        Assert.assertEquals("The idFieldName should be content", fieldName, "content");
    }

    @Test
    public void getProperties() throws Exception {
        Map<String, String> map = this.parser.getProperties();

        Assert.assertNotNull("The properties should not be null", map);
        Assert.assertEquals("The size of properties should be 1", map.size(), 1);
    }

}

@DocumentType(name = "cmis:folder")
class Folder {

    @ID
    String id;

}

@DocumentType(name = "cmis:document")
class Document {

    @ID
    String id;

    @Parent
    Folder parent;

    @Metadata(name = "cmis:name", mandatory = true)
    String name;

    @Content
    String content;
}