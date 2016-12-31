package com.gsdenys.cpa.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Document type annotation definition
 *
 * All class annotated with this will be considered a document type definition.
 *
 * @author Denys G. Santos (gsdenys@gmail.com)
 * @version 0.0.1
 * @since 0.0.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Type {

    /**
     * The name of document type. e.g <code>cmis:document</code>
     *
     * @return String name of document type
     */
    String name();

    BaseType base() default BaseType.DOCUMENT;

    VersioningType versioning() default VersioningType.MAJOR;

    String encode() default "UTF-8";
}
