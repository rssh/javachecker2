/*
 * JavaClassConstructorModel.java
 *
 * Created on May 3, 2007, 6:43 AM
 */

package ua.gradsoft.javachecker.models;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import ua.gradsoft.javachecker.util.Function;
import ua.gradsoft.javachecker.util.FunctionMap;
import ua.gradsoft.javachecker.util.ImmutableMappedList;
import ua.gradsoft.javachecker.util.IntegerOrderList;
import ua.gradsoft.javachecker.util.ImmutableListAsMap;
import ua.gradsoft.javachecker.util.ImmutableMappedCollection;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;



/**
 *Model for constructor of class
 * @author rssh
 */
public final class JavaClassConstructorModel extends JavaConstructorModel implements JavaClassTopLevelBlockOwnerModel
{
    
    /** Creates a new instance of JavaClassConstructorModel */
    public JavaClassConstructorModel(Constructor constructor, JavaClassTypeModel owner) {
        constructor_=constructor;
        owner_=owner;
    }
    
    public List<JavaTypeVariableAbstractModel> getTypeParameters()
    {  
        return JavaClassTopLevelBlockOwnerModelHelper.getTypeParameters(this);
    }
    
    public List<JavaTypeModel>  getFormalParametersTypes() throws TermWareException
    {
       return JavaClassTopLevelBlockOwnerModelHelper.getFormalParametersTypes(this); 
    }

    
    public List<JavaFormalParameterModel> getFormalParametersList()throws TermWareException
    {
       return JavaClassTopLevelBlockOwnerModelHelper.getFormalParametersList(this);
    }
    
    public Map<String, JavaFormalParameterModel> getFormalParametersMap() throws TermWareException
    {        
       return JavaClassTopLevelBlockOwnerModelHelper.getFormalParametersMap(this);
    }


    public List<JavaTypeModel>  getThrowsList() throws TermWareException
    {
       return JavaClassTopLevelBlockOwnerModelHelper.getThrowsList(this);
    }
    
    
    public Map<String,JavaAnnotationInstanceModel> getAnnotationsMap()
    {
       return JavaClassTopLevelBlockOwnerModelHelper.getAnnotationsMap(this);
     }
    
                  
    
    public String getName()
    { return owner_.getName(); }
    
    public JavaTypeModel getTypeModel()
    { return owner_; }
    
    public JavaClassTypeModel  getClassTypeModel()
    { return owner_; }
    
    public boolean isSupportBlockModel()
    { return false; }
    
    public JavaTopLevelBlockModel getTopLevelBlockModel()
    { return null; }
    
    
    public Term getModelTerm() throws TermWareException
    {
        Term jthis = TermUtils.createJTerm(this);
        Term retval = TermUtils.createTerm("ClassConstructorModel",jthis);
        return retval;
    }
        
    
    public boolean isVarArgs()
    { return constructor_.isVarArgs(); }
    
    public Type[]  getClassFormalParameterTypes()
    {
        return constructor_.getGenericParameterTypes();
    }

    public Type[]  getClassThrowsTypes()
    {
        return constructor_.getGenericExceptionTypes();
    }
    
    public TypeVariable[] getClassTypeParameters()
    {
        return constructor_.getTypeParameters();
    }
    
    public Annotation[]  getDeclaredAnnotations()
    {
       return constructor_.getDeclaredAnnotations();
    }
    
    public Annotation  getAnnotation(Class annotationClass)
    { return constructor_.getAnnotation(annotationClass); }
    
    public Annotation[][]  getParameterAnnotations()
    { return constructor_.getParameterAnnotations(); }
    
    private Constructor constructor_;
    private JavaClassTypeModel owner_;
}
