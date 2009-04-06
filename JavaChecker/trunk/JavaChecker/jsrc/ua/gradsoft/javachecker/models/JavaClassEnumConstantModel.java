/*
 * JavaClassEnumConstantModel.java
 *
 *
 * Copyright (c) 2006-2009 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.lang.annotation.ElementType;
import java.lang.reflect.Field;
import java.util.Map;
import ua.gradsoft.javachecker.models.expressions.JavaClassObjectConstantExpressionModel;
import ua.gradsoft.javachecker.util.Function;
import ua.gradsoft.javachecker.util.FunctionMap;
import ua.gradsoft.javachecker.util.ImmutableMappedCollection;
import ua.gradsoft.javachecker.util.IntegerOrderList;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Enum constant from class.
 * @author Ruslan Shevchenko
 */
public class JavaClassEnumConstantModel extends JavaEnumConstantModel
{
    
    /**
     * constructor of enum constant.
     *@param o must be a instance of enum constant
     *@param JavaClassTypeModel
     */
    public JavaClassEnumConstantModel(Object instance, JavaClassTypeModel owner)
    {
       owner_=owner;      
       instance_=instance;
    }
    

    public Map<String, JavaAnnotationInstanceModel> getAnnotationsMap() throws TermWareException {
      try{  
        final Field f = owner_.getJavaClass().getDeclaredField(getName());
        return new FunctionMap<String,JavaAnnotationInstanceModel>(
                  new ImmutableMappedCollection<Integer,String>(
                    new IntegerOrderList(f.getDeclaredAnnotations().length),
                    new Function<Integer,String>(){
            public String function(Integer x){            
                return f.getDeclaredAnnotations()[x].annotationType().getName();
            }
        }
                  ),
                  new Function<String,JavaAnnotationInstanceModel>(){
                      public JavaAnnotationInstanceModel function(String key) throws TermWareException
                      {
                          return new JavaClassAnnotationInstanceModel(ElementType.FIELD,f.getAnnotation(JavaClassTypeModel.forName(key)),JavaClassEnumConstantModel.this);
                      }
                  }
                          
                          
                );
      }catch(NoSuchFieldException ex){
          throw new AssertException("Field with name "+getName()+" is not found in class "+owner_.getFullName());
      }
    }
    
    public String getName()
    { return instance_.toString(); }
    
    public JavaTypeModel  getType()
    { return owner_; }
    
    public JavaTypeModel  getOwnerType()
    { return owner_; }
    
    public boolean isSupportInitializerExpression()
    { return true; }
    
    public JavaExpressionModel getInitializerExpression()
    {
        return new JavaClassObjectConstantExpressionModel(instance_,owner_);
    }
    
    /**
     * ClassEnumConstantModel(this)
     */
    public Term getModelTerm() throws TermWareException
    {
        Term tthis=TermUtils.createJTerm(this);
        Term retval=TermUtils.createTerm("ClassEnumConstantModel",tthis);
        return retval;
    }
    
    private Object instance_;
    private JavaClassTypeModel owner_;
        
    
}
