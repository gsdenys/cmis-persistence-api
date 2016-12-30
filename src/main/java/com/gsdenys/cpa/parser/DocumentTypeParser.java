package com.gsdenys.cpa.parser;

import com.gsdenys.cpa.annotations.DocumentType;
import com.gsdenys.cpa.annotations.ID;
import com.gsdenys.cpa.annotations.Metadata;
import com.gsdenys.cpa.annotations.Parent;
import com.gsdenys.cpa.exception.CpaAnnotationException;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Realize a parser at the entity.
 *
 * @author Denys G. Santos (gsdenys@gmail.com)
 * @version 0.0.1
 * @since 0.0.1
 */
public class DocumentTypeParser<T> {
    MetadataParser<T> metadataParser = new MetadataParser<T>();

    T entity;

    String mthIdName;
    String parentName;
    String docTypeName;

    Map<String, String> properties;

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

    private void validate() throws CpaAnnotationException {
        this.docType();

    }

    private void docType() throws CpaAnnotationException {
        //check by @DocumentType
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
     * @throws CpaAnnotationException error that will be throw case an annotation error was identified
     */
    private void fields() throws CpaAnnotationException {
        boolean hasID = false;

        for (Field field : this.entity.getClass().getDeclaredFields()) {
            boolean isMetadata = this.checkMetadata(field);
            boolean isId = this.checkID(field, isMetadata, hasID);

            if (isId) {
                hasID = true;
            }

            boolean b = this.checkParent(field, isMetadata, isId);
        }
    }

    /**
     * Check if the field has a {@link Metadata} annotation, and when have put it in a properties {@link Map}
     *
     * @param field the field that will be check
     * @return boolean has or not metadata annotation
     */
    private boolean checkMetadata(Field field) {
        if (field.isAnnotationPresent(Metadata.class)) {
            this.properties.put(field.getAnnotation(Metadata.class).name(), field.getName());
            return true;
        }

        return false;
    }


    /**
     * Check the {@link ID} annotation
     *
     * @param field      the field to check
     * @param isMetadata already have {@link Metadata} annotation
     * @param hasID      already hava an {@link ID} annotation at the class
     * @return boolean if the {@link ID} annotation is present
     * @throws CpaAnnotationException
     */
    private boolean checkID(final Field field, boolean isMetadata, boolean hasID) throws CpaAnnotationException {
        if (field.isAnnotationPresent(ID.class)) {
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

            this.mthIdName = field.getName();
            return true;
        }

        return false;
    }

    /**
     * check by the {@link Parent} annotation
     *
     * @param field      the field to be check
     * @param isMetadata already have {@link Metadata} annotation at the field
     * @param isId       already have @{@link ID} at the field
     * @return boolean if have the {@link Parent} annotation at the field
     * @throws CpaAnnotationException
     */
    private boolean checkParent(Field field, boolean isMetadata, boolean isId) throws CpaAnnotationException {
        if (field.isAnnotationPresent(Parent.class)) {
            if (isId || isMetadata) {
                throw new CpaAnnotationException(
                        "The Annotation @Parent don't be applied in a field that already have @ID or @Metadata"
                );
            }

            this.parentName = field.getName();
            return true;
        }
        return false;
    }


    /**
     * get The docType name
     *
     * @return String the docType
     */
    public String getDocTypeName() {
        return this.getDocTypeName();
    }


}
