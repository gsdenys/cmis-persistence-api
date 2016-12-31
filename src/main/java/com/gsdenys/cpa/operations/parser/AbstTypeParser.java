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

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract class to create a parser
 *
 * @param <T> element to be parsed
 * @author Denys G. Santos (gsdenys@gmail.com)
 * @version 0.0.1
 * @since 0.0.1
 */
abstract class AbstTypeParser<T> {
    Class<T> clazz;

    BaseType baseType;
    String type;

    Map<String, String> properties;
    ElementsName elementsName;
    FieldChecker checker;


    AbstTypeParser(Class<T> clazz, boolean validate) throws CpaRuntimeException, CpaAnnotationException {
        this.clazz = clazz;
        this.properties = new HashMap<>();
        this.checker = new FieldChecker();
        this.elementsName = new ElementsName();

        if (validate) {
            this.validate();
        }
    }

    AbstTypeParser(Class<T> clazz) throws CpaRuntimeException, CpaAnnotationException {
        this(clazz, true);
    }

    /**
     * Validate object and extract informations
     *
     * @throws CpaAnnotationException error throws when any annotation incompatibility was identified
     * @throws CpaRuntimeException    any erros of runtime
     */
    void validate() throws CpaAnnotationException, CpaRuntimeException {
        this.processTypeAnnotation();
        this.processFieldsAnnotation();
    }

    /**
     * check by the {@link Type} annotation
     *
     * @throws CpaAnnotationException error throws when any annotation incompatibility was identified
     */
    void processTypeAnnotation() throws CpaAnnotationException {
        if (!this.clazz.isAnnotationPresent(Type.class)) {
            throw new CpaAnnotationException(
                    "The @Type annotation is required for a document type"
            );
        }

        Type type = this.clazz.getDeclaredAnnotation(Type.class);
        this.type = type.name();
        this.baseType = type.base();
    }

    /**
     * Parse the clazz processFieldsAnnotation discovering the metadata mapping and the ids
     *
     * @throws CpaAnnotationException error throws when any annotation incompatibility was identified
     * @throws CpaRuntimeException    any errors of runtime
     */
    void processFieldsAnnotation() throws CpaAnnotationException, CpaRuntimeException {

        for (Field field : this.clazz.getDeclaredFields()) {
            FieldChecker localChecker = new FieldChecker();

            this.checkMetadata(field, localChecker);
            this.checkID(field, localChecker);
            this.checkContent(field, localChecker);
            this.checkEncode(field, localChecker);
            this.checkParent(field, localChecker);
            this.checkVersioning(field, localChecker);
        }
    }

    /**
     * Check if the field has a {@link Metadata} annotation, and when have put it in a properties {@link Map}
     *
     * @param field the field that will be check
     * @throws CpaAnnotationException error throws when any annotation incompatibility was identified
     * @throws CpaRuntimeException    any errors of runtime
     */
    void checkMetadata(final Field field, FieldChecker fieldChecker)
            throws CpaAnnotationException, CpaRuntimeException {

        if (!field.isAnnotationPresent(Metadata.class)) {
            fieldChecker.setMetadata(false);
            return;
        }

        if (fieldChecker.multipleChecker()) {
            throw new CpaAnnotationException(
                    "The Annotations @Metadata can't be applied to the some field that " +
                            "others CPA Annotations is already applied."
            );
        }

        String typeName = field.getAnnotation(Metadata.class).name();
        this.checker.setMetadata(true);
        fieldChecker.setMetadata(true);

        if (this.properties.get(typeName) != null) {
            throw new CpaAnnotationException(
                    "The some metadata name can be mapped to more then one field"
            );
        }

        this.properties.put(typeName, field.getName());
    }


    /**
     * Check the {@link ID} annotation
     *
     * @param field        the field to check
     * @param fieldChecker the {@link FieldChecker} for field
     * @throws CpaAnnotationException error throws when any annotation incompatibility was identified
     * @throws CpaRuntimeException    any errors of runtime
     */
    void checkID(final Field field, FieldChecker fieldChecker)
            throws CpaAnnotationException, CpaRuntimeException {

        if (!field.isAnnotationPresent(ID.class)) {
            fieldChecker.setId(false);
            return;
        }

        if (this.checker.isId()) {
            throw new CpaAnnotationException(
                    "The Entity can't have more then one @ID annotations mapped."
            );
        }

        fieldChecker.setId(true);
        this.checker.setId(true);

        if (fieldChecker.multipleChecker()) {
            throw new CpaAnnotationException(
                    "The Annotations @ID can't be applied to the some field that " +
                            "others CPA Annotations is already applied."
            );
        }

        this.elementsName.setId(field.getName());
    }

    /**
     * check by the {@link Parent} annotation
     *
     * @param field        the field to be check
     * @param fieldChecker checker for the field
     * @throws CpaAnnotationException error throws when any annotation incompatibility was identified
     * @throws CpaRuntimeException    any errors of runtime
     */
    void checkParent(Field field, FieldChecker fieldChecker)
            throws CpaAnnotationException, CpaRuntimeException {

        if (!field.isAnnotationPresent(Parent.class)) {
            fieldChecker.setParent(false);
            return;
        }

        if (this.checker.isParent()) {
            throw new CpaAnnotationException(
                    "The Entity can't have more then one @Parent annotations mapped."
            );
        }

        fieldChecker.setParent(true);
        this.checker.setParent(true);

        if (fieldChecker.multipleChecker()) {
            throw new CpaAnnotationException(
                    "The Annotations @Parent can't be applied to the some field that " +
                            "others CPA Annotations is already applied."
            );
        }

        this.elementsName.setParent(field.getName());
    }

    /**
     * check by the {@link Encode} annotation
     *
     * @param field        the field to be check
     * @param fieldChecker checker for the field
     * @throws CpaAnnotationException error throws when any annotation incompatibility was identified
     * @throws CpaRuntimeException    any errors of runtime
     */
    void checkEncode(Field field, FieldChecker fieldChecker)
            throws CpaAnnotationException, CpaRuntimeException {

        if (!field.isAnnotationPresent(Encode.class)) {
            fieldChecker.setEncode(false);
            return;
        }

        if (this.checker.isEncode()) {
            throw new CpaAnnotationException(
                    "The Entity can't have more then one @Encode annotations mapped."
            );
        }

        fieldChecker.setEncode(true);
        this.checker.setEncode(true);

        if (fieldChecker.multipleChecker()) {
            throw new CpaAnnotationException(
                    "The Annotations @Encode can't be applied to the some field that " +
                            "others CPA Annotations is already applied."
            );
        }

        this.elementsName.setEncode(field.getName());
    }

    /**
     * check by the {@link Versioning} annotation
     *
     * @param field        the field to be check
     * @param fieldChecker checker for the field
     * @throws CpaAnnotationException error throws when any annotation incompatibility was identified
     * @throws CpaRuntimeException    any errors of runtime
     */
    void checkVersioning(Field field, FieldChecker fieldChecker)
            throws CpaAnnotationException, CpaRuntimeException {

        if (!field.isAnnotationPresent(Versioning.class)) {
            fieldChecker.setVersioning(false);
            return;
        }

        if (this.checker.isVersioning()) {
            throw new CpaAnnotationException(
                    "The Entity can't have more then one @Versioning annotations mapped."
            );
        }

        fieldChecker.setVersioning(true);
        this.checker.setVersioning(true);

        if (fieldChecker.multipleChecker()) {
            throw new CpaAnnotationException(
                    "The Annotations @Versioning can't be applied to the some field that " +
                            "others CPA Annotations is already applied."
            );
        }

        this.elementsName.setVersioning(field.getName());
    }


    /**
     * check by the {@link Content} annotation
     *
     * @param field        the field to be check
     * @param fieldChecker checker for the field
     * @throws CpaAnnotationException error throws when any annotation incompatibility was identified
     * @throws CpaRuntimeException    any errors of runtime
     */
    void checkContent(Field field, FieldChecker fieldChecker)
            throws CpaAnnotationException, CpaRuntimeException {

        if (!field.isAnnotationPresent(Content.class)) {
            fieldChecker.setContent(false);
            return;
        }

        if (this.checker.isContent()) {
            throw new CpaAnnotationException(
                    "The Entity can't have more then one @Content annotations mapped."
            );
        }

        fieldChecker.setContent(true);
        this.checker.setContent(true);

        if (fieldChecker.multipleChecker()) {
            throw new CpaAnnotationException(
                    "The Annotations @Content can't be applied to the some field that " +
                            "others CPA Annotations is already applied."
            );
        }

        this.elementsName.setContent(field.getName());
    }
}
