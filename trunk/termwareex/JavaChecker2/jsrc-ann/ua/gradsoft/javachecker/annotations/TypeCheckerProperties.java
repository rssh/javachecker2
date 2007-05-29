/*
 * TypeCheckerProperties.java
 *
 *(C) Grad-Soft Ltd, 2007
 *
 */

package ua.gradsoft.javachecker.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *Checker properties, which binded to type.
 * @author rssh
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface TypeCheckerProperties {
       public String[] value() default {};
}
