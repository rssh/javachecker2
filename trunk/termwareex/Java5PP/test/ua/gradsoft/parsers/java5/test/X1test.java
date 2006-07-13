/*
 * x1test.java
 *
 * Owner: Ruslan Shevchenko
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.parsers.java5.test;

import junit.framework.*;
import ua.gradsoft.parsers.java5.JPPMain;
import ua.gradsoft.termware.*;

/**
 *
 * @author Ruslan Shevchenko
 */
public class X1test extends TestCase
{
    
    /** Creates a new instance of x1test */
    public X1test() {
        
    }
    
    public void testX1() throws Exception
    {
        Term t = JPPMain.parseString(xClass);
        //t.println(System.out);
        assert(t.getName().equals("CompilationUnit"));
        Term dcl=t.getSubtermAt(0).getSubtermAt(1);
        assert(dcl.getName().equals("ClassOrInterfaceDeclaration"));
        assert(dcl.getSubtermAt(0).getName().equals("class"));
        Term idt=dcl.getSubtermAt(1);
        assert(idt.getName().equals("Identifier"));
        assert(idt.getSubtermAt(0).getName().equals("X"));        
    }
    
    private static String xClass="class X {}";
    
}
