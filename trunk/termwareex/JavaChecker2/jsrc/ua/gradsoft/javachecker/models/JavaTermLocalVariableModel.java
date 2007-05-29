/*
 * JavaTermLocalVariableModel.java
 *
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.lang.annotation.ElementType;
import java.util.Map;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.JUtils;
import ua.gradsoft.javachecker.attributes.AttributedEntity;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Model of local variable.
 */
public class JavaTermLocalVariableModel implements JavaLocalVariableModel
{
    
    public JavaTermLocalVariableModel(Term identifierTerm,JavaLocalVariableKind kind, 
                                      Term modifiersTerm, Term typeTerm, 
                                      Term initOrIterateExpressionTerm,
                                      JavaTermExpressionModel initExpression,
                                      JavaTermStatementModel statement) throws TermWareException
    {
      identifierTerm_=identifierTerm;
      kind_=kind;
      typeTerm_=typeTerm;
      initOrIterateExpressionTerm_=initOrIterateExpressionTerm;
      initExpression_=initExpression;
      statement_=statement;
      resolvedType_=null;
      modifiers_=new JavaTermModifiersModel(modifiersTerm,ElementType.LOCAL_VARIABLE,this);
    }
    
    public String getName()
    { return identifierTerm_.getSubtermAt(0).getString(); }
    
    public JavaVariableKind getKind()
    { return JavaVariableKind.LOCAL_VARIABLE; }
    
    /**
     * get type model.  
     */
    public JavaTypeModel getTypeModel() throws TermWareException, EntityNotFoundException
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
    
    public Map<String,JavaAnnotationInstanceModel> getAnnotationsMap()
    { return modifiers_.getAnnotationsMap(); }
    
    public JavaModifiersModel getModifiersModel()
    { return modifiers_; }
    
    private JavaTypeModel resolveType() throws TermWareException, EntityNotFoundException
    {
      if (resolvedType_==null) {  
        try {  
          resolvedType_ = JavaResolver.resolveTypeToModel(typeTerm_,statement_);
        }catch(EntityNotFoundException ex){          
          ex.setFileAndLine(JUtils.getFileAndLine(identifierTerm_));
          throw ex;
        }
      }
      return resolvedType_;
    }
    
   
    private AttributedEntity getLocalVariableAttributesRoot() throws TermWareException
    {
        return statement_.getTopLevelBlockModel().getOwnerModel().getChildAttributes("*LocalVariables");
    }
    
    public Term getAttribute(String name) throws TermWareException
    {
       return getLocalVariableAttributesRoot().getAttribute(name);
    }
    
    public void setAttribute(String name, Term value) throws TermWareException
    {
       getLocalVariableAttributesRoot().setAttribute(name,value);
    }
    
    public AttributedEntity getChildAttributes(String childName)
    { return null; }

    
    private Term identifierTerm_;
    private Term typeTerm_;
    private Term modifiersTerm_;
    private Term initOrIterateExpressionTerm_;
    private JavaTermExpressionModel initExpression_;
    private JavaLocalVariableKind  kind_;    
    private JavaTermStatementModel statement_;

    private JavaTypeModel resolvedType_;
    private JavaTermModifiersModel modifiers_;    
    
}
