/*
 * InvalidCheckerCommentException.java
 *
 * Created on четвер, 26, лютого 2004, 6:21
 */

package ua.kiev.gradsoft.JavaChecker;

import ua.kiev.gradsoft.TermWare.*;
import ua.kiev.gradsoft.TermWare.exceptions.*;

/**
 *Throwed during unsuccesfull parsing or analyzing of checker comment
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
    
    /**
     * Constructs an instance of <code>InvalidCheckerCommentException</code> with the specified detail message.
     * @param expression - expression which we can't parse.
     * @param msg the detail message.
     */
    public InvalidCheckerCommentException(ITerm expression, String msg) throws TermWareException
    {
        super("["+TermHelper.termToString(expression)+"] - "+msg);
    }
    
}
