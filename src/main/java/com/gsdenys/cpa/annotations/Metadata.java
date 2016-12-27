package com.gsdenys.cpa.annotations;

import com.gsdenys.cpa.exception.CpaAnnotationException;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A Metadata annotation definition
 *
 * All field annotated with this will be considered a metadata. Note that this just be util if its was used
 * inside a {@link DocumentType} or {@link SecondaryType} annotated class.
 *
 * This annotation can be used in a single field or a {@link java.util.Collection} (any {@link java.util.Collection}
 * extensions is also valid) of fields. Any other  use of this annotation will return
 * a {@link CpaAnnotationException}
 *
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
     * The name of metadata. by definition in the majority of content management software its use a
     * patter like <code>cmis:title</code>
     *
     * @return string metadata name
     */
    String name();

    /**
     * Define if the metadata field is mandatory or not. By default it's <code>false</code>
     *
     * Once this attribute was applied to a field and this field value is null, the para parser will return
     * a {@link CpaAnnotationException}
     *
     * @return boolean mandatory value
     */
    boolean mandatory() default false;
}