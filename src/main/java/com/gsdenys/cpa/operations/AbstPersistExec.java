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

import com.gsdenys.cpa.annotations.BaseType;
import com.gsdenys.cpa.exception.CpaAnnotationException;
import com.gsdenys.cpa.exception.CpaPersistenceException;
import com.gsdenys.cpa.exception.CpaRuntimeException;
import com.gsdenys.cpa.operations.parser.EntityParser;
import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.ObjectId;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ContentStreamImpl;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URLConnection;
import java.util.Map;

/**
 * Abstract class to Persist Execution
 *
 * @author Denys G. Santos (gsdenys@gmail.com)
 * @version 0.0.1
 * @since 0.0.1
 */
abstract class AbstPersistExec {

    protected CmisExec cmisExec;

    /**
     * default Contructor
     *
     * @param cmisExec the cmisExec object
     */
    public AbstPersistExec(CmisExec cmisExec) {
        this.cmisExec = cmisExec;
    }

    /**
     * Update the entity
     *
     * @param entity  the entity
     * @param parser  the parser for entity
     * @param session the cmis session
     * @param <E>     some item
     * @throws CpaAnnotationException  when some incompatibility annotation was found
     * @throws CpaPersistenceException when occur an error during the persistence phase
     */
     protected <E> void update(E entity, EntityParser parser, Session session)
            throws CpaAnnotationException, CpaPersistenceException {

        //TODO add here the error when content management do not permit to update less checkout

        Map<String, ?> properties = parser.getProperties(entity);
        BaseType baseType = parser.getBaseType();

        String idStr = parser.getId(entity);
        ObjectId id = session.createObjectId(idStr);

        CmisObject cmisObject = session.getObject(id);
        cmisObject.updateProperties(properties);

        //update parent (move)
         ObjectId pId = session.getObject(parser.getParentId(entity));
         CmisObject parent = session.getObject(pId);

        if (baseType.equals(BaseType.DOCUMENT)) {
            InputStream is = parser.getContent(entity);
            String name = (String) properties.get(PropertyIds.NAME);

            ContentStream stream = this.getContent(is, name);

            Document doc = (Document) cmisObject;
            doc.setContentStream(stream, true);
        }

        //TODO check about update item
    }

    /**
     * create new entity
     *
     * @param entity  the entity
     * @param parser  the parser for entity
     * @param session the cmis session
     * @param <E>     some item
     * @throws CpaAnnotationException  when some incompatibility annotation was found
     * @throws CpaPersistenceException when occur an error during the persistence phase
     */
    protected <E> void create(E entity, EntityParser parser, Session session)
            throws CpaAnnotationException, CpaRuntimeException {

        Map<String, ?> properties = parser.getProperties(entity);
        ObjectId idFolder = session.createObjectId(parser.getParentId(entity));
        VersioningState version = parser.getVersioning(entity).getVersion();

        BaseType baseType = parser.getBaseType();

        //Create new Document
        if (baseType.equals(BaseType.DOCUMENT)) {
            InputStream is = parser.getContent(entity);

            ContentStream cs = this.getContent(is, (String) properties.get(PropertyIds.NAME));
            String id = session.createDocument(properties, idFolder, cs, version).getId();

            parser.setId(entity, id);
            return;
        }

        //create new folder
        if (baseType.equals(BaseType.FOLDER)) {
            ObjectId id = session.createFolder(properties, idFolder);
            parser.setId(entity, id.getId());
            return;
        }

        // TODO create new item
    }

    /**
     * Update a document at content management
     *
     * @param prop the properties
     * @param id   the id of Document
     * @param is   the content input stream
     * @param vs   the versioning state
     * @return String id of generated document
     * @throws Exception any erros that can occur during the new document creation
     */
    protected void updateDocument(
            Map<String, ?> prop, ObjectId id, InputStream is, VersioningState vs, Session session)
            throws CpaPersistenceException {

        Document doc = (Document) session.getObject(id);
        prop.remove(PropertyIds.OBJECT_TYPE_ID);
        prop.remove(PropertyIds.OBJECT_ID);

        //update the properties
        doc.updateProperties(prop);

        //update the content
        if (is != null) {
            ContentStream stream = this.getContent(is, (String) prop.get(PropertyIds.NAME));
            doc.setContentStream(stream, true);
        }
    }

    /**
     * Get Content from {@link InputStream}
     *
     * @param is   the {@link InputStream}
     * @param name the name of content. e.g test.pdf
     * @return the content stream generated
     * @throws CpaPersistenceException any error during get the size of content
     */
    protected ContentStream getContent(InputStream is, String name) throws CpaPersistenceException {

        if (is == null) {
            return null;
        }

        String mimeType;

        try {
            //get mimmetype from stream
            mimeType = URLConnection.guessContentTypeFromStream(is);
            if (mimeType == null) {
                mimeType = "text/plain";
            }
        } catch (IOException e) {
            throw new CpaPersistenceException(
                    "Unable to identify the mimetype of content",
                    e.getCause()
            );
        }

        BigInteger size = null;
        try {
            size = new BigInteger(String.valueOf(is.available()));
        } catch (IOException e) {
            throw new CpaPersistenceException(
                    "Unable to identify the size of content",
                    e.getCause()
            );
        }

        ContentStream cs = new ContentStreamImpl(name, size, mimeType, is);

        return cs;
    }

    /**
     * get document from cmis
     *
     * @param entity
     * @param <E>
     * @return
     * @throws CpaRuntimeException
     * @throws CpaAnnotationException
     */
    protected  <E> Document getDocument(E entity) throws CpaRuntimeException, CpaAnnotationException {
        EntityParser parser = this.cmisExec.getEntityParser(entity.getClass());

        if (!parser.getBaseType().equals(BaseType.DOCUMENT)) {
            throw new CpaRuntimeException("Cannot apply checkout to entity nod derived by cmis:document");
        }

        //load CMIS object from repository
        Session session = this.cmisExec.getSession();
        ObjectId id = session.createObjectId(parser.getId(entity));

        return  (Document) session.getObject(id);
    }
}
