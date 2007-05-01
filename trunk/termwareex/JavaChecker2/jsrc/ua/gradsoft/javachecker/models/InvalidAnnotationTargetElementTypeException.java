/*
 * InvalidAnnotationTargetElementTypeException.java
 *
 */

package ua.gradsoft.javachecker.models;

import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Throwed, when we try get incorrect target.
 * @author rssh
 */
public class InvalidAnnotationTargetElementTypeException extends AssertException
{
    
    /** Creates a new instance of InvalidAnnotationTargetElementTypeException */
    public InvalidAnnotationTargetElementTypeException() {
        super("Invalid annotation target element type for request");
    }
    
}
