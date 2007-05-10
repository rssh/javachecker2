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
public class JavaClassConstructorModel extends JavaConstructorModel implements JavaClassTopLevelBlockOwnerModel
{
    
    /** Creates a new instance of JavaClassConstructorModel */
    public JavaClassConstructorModel(Constructor constructor, JavaClassTypeModel owner) {
        constructor_=constructor;
        owner_=owner;
    }
    
    public List<JavaTypeVariableAbstractModel> getTypeParameters()
    {  
        return new ImmutableMappedList<TypeVariable,JavaTypeVariableAbstractModel>(
           Arrays.asList(constructor_.getTypeParameters()),
           new Function<TypeVariable,JavaTypeVariableAbstractModel>(){
            public JavaTypeVariableAbstractModel function(TypeVariable x){
                return new JavaClassTypeVariableModel(x);
            }
        }     
                );
    }
    
    public List<JavaTypeModel>  getFormalParametersTypes() throws TermWareException
    {
        final int paramLength = constructor_.getGenericParameterTypes().length; 
        return new ImmutableMappedList<Integer,JavaTypeModel>(
                new IntegerOrderList(paramLength),
                new Function<Integer,JavaTypeModel>(){
            public JavaTypeModel function(Integer i) throws TermWareException
            {
                JavaTypeModel retval = JavaClassTypeModel.createTypeModel(constructor_.getGenericParameterTypes()[i]);
                if (i==constructor_.getGenericParameterTypes().length-1) {
                    if (constructor_.isVarArgs()) {
                        retval = new JavaArrayTypeModel(retval);
                    }
                }
                return retval;
            }
        }
                );
    }

    
    public List<JavaFormalParameterModel> getFormalParametersList()throws TermWareException
    {
      final int paramLength = constructor_.getTypeParameters().length; 
       return new ImmutableMappedList<Integer,JavaFormalParameterModel>(
                  new IntegerOrderList(paramLength),
                  new Function<Integer,JavaFormalParameterModel>(){
           public JavaFormalParameterModel function(Integer i) throws TermWareException
           {             
               return new JavaClassFormalParameterModel(
                       "fp"+i,
                       new JavaClassModifiersModel(i==paramLength-1 && constructor_.isVarArgs() ? JavaModifiersModel.VARARGS : 0),
                       JavaClassTypeModel.createTypeModel(constructor_.getTypeParameters()[i]),
                       JavaClassConstructorModel.this,
                       i
                       );                       
           }
       }
               );
    }
    
    
    public Map<String, JavaFormalParameterModel> getFormalParametersMap() throws TermWareException
    {        
       return new ImmutableListAsMap<String,JavaFormalParameterModel>(
           getFormalParametersList(),    
           new Function<String,Integer>() {
               public Integer function(String key){
                   if (key.length()<2) return -1;
                   return Integer.parseInt(key.substring(2));
               }
           },
           new Function<Integer,String>() {
               public String function(Integer key){
                   return "fp"+Integer.toString(key);
               }
           }
           );   
    }

    
    public Map<String,JavaAnnotationInstanceModel> getAnnotationsMap()
    {
       return new FunctionMap<String,JavaAnnotationInstanceModel>(
               new ImmutableMappedCollection<Integer,String>(
                 new IntegerOrderList(constructor_.getDeclaredAnnotations().length),
                 new Function<Integer,String>(){
           public String function(Integer x){              
                 return constructor_.getDeclaredAnnotations()[x].annotationType().getName();   
           }           
                 }),
               new Function<String,JavaAnnotationInstanceModel>(){
           public JavaAnnotationInstanceModel function(String s) throws TermWareException
           {               
               return new JavaClassAnnotationInstanceModel(ElementType.CONSTRUCTOR,
                       constructor_.getAnnotation(JavaClassTypeModel.forName(s)),
                       JavaClassConstructorModel.this);
           }
       }               
               );
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
    
    
    public Annotation[][]  getParameterAnnotations()
    { return constructor_.getParameterAnnotations(); }
    
    private Constructor constructor_;
    private JavaClassTypeModel owner_;
}
