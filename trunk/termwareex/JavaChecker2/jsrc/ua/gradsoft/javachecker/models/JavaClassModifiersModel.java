/*
 * JavaClassModifiersModel.java
 *
 */

package ua.gradsoft.javachecker.models;

/**
 *Modifiers for class.
 * @author rssh
 */
public class JavaClassModifiersModel extends JavaModifiersModel
{
    
    /** Creates a new instance of JavaClassModifiersModel */
    public JavaClassModifiersModel(int value) {
        value_=value;
    }
    
    public int getIntValue()
    { return value_; }
    
    private int value_;
}
