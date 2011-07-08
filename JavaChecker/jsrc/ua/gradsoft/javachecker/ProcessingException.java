/*
 * ProcessingException.java
 *
 */

package ua.gradsoft.javachecker;

/**
 *Some exception, which occured during processing, in such way,
 *that we must report this to top-level.
 * @author  Ruslan Shevchenko
 */
public class ProcessingException extends Exception {
    
     
    /**
     * Constructs an instance of <code>ProcessingException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ProcessingException(String msg) {
        super(msg);
    }
    
    /**
     * Constructs an instance of <code>ProcessingException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ProcessingException(String msg, Throwable ex) {
        super(msg,ex);     
    }
   
    public Throwable getInternalException()
    {
        return getCause();
    }
    
    
}
