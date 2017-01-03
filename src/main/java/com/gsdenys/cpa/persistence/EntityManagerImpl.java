package com.gsdenys.cpa.persistence;

import com.gsdenys.cpa.exception.CpaAnnotationException;
import com.gsdenys.cpa.exception.CpaRuntimeException;
import com.gsdenys.cpa.operations.CmisExec;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * org.cmis.cpa.persistence
 *
 * @author gsdenys
 * @version 0.0.0
 * @since 0.0.0
 */
public class EntityManagerImpl implements EntityManager {

    private CmisExec cmisExec;

    /**
     * Builder expose just as package-protected
     *
     * @param cmisExec the cmis executor object
     */
    EntityManagerImpl(CmisExec cmisExec) {
        this.cmisExec = cmisExec;
    }

    @Override
    public <E> void persist(E entity) throws CpaAnnotationException, CpaRuntimeException {
        this.cmisExec.getPersistExec().persist(entity);
    }

    @Override
    public <E> void persist(E entity, Boolean lockMode) throws CpaRuntimeException, CpaAnnotationException {
        this.persist(entity);
        this.lock(entity, lockMode);
    }

    @Override
    public <E> void refresh(E entity) throws CpaAnnotationException, CpaRuntimeException {
        this.cmisExec.getPersistExec().refresh(entity);
    }

    @Override
    public <E> void refresh(E entity, Boolean lockMode) throws CpaAnnotationException, CpaRuntimeException {
        this.refresh(entity);
        this.lock(entity, lockMode);
    }

    @Override
    public <E> void lock(E entity, Boolean lockMode) throws CpaAnnotationException, CpaRuntimeException {
        this.cmisExec.getLockExec().lock(entity, lockMode);
    }

    @Override
    public <E> boolean isLocked(E entity) throws CpaAnnotationException, CpaRuntimeException {
        return this.cmisExec.getLockExec().isLocked(entity);
    }

    @Override
    public <E> boolean isLockedBy(E entity, String userName)
            throws CpaAnnotationException, CpaRuntimeException {
        return this.cmisExec.getLockExec().isLockedBy(entity, userName);
    }

    @Override
    public <E> String lockedBy(E entity) throws CpaAnnotationException, CpaRuntimeException {
        return this.cmisExec.getLockExec().lockedBy(entity);
    }

    @Override
    public <E> void remove(E entity) throws CpaAnnotationException, CpaRuntimeException {
        this.cmisExec.getPersistExec().remove(entity, true);
    }

    @Override
    public <E> void remove(E entity, boolean allVersions) throws CpaAnnotationException, CpaRuntimeException {
        this.cmisExec.getPersistExec().remove(entity, allVersions);
    }

    @Override
    public <E> E copy(E entity, E folderDest) throws CpaAnnotationException, CpaRuntimeException {
        return this.cmisExec.getLocationExec().copy(entity, folderDest);
    }

    @Override
    public <E> Map<String, Object> getProperties(E entity) throws CpaAnnotationException, CpaRuntimeException {
        this.checkEntityNotNull(entity);
        return this.cmisExec.getPersistExec().getProperties(entity);
    }

    @Override
    public <E> Map<String, Object> getProperties(E entity, Boolean refreshBefore) throws CpaAnnotationException, CpaRuntimeException {
        this.checkEntityNotNull(entity);

        if (refreshBefore) {
            this.refresh(entity);
        }

        return this.getProperties(entity);
    }

    @Override
    public <E> void setProperties(E entity, final Map<String, Object> properties) throws CpaAnnotationException, CpaRuntimeException {
        this.checkEntityNotNull(entity);

        for (String key : properties.keySet()) {
            Object value = properties.get(key);

            Class clazz = entity.getClass();
            try {
                Field field = clazz.getDeclaredField(key);
                field.setAccessible(true);
                field.set(entity, value);
            } catch (NoSuchFieldException e) {
                throw new CpaRuntimeException(
                        "The field '" + key + "' do not exist at '" + clazz.getSimpleName() + "' class",
                        e.getCause()
                );
            } catch (IllegalAccessException e) {
                throw new CpaRuntimeException(
                        "The value of property '" + key +
                                "' field does not match the expected in '" + clazz.getSimpleName() + "' class",
                        e.getCause()
                );
            }
        }
    }

    /**
     * Check if entity is null and when true throws a {@link CpaRuntimeException} error
     *
     * @param entity the entity to be tested
     * @param <E>    some element
     * @throws CpaRuntimeException and exception that be throw when entity is null
     */
    private <E> void checkEntityNotNull(E entity) throws CpaRuntimeException {
        if (entity == null) {
            throw new CpaRuntimeException("Unable to perform get properties. The entity is null");
        }
    }
}
