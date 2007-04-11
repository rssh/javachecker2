/*
 * JavaTermLocalVariableModel.java
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
 *Model of local variable.
 */
public class JavaTermLocalVariableModel implements JavaLocalVariableModel
{
    
    public JavaTermLocalVariableModel(Term identifierTerm,JavaLocalVariableKind kind, 
                                      Term typeTerm, 
                                      Term initOrIterateExpressionTerm,
                                      JavaTermExpressionModel initExpression,
                                      JavaTermStatementModel statement)
    {
      identifierTerm_=identifierTerm;
      kind_=kind;
      typeTerm_=typeTerm;
      initOrIterateExpressionTerm_=initOrIterateExpressionTerm;
      initExpression_=initExpression;
      statement_=statement;
    }
    
    public String getName()
    { return identifierTerm_.getSubtermAt(0).getString(); }
    
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
        Term identifier=identifierTerm_;
        Term initTerm = TermUtils.createNil();
        if (initExpression_!=null) {
            initTerm = initExpression_.getModelTerm();
        }
        JavaPlaceContext ctx = JavaPlaceContextFactory.createNewStatementContext(statement_);
        Term tctx = TermUtils.createJTerm(ctx);
        Term retval = TermUtils.createTerm("LocalVariableModel",typeRef,identifier,initTerm,tctx);
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
    
    private Term identifierTerm_;
    private Term typeTerm_;
    private Term initOrIterateExpressionTerm_;
    private JavaTermExpressionModel initExpression_;
    private JavaLocalVariableKind  kind_;    
    private JavaTermStatementModel statement_;
    
    
}
