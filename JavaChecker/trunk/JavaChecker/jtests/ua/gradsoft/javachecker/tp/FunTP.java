/*
 * FunTP.java
 *
 * Owner: Ruslan Shevchenko
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.tp;

/**
 *Illustrate method with type parameters.
 * @author Ruslan Shevchenko
 */
public class FunTP {
    
    
    
    public <T>  T getValue(Class<T> tClass, String s) throws Exception
    {
      if (tClass.isAssignableFrom(String.class)) {          
          T t = tClass.newInstance();
          t = (T)s;
          return t;
      } else{
          throw new Exception("Not implemented");
      } 
    }
  
    public <T,U>  String somethingWithTwoParameters(T t, U u)
    {
        return t.toString()+"/"+u.toString();
    }
    
}
