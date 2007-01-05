/*
 * CT.java
 *
 * Created on неділя, 3, грудня 2006, 6:41
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package data.ua.gradsoft.test.x1;

/**
 *
 * @author Ruslan Shevchenko
 */
public class CT<T> {
    
    public T  getT()
    { return t_; }
    
    public void setT(T t)
    { t_=t; }
    
    private T  t_;
}
