/*
 * JavaMemberVariableModel.java
 *
 * Copyright (c) 2004-2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.attributes.AttributedEntity;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Abstract model for member variable.
 * @author Ruslan Shevchenko
 */
public abstract class JavaMemberVariableModel implements JavaVariableModel
{
    
    public abstract JavaModifiersModel getModifiers();

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
    
    public Term  getAttribute(String name) throws TermWareException
    {
        return getOwnerType().getAttributes().getFieldAttribute(getName(),name);
    }
    
    public void  setAttribute(String name,Term value) throws TermWareException
    {
        getOwnerType().getAttributes().setFieldAttribute(getName(),name,value);
    }
    
    public AttributedEntity  getChildAttributes(String childName)
    {
        return null;
    }
    
    public abstract JavaTypeModel getType() throws TermWareException, EntityNotFoundException;
    
    public abstract JavaTypeModel getOwnerType();
    
    public JavaTopLevelBlockOwnerModel getTopLevelBlockOwner() { return null; }
    
    public abstract boolean  isSupportInitializerExpression();
    
    public abstract JavaExpressionModel  getInitializerExpression() throws TermWareException, EntityNotFoundException;
    
    public abstract Term getModelTerm() throws TermWareException, EntityNotFoundException;
    
}
