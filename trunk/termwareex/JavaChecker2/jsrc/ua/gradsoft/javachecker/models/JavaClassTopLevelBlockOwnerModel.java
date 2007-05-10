/*
 * JavaClassTopLevelBlockOwnerModel.java
 *
 */

package ua.gradsoft.javachecker.models;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

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
     *get pointer to parameters annotations.
     */
    public Annotation[][]  getParameterAnnotations();
    
}
