/*
 * JavaTypeArgumentBoundTopLevelBlockOwnerModel.java
 *
 */

package ua.gradsoft.javachecker.models;

import ua.gradsoft.termware.TermWareException;

/**
 *Interface for top level block owner, bound with type variables
 * @author Ruslan Shevchenko
 */
public interface JavaTypeArgumentBoundTopLevelBlockOwnerModel extends JavaTopLevelBlockOwnerModel
{
    
    public JavaTopLevelBlockOwnerModel getOrigin();
    
    public JavaTypeArgumentsSubstitution getSubstitution() throws TermWareException;
    
    public JavaTypeArgumentBoundTypeModel getTypeArgumentBoundTypeModel() throws TermWareException;
    
}
