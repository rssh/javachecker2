/*
 * Pair.java
 *
 *
 */

package ua.gradsoft.javachecker.util;

import java.util.Map;

/**
 *Just Pair.
 */
public class Pair<T1,T2> 
{
    
    /** Creates a new instance of Pair */
    public Pair(T1 first, T2 second) {
        first_=first;
        second_=second;
    }
    
    public Pair(Map.Entry<T1,T2> e)
    {
       this(e.getKey(),e.getValue()); 
    }
    
    public T1  getFirst()
    { return first_; }
    
    public T2  getSecond()
    { return second_; }
        
    private T1 first_;
    private T2 second_;
}
