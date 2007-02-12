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
                                      Term typeTerm, Term initOrIterateExpressionTerm,
                                      JavaTermStatementModel statement)
    {
      name_=name;
      kind_=kind;
      typeTerm_=typeTerm;
      initOrIterateExpressionTerm_=initOrIterateExpressionTerm;
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
    private JavaLocalVariableKind  kind_;    
    private JavaTermStatementModel statement_;
    
    
}
