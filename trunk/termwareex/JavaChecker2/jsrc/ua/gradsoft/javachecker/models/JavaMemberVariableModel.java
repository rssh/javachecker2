/*
 * JavaMemberVariableModel.java
 *
 * Copyright (c) 2004-2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Abstract model for member variable.
 * @author Ruslan Shevchenko
 */
public abstract class JavaMemberVariableModel implements JavaVariableModel
{
    
    public abstract JavaModifiersModel getModifiersModel();

    /**
     *@return name of member variable.
     */
    public abstract String getName(); 
    
    /**
     *@return JavaVariableKind.MEMBER_VARIABLE
     */
    public JavaVariableKind  getKind()
    {
        return JavaVariableKind.MEMBER_VARIABLE;
    }        
    
    public abstract JavaTypeModel getTypeModel() throws TermWareException, EntityNotFoundException;
    
    public abstract JavaTypeModel getOwner();
        
    public abstract Term getModelTerm() throws TermWareException, EntityNotFoundException;
    
}
