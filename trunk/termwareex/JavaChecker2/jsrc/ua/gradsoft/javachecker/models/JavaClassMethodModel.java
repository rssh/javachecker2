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
        List<JavaTypeVariableAbstractModel> retval = new LinkedList<JavaTypeVariableAbstractModel>();
        TypeVariable<?> typeVariables[] = method_.getTypeParameters();
        for(int i=0; i<typeVariables.length; ++i) {
            retval.add(new JavaClassTypeVariableModel(typeVariables[i]));
        }
        return retval;
    }
    
    public JavaTypeModel  getResultType() throws TermWareException
    {
        Type resultType = method_.getGenericReturnType();
        return JavaClassTypeModel.createTypeModel(resultType);
    }
    
    public List<JavaTypeModel>  getFormalParametersTypes() throws TermWareException
    {
      return new ImmutableMappedList<JavaFormalParameterModel,JavaTypeModel>(
              getFormalParametersList(),
              new Function<JavaFormalParameterModel,JavaTypeModel>() {
          public JavaTypeModel function(JavaFormalParameterModel x) throws TermWareException {              
              try{
                return x.getType();              
              }catch(EntityNotFoundException ex){
                  throw new AssertException(ex.getMessage(),ex);
              }
          }
      }
              );
    }
    
    public boolean isVarArgs()
    {
      return method_.isVarArgs();   
    }
    
    public boolean isSynthetic()
    { return method_.isSynthetic(); }
    
    public Type[] getClassFormalParameterTypes()
    { return method_.getGenericParameterTypes(); }
    
            
    public List<JavaFormalParameterModel> getFormalParametersList()throws TermWareException
    {
        boolean debug=false;
//        if (method_.getName().equals("createTerm")) {
//            debug=true;
//        }
        if (debug) {
            LOG.info("getFormalParametersList for "+method_.getName());
        }
        Type[] parameterTypes=method_.getGenericParameterTypes();
        List<JavaFormalParameterModel> retval = new ArrayList<JavaFormalParameterModel>(parameterTypes.length);
        for(int i=0; i<parameterTypes.length; ++i) {
            String name="fp"+i;
            JavaTypeModel c = JavaClassTypeModel.createTypeModel(parameterTypes[i]);
            int parameterModifiers=0;
            if (i==parameterTypes.length-1) {
                if (method_.isVarArgs()) {                    
                    parameterModifiers |= JavaTermModifiersModel.VARARGS;
                  // we does not need create array type here -- it is as array in parameterTypes. 
                  //  c=new JavaArrayTypeModel(c,null);
                    if (debug) {
                        LOG.info("varargs for "+i+", nowc="+c.getName());
                    }
                }else{
                    if (debug) {
                        LOG.info("not varargs, c="+c.getName());
                    }
                }
            }
            retval.add(new JavaClassFormalParameterModel(name,new JavaClassModifiersModel(parameterModifiers),c,this,i));
        }
        return retval;
    }
    
    
    public Map<String, JavaFormalParameterModel> getFormalParametersMap() throws TermWareException
    {
        boolean debug=false;
   //     if (method_.getName().equals("createTerm")) {
   //         debug=true;
   //     }
        if (debug) {
            LOG.info("getFormalParametersList for "+method_.getName());
        }        
       Map<String,JavaFormalParameterModel> retval = new TreeMap<String,JavaFormalParameterModel>(); 
       Type[] parameterTypes=method_.getGenericParameterTypes();
       for(int i=0; i<parameterTypes.length; ++i){
           String name="fp"+i;
           JavaTypeModel c = JavaClassTypeModel.createTypeModel(parameterTypes[i]);
           int parameterModifiers=0;
           if (i==parameterTypes.length-1) {
               if (method_.isVarArgs()) {
                   parameterModifiers |= JavaTermModifiersModel.VARARGS;
                   // we does not need create array type here -- it is as array in parameterTypes. 
                   //c=new JavaArrayTypeModel(c,null);
                    if (debug) {
                        LOG.info("varargs for "+i+", nowc="+c.getName());
                    }                   
               }else{
                    if (debug) {
                        LOG.info("not varargs, c="+c.getName());
                    }                   
               }
           }
           retval.put(name,new JavaClassFormalParameterModel(name,new JavaClassModifiersModel(parameterModifiers),c,this,i));
       }
       return retval;
    }
 
    public Map<String, JavaAnnotationInstanceModel> getAnnotationsMap()
    {
        TreeMap<String,JavaAnnotationInstanceModel> retval = new TreeMap<String,JavaAnnotationInstanceModel>();
        Annotation[] cas = method_.getAnnotations();
        for(int i=0; i<cas.length; ++i) {
            String name = cas[i].annotationType().getSimpleName();
            JavaAnnotationInstanceModel an = new JavaClassAnnotationInstanceModel(ElementType.METHOD,cas[i],this);
            retval.put(name,an);
        }
        return retval;
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

    public  Annotation[][] getParameterAnnotations()
    { return method_.getParameterAnnotations(); }
    
    private Method   method_;
    
    private final Logger LOG = Logger.getLogger(JavaClassMethodModel.class.getName());
}
