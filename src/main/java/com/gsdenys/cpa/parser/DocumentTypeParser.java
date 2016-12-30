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
import com.gsdenys.cpa.exception.CpaAnnotationException;

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
public class DocumentTypeParser<T> {

    private T entity;

    private String idName;
    private String parentName;
    private String docTypeName;
    private String contentName;

    private Map<String, String> properties;

    /**
     * Default builder
     *
     * @param entity the entity to be parsed
     */
    public DocumentTypeParser(T entity) throws CpaAnnotationException {
        this.entity = entity;

        this.properties = new HashMap<>();

        this.validate();
    }

    /**
     * Validate object and extract informations
     *
     * @throws CpaAnnotationException error throws when any annotation incompatibility was identified
     */
    private void validate() throws CpaAnnotationException {
        this.docType();
        this.fields();
    }

    /**
     * check by the {@link DocumentType} annotation
     *
     * @throws CpaAnnotationException error throws when any annotation incompatibility was identified
     */
    private void docType() throws CpaAnnotationException {
        if (!this.entity.getClass().isAnnotationPresent(DocumentType.class)) {
            throw new CpaAnnotationException(
                    "The @DocumentType annotation is required for a document type"
            );
        }

        this.docTypeName = this.entity.getClass().getDeclaredAnnotation(DocumentType.class).name();
    }

    /**
     * Parse the entity fields discovering the metadata mapping and the ids
     *
     * @throws CpaAnnotationException error throws when any annotation incompatibility was identified
     */
    private void fields() throws CpaAnnotationException {
        boolean hasID = false;
        boolean hasContent = false;
        boolean hasParent = false;

        for (Field field : this.entity.getClass().getDeclaredFields()) {
            boolean isMetadata = this.checkMetadata(field);
            boolean isId = this.checkID(field, isMetadata, hasID);

            if (isId) {
                hasID = true;
            }

            boolean isParent = this.checkParent(field, isMetadata, isId, hasParent);

            if (isParent) {
                hasParent = true;
            }

            boolean isContent = this.checkContent(field, isMetadata, isId, isParent, hasContent);

            if (isContent) {
                hasContent = true;
            }
        }
    }

    /**
     * Check if the field has a {@link Metadata} annotation, and when have put it in a properties {@link Map}
     *
     * @param field the field that will be check
     * @return boolean has or not metadata annotation
     */
    private boolean checkMetadata(Field field) {

        if (!field.isAnnotationPresent(Metadata.class)) {
            return false;
        }

        this.properties.put(field.getAnnotation(Metadata.class).name(), field.getName());
        return true;

    }


    /**
     * Check the {@link ID} annotation
     *
     * @param field      the field to check
     * @param isMetadata already have {@link Metadata} annotation
     * @param hasID      already hava an {@link ID} annotation at the class
     * @return boolean if the {@link ID} annotation is present
     * @throws CpaAnnotationException error throws when any annotation incompatibility was identified
     */
    private boolean checkID(final Field field, boolean isMetadata, boolean hasID) throws CpaAnnotationException {

        if (!field.isAnnotationPresent(ID.class)) {
            return false;
        }

        if (isMetadata) {
            throw new CpaAnnotationException(
                    "The Annotations @Metadata and @ID can't be applied to the some field."
            );
        }

        if (hasID) {
            throw new CpaAnnotationException(
                    "The Entity can't have more then one @ID annotations mapped."
            );
        }

        this.idName = field.getName();
        return true;

    }

    /**
     * check by the {@link Parent} annotation
     *
     * @param field      the field to be check
     * @param isMetadata already have {@link Metadata} annotation at the field
     * @param isId       already have @{@link ID} at the field
     * @return boolean if have the {@link Parent} annotation at the field
     * @throws CpaAnnotationException error throws when any annotation incompatibility was identified
     */
    private boolean checkParent(Field field, boolean isMetadata, boolean isId, boolean hasParent)
            throws CpaAnnotationException {

        if (!field.isAnnotationPresent(Parent.class)) {
            return false;
        }

        if (isId || isMetadata) {
            throw new CpaAnnotationException(
                    "The Annotation @Parent don't be applied in a field that already have @ID or @Metadata"
            );
        }

        if (hasParent) {
            throw new CpaAnnotationException(
                    "The Entity can't have more then one @Parent annotations mapped."
            );
        }

        this.parentName = field.getName();
        return true;
    }


    /**
     * check by the {@link Content} annotation
     *
     * @param field      the field to be check
     * @param isMetadata already have {@link Metadata} annotation at the field
     * @param isId       already have @{@link ID} annotation at the field
     * @param isParent   already have {@link Parent} annotation at the field
     * @return boolean if have the {@link Parent} annotation at the field
     * @throws CpaAnnotationException error throws when any annotation incompatibility was identified
     */
    private boolean checkContent(Field field, boolean isMetadata, boolean isId, boolean isParent, boolean hasContent)
            throws CpaAnnotationException {

        if (!field.isAnnotationPresent(Content.class)) {
            return false;
        }

        if (isId || isMetadata || isParent) {
            throw new CpaAnnotationException(
                    "The Annotation @Content don't be applied in a field that already have @ID, @Metadata or @Parent"
            );
        }

        if (hasContent) {
            throw new CpaAnnotationException(
                    "The Entity can't have more then one @Content annotations mapped."
            );
        }

        this.contentName = field.getName();
        return true;

    }

    /**
     * Get the name of field that represent the ID
     *
     * @return String the field name
     */
    public String getIdName() {
        return idName;
    }

    /**
     * Get the name of field that represent the Parent
     *
     * @return String the field name
     */
    public String getParentName() {
        return parentName;
    }

    /**
     * Get the Name of Document Type
     *
     * @return String the document Type
     */
    public String getDocTypeName() {
        return docTypeName;
    }

    /**
     * Get the name of field that represents the Content
     *
     * @return String te Content field
     */
    public String getContentName() {
        return contentName;
    }

    /**
     * Get a map containing all metadata field name end their related mapped field
     * <p>
     * e.g (cmis:name, name)
     *
     * @return Map the metadata field map
     */
    public Map<String, String> getProperties() {
        return properties;
    }
}
