/*
 * JavaStatementModel.java
 *
 *
 * Copyright (c) 2006-2007 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.List;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Model for Java Statement
 * @author Ruslan Shevchenko
 */
public interface JavaStatementModel {
    
    public JavaStatementKind  getKind();
    
    /**
     *get top level block, in which statement is defined.
     */
    public JavaTopLevelBlockModel  getTopLevelBlockModel();
    
    
    /**
     * get parent statement (if one eists), otherwise - return null.
     *@return parent statement or null if one does not exists.
     *@Nullable
     */
    public JavaStatementModel getParentStatementModel();

    
    
    /**
     *get previous statement.
     *@return previous statement in the same block (if one exists), oherwise - return null.
     *@Nullable
     */
    public JavaStatementModel getPreviousStatementModel();
    
    
    /**
     *get child statements.
     */
    public List<JavaStatementModel> getChildStatements();
    
    /**
     *get local variables, defined in this statement.
     *
     */
    public List<JavaLocalVariableModel> getLocalVariables();
        
    /**
     * local type, defined in this statements.
     */
    public JavaTypeModel getLocalType() throws TermWareException;
    
    
    /**
     * top-level expressions, defined in this statement.
     */
    public List<JavaExpressionModel>  getExpressions();
    
    //*
    // * getExpressions() ? -- will be beabled ifneeded.
    // */
    
    /**
     * Model term
     */
    public Term  getModelTerm() throws TermWareException, EntityNotFoundException;
}
