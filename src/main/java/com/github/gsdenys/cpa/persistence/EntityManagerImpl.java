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
package com.github.gsdenys.cpa.persistence;

import com.github.gsdenys.cpa.exception.CpaAnnotationException;
import com.github.gsdenys.cpa.operations.CmisExec;
import com.github.gsdenys.cpa.exception.CpaRuntimeException;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Implementation of {@link EntityManager}.
 *
 * @author Denys G. Santos (gsdenys@gmail.com)
 * @version 0.0.1
 * @since 0.0.1
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
