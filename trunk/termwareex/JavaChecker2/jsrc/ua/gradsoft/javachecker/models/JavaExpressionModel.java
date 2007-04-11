/*
 * JavaExpressionModel.java
 *
 *
 * Copyright (c) 2006-2007 GradSoft  Ukraine
 *http://www.gradsoft.ua
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.List;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Model of Java Expression
 * @author Ruslan Shevchenko
 */
public interface JavaExpressionModel {
    
    public JavaExpressionKind  getKind();
    
    public List<JavaExpressionModel>  getSubExpressions()  throws TermWareException, EntityNotFoundException;
    
    public JavaTypeModel  getType() throws TermWareException, EntityNotFoundException;
    
    public Term  getModelTerm() throws TermWareException, EntityNotFoundException;
    
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
