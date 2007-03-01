/*
 * Holder.java
 *
 * Created on 1 Март 2007 г., 4:28
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ua.gradsoft.javachecker.util;

/**
 *
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
