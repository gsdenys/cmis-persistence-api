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
package com.github.gsdenys.cpa.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Document type annotation definition
 * <p>
 * All class annotated with this will be considered a document type definition.
 *
 * @author Denys G. Santos (gsdenys@gmail.com)
 * @version 0.0.1
 * @since 0.0.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Entity {

    /**
     * The name of document type. e.g <code>cmis:document</code>
     * <p>
     * <b>this parameter as required</b>
     *
     * @return String name of document type
     */
    String name();

    /**
     * The document base type. By default this parameter is set as {@link BaseType#DOCUMENT}
     *
     * @return BaseType the document baseType.
     */
    BaseType base() default BaseType.DOCUMENT;

    /**
     * The versioning strategy. By default this parameter is {@link VersioningType#NONE}
     *
     * @return VersioningType the type of versioning used
     */
    VersioningType versioning() default VersioningType.NONE;

    /**
     * The default encode of document. By default this encode is <b>UTF-8</b>
     *
     * @return String the document encode
     */
    String encode() default "UTF-8";
}
