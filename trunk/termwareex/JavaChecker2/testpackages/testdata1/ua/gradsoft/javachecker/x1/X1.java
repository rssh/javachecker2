/*
 * X1.java
 *
 * Copyright (c) 2004-2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.x1;

/**
 *
 * @author Ruslan Shevchenko
 */
public class X1 {
    
    /** Creates a new instance of X1 */
    public X1() {
    }

    public int qqq() throws Exception
    { throw new Exception("dummy"); }

    /**
     *@cheker disable(ThrowGenericException)
     */
    public int qqq1() throws Exception
    { throw new Exception("dummy"); }

    
}
