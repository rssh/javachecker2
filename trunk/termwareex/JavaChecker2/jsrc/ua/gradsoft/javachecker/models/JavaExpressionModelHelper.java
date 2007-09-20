/*
 * JavaExpressionModelHelper.java
 *
 * Created on August 31, 2007, 8:47 PM
 *
 */

package ua.gradsoft.javachecker.models;

import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.termware.TermWareException;

/**
 *Helper for JavaExpressionModel 
 * @author rssh
 */
public class JavaExpressionModelHelper {
    
    public static boolean subExpressionsAreConstants(JavaExpressionModel e) throws TermWareException, EntityNotFoundException
    {
        for(JavaExpressionModel expr: e.getSubExpressions()) {
            if (!expr.isConstantExpression()) {
                return false;
            }
        }
        return true;
    }
    
}
