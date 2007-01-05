/*
 * JavaMemberVariableAbstractModel.java
 *
 * Copyright (c) 2004-2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import ua.gradsoft.termware.TermWareException;

/**
 *Abstract model for member variable.
 * @author Ruslan Shevchenko
 */
public abstract class JavaMemberVariableAbstractModel {

    
    public abstract JavaModifiersModel getModifiersModel();
    
    public abstract String getName();        
    
    public abstract JavaTypeModel getTypeModel() throws TermWareException;
    
    public abstract JavaTypeModel getOwner();
    
    public abstract boolean canCheck();
    
    public abstract boolean check() throws TermWareException;
    
}
