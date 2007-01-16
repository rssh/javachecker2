/*
 * JavaTermAnnotationTypeModel.java
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.List;
import java.util.Map;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Model of annotation type which holds term.
 * @author Ruslan Shevchenko
 */
public class JavaTermAnnotationTypeModel extends JavaTermTypeAbstractModel
{
            
    public JavaTermAnnotationTypeModel(int modifiers, Term t, JavaPackageModel packageModel) throws TermWareException
    {
      super(modifiers,t,packageModel);
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
    { return JavaModelConstants.TYPEMODEL_EMPTY_LIST; }
    
    
    public Map<String,List<JavaMethodAbstractModel> > getMethodModels() throws NotSupportedException
    {
      throw new NotSupportedException();
    }
    
    public boolean hasTypeParameters()
    { return false; }
    
    
}
