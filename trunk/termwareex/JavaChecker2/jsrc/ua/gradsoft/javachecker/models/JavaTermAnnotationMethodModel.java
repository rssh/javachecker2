/*
 * JavaTermAnnotationMethodModel.java
 *
 * Created on понеділок, 5, лютого 2007, 3:03
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Annotation element.
 * @author Ruslan Shevchenko
 */
public class JavaTermAnnotationMethodModel extends JavaMethodModel
{
    
    /**
     * Creates a new instance of JavaMethodModel
     */
    public JavaTermAnnotationMethodModel(JavaTypeModel typeModel, int modifiers, Term typeTerm, Term identifierTerm, Term defaultValueTerm) 
    {
      super(typeModel);
      modifiersModel_ = new JavaModifiersModel(modifiers);
      typeTerm_=typeTerm;
      identifierTerm_=identifierTerm;
      defaultValueTerm_=defaultValueTerm;      
    }
    
    public String getName()
    {
      return identifierTerm_.getSubtermAt(0).getString();  
    }
    
    public JavaModifiersModel getModifiers()
    {
      return modifiersModel_;  
    }
    
    public List<JavaTypeVariableAbstractModel>  getTypeParameters() 
    {
      return Collections.emptyList();  
    }
 
    public JavaTypeModel  getResultType() throws TermWareException
    {
      if (resolvedType_==null) {
        try {  
          resolvedType_=JavaResolver.resolveTypeToModel(typeTerm_,getTypeModel());
        }catch(EntityNotFoundException ex){
            resolvedType_=JavaUnknownTypeModel.INSTANCE;
        }
      }
      return resolvedType_;
    }
    
    public List<JavaTypeModel> getFormalParametersTypes() throws TermWareException
    {
      return Collections.emptyList();  
    }
    
    public List<JavaFormalParameterModel> getFormalParametersList()
    {
      return Collections.emptyList();  
    }
    
    public Map<String,JavaFormalParameterModel>  getFormalParametersMap() throws TermWareException
    {
      return Collections.emptyMap();  
    }                
    
    
    

    public boolean isSupportBlockModel() {
        return false;
    }
    
    public JavaTopLevelBlockModel getTopLevelBlockModel() throws NotSupportedException {
        throw new NotSupportedException();
    }

    
    
    /**
     * AnnotationMethodModel(modifiers,name,type,defaultValue, context)
     */
    public Term getModelTerm() throws TermWareException
    {
        
       Term modifiersTerm = TermUtils.createTerm("Modifiers",TermUtils.createInt(modifiersModel_.getIntValue()));
       Term nameTerm = identifierTerm_;
       JavaTypeModel tm = getTypeModel();
       Term ttm = TermUtils.createJTerm(tm);
       Term typeTerm = TermUtils.createTerm("TypeRef",typeTerm_,ttm);
       Term defaultValue = defaultValueTerm_;
       JavaPlaceContext ctx = JavaPlaceContextFactory.createNewMethodContext(this);
       Term tctx = TermUtils.createJTerm(ctx);
       Term retval = TermUtils.createTerm("AnnotationTypeModel", modifiersTerm, nameTerm,
                                           typeTerm,defaultValue, tctx);
       return retval;
    }
    
    
    private JavaModifiersModel modifiersModel_;
    private Term typeTerm_;
    private JavaTypeModel resolvedType_=null;
    private Term identifierTerm_;
    private Term defaultValueTerm_;    
    
}
