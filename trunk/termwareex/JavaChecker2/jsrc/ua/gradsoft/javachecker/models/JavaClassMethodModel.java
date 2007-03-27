/*
 * JavaClassMethodModel.java
 *
 * Copyright (c) 2004-2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Model class, which based on reflection mechanizm
 * @author Ruslan Shevchenko
 */
public class JavaClassMethodModel extends JavaMethodModel
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
    
    public JavaModifiersModel getModifiers()
    {
      return new JavaModifiersModel(JavaClassTypeModel.translateModifiers(method_.getModifiers()));       
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
        List<JavaTypeModel> retval = new LinkedList<JavaTypeModel>();
        Type[] parameterTypes=method_.getGenericParameterTypes();
        for(int i=0; i<parameterTypes.length; ++i) {
            JavaTypeModel c = JavaClassTypeModel.createTypeModel(parameterTypes[i]);
            retval.add(c);
        }
        return retval;
    }            
    
    public List<JavaFormalParameterModel> getFormalParametersList()throws TermWareException
    {
        Type[] parameterTypes=method_.getGenericParameterTypes();
        List<JavaFormalParameterModel> retval = new ArrayList<JavaFormalParameterModel>(parameterTypes.length);
        for(int i=0; i<parameterTypes.length; ++i) {
            String name="fp"+i;
            JavaTypeModel c = JavaClassTypeModel.createTypeModel(parameterTypes[i]);
            int parameterModifiers=0;
            if (i==parameterTypes.length-1) {
                if (method_.isVarArgs()) {
                    parameterModifiers |= JavaModifiersModel.VARARGS;
                }
            }
            retval.add(new JavaFormalParameterModel(parameterModifiers,name,c,this,i));
        }
        return retval;
    }
    
    
    public Map<String,JavaFormalParameterModel> getFormalParametersMap() throws TermWareException
    {
       Map<String,JavaFormalParameterModel> retval = new TreeMap<String,JavaFormalParameterModel>(); 
       Type[] parameterTypes=method_.getGenericParameterTypes();
       for(int i=0; i<parameterTypes.length; ++i){
           String name="fp"+i;
           JavaTypeModel c = JavaClassTypeModel.createTypeModel(parameterTypes[i]);
           retval.put(name,new JavaFormalParameterModel(0,name,c,this,i));
       }
       return retval;
    }
 
    
     public boolean         isSupportBlockModel()
     { return false; }
    
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

    
    
    private Method   method_;
    
    
}
