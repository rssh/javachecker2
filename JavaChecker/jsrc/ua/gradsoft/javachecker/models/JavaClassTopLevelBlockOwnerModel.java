/*
 * JavaClassTopLevelBlockOwnerModel.java
 *
 */

package ua.gradsoft.javachecker.models;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

/**
 *Interface for top-level block owner model
 * @author rssh
 */
public interface JavaClassTopLevelBlockOwnerModel extends JavaTopLevelBlockOwnerModel
{
    
     /**
     *get model of type.
     */
    public JavaClassTypeModel getClassTypeModel();    
    
    
    /**
     *true, if this consturctor or method have variable arguments.
     */
    public boolean isVarArgs();
    
    /**
     *get set of top-level parameters
     */
    public Type[]  getClassFormalParameterTypes();

    /**
     *get set of top-level parameters
     */
    public TypeVariable[]  getClassTypeParameters();
    
    
    /**
     *get types in exception specification
     */
    public Type[]  getClassThrowsTypes();
    
    
    /**
     *get pointer to parameters annotations.
     */
    public Annotation[][]  getParameterAnnotations();
    
    /**
     *get declared annotations.
     */
    public Annotation[]  getDeclaredAnnotations();
    
    /**
     *get declared annotations.
     */
    public Annotation  getAnnotation(Class annotationClass);
    
    
}
