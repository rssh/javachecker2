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
    public JavaTypeModel getType() throws TermWareException, EntityNotFoundException;
    
    /**
     * return modifiers
     */
    public JavaModifiersModel  getModifiers();
    
    
    /**
     *return set of annotations for this variable.
     */
    public Map<String, JavaAnnotationInstanceModel>  getAnnotationsMap() throws TermWareException;
    
    /**
     * return type which own this variable.
     *(i. e. class where one is defined)
     */
    public JavaTypeModel getOwnerType();
    
    /**
     * return owner of top level block, where variablke is defined (constructor or method or initializer)
     *if one exists, otherwise return null.
     */
    public JavaTopLevelBlockOwnerModel getTopLevelBlockOwner();
    
}
