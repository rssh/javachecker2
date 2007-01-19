/*
 * JavaLocalVariableModel.java
 *
 * Created on �������, 9, ���� 2007, 0:06
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import ua.gradsoft.termware.TermWareException;

/**
 *Model for java local variable, defined in block.
 * @author Ruslan Shevchenko
 */
public interface JavaLocalVariableModel extends JavaVariableModel
{

    public String getName();
    
    public JavaTypeModel  getTypeModel() throws TermWareException;
        
    public JavaStatementModel getStatement();
    
    public boolean isForHead();
}
