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

import com.gsdenys.cpa.annotations.*;
import com.gsdenys.cpa.exception.CpaAnnotationException;
import com.gsdenys.cpa.exception.CpaRuntimeException;
import org.apache.chemistry.opencmis.commons.PropertyIds;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Validate and extract data
 *
 * @author Denys G. Santos (gsdenys@gmail.com)
 * @version 0.0.1
 * @since 0.0.1
 */
public class EntityParser<T> extends AbstTypeParser<T> {

    /**
     * Default builder
     *
     * @param clazz the clazz to be parsed
     * @throws CpaAnnotationException error throws when any annotation incompatibility was identified
     * @throws CpaRuntimeException    any erros of runtime
     */
    public EntityParser(Class<T> clazz) throws CpaAnnotationException, CpaRuntimeException {
        super(clazz);
    }


    /**
     * Get the Name of Document Entity
     *
     * @return String the document Entity
     */
    public String getDocTypeName() {
        return super.type;
    }

    /**
     * Get the value of property
     *
     * @param entity    the entity
     * @param fieldName the field name
     * @return Object the value of field
     * @throws NoSuchFieldException   field no exist
     * @throws IllegalAccessException field is not accessible
     */
    private Object get(final T entity, final String fieldName)
            throws NoSuchFieldException, IllegalAccessException {
        Field f = entity.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);

        Object obj = f.get(entity);

        return (obj != null) ? obj : null;
    }


    /**
     * Get the value of {@link ID} annotated field
     *
     * @param entity the entiti
     * @return String value of {@link ID} annotated field
     * @throws CpaAnnotationException any identified annotation error
     */
    public String getId(final T entity) throws CpaAnnotationException {
        try {
            Object objId = this.get(entity, super.elementsName.getId());

            if (objId == null) {
                return null;
            }

            return (String) objId;
        } catch (NoSuchFieldException e) {
            throw new CpaAnnotationException(
                    "@ID annotated field not Found",
                    e.getCause()
            );
        } catch (IllegalAccessException e) {
            throw new CpaAnnotationException(
                    "@ID annotated field is not accessible",
                    e.getCause()
            );
        } catch (ClassCastException e) {
            throw new CpaAnnotationException("The @Parent annotated field needs to have a String type");
        }
    }

    /**
     * Get the value of {@link Parent} annotated field
     *
     * @param entity the entiti
     * @return String value of {@link Parent} annotated field
     * @throws CpaAnnotationException any identified annotation error
     */
    public String getParentId(final T entity) throws CpaAnnotationException {
        try {
            return (String) this.get(entity, super.elementsName.getParent());
        } catch (NoSuchFieldException e) {
            throw new CpaAnnotationException(
                    "@ID annotated field not Found",
                    e.getCause()
            );
        } catch (IllegalAccessException e) {
            throw new CpaAnnotationException(
                    "@ID annotated field is not accessible",
                    e.getCause()
            );
        } catch (ClassCastException e) {
            throw new CpaAnnotationException(
                    "The @Parent annotated field needs to have a String type",
                    e.getCause()
            );
        } catch (NullPointerException e) {
            throw new CpaAnnotationException(
                    "The @Parent annotated field needs to exists. It cannot be null",
                    e.getCause()
            );
        }
    }

    /**
     * Get the value of {@link Content} annotated field
     *
     * @param entity the entiti
     * @return String value of {@link Content} annotated field
     * @throws CpaAnnotationException any identified annotation error
     */
    public InputStream getContent(final T entity) throws CpaAnnotationException {
        try {
            Object obj = this.get(entity, super.elementsName.getContent());

            if (obj == null) {
                return null;
            }

            return (InputStream) obj;
        } catch (NoSuchFieldException e) {
            throw new CpaAnnotationException(
                    "@Content annotated field not Found",
                    e.getCause()
            );
        } catch (IllegalAccessException e) {
            throw new CpaAnnotationException(
                    "@Content annotated field is not accessible",
                    e.getCause()
            );
        } catch (ClassCastException e) {
            throw new CpaAnnotationException(
                    "The @Content annotated field needs to have a InputStream type",
                    e.getCause()
            );
        }
    }

    /**
     * Get the properties mapped at the entity
     *
     * @param entity the entity to get the field map
     * @return Map the metadata field map
     * @throws CpaAnnotationException any identified annotation error
     */
    public Map<String, ?> getProperties(final T entity) throws CpaAnnotationException {
        Map<String, Object> prop = new HashMap<>();

        prop.put(PropertyIds.OBJECT_TYPE_ID, super.type);

        for (String key : super.properties.keySet()) {
            String fieldName = super.properties.get(key);

            try {
                Object obj = this.get(entity, fieldName);
                prop.put(key, obj);
            } catch (NoSuchFieldException e) {
                throw new CpaAnnotationException(
                        "Unable locate the field '" + fieldName +
                                "' mapped as a @Metadata with name '" + key +
                                "' at the '" + entity.getClass().getSimpleName() + "' class",
                        e.getCause()
                );
            } catch (IllegalAccessException e) {
                throw new CpaAnnotationException(
                        "Unable access the field '" + fieldName +
                                "' mapped as a @Metadata with name '" + key +
                                "' at the '" + entity.getClass().getSimpleName() + "' class",
                        e.getCause()
                );
            }
        }

        return prop;
    }

    /**
     * get the base type
     *
     * @return BaseType the base type
     */
    public BaseType getBaseType() {
        return super.baseType;
    }

    /**
     * set the id in the entity
     *
     * @param entity the entity
     * @param id     the ID
     * @throws CpaRuntimeException any error during the set proccess
     */
    public void setId(T entity, String id) throws CpaRuntimeException {
        try {
            Field idE = this.clazz.getDeclaredField(this.elementsName.getId());
            idE.setAccessible(true);
            idE.set(entity, id);
        } catch (NoSuchFieldException e) {
            throw new CpaRuntimeException(
                    "The Field that represents id was not Found",
                    e.getCause()
            );
        } catch (IllegalAccessException e) {
            throw new CpaRuntimeException(
                    "The field that represents id is not accessible",
                    e.getCause()
            );
        }
    }

    /**
     * set the id in the entity
     *
     * @param entity the entity
     * @param is     the input stream
     * @throws CpaRuntimeException any error during the set proccess
     */
    public void setContent(T entity, final InputStream is) throws CpaRuntimeException {
        try {
            Field contentField = this.clazz.getDeclaredField(this.elementsName.getContent());
            contentField.setAccessible(true);
            contentField.set(entity, is);
        } catch (NoSuchFieldException e) {
            throw new CpaRuntimeException(
                    "Field that represents content not Found",
                    e.getCause()
            );
        } catch (IllegalAccessException e) {
            throw new CpaRuntimeException(
                    "The field that represents content is not accessible",
                    e.getCause()
            );
        }
    }


    /**
     * get the encode mapped at the entity
     *
     * @param entity the entity to get encode
     * @return String the encode type
     * @throws CpaAnnotationException error throws when any annotation incompatibility was identified
     */
    public String getEncode(final T entity) throws CpaAnnotationException {
        if (!super.checker.isEncode()) {
            return entity.getClass().getAnnotation(Entity.class).encode();
        }

        try {
            Field field = entity.getClass().getField(super.elementsName.getEncode());
            field.setAccessible(true);
            return (String) field.get(entity);
        } catch (NoSuchFieldException e) {
            throw new CpaAnnotationException(
                    "Unable to find the Field annotated with @Encode",
                    e.getCause()
            );
        } catch (IllegalAccessException e) {
            throw new CpaAnnotationException(
                    "The Field annotated with @Encode has no accessible.",
                    e.getCause()
            );
        } catch (ClassCastException e) {
            throw new CpaAnnotationException(
                    "The Field annotated with @Encode needs to have a String type",
                    e.getCause()
            );
        }
    }

    /**
     * get the kind of versioning mapped at the entity
     *
     * @param entity the entity to get encode
     * @return String the kind of versioning type
     * @throws CpaAnnotationException error throws when any annotation incompatibility was identified
     */
    public VersioningType getVersioning(final T entity) throws CpaAnnotationException {
        if (!super.checker.isVersioning()) {
            return entity.getClass().getAnnotation(Entity.class).versioning();
        }

        try {
            Field field = entity.getClass().getField(super.elementsName.getVersioning());
            field.setAccessible(true);
            return (VersioningType) field.get(entity);
        } catch (NoSuchFieldException e) {
            throw new CpaAnnotationException(
                    "Unable to find the Field annotated with @VersioningType",
                    e.getCause()
            );
        } catch (IllegalAccessException e) {
            throw new CpaAnnotationException(
                    "The Field annotated with @VersioningType has no accessible.",
                    e.getCause()
            );
        } catch (ClassCastException e) {
            throw new CpaAnnotationException(
                    "The Field annotated with @VersioningType needs to have a String VersioningType",
                    e.getCause()
            );
        }
    }

    /**
     * Get a entity from the properties map
     *
     * @param prop       the properties map
     * @param is         in case of document its the content of, other else its null]
     * @param parentID   the id of parent node
     * @param encode     the content encode
     * @param id         the id of entity
     * @param versioning the versioning type ({@link VersioningType})
     * @return T the entity
     * @throws CpaRuntimeException any errors of runtime
     */
    public T getEntity(final Map<String, ?> prop, final InputStream is, final String parentID,
                       final String encode, final String id, VersioningType versioning)
            throws CpaRuntimeException {

        T entity;

        Map<String, String> inverseMap = new HashMap<>();
        super.properties.forEach((key, value) -> inverseMap.put(value, key));

        try {
            entity = super.clazz.newInstance();

            for (Field field : super.clazz.getDeclaredFields()) {
                String key = inverseMap.get(field.getName());

                field.setAccessible(true);

                if (field.isAnnotationPresent(Metadata.class)) {
                    Object obj = prop.get(key);
                    field.set(entity, obj);
                    continue;
                }

                if (field.isAnnotationPresent(Content.class)) {
                    field.set(entity, is);
                    continue;
                }

                if (field.isAnnotationPresent(Versioning.class)) {
                    field.set(encode, versioning);
                    continue;
                }

                if (field.isAnnotationPresent(Encode.class)) {
                    field.set(entity, encode);
                }

                if (field.isAnnotationPresent(Parent.class)) {
                    field.set(entity, parentID);
                }

                if (field.isAnnotationPresent(ID.class)) {
                    field.set(entity, id);
                }
            }
        } catch (InstantiationException e) {
            throw new CpaRuntimeException(
                    "Unable to create a new instance of " + super.clazz.getSimpleName(),
                    e.getCause()
            );
        } catch (IllegalAccessException e) {
            throw new CpaRuntimeException(
                    "Unable to access some " + super.clazz.getSimpleName() + " field",
                    e.getCause()
            );
        }

        return entity;
    }

    public void refreshProperty(T entity, Map<String, ?> properties) throws CpaRuntimeException {

        Map<String, String> inverseMap = new HashMap<>();
        super.properties.forEach((key, value) -> inverseMap.put(value, key));

        try {
            entity = super.clazz.newInstance();

            for (Field field : super.clazz.getDeclaredFields()) {
                String key = inverseMap.get(field.getName());

                field.setAccessible(true);

                if (field.isAnnotationPresent(Metadata.class)) {
                    Object obj = properties.get(key);
                    field.set(entity, obj);
                    continue;
                }
            }
        } catch (InstantiationException e) {
            throw new CpaRuntimeException(
                    "Unable to create a new instance of " + super.clazz.getSimpleName(),
                    e.getCause()
            );
        } catch (IllegalAccessException e) {
            throw new CpaRuntimeException(
                    "Unable to access some " + super.clazz.getSimpleName() + " field",
                    e.getCause()
            );
        }
    }
}
