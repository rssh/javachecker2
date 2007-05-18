/*
 * JavaVariableModel.java
 *
 */

package ua.gradsoft.javachecker.models;

import java.util.Map;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.attributes.AttributedEntity;
import ua.gradsoft.termware.TermWareException;

/**
 *Model of java variable, which we can see in code.
 * @author Ruslan Shevchenko
 */
public interface JavaVariableModel extends AttributedEntity
{
    
    /**
     *get name of variable.
     */
    public String getName();
    
    /**
     *get kind of variable
     */
    public JavaVariableKind getKind();
    
    /**
     *get type of variable
     */
    public JavaTypeModel getTypeModel() throws TermWareException, EntityNotFoundException;
    
    /**
     * return modifiers
     */
    public JavaModifiersModel  getModifiersModel();
    
    
    /**
     *return set of annotations for this variable.
     */
    public Map<String, JavaAnnotationInstanceModel>  getAnnotationsMap() throws TermWareException;
    
    
}
