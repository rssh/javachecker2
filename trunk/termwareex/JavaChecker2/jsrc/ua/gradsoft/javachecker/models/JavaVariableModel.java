/*
 * JavaVariableModel.java
 *
 */

package ua.gradsoft.javachecker.models;

import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.termware.TermWareException;

/**
 *Model of java variable, which we can see in code.
 * @author Ruslan Shevchenko
 */
public interface JavaVariableModel {
    
    /**
     *get name of variable.
     */
    public String getName();
    
    public JavaVariableKind getKind();
    
    public JavaTypeModel getTypeModel() throws TermWareException, EntityNotFoundException;
    
    
}
