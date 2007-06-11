/*
 * ConstructorCheckerProperties.java
 *
 * Created on May 11, 2007, 7:04 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ua.gradsoft.javachecker.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *Checker Properties, set to constructor.
 * @author rssh
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.CONSTRUCTOR)
public @interface ConstructorCheckerProperties {
public String[] value() default {};   
}


