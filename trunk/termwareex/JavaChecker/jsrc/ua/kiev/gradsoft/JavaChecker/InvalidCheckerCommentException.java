/*
 * InvalidCheckerCommentException.java
 *
 * Created on ������, 26, ������ 2004, 6:21
 */

package ua.kiev.gradsoft.JavaChecker;

/**
 *
 * @author  Ruslan Shevchenko
 */
public class InvalidCheckerCommentException extends Exception 
{
        
    /**
     * Constructs an instance of <code>InvalidCheckerCommentException</code> with the specified detail message.
     * @param expression - expression which we can't parse.
     * @param msg the detail message.
     */
    public InvalidCheckerCommentException(String expression, String msg) {
        super("["+expression+"] - "+msg);
    }
}
