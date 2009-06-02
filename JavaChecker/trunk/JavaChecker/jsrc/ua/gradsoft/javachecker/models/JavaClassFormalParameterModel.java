/*
 * JavaClassFormalParameterModel.java
 *
 */

package ua.gradsoft.javachecker.models;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import ua.gradsoft.javachecker.util.CachedMap;
import ua.gradsoft.javachecker.util.Function;
import ua.gradsoft.javachecker.util.FunctionMap;
import ua.gradsoft.javachecker.util.ImmutableMappedCollection;
import ua.gradsoft.javachecker.util.ImmutableMappedList;
import ua.gradsoft.javachecker.util.IntegerOrderList;

/**
 *
 * @author rssh
 */
public class JavaClassFormalParameterModel extends JavaFormalParameterModel
{
    
    /** Creates a new instance of JavaClassFormalParameterModel */
    public JavaClassFormalParameterModel(
            String name,
            Annotation[] annotations,
            int          modifiers,            
            JavaTypeModel type,
            JavaClassTopLevelBlockOwnerModel owner,
            int index
            ) {
        name_=name;       
        annotations_=annotations;
        type_=type;
        owner_=owner;
        index_=index;
        modifiers_=new JavaClassModifiersModel(getAnnotationsList(),modifiers);
    }
    
    public JavaModifiersModel  getModifiers()
    { return modifiers_; }
    
    public String getName()
    { return name_; }
    
    public JavaTypeModel getType()
    { return type_; }
    
    public JavaTopLevelBlockOwnerModel getTopLevelBlockOwner()
    { return owner_; }
    
    public Map<String,JavaAnnotationInstanceModel> getAnnotationsMap()
    {
        final int size=owner_.getParameterAnnotations()[index_].length;
        if (annotationsCache_==null) {
           annotationsCache_=new TreeMap<String,JavaAnnotationInstanceModel>();
        }
        return new CachedMap<String,JavaAnnotationInstanceModel>(
                annotationsCache_,
                new FunctionMap<String,JavaAnnotationInstanceModel>(
                  new ImmutableMappedCollection<Integer,String>(
                    new IntegerOrderList(size),
                    new Function<Integer,String>(){
                      public String function(Integer x){
                         return owner_.getParameterAnnotations()[index_][x].annotationType().getName();
                      }
                    }
                  ),
                  new Function<String,JavaAnnotationInstanceModel>(){
                    public JavaAnnotationInstanceModel function(String s)  {
                      for(int i=0; i<owner_.getParameterAnnotations()[index_].length; ++i) {
                          if (owner_.getParameterAnnotations()[index_][i].annotationType().getName().equals(s)){
                             return new JavaClassAnnotationInstanceModel(ElementType.PARAMETER,owner_.getParameterAnnotations()[index_][i],JavaClassFormalParameterModel.this);
                          }
                      }
                      return null;
                    }
                  }
                )
              );            
    }
    
    
    public List<JavaAnnotationInstanceModel> getAnnotationsList()
    {
        return new ImmutableMappedList<Integer,JavaAnnotationInstanceModel>(
                new IntegerOrderList(annotations_.length),
                new Function<Integer,JavaAnnotationInstanceModel>(){
            public JavaAnnotationInstanceModel function(Integer i)
            {
                return new JavaClassAnnotationInstanceModel(ElementType.PARAMETER,annotations_[i],JavaClassFormalParameterModel.this);
            }
        }
                );                           
    }

    public boolean isConstant()
    { return false; }
    
    public int getIndex()
    { return index_; }
    
    private TreeMap<String,JavaAnnotationInstanceModel> annotationsCache_=null;
    
    private String name_;
    private JavaModifiersModel modifiers_; 
    private JavaTypeModel      type_;
    private JavaClassTopLevelBlockOwnerModel owner_;
    private Annotation[]  annotations_;
    int     index_;
    
}
