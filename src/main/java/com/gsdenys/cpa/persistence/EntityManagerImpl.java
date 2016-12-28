package com.gsdenys.cpa.persistence;

import com.gsdenys.cpa.annotations.parser.DocumentTypeParser;
import com.gsdenys.cpa.exception.CpaAnnotationException;
import com.gsdenys.cpa.exception.CpaPersistenceException;
import com.gsdenys.cpa.exception.CpaRuntimeException;
import com.gsdenys.cpa.operations.CmisExec;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
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

   @Override
    public <E> void persist(E entity) throws CpaPersistenceException, CpaAnnotationException {
        DocumentTypeParser<E> typeParser = new DocumentTypeParser<E>();

        //validate object before try to persist
        typeParser.validate(entity);

        //persist Object at cmis
        this.cmisExec.persist(entity);
    }

    @Override
    public <E> void persist(E entity, Boolean lockMode) throws CpaPersistenceException, CpaAnnotationException {
        this.persist(entity);
        this.lock(entity, lockMode);
    }

    @Override
    public <E> void refresh(E entity) throws CpaAnnotationException {
        //TODO do anything
    }

    @Override
    public <E> void refresh(E entity, Boolean lockMode) throws CpaAnnotationException {
        //TODO do anything
    }

    @Override
    public <E> void lock(E entity, Boolean lockMode) throws CpaAnnotationException {
        //TODO do anything
    }

    @Override
    public <E> boolean isLocked(E entity) throws CpaAnnotationException {
        //TODO do anything
        return false;
    }

    @Override
    public <E> boolean isLockedBy(E entity, String userName) throws CpaAnnotationException {
        //TODO do anything
        return false;
    }

    @Override
    public <E> String lockedBy(E entity) throws CpaAnnotationException {
        //TODO do anything
        return null;
    }

    @Override
    public <E> void remove(E entity) throws CpaAnnotationException, CpaPersistenceException {
        //TODO do anything
    }

    @Override
    public <E> void move(E entity, E folderDest) throws CpaAnnotationException, CpaPersistenceException {
        //TODO do anything
    }

    @Override
    public <E> void copy(E entity, E folderDest) throws CpaAnnotationException, CpaPersistenceException {
        //TODO do anything
    }

    @Override
    public <E> Map<String, Object> getProperties(E entity) throws CpaAnnotationException, CpaRuntimeException {
        this.checkEntityNotNull(entity);

        Map<String, Object> map = new HashMap<>();
        Field fields[] = entity.getClass().getDeclaredFields();

        for (Field field : fields){
            try {
                field.setAccessible(true);
                Object obj = field.get(entity);
                map.put(field.getName(), obj);
            } catch (IllegalAccessException e) {
                throw new CpaRuntimeException("Unable to perform get operation from " + field.getName(), e.getCause());
            }
        }

        return map;
    }

    @Override
    public <E> Map<String, Object> getProperties(E entity, Boolean refreshBefore) throws CpaAnnotationException, CpaRuntimeException {
        this.checkEntityNotNull(entity);

        if(refreshBefore) {
            this.refresh(entity);
        }

        return this.getProperties(entity);
    }

    @Override
    public <E> void setProperties(E entity, Map<String, Object> properties) throws CpaAnnotationException, CpaRuntimeException {
        this.checkEntityNotNull(entity);

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

    /**
     * Check if entity is null and when true throws a {@link CpaRuntimeException} error
     *
     * @param entity the entity to be tested
     * @param <E> some element
     * @throws CpaRuntimeException and exception that be throw when entity is null
     */
    private <E> void checkEntityNotNull(E entity) throws CpaRuntimeException {
        if(entity == null) {
            throw new CpaRuntimeException("Unable to perform get properties. The entity is null");
        }
    }
}
