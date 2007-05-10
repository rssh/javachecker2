/*
 * JavaClassFormalParameterModel.java
 *
 */

package ua.gradsoft.javachecker.models;

import java.lang.annotation.ElementType;
import java.util.Map;
import java.util.TreeMap;
import ua.gradsoft.javachecker.util.CachedMap;
import ua.gradsoft.javachecker.util.Function;
import ua.gradsoft.javachecker.util.FunctionMap;
import ua.gradsoft.javachecker.util.ImmutableMappedCollection;
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
            JavaModifiersModel modifiers,   
            JavaTypeModel type,
            JavaClassTopLevelBlockOwnerModel owner,
            int index
            ) {
        name_=name;
        modifiers_=modifiers;
        type_=type;
        owner_=owner;
        index_=index;
    }
    
    public JavaModifiersModel  getModifiersModel()
    { return modifiers_; }
    
    public String getName()
    { return name_; }
    
    public JavaTypeModel getTypeModel()
    { return type_; }
    
    public JavaTopLevelBlockOwnerModel getOwner()
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
    
    public int getIndex()
    { return index_; }
    
    private TreeMap<String,JavaAnnotationInstanceModel> annotationsCache_=null;
    
    private String name_;
    private JavaModifiersModel modifiers_; 
    private JavaTypeModel      type_;
    private JavaClassTopLevelBlockOwnerModel owner_;
    int     index_;
    
}
