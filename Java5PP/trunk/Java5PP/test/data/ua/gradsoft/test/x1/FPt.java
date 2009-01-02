/*
 * FPt.java
 *
 * Copyright (c) 2004-2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package data.ua.gradsoft.test.x1;

/**
 *Class which illustrate work with TypeParameters
 * @author Ruslan Shevchenko
 */
public class FPt {

    
       static <T,U>  String ctu(T t, U u)
       {
           return t.toString()+u.toString();
       }
       
       static <T, U extends T> T ctu1(T t, U u)
       {
           T t1=u;
           return t1;
       }
    
}
