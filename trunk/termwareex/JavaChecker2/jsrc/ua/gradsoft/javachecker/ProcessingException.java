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
    public ProcessingException(String msg, Exception ex) {
        super(msg,ex);
        ex_=ex;
    }
   
    public Exception getInternalException()
    {
        return ex_; 
    }
    
    private Exception ex_=null;
    
}
