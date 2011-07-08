/*
 * JavaObjectConstantExpressionModel.java
 *
 * Created on April 25, 2007, 4:47 PM
 *
 */

package ua.gradsoft.javachecker.models.expressions;

import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.models.JavaExpressionModel;
import ua.gradsoft.termware.TermWareException;

/**
 *Expression for object constant. 
 * @author rssh
 */
public interface JavaObjectConstantExpressionModel extends JavaExpressionModel
{
        
    /**
     *@return value of expression as Java object constant.
     */
    public  Object getConstant() throws TermWareException, EntityNotFoundException;
    
}
