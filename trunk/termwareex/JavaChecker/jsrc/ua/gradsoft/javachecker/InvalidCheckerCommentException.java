/*
 * InvalidCheckerCommentException.java
 *
 * Created on четвер, 26, лютого 2004, 6:21
 */

package ua.gradsoft.javachecker;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;


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
    public InvalidCheckerCommentException(Term expression, String msg) throws TermWareException
    {
        super("["+TermHelper.termToString(expression)+"] - "+msg);
    }
    
}
