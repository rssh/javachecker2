/*
 * CheckerDisable.java
 *
 */

package ua.gradsoft.javachecker.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *Disable some checks for annotated type.
 *Value is a comma separated list of checks to disable.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface CheckerDisable {
    
    public String[] value = {};
    
}
