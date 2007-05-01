/*
 * JavaLocalVariableModel.java
 *
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Model for java local variable, defined in block.
 * @author Ruslan Shevchenko
 */
public interface JavaLocalVariableModel extends JavaVariableModel
{

    public String getName();
    
    public JavaTypeModel  getTypeModel() throws TermWareException, EntityNotFoundException;
        
    public JavaStatementModel getStatement();
    
    public Term getModelTerm() throws TermWareException, EntityNotFoundException;
    
    /**
     *return InitExpressionModel or null if one is empty.
     */
    public JavaExpressionModel  getInitExpressionModel();
    
    public boolean isForHead();
}
