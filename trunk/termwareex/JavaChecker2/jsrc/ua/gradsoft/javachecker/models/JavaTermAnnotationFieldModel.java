/*
 * JavaTermAnnotationFieldModel.java
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.lang.annotation.ElementType;
import java.util.Map;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Annotation element.
 * @author Ruslan Shevchenko
 */
public class JavaTermAnnotationFieldModel extends JavaMemberVariableModel
{
    
    /**
     * Creates a new instance of JavaMethodModel
     */
    public JavaTermAnnotationFieldModel(JavaTypeModel typeModel, Term modifiers, Term typeTerm, Term identifierTerm, Term defaultValueTerm) throws TermWareException
    {
      owner_=typeModel;
      modifiersModel_ = new JavaTermModifiersModel(modifiers,ElementType.FIELD,this);
      typeTerm_=typeTerm;
      identifierTerm_=identifierTerm;
      defaultValueTerm_=defaultValueTerm;      
    }
    
    public String getName()
    {
       
      return identifierTerm_.getSubtermAt(0).getString();  
    }
    
    public JavaTermModifiersModel getModifiersModel()
    {
      return modifiersModel_;  
    }
    
    public JavaTypeModel  getOwner()
    {
        return owner_;
    }

    public JavaTypeModel  getTypeModel() throws TermWareException
    {
      if (resolvedType_==null) {
        try {  
          resolvedType_=JavaResolver.resolveTypeToModel(typeTerm_,owner_);
        }catch(EntityNotFoundException ex){
            resolvedType_=JavaUnknownTypeModel.INSTANCE;
        }
      }
      return resolvedType_;
    }
    

    public Map<String,JavaAnnotationInstanceModel>  getAnnotationsMap()
    { return modifiersModel_.getAnnotationsMap(); }
    
    
    /**
     * AnnotationFieldModel(modifiers,name,type,defaultValue, context)
     */
    public Term getModelTerm() throws TermWareException
    {
        
       Term modifiersTerm = TermUtils.createTerm("Modifiers",TermUtils.createInt(modifiersModel_.getIntValue()));
       Term nameTerm = identifierTerm_;
       JavaTypeModel tm = getTypeModel();
       Term ttm = TermUtils.createJTerm(tm);
       Term typeTerm = TermUtils.createTerm("TypeRef",typeTerm_,ttm);
       Term defaultValue = defaultValueTerm_;
       JavaPlaceContext ctx = JavaPlaceContextFactory.createNewTypeContext(getOwner());
       Term tctx = TermUtils.createJTerm(ctx);
       Term retval = TermUtils.createTerm("AnnotationFieldModel", modifiersTerm, nameTerm,
                                           typeTerm,defaultValue, tctx);
       return retval;
    }
    
    public boolean isSupportInitializerExpression()
    { return !defaultValueTerm_.isNil(); }
    
    public JavaExpressionModel getInitializerExpression() throws TermWareException
    {
       if (!defaultValueTerm_.isNil()) { 
         return JavaTermExpressionModel.create(defaultValueTerm_.getSubtermAt(0),null,owner_); 
       }else{
         return null;  
       }
    }
    
    private JavaTypeModel   owner_;
    private JavaTermModifiersModel modifiersModel_;
    private Term typeTerm_;
    private JavaTypeModel resolvedType_=null;
    private Term identifierTerm_;
    private Term defaultValueTerm_;    
    
}
