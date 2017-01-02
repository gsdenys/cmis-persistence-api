package com.gsdenys.cpa.operations;

import com.gsdenys.cpa.exception.CpaAnnotationException;
import com.gsdenys.cpa.exception.CpaRuntimeException;
import com.gsdenys.cpa.operations.parser.EntityParser;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;

import java.io.InputStream;
import java.util.Map;


public class LockExec {

    private CmisExec cmisExec;

    /**
     * Default constructor
     *
     * @param exec executor CMIS
     */
    public LockExec(CmisExec exec) {
        this.cmisExec = exec;
    }

    /**
     * Check if the entity is in lockedMode (checkout)
     *
     * @param entity the entity to be refreshed and locked
     * @param <E>    some entity
     * @return boolean  <b>true</b> case the entity is locked by <b>userName</b>, other else <b>false</b>
     * @throws CpaAnnotationException error that will occur case the entity has no correctly annotated
     */
    public <E> boolean isLocked(final E entity)
            throws CpaAnnotationException, CpaRuntimeException {
        Document document = this.cmisExec.getDocument(entity);
        return Boolean.TRUE.equals(document.isVersionSeriesCheckedOut());
    }

    /**
     * Get the user name of the user that had locked the entity
     *
     * @param entity the entity to be refreshed and locked
     * @param <E>    some element
     * @return String the user name
     * @throws CpaAnnotationException error that will occur case the entity has no correctly annotated
     */
    public <E> String lockedBy(final E entity) throws CpaAnnotationException, CpaRuntimeException {
        Document document = this.cmisExec.getDocument(entity);
        return document.getVersionSeriesCheckedOutBy();
    }

    /**
     * Check if the entity is in lockedMode (checkout) by <b>userName</b>
     *
     * @param entity   the entity to be refreshed and locked
     * @param userName the user name
     * @param <E>      some entity
     * @return boolean  <b>true</b> case the entity is locked by <b>userName</b>, other else <b>false</b>
     * @throws CpaAnnotationException error that will occur case the entity has no correctly annotated
     */
    public <E> boolean isLockedBy(final E entity, final String userName)
            throws CpaAnnotationException, CpaRuntimeException {
        Document document = this.cmisExec.getDocument(entity);

        if (!isLocked(document)) {
            throw new CpaRuntimeException("The document was not locked");
        }

        String user = document.getVersionSeriesCheckedOutBy();

        return user.equals(userName);
    }

    /**
     * Lock an entity instance that is contained in the persistence context
     *
     * @param entity   the entity to be refreshed and locked
     * @param lockMode define if the entity needs to be locked (checkout) or unlocked (checkin)
     * @param <E>      some element
     * @throws CpaAnnotationException error that will occur case the entity has no correctly annotated
     */
    public <E> void lock(E entity, final boolean lockMode) throws CpaAnnotationException, CpaRuntimeException {
        Document document = this.cmisExec.getDocument(entity);

        //TODO deprecate this method and add the username as parameter. this require to use the cmis directly (less chemistry)

        boolean isLocked = document.isVersionSeriesCheckedOut();
        if (isLocked && lockMode) {
            String userName = document.getVersionSeriesCheckedOutBy();
            throw new CpaRuntimeException("The entity already locked by " + userName);
        } else if (isLocked == lockMode) {
            throw new CpaRuntimeException("The entity is already unlocked");
        }

        if (lockMode) {
            document.checkOut();
        } else {
            this.checking(document, entity);
        }
    }


    /**
     * Checking the content
     *
     * @param document the ocument to be unchecked
     * @param entity   the entity
     * @param <E>      some type
     * @throws CpaRuntimeException    the errors at runtime phase
     * @throws CpaAnnotationException error that will occur case the entity has no correctly annotated
     */
    private <E> void checking(Document document, E entity) throws CpaRuntimeException, CpaAnnotationException {
        EntityParser parser = this.cmisExec.getEntityParser(entity.getClass());

        Map<String, Object> prop = parser.getProperties(entity);

        InputStream inputStream = parser.getContent(entity);
        String name = (String) prop.get(PropertyIds.NAME);

        ContentStream stream = this.cmisExec.getPersistExec().getContent(inputStream, name);

        VersioningState state = parser.getVersioning(entity).getVersion();

        document.checkIn(
                (state.equals(VersioningState.MAJOR)),
                prop,
                stream,
                "Checking using CPA - CMIS Persistence API"
        );
    }
}
