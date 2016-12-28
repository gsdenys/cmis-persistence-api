package com.gsdenys.cpa.persistence;

import com.gsdenys.cpa.annotations.parser.DocumentTypeParser;
import com.gsdenys.cpa.exception.CpaAnnotationException;
import com.gsdenys.cpa.exception.CpaPersistenceException;
import com.gsdenys.cpa.exception.CpaRuntimeException;
import com.gsdenys.cpa.operations.CmisExec;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * org.cmis.cpa.persistence
 *
 * @author gsdenys
 * @version 0.0.0
 * @since 0.0.0
 */
public class EntityManagerImpl implements EntityManager {

    public static final String CPA_DEFAULT_REPOSITORY = "cpa-default-repository";

    private CmisExec cmisExec;

    /**
     * Builder expose just as package-protected
     *
     * @param cmisExec
     *          the cmis executor object
     */
    EntityManagerImpl(CmisExec cmisExec) {
        this.cmisExec = cmisExec;
    }

    /**
     * Save de obj at content management system through CMIS
     *
     * @param obj
     *          the object to be saved at content Management
     * @param <E>
     * @throws CpaPersistenceException
     * @throws CpaAnnotationException
     */
    public <E> void persist(E obj) throws CpaPersistenceException, CpaAnnotationException {
        DocumentTypeParser<E> typeParser = new DocumentTypeParser<E>();

        //validate object before try to persist
        typeParser.validate(obj);

        //persist Object at cmis
        this.cmisExec.persist(obj);
    }

    @Override
    public <E> void persist(E entity, Boolean lockMode) throws CpaPersistenceException, CpaAnnotationException {

    }

    @Override
    public <E> void refresh(E entity) throws CpaAnnotationException {

    }

    @Override
    public <E> void refresh(E entity, Boolean lockMode) throws CpaAnnotationException {

    }

    @Override
    public <E> void lock(E entity, Boolean lockMode) throws CpaAnnotationException {

    }

    @Override
    public <E> boolean isLocked(E entity) throws CpaAnnotationException {
        return false;
    }

    @Override
    public <E> boolean isLockedBy(E entity, String userName) throws CpaAnnotationException {
        return false;
    }

    @Override
    public <E> String lockedBy(E entity) throws CpaAnnotationException {
        return null;
    }

    @Override
    public <E> void remove(E entity) throws CpaAnnotationException, CpaPersistenceException {

    }

    @Override
    public <E> void move(E entity, E folderDest) throws CpaAnnotationException, CpaPersistenceException {

    }

    @Override
    public <E> void copy(E entity, E folderDest) throws CpaAnnotationException, CpaPersistenceException {

    }

    @Override
    public <E> Map<String, Object> getProperties(E entity) throws CpaAnnotationException {
        return null;
    }

    @Override
    public <E> Map<String, Object> getProperties(E entity, Boolean refresBefore) throws CpaAnnotationException {
        return null;
    }

    @Override
    public <E> void setProperties(E entity, Map<String, Object> properties) throws CpaAnnotationException, CpaRuntimeException {

        if(entity == null) {
            throw new CpaRuntimeException("Unable to perform properties set in the null object");
        }

        for (String key: properties.keySet()) {
            Object value = properties.get(key);

            Class clazz = entity.getClass();
            try {
                Field field = clazz.getDeclaredField(key);
                field.set(entity, value);
            } catch (NoSuchFieldException e) {
                throw new CpaRuntimeException(
                        "The field '" + key + "' do not exist at '" + clazz.getSimpleName() +"' class",
                        e.getCause()
                );
            } catch (IllegalAccessException e) {
                throw new CpaRuntimeException(
                        "The value of property '" + key +
                                "' field does not match the expected in '" + clazz.getSimpleName() +"' class",
                        e.getCause()
                );
            }
        }
    }
}
