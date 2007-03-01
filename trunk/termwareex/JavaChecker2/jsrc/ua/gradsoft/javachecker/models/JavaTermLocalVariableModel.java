/*
 * JavaTermLocalVariableModel.java
 *
 * Created on субота, 13, січня 2007, 18:43
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Model of local variable.
 */
public class JavaTermLocalVariableModel implements JavaLocalVariableModel
{
    
    public JavaTermLocalVariableModel(String name,JavaLocalVariableKind kind, 
                                      Term typeTerm, 
                                      Term initOrIterateExpressionTerm,
                                      JavaTermExpressionModel initExpression,
                                      JavaTermStatementModel statement)
    {
      name_=name;
      kind_=kind;
      typeTerm_=typeTerm;
      initOrIterateExpressionTerm_=initOrIterateExpressionTerm;
      initExpression_=initExpression;
      statement_=statement;
    }
    
    public String getName()
    { return name_; }
    
    public JavaVariableKind getKind()
    { return JavaVariableKind.LOCAL_VARIABLE; }
    
    /**
     * get type model.
     *(if type can't be resolved - return JavaUnknonwTypeModel.INSTANCE)
     */
    public JavaTypeModel getTypeModel() throws TermWareException
    { return resolveType(); }
    
    public JavaStatementModel getStatement()
    { return statement_; }
    
    public boolean isForHead()
    { return statement_.getKind()==JavaStatementKind.FOR_STATEMENT; }
    
    public JavaExpressionModel  getInitExpressionModel()
    { return initExpression_; }
            
    /**
     * LocalVariableModel(TyeRef(),Identifier,Init)
     */
    public Term getModelTerm() throws TermWareException, EntityNotFoundException
    {
        JavaTypeModel type=resolveType();
        Term typeRef=TermUtils.createTerm("TypeRef",typeTerm_,TermUtils.createJTerm(type));
        Term identifier=TermUtils.createIdentifier(name_);
        Term initTerm = TermUtils.createNil();
        if (initExpression_!=null) {
            initTerm = initExpression_.getModelTerm();
        }
        Term retval = TermUtils.createTerm("LocalVariableModel",typeRef,identifier,initTerm);
        return retval;
    }
    

    public Term getTypeTerm()
    { return typeTerm_; }
    
    public Term getInitOrIterateTerm()
    { return initOrIterateExpressionTerm_; }
    
    private JavaTypeModel resolveType() throws TermWareException
    {
      try {  
        return JavaResolver.resolveTypeToModel(typeTerm_,statement_);
      }catch(EntityNotFoundException ex){
          return JavaUnknownTypeModel.INSTANCE;
      }
    }
    
    private String name_;
    private Term typeTerm_;
    private Term initOrIterateExpressionTerm_;
    private JavaTermExpressionModel initExpression_;
    private JavaLocalVariableKind  kind_;    
    private JavaTermStatementModel statement_;
    
    
}
