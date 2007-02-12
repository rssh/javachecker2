/*
 * NotSupportedException.java
 *
 * Created on �'������, 27, ������ 2004, 12:45
 */

package ua.gradsoft.javachecker;

/**
 *This exception is throwed, when we call methods of some model,
 *which is not supported by implementation.
 *  (example - get method body term from method model, generated by
 *   compiled class).
 * @author  Ruslan Shevchenko
 */
public class NotSupportedException extends Exception {
    
    /**
     * Creates a new instance of <code>NotSupportedException</code> without detail message.
     */
    public NotSupportedException() {
    }
    
    
    /**
     * Constructs an instance of <code>NotSupportedException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public NotSupportedException(String msg) {
        super(msg);
    }
}
