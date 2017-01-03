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

import com.github.gsdenys.cpa.exception.CpaAnnotationException;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A Metadata annotation definition
 * <p>
 * All field annotated with this will be considered a metadata. Note that this just be util if its was used
 * inside a {@link Entity} or {@link Aspect} annotated class.
 * <p>
 * This annotation can be used in a single field or a {@link java.util.Collection} (any {@link java.util.Collection}
 * extensions is also valid) of fields. Any other  use of this annotation will return
 * a {@link CpaAnnotationException}
 * <p>
 * Case this annotation was applied to a {@link java.util.Collection} it means that the element was defined at content
 * management as a metadata multiple (that can receive multiple of values like a list).
 *
 * @author Denys G. Santos (gsdenys@gmail.com)
 * @version 0.0.1
 * @since 0.0.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Metadata {

    /**
     * The name of metadata. by definition, in the majority of content management software, its use a
     * pattern like <code>cmis:title</code>
     *
     * @return string metadata name
     */
    String name();

    /**
     * Define if the metadata field is mandatory or not. By default it's <code>false</code>
     * <p>
     * Once this attribute was applied to a field and this field value is null, the para parser will return
     * a {@link CpaAnnotationException}
     *
     * @return boolean mandatory value
     */
    boolean mandatory() default false;
}