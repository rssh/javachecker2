/*
 * Pair.java
 *
 * Created on 1 Март 2007 г., 7:46
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ua.gradsoft.javachecker.util;

/**
 *Just Pair.
 * @author RSSH
 */
public class Pair<T1,T2> 
{
    
    /** Creates a new instance of Pair */
    public Pair(T1 first, T2 second) {
        first_=first;
        second_=second;
    }
    
    public T1  getFirst()
    { return first_; }
    
    public T2  getSecond()
    { return second_; }
        
    private T1 first_;
    private T2 second_;
}
