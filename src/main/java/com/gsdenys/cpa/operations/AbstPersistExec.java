package com.gsdenys.cpa.operations;

import com.gsdenys.cpa.annotations.BaseType;
import com.gsdenys.cpa.exception.CpaAnnotationException;
import com.gsdenys.cpa.exception.CpaPersistenceException;
import com.gsdenys.cpa.exception.CpaRuntimeException;
import com.gsdenys.cpa.operations.parser.TypeParser;
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
     protected <E> void update(E entity, TypeParser parser, Session session)
            throws CpaAnnotationException, CpaPersistenceException {

        //TODO add here the error when content management do not permit to update less checkout

        Map<String, ?> properties = parser.getProperties(entity);
        BaseType baseType = parser.getBaseType();

        String idStr = parser.getId(entity);
        ObjectId id = session.createObjectId(idStr);

        CmisObject cmisObject = session.getObject(id);
        cmisObject.updateProperties(properties);

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
    protected <E> void create(E entity, TypeParser parser, Session session)
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
}
