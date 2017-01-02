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
package com.gsdenys.cpa.operations.parser;

import com.gsdenys.cpa.entities.*;
import com.gsdenys.cpa.exception.CpaAnnotationException;
import com.gsdenys.cpa.exception.CpaRuntimeException;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;

/**
 * Test Case for {@link AbstTypeParser} abstract class
 *
 * @author Denys G. Santos (gsdenys@gmail.com)
 * @version 0.0.1
 * @since 0.0.1
 */
public class AbstEntityParserTest {

    @Test
    public void validate() throws Exception {
        ParserForTest<Document> p = new ParserForTest<>(Document.class, false);

        p.validate();

        Assert.assertNotNull("The type should not be null", p.type);
        Assert.assertNotNull("The baseType should not be null", p.baseType);
        Assert.assertNotNull("The checker should not be null", p.checker);

        Assert.assertNotNull("The content should not be null", p.elementsName.getContent());
        Assert.assertNotNull("The ID should not be null", p.elementsName.getId());
        Assert.assertNotNull("The Parent should be null", p.elementsName.getParent());

        Assert.assertNull("The encode should be null", p.elementsName.getEncode());
        Assert.assertNull("The Versioning should be null", p.elementsName.getVersioning());

        Assert.assertNotNull("The properties should not be null", p.properties);
        Assert.assertFalse("The properties should not be null", p.properties.isEmpty());

        ParserForTest<DocumentFullConfigured> pF = new ParserForTest<>(DocumentFullConfigured.class, false);
        pF.validate();

        Assert.assertNotNull("The content should not be null", pF.elementsName.getContent());
        Assert.assertNotNull("The ID should not be null", pF.elementsName.getId());
        Assert.assertNotNull("The encode should not be null", pF.elementsName.getEncode());
        Assert.assertNotNull("The Parent should not be null", pF.elementsName.getParent());
        Assert.assertNotNull("The Versioning should not be null", pF.elementsName.getVersioning());

        try {
            ParserForTest<DocumentErrorMetadataID> pEMI = new ParserForTest<>(
                    DocumentErrorMetadataID.class,
                    false
            );
            pEMI.validate();
            Assert.fail("Cannot execute validation to a type with annotation error");
        } catch (CpaAnnotationException | CpaRuntimeException e) {
            //nothing to do
        }
    }

    @Test
    public void processTypeAnnotation() throws Exception {
        ParserForTest<Document> parser = new ParserForTest<>(Document.class, false);

        parser.processTypeAnnotation();

        Assert.assertNotNull("The type should not be null", parser.type);
        Assert.assertEquals(
                "The type should be cmis:document",
                parser.type,
                "cmis:document"
        );
    }

    @Test
    public void processFieldsAnnotation() throws Exception {
        ParserForTest<Document> p = new ParserForTest<>(Document.class, false);
        p.processFieldsAnnotation();

        Assert.assertNotNull("The content should not be null", p.elementsName.getContent());
        Assert.assertNotNull("The ID should not be null", p.elementsName.getId());
        Assert.assertNotNull("The Parent should be null", p.elementsName.getParent());

        Assert.assertNull("The encode should be null", p.elementsName.getEncode());
        Assert.assertNull("The Versioning should be null", p.elementsName.getVersioning());

        Assert.assertNotNull("The properties should not be null", p.properties);
        Assert.assertFalse("The properties should not be null", p.properties.isEmpty());

        ParserForTest<DocumentFullConfigured> pF = new ParserForTest<>(DocumentFullConfigured.class, false);
        pF.processFieldsAnnotation();

        Assert.assertNotNull("The content should not be null", pF.elementsName.getContent());
        Assert.assertNotNull("The ID should not be null", pF.elementsName.getId());
        Assert.assertNotNull("The encode should not be null", pF.elementsName.getEncode());
        Assert.assertNotNull("The Parent should not be null", pF.elementsName.getParent());
        Assert.assertNotNull("The Versioning should not be null", pF.elementsName.getVersioning());

        try {
            ParserForTest<DocumentErrorMetadataID> pEMI = new ParserForTest<>(
                    DocumentErrorMetadataID.class,
                    false
            );
            pEMI.processFieldsAnnotation();
            Assert.fail("Cannot execute validation to a type with annotation error");
        } catch (CpaAnnotationException | CpaRuntimeException e) {
            //nothing to do
        }
    }

    @Test
    public void checkMetadata() throws Exception {
        ParserForTest<Document> p = new ParserForTest<>(Document.class, false);
        Field field = Document.class.getDeclaredField("name");
        FieldChecker fieldChecker = new FieldChecker();

        p.checkMetadata(field, fieldChecker);

        Assert.assertNotNull("The properties should not be null", p.properties);
        Assert.assertFalse("The properties should not be null", p.properties.isEmpty());

        try {
            ParserForTest<DocumentError2Metadata> p2M = new ParserForTest<>(
                    DocumentError2Metadata.class,
                    false
            );

            Field fieldE2 = DocumentError2Metadata.class.getDeclaredField("name2");
            Field fieldE1 = DocumentError2Metadata.class.getDeclaredField("name");

            FieldChecker fieldCheckerE = new FieldChecker();

            p2M.checkMetadata(fieldE1, fieldCheckerE);
            p2M.checkMetadata(fieldE2, fieldCheckerE);

            Assert.fail("Cannot execute validation to a type with 2 annotation mapping same property");
        } catch (CpaAnnotationException | CpaRuntimeException e) {
            //nothing to do
        }
    }

    @Test
    public void checkID() throws Exception {
        ParserForTest<Document> p = new ParserForTest<>(Document.class, false);
        Field field = Document.class.getDeclaredField("id");
        FieldChecker fieldChecker = new FieldChecker();

        p.checkID(field, fieldChecker);

        Assert.assertNotNull("The idPropertyName should not be null", p.elementsName.getId());

        try {
            ParserForTest<DocumentError2ID> p2M = new ParserForTest<>(
                    DocumentError2ID.class,
                    false
            );

            Field fieldE2 = DocumentError2ID.class.getDeclaredField("id2");
            Field fieldE1 = DocumentError2ID.class.getDeclaredField("id");

            FieldChecker fieldCheckerE = new FieldChecker();

            p2M.checkID(fieldE1, fieldCheckerE);
            p2M.checkID(fieldE2, fieldCheckerE);

            Assert.fail("Cannot execute validation to a type with 2 or more fields annotated with @ID");
        } catch (CpaAnnotationException | CpaRuntimeException e) {
            //nothing to do
        }

        try {
            ParserForTest<DocumentErrorMetadataID> pMI = new ParserForTest<>(
                    DocumentErrorMetadataID.class,
                    false
            );

            Field fieldE2 = DocumentErrorMetadataID.class.getDeclaredField("name");
            FieldChecker fieldCheckerE = new FieldChecker();


            pMI.checkMetadata(fieldE2, fieldCheckerE);
            pMI.checkID(fieldE2, fieldCheckerE);

            Assert.fail(
                    "Cannot execute validation to a type that have a fields annotated with @ID " +
                            "and @Metadata at the some time"
            );
        } catch (CpaAnnotationException | CpaRuntimeException e) {
            //nothing to do
        }
    }

    @Test
    public void checkParent() throws Exception {
        ParserForTest<Document> p = new ParserForTest<>(Document.class, false);
        Field field = Document.class.getDeclaredField("parent");
        FieldChecker fieldChecker = new FieldChecker();

        p.checkParent(field, fieldChecker);

        Assert.assertNotNull("The properties should not be null", p.elementsName.getParent());

        try {
            ParserForTest<DocumentError2Parent> p2M = new ParserForTest<>(
                    DocumentError2Parent.class,
                    false
            );

            Field fieldE2 = DocumentError2Parent.class.getDeclaredField("parent2");
            Field fieldE1 = DocumentError2Parent.class.getDeclaredField("parent");

            FieldChecker fieldCheckerE = new FieldChecker();

            p2M.checkParent(fieldE1, fieldCheckerE);
            p2M.checkParent(fieldE2, fieldCheckerE);

            Assert.fail("Cannot execute validation to a type with 2 or more fields annotated with @Parent");
        } catch (CpaAnnotationException | CpaRuntimeException e) {
            //nothing to do
        }

        try {
            ParserForTest<DocumentErrorMetadataParent> p2M = new ParserForTest<>(
                    DocumentErrorMetadataParent.class,
                    false
            );

            Field fieldE2 = DocumentErrorMetadataParent.class.getDeclaredField("name");
            FieldChecker fieldCheckerE = new FieldChecker();

            p2M.checkMetadata(fieldE2, fieldCheckerE);
            p2M.checkParent(fieldE2, fieldCheckerE);

            Assert.fail(
                    "Cannot execute validation to a type that have a fields annotated with @Parent " +
                            "and @Metadata at the some time"
            );
        } catch (CpaAnnotationException | CpaRuntimeException e) {
            //nothing to do
        }
    }

    @Test
    public void checkEncode() throws Exception {
        ParserForTest<DocumentFullConfigured> p = new ParserForTest<>(DocumentFullConfigured.class, false);
        Field field = DocumentFullConfigured.class.getDeclaredField("encode");
        FieldChecker fieldChecker = new FieldChecker();

        p.checkEncode(field, fieldChecker);

        Assert.assertNotNull("The Encode element name should not be null", p.elementsName.getEncode());

        try {
            ParserForTest<DocumentError2Encode> p2M = new ParserForTest<>(
                    DocumentError2Encode.class,
                    false
            );

            Field fieldE2 = DocumentError2Encode.class.getDeclaredField("encode2");
            Field fieldE1 = DocumentError2Encode.class.getDeclaredField("encode");

            FieldChecker fieldCheckerE = new FieldChecker();

            p2M.checkEncode(fieldE1, fieldCheckerE);
            p2M.checkEncode(fieldE2, fieldCheckerE);

            Assert.fail("Cannot execute validation to a type with 2 or more fields annotated with @Encode");
        } catch (CpaAnnotationException | CpaRuntimeException e) {
            //nothing to do
        }

        try {
            ParserForTest<DocumentErrorMetadataEncode> p2M = new ParserForTest<>(
                    DocumentErrorMetadataEncode.class,
                    false
            );

            Field fieldE2 = DocumentErrorMetadataEncode.class.getDeclaredField("name");
            FieldChecker fieldCheckerE = new FieldChecker();

            p2M.checkMetadata(fieldE2, fieldCheckerE);
            p2M.checkEncode(fieldE2, fieldCheckerE);

            Assert.fail(
                    "Cannot execute validation to a type that have a fields annotated with @Encode " +
                            "and @Metadata at the some time"
            );
        } catch (Exception e) {
            //nothing to do
        }
    }

    @Test
    public void checkVersioning() throws Exception {
        ParserForTest<DocumentFullConfigured> p = new ParserForTest<>(DocumentFullConfigured.class, false);
        Field field = DocumentFullConfigured.class.getDeclaredField("versioningType");
        FieldChecker fieldChecker = new FieldChecker();

        p.checkVersioning(field, fieldChecker);

        Assert.assertNotNull("The Versioning element name should not be null", p.elementsName.getVersioning());

        try {
            ParserForTest<DocumentError2Versioning> p2M = new ParserForTest<>(
                    DocumentError2Versioning.class,
                    false
            );

            Field fieldE2 = DocumentError2Versioning.class.getDeclaredField("versioningType2");
            Field fieldE1 = DocumentError2Versioning.class.getDeclaredField("versioningType");

            FieldChecker fieldCheckerE = new FieldChecker();

            p2M.checkVersioning(fieldE1, fieldCheckerE);
            p2M.checkVersioning(fieldE2, fieldCheckerE);

            Assert.fail("Cannot execute validation to a type with 2 or more fields annotated with @Versioning");
        } catch (CpaAnnotationException | CpaRuntimeException e) {
            //nothing to do
        }

        try {
            ParserForTest<DocumentErrorMetadataVersioning> p2M = new ParserForTest<>(
                    DocumentErrorMetadataVersioning.class,
                    false
            );

            Field fieldE2 = DocumentErrorMetadataVersioning.class.getDeclaredField("name");
            FieldChecker fieldCheckerE = new FieldChecker();

            p2M.checkMetadata(fieldE2, fieldCheckerE);
            p2M.checkVersioning(fieldE2, fieldCheckerE);

            Assert.fail(
                    "Cannot execute validation to a type that have a fields annotated with @Versioning " +
                            "and @Metadata at the some time"
            );
        } catch (Exception e) {
            //nothing to do
        }
    }

    @Test
    public void checkContent() throws Exception {
        ParserForTest<DocumentFullConfigured> p = new ParserForTest<>(DocumentFullConfigured.class, false);
        Field field = DocumentFullConfigured.class.getDeclaredField("content");
        FieldChecker fieldChecker = new FieldChecker();

        p.checkContent(field, fieldChecker);

        Assert.assertNotNull("The Versioning element name should not be null", p.elementsName.getContent());

        try {
            ParserForTest<DocumentError2Content> p2M = new ParserForTest<>(
                    DocumentError2Content.class,
                    false
            );

            Field fieldE2 = DocumentError2Content.class.getDeclaredField("content2");
            Field fieldE1 = DocumentError2Content.class.getDeclaredField("content");

            FieldChecker fieldCheckerE = new FieldChecker();

            p2M.checkContent(fieldE1, fieldCheckerE);
            p2M.checkContent(fieldE2, fieldCheckerE);

            Assert.fail("Cannot execute validation to a type with 2 or more fields annotated with @Versioning");
        } catch (CpaAnnotationException | CpaRuntimeException e) {
            //nothing to do
        }

        try {
            ParserForTest<DocumentErrorMetadataContent> p2M = new ParserForTest<>(
                    DocumentErrorMetadataContent.class,
                    false
            );

            Field fieldE2 = DocumentErrorMetadataContent.class.getDeclaredField("name");
            FieldChecker fieldCheckerE = new FieldChecker();

            p2M.checkMetadata(fieldE2, fieldCheckerE);
            p2M.checkContent(fieldE2, fieldCheckerE);

            Assert.fail(
                    "Cannot execute validation to a type that have a fields annotated with @Content " +
                            "and @Metadata at the some time"
            );
        } catch (Exception e) {
            //nothing to do
        }
    }
}

/**
 * Class that extends from {@link AbstTypeParser} for a test propose
 *
 * @param <T> some entity element
 */
class ParserForTest<T> extends AbstTypeParser<T> {

    ParserForTest(Class<T> clazz, boolean validate) throws CpaRuntimeException, CpaAnnotationException {
        super(clazz, validate);
    }
}
