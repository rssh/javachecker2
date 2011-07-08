/*
 * LC1.java
 *
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package t1.testdata.localclass1;

/**
 *Example of local class declaration.
 * @author Ruslan Shevchenko
 */
public class LC1 {
    
    public static void main(String[] args)
    {
        class LocalClass {
            LocalClass(int x)
            { x_=x; }
            public int getX()
            { return x_; }
            private int x_;
        };
        LocalClass lc=new LocalClass(10);
        System.out.println(lc.getClass().getName());
        l2();
        l3();
    }

    public static void l2()
    {
        class LocalClass {
            LocalClass(double x)
            { x_=x; }
            public double getX()
            { return x_; }
            private double x_;
        };
        LocalClass lc=new LocalClass(10);
        System.out.println(lc.getClass().getName());
    }
    
    public static void l3()
    {
        Runnable x = new Runnable() {
            
            public void run() {
                /* do nothing */
            }
            
        };       
        System.out.println(x.getClass().getName());
    }
    
}
