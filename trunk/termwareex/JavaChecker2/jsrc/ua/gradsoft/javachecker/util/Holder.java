/*
 * Holder.java
 *
 */

package ua.gradsoft.javachecker.util;

/**
 *Holder for values
 * @author RSSH
 */
public class Holder<T>
{

    public Holder()
    { value_=null; }
    
    public T getValue()
    { return value_; }
    
    public void setValue(T value)
    { value_=value; }
    
    private T  value_;
}
