/*
 * JavaStatementModel.java
 *
 * Created on вівторок, 9, січня 2007, 20:08
 *
 * Copyright (c) 2006-2007 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.List;

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
     *get list of local types, defined in this statements.
     */
    public JavaTypeModel getLocalType();
    
}
