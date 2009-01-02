/*
 * JavaTermTopLevelBlockOwnerModel.java
 *
 */

package ua.gradsoft.javachecker.models;

/**
 *Interface for term-based abstract block model owner
 * @author Ruslan Shevchenko
 */
public interface JavaTermTopLevelBlockOwnerModel extends JavaTopLevelBlockOwnerModel
{
    
    /**
     *get model of type.
     */
    public JavaTermTypeAbstractModel getTermTypeAbstractModel();
    
    
}
