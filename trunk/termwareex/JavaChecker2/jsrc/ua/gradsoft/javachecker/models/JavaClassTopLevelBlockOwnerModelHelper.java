/*
 * JavaClassTopLevelBlockOwnerModelHelper.java
 *
 */

package ua.gradsoft.javachecker.models;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import ua.gradsoft.javachecker.util.Function;
import ua.gradsoft.javachecker.util.FunctionMap;
import ua.gradsoft.javachecker.util.ImmutableListAsMap;
import ua.gradsoft.javachecker.util.ImmutableMappedCollection;
import ua.gradsoft.javachecker.util.ImmutableMappedList;
import ua.gradsoft.javachecker.util.IntegerOrderList;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Helper for operations, common for class constructors and methods.
 * @author rssh
 */
public class JavaClassTopLevelBlockOwnerModelHelper {

    public static List<JavaTypeVariableAbstractModel> getTypeParameters(final JavaClassTopLevelBlockOwnerModel executable)
    {  
        return new ImmutableMappedList<TypeVariable,JavaTypeVariableAbstractModel>(
           Arrays.asList(executable.getClassTypeParameters()),
           new Function<TypeVariable,JavaTypeVariableAbstractModel>(){
            public JavaTypeVariableAbstractModel function(TypeVariable x){
                return new JavaClassTypeVariableModel(x);
            }
        }     
                );
    }
    
    public static List<JavaTypeModel>  getFormalParametersTypes(JavaClassTopLevelBlockOwnerModel executable) throws TermWareException
    {        
        final Type[] typearray = executable.getClassFormalParameterTypes();
        final int paramLength = typearray.length; 
        return new ImmutableMappedList<Integer,JavaTypeModel>(
                new IntegerOrderList(paramLength),
                new Function<Integer,JavaTypeModel>(){
            public JavaTypeModel function(Integer i) throws TermWareException
            {
                return JavaClassTypeModel.createTypeModel(typearray[i]);             
            }
        }
                );
    }

       /*
        *TODO: two variants test for speed.        
        */    
    public static List<JavaFormalParameterModel> getFormalParametersList(JavaClassTopLevelBlockOwnerModel executable)  throws TermWareException
    {
      if (false) {
      final int paramLength = executable.getClassFormalParameterTypes().length;    
      final JavaClassTopLevelBlockOwnerModel  lexecutable = executable;
      List<JavaFormalParameterModel> retval = new ImmutableMappedList<Integer,JavaFormalParameterModel>(
                  new IntegerOrderList(paramLength),
                  new Function<Integer,JavaFormalParameterModel>(){
           public JavaFormalParameterModel function(Integer i) throws TermWareException
           {             
               return new JavaClassFormalParameterModel(
                       "fp"+i,
                       lexecutable.getParameterAnnotations()[i],
                       (i==paramLength-1 && lexecutable.isVarArgs() ? JavaModifiersModel.VARARGS : 0),
                       JavaClassTypeModel.createTypeModel(lexecutable.getClassFormalParameterTypes()[i]),
                       lexecutable,
                       i
                       );                       
           }
       }
               );
               
               
       return retval;          
      }else{
          Type[] types = executable.getClassFormalParameterTypes();
          List<JavaFormalParameterModel> retval = new ArrayList<JavaFormalParameterModel>(types.length);
          for(int i=0; i<types.length; ++i) {
            retval.add(new JavaClassFormalParameterModel(
                       "fp"+i,
                       executable.getParameterAnnotations()[i],
                       (i==types.length-1 && executable.isVarArgs() ? JavaModifiersModel.VARARGS : 0),
                       JavaClassTypeModel.createTypeModel(types[i]),
                       executable,
                       i
                       )
                      );              
          }
          return retval;
      }
    }
    
    
    public static Map<String, JavaFormalParameterModel> getFormalParametersMap(final JavaClassTopLevelBlockOwnerModel executable) throws TermWareException
    {        
       return new ImmutableListAsMap<String,JavaFormalParameterModel>(
           getFormalParametersList(executable),    
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

    public static List<JavaAnnotationInstanceModel> getFormalParameterAnnotationsList(final JavaClassTopLevelBlockOwnerModel executable, final int i, final JavaFormalParameterModel fpi)
    {
        return new ImmutableMappedList<Integer,JavaAnnotationInstanceModel>(
                new IntegerOrderList(executable.getParameterAnnotations()[i].length),
                new Function<Integer,JavaAnnotationInstanceModel>(){
            public JavaAnnotationInstanceModel function(final Integer k){
                return new JavaClassAnnotationInstanceModel(ElementType.PARAMETER,executable.getParameterAnnotations()[i][k],fpi);
            }
        }
                );
    }


    public static List<JavaTypeModel>  getThrowsList(final JavaClassTopLevelBlockOwnerModel executable) throws TermWareException
    {
        final int length = executable.getClassThrowsTypes().length; 
        return new ImmutableMappedList<Integer,JavaTypeModel>(
                new IntegerOrderList(length),
                new Function<Integer,JavaTypeModel>(){
            public JavaTypeModel function(Integer i) throws TermWareException
            {
                return JavaClassTypeModel.createTypeModel(executable.getClassThrowsTypes()[i]);             
            }
        }
                );
    }
    
    
    public static Map<String,JavaAnnotationInstanceModel> getAnnotationsMap(final JavaClassTopLevelBlockOwnerModel executable)
    {
       return new FunctionMap<String,JavaAnnotationInstanceModel>(
               new ImmutableMappedCollection<Integer,String>(
                 new IntegerOrderList(executable.getDeclaredAnnotations().length),
                 new Function<Integer,String>(){
           public String function(Integer x){              
                 return executable.getDeclaredAnnotations()[x].annotationType().getName();   
           }           
                 }),
               new Function<String,JavaAnnotationInstanceModel>(){
           public JavaAnnotationInstanceModel function(String s) throws TermWareException
           {                
               Class annotationClass=null;
               try {
                   annotationClass = JavaClassTypeModel.forName(s);
               }catch(AssertException ex){
                   Throwable exCause = ex.getCause();
                   if (exCause!=null){
                       if (exCause instanceof ClassNotFoundException) {
                           return null;
                       }
                   }
                   throw ex;                   
               }
               Annotation annotation = executable.getAnnotation(JavaClassTypeModel.forName(s));
               if (annotation==null) {
                   return null;
               }
               return new JavaClassAnnotationInstanceModel(ElementType.CONSTRUCTOR,
                                                   annotation,
                                                   executable);
           }
       }               
               );
     }
    
    
}
