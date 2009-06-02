/*
 * JavaTermEnumConstantModel.java
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Model of enum constant
 * @author Ruslan Shevchenko
 */
public class JavaTermEnumConstantModel extends JavaEnumConstantModel
{
    
    JavaTermEnumConstantModel(Term enumConstantTerm, JavaTermEnumModel owner) throws TermWareException
    {      
      owner_=owner;
      identifierTerm_ = enumConstantTerm.getSubtermAt(IDENTIFIER_TERM_INDEX);
      name_ = identifierTerm_.getSubtermAt(0).getString();
      if (enumConstantTerm.getArity()>=ARGUMENTS_TERM_INDEX+1) {
          argumentsTerm_ = enumConstantTerm.getSubtermAt(ARGUMENTS_TERM_INDEX);
      }else{
          argumentsTerm_ = TermUtils.createNil();
      }
      //System.err.println("enum constant "+name_+", arity="+enumConstantTerm.getArity());  
      if (enumConstantTerm.getArity()>=CLASSORINTERFACE_BODY_TERM_INDEX+1) {
          subtype_=new JavaTermEnumAnonimousTypeModel(name_,enumConstantTerm.getSubtermAt(CLASSORINTERFACE_BODY_TERM_INDEX),owner); 
      }
    }
    
    public String getName()
    {
      return name_;  
    }
    
    
    public JavaTypeModel getType()
    {
      return (subtype_==null) ? owner_ : subtype_;  
    }
    
    public JavaTypeModel getOwnerType()
    { return owner_; }

    public List<JavaTermExpressionModel>  getArgumentModels() throws TermWareException
    {          
      if (arguments_==null) {
          Term at = argumentsTerm_;
          if (!at.isNil()) {
              at = at.getSubtermAt(0);
          }
            arguments_=new LinkedList<JavaTermExpressionModel>();
            while(!at.isNil()) {
              Term argterm = at.getSubtermAt(0);
              at = at.getSubtermAt(1);
              JavaTermExpressionModel expr = JavaTermExpressionModel.create(argterm,null,owner_);
              arguments_.add(expr);
            }
      }  
      
      return arguments_;
    }
    
    public Map<String,JavaAnnotationInstanceModel> getAnnotationsMap()
    { return Collections.emptyMap(); }
    
    /**
     * EnumConstantModel(identifierTerm,arguments,subtype)
     */
    public Term getModelTerm() throws TermWareException, EntityNotFoundException
    {
       Term argumentsModelTerm = TermUtils.createNil();
       for(JavaTermExpressionModel expr: getArgumentModels()) {
           Term argm = expr.getModelTerm();
           argumentsModelTerm = TermUtils.createTerm("cons",argm,argumentsModelTerm);
       }
       argumentsModelTerm = TermUtils.reverseListTerm(argumentsModelTerm);
       Term subtypeModelTerm = (subtype_==null) ? TermUtils.createNil() : subtype_.getModelTerm();
       
       return TermUtils.createTerm("EnumConstantModel",identifierTerm_,argumentsModelTerm,subtypeModelTerm);
    }
        
    public Term getIdentifierTerm()
    { return identifierTerm_; }
    
    public boolean isSupportInitializerExpression()
    { return false;}
    
    public JavaExpressionModel getInitializerExpression()
    { return null; }

    public boolean isConstant()
    { return true; }
    
    private String               name_;
    private Term                 identifierTerm_;
    private Term                 argumentsTerm_;
    private List<JavaTermExpressionModel>  arguments_=null;
    private JavaTermEnumModel    owner_;
    private JavaTermEnumAnonimousTypeModel subtype_=null;
    
    public static final int IDENTIFIER_TERM_INDEX=0;
    public static final int ARGUMENTS_TERM_INDEX=1;
    public static final int CLASSORINTERFACE_BODY_TERM_INDEX=2;
}
