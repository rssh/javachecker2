/*
 * EntittyNotFoundException.java
 *
 * Created on понеділок, 23, лютого 2004, 12:07
 */

package ua.kiev.gradsoft.JavaChecker;

/**
 *
 * @author  Ruslan Shevchenko
 */
public class EntityNotFoundException extends java.lang.Exception {
    
   
    
    /**
     * Constructs an instance of <code>EntittyNotFoundException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public EntityNotFoundException(String entityType, String entityName, String msg) {
        super("entity "+entityName +" ("+entityName+") is not found "+msg);
    }
}
