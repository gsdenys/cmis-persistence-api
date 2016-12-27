package com.gsdenys.cpa.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A secondary Document Type annotation definition
 *
 * All class annotated with this will be considered a secondary document type definition.
 *
 * @author Denys G. Santos (gsdenys@gmail.com)
 * @version 0.0.1
 * @since 0.0.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SecondaryType {
    /**
     * The name of document type. e.g <code>my:secondarytype</code>
     *
     * @return String name of secondary document type
     */
    String name();
}
