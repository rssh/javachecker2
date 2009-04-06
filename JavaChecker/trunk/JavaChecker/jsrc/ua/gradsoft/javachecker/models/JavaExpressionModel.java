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
import ua.gradsoft.javachecker.PartialConditionResult;
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

    /**
     * @return model term
     * @throws ua.gradsoft.termware.TermWareException
     * @throws ua.gradsoft.javachecker.EntityNotFoundException
     */
    public Term  getModelTerm() throws TermWareException, EntityNotFoundException;
    
    /**     
     * @return term, suitable for parsing as part of sourcr
     * @throws ua.gradsoft.termware.TermWareException
     * @throws ua.gradsoft.javachecker.EntityNotFoundException
     */
    public Term  getTerm() throws TermWareException, EntityNotFoundException;


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
    
    public boolean  isConstantExpression() throws TermWareException, EntityNotFoundException;
    
    //public PartialConditionResult forse(JavaTraceContext trace);
      
    //TODO:
    //   think about semantics: this must be full or partial evaluation ?
    //   if full -- may be check at first.
    //public JavaExpressionModel  eval(JavaTraceContext trace);
    
    
       
}
