/*
 * JavaExpressionModel.java
 *
 * Created on понеділок, 5, лютого 2007, 6:05
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.List;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.termware.TermWareException;

/**
 *Model of Java Expression
 * @author Ruslan Shevchenko
 */
public interface JavaExpressionModel {
    
    public JavaExpressionKind  getKind();
    
    public List<JavaExpressionModel>  getSubExpressions();
    
    public JavaTypeModel  getType() throws TermWareException, EntityNotFoundException;
    
    /**
     * return true, if expression represents type.
     */
    public boolean  isType() throws TermWareException, EntityNotFoundException;
    
    /**
     * statement model, or null if expression is outside statement.
     */
    public JavaStatementModel   getStatementModel();
    
    /**
     * enclosing type of statement.
     */
    public JavaTypeModel  getEnclosedType();
    
}
