/*
 * JavaClassMethodModel.java
 *
 * Copyright (c) 2004-2007 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.javachecker.models.expressions.JavaClassObjectConstantExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermNullLiteralExpressionModel;
import ua.gradsoft.javachecker.util.Function;
import ua.gradsoft.javachecker.util.ImmutableMappedList;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Model class, which based on reflection mechanizm
 * @author Ruslan Shevchenko
 */
public class JavaClassMethodModel extends JavaMethodModel implements JavaClassTopLevelBlockOwnerModel
{
    
    /** Creates a new instance of JavaClassMethodModel */
    public JavaClassMethodModel(Method method,JavaClassTypeModel classModel) 
    {
        super(classModel);
        method_=method;        
    }
    
    public String getName()
    {
        return method_.getName();
    }
    
    public JavaClassTypeModel  getClassTypeModel()
    {
        return (JavaClassTypeModel)getTypeModel();
    }
    
    public JavaTermModifiersModel getModifiers()
    {
      return new JavaTermModifiersModel(JavaClassTypeModel.translateModifiers(method_.getModifiers()));       
    }
    
    public List<JavaTypeVariableAbstractModel>  getTypeParameters()
    {
       return JavaClassTopLevelBlockOwnerModelHelper.getTypeParameters(this); 
    }
    
    public JavaTypeModel  getResultType() throws TermWareException
    {
        Type resultType = method_.getGenericReturnType();
        return JavaClassTypeModel.createTypeModel(resultType);
    }
    
    public List<JavaTypeModel>  getFormalParametersTypes() throws TermWareException
    {
      return JavaClassTopLevelBlockOwnerModelHelper.getFormalParametersTypes(this);
    }
    
    public boolean isVarArgs()
    {
      return method_.isVarArgs();   
    }
    
    public boolean isSynthetic()
    { return method_.isSynthetic(); }
    
    public TypeVariable[] getClassTypeParameters()
    { return method_.getTypeParameters(); }
    
    public Type[] getClassFormalParameterTypes()
    { return method_.getGenericParameterTypes(); }
    
    public Type[]  getClassThrowsTypes()
    { return method_.getGenericExceptionTypes(); }
    
            
    public List<JavaFormalParameterModel> getFormalParametersList()throws TermWareException
    {
       return JavaClassTopLevelBlockOwnerModelHelper.getFormalParametersList(this); 
    }
    
    
    public Map<String, JavaFormalParameterModel> getFormalParametersMap() throws TermWareException
    {
        return JavaClassTopLevelBlockOwnerModelHelper.getFormalParametersMap(this);
    }
    
    public List<JavaTypeModel>  getThrowsList() throws TermWareException
    { return JavaClassTopLevelBlockOwnerModelHelper.getThrowsList(this); }
 
    public Map<String, JavaAnnotationInstanceModel> getAnnotationsMap()
    {
      return JavaClassTopLevelBlockOwnerModelHelper.getAnnotationsMap(this);  
    }
    
     /**
      *Class terms does not supports block model, so return false.
      *@return false
      */
     public boolean         isSupportBlockModel()
     { return false; }
    
     
     public boolean         hasDefaultValue()
     { return method_.getDeclaringClass().isAnnotation() && method_.getGenericParameterTypes().length==0; }

     public JavaExpressionModel  getDefaultValue() throws NotSupportedException, TermWareException
     {
        JavaExpressionModel retval=null; 
        Object o = method_.getDefaultValue();
        if (o!=null) {
            retval = new JavaClassObjectConstantExpressionModel(o,getTypeModel());
        }else{
            if (hasDefaultValue()) {
                retval = new JavaTermNullLiteralExpressionModel(TermUtils.createTerm("NullLiteral",new Term[0]),(JavaTermStatementModel)null,getTypeModel());
            }else{
                throw new NotSupportedException();   
            }            
        }
        return retval;
     }
     
    /**
     * ClassMethod(context)
     */
    public Term getModelTerm() throws TermWareException
    {
       JavaPlaceContext ctx=JavaPlaceContextFactory.createNewMethodContext(this);
       Term tctx = TermUtils.createJTerm(ctx);
       return TermUtils.createTerm("ClassMethod",tctx);
    }
     
    public JavaTopLevelBlockModel  getTopLevelBlockModel() throws NotSupportedException
    { throw new NotSupportedException(); }

    public Annotation[]  getDeclaredAnnotations()
    { return method_.getDeclaredAnnotations(); }
    
    public Annotation  getAnnotation(Class annotationClass)
    {
      return method_.getAnnotation(annotationClass);  
    }
    
    public  Annotation[][] getParameterAnnotations()
    { return method_.getParameterAnnotations(); }
    
    
    private Method   method_;
    
    private final Logger LOG = Logger.getLogger(JavaClassMethodModel.class.getName());
}
