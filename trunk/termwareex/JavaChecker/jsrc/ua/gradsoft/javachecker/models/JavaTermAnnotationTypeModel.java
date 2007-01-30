/*
 * JavaTermAnnotationTypeModel.java
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
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Model of annotation type which holds term.
 *TODO: build body. now body is not build at all.
 * @author Ruslan Shevchenko
 */
public class JavaTermAnnotationTypeModel extends JavaTermTypeAbstractModel
{
            
    public JavaTermAnnotationTypeModel(int modifiers, Term t, JavaPackageModel packageModel) throws TermWareException
    {
      super(modifiers,t,packageModel);
      name_=t.getSubtermAt(0).getSubtermAt(0).getString();
    }
    
    public boolean isClass()
    {
      return false;  
    }

    public boolean isInterface()
    {
      return false;  
    }
    
    
    public boolean isEnum()
    {
      return false;  
    }
    
    public boolean isAnnotationType()
    {
      return true;  
    }
        
    public boolean hasMethodModels()
    { return false; }
    
    public JavaTypeModel getSuperClass() throws TermWareException
    {
      try {  
        return JavaResolver.resolveTypeModelFromPackage("Annotation","java.lang"); 
      }catch(EntityNotFoundException ex){
          throw new AssertException(ex.getMessage());
      }
    }
    
    
    public List<JavaTypeModel>  getSuperInterfaces() 
    { return Collections.emptyList(); }
    
    
    public Map<String, List<JavaMethodModel>> getMethodModels() throws NotSupportedException
    {
      throw new NotSupportedException();
    }

    public Map<String, JavaEnumConstantModel> getEnumConstantModels() throws NotSupportedException {
        throw new NotSupportedException();
    }

    /**
     * AnnotationTypeDeclarationModel(name,[...],context)
     *where AnnotationTypeBodyModel is list,
     */
    public Term getModelTerm() throws TermWareException
    {
        Term nameTerm=t_.getSubtermAt(0);
        Term typeBodyModel=getMemberModelsList();
        JavaPlaceContext context = JavaPlaceContextFactory.createNewTypeContext(this);
        Term tcontext = TermUtils.createJTerm(context);
        return TermUtils.createTerm("AnnotationTypeModel",nameTerm,typeBodyModel,tcontext);
    }
    
    
    
    public boolean hasTypeParameters()
    { return false; }

    
    
}
