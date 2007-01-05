/*
 * EntittyNotFoundException.java
 *
 * Created on ��������, 23, ������ 2004, 12:07
 */

package ua.gradsoft.javachecker;

/**
 *Throwed when we can't find some entity in model-s findXXX method.
 *  (for example - method with some name)
 * @author  Ruslan Shevchenko
 */
public class EntityNotFoundException extends Exception {
    
   
    
    /**
     * Constructs an instance of <code>EntittyNotFoundException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public EntityNotFoundException(String entityType, String entityName, String msg) {
        super("entity "+entityName +" ("+entityName+") is not found "+msg);
    }
}
