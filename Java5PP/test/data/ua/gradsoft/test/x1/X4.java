/*
 * X4.java
 *
 * Copyright (c) 2004-2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package data.ua.gradsoft.test.x1;

import ua.gradsoft.termware.TermWare;

/**
 *
 * @author Ruslan Shevchenko
 */
public abstract class X4 extends X3 implements I1
{
    
    /** Creates a new instance of X4 */
    public X4() {
    }
    
    /**
     * doSomething comment.
     */
    public void doSomething()
    {
        int x=this.qqq(1,1.0,new X2());
        int y=this.qqq(2,1.0,new X2());
        int z=y-x;
    }
    
    /**
     * doSomething1 comment.
     */
    int doSomething1(int x)
    {
        return x;
    }
    
    void main(String[] args)
    {
        for(int i=0; i<args.length; ++i) {
            System.out.println(args[i]+doSomething1(i));
        }
    }
    
    static { TermWare.getInstance().init(); }
    
}
