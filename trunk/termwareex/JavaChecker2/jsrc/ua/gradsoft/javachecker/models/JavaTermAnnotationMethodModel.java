/*
 * JavaTermAnnotationMethodModel.java
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.lang.annotation.ElementType;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.JUtils;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Annotation element.
 * @author Ruslan Shevchenko
 */
public class JavaTermAnnotationMethodModel extends JavaMethodModel
{
    
    /**
     * create annotation field.
     */
    public JavaTermAnnotationMethodModel(JavaTypeModel typeModel, Term modifiers, Term typeTerm, Term identifierTerm, Term defaultValueTerm) throws TermWareException
    {
      super(typeModel);
      modifiersModel_ = new JavaTermModifiersModel(modifiers,ElementType.FIELD,this);
      typeTerm_=typeTerm;
      identifierTerm_=identifierTerm;
      defaultValueTerm_=defaultValueTerm;      
    }
    
    public String getName()
    {
       
      return identifierTerm_.getSubtermAt(0).getString();  
    }
    
    public JavaTermModifiersModel getModifiers()
    {
      return modifiersModel_;  
    }
      
    public List<JavaTypeVariableAbstractModel>  getTypeParameters() throws TermWareException
    { return Collections.emptyList(); }
    

    public JavaTypeModel  getResultType() throws TermWareException
    {
      if (resolvedReturnType_==null) {
        try {  
          resolvedReturnType_=JavaResolver.resolveTypeToModel(typeTerm_,getTypeModel());
        }catch(EntityNotFoundException ex){
            ex.setFileAndLine(JUtils.getFileAndLine(typeTerm_));    
            throw new InvalidJavaTermException(ex.getMessage(),typeTerm_,ex);
        }
      }
      return resolvedReturnType_;
    }
    
    public List<JavaTypeModel>  getFormalParametersTypes()
    { return Collections.emptyList(); }
    
    public List<JavaFormalParameterModel>  getFormalParametersList()
    { return Collections.emptyList(); }
    
    public Map<String,JavaFormalParameterModel> getFormalParametersMap()
    { return Collections.emptyMap(); }

    public Map<String,JavaAnnotationInstanceModel>  getAnnotationsMap()
    { return modifiersModel_.getAnnotationsMap(); }
    
    public List<JavaTypeModel>  getThrowsList()
    { return Collections.emptyList(); }
    
    public boolean isSupportBlockModel()
    { return false; }
    
    public JavaTopLevelBlockModel getTopLevelBlockModel()
    {  return null; }
 
    public boolean isSynthetic()
    { return false; }
    
    /**
     * AnnotationMethodModel(modifiers,name,type,defaultValue, context)
     */
    public Term getModelTerm() throws TermWareException, EntityNotFoundException
    {
        
       Term modifiersTerm = modifiersModel_.getModelTerm(); 
       Term nameTerm = identifierTerm_;
       JavaTypeModel tm = this.getResultType();
       Term ttm = TermUtils.createJTerm(tm);
       Term typeRef = TermUtils.createTerm("TypeRef",typeTerm_,ttm);
       Term defaultValue = defaultValueTerm_;
       JavaPlaceContext ctx = JavaPlaceContextFactory.createNewTypeContext(getTypeModel());
       Term tctx = TermUtils.createJTerm(ctx);
       Term retval = TermUtils.createTerm("AnnotationMethodModel", modifiersTerm, nameTerm,
                                           typeRef,defaultValue, tctx);
       return retval;
    }
    
    public boolean hasDefaultValue()
    { return !defaultValueTerm_.isNil(); }
    
    public JavaExpressionModel getDefaultValue() throws TermWareException, EntityNotFoundException
    {
       if (!defaultValueTerm_.isNil()) { 
         try {  
           JavaTypeModel owner = getTypeModel();  
           return JavaTermExpressionModel.create(defaultValueTerm_.getSubtermAt(0),null,owner,owner.getDefaultAnnotationInstanceModel(),getName()); 
         }catch(NotSupportedException ex){
             throw new AssertException("owner of annotation field model does not support getDefaultAnnotationInstanceModel",ex);
         }
       }else{
         return null;  
       }
    }
     
    private JavaTermModifiersModel modifiersModel_;
    private Term typeTerm_;
    private JavaTypeModel resolvedReturnType_=null;
    private Term identifierTerm_;
    private Term defaultValueTerm_;    
    
}
