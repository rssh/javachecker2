/*
 * ParameterCheckerProperties.java
 *
 */

package ua.gradsoft.javachecker.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *Properties of formal parameter
 * @author rssh
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.PARAMETER)
public @interface ParameterCheckerProperties {
     public String[] value() default {};  
}
