/*
 * JPPTestSuite.java
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.parsers.java5.test;

import junit.framework.TestSuite;

/**
 *Main test suite for JPP.
 * @author Ruslan Shevchenko
 */
public class JPPTestSuite extends TestSuite
{
    
    /** Creates a new instance of JPPTestSuite */
    public JPPTestSuite() {
        this.addTestSuite(JCTest.class);        
        this.addTestSuite(X1test.class);        
        this.addTestSuite(ASTTransformTest.class);
        this.addTestSuite(PrinterTest.class);
        this.addTestSuite(ExprTest.class);
        this.addTestSuite(ParseHelperTest.class);
        this.addTestSuite(JettyServerTest.class);
        //this.addTestSuite(JavaSrcTest.class);
    }
    
    
    public void testMakeAntHappy()
    {}
    
    public static void main(String[] args)
    {
      junit.textui.TestRunner.run(new JPPTestSuite());    
    }

    
}
