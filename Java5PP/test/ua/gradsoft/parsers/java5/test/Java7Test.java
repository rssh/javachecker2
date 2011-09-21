/*
 * Java7Test
 *
 * Owner: Ruslan Shevchenko
 *
 * Copyright (c) 2011 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.parsers.java5.test;

import java.io.FileReader;
import java.io.Reader;
import junit.framework.TestCase;
import ua.gradsoft.parsers.java5.JPPMain;
import ua.gradsoft.parsers.java5.JavaParserFactory;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWare;


// test for parsing Java7 features.
public class Java7Test extends TestCase
{
    
    
    protected void setUp()
    {                                                  
       TestCommon.setUp();
    }
    
    
    public void testStrSwt() throws Exception
    {
      Term source=TestCommon.readJavaSourceFromFile(
                                     "testdata/coin/strswt/StrSwt.java",
                                     false);
      assertTrue("received term is not compilation unit",source.getName().equals("CompilationUnit"));
      //System.out.print("source:"); 
      //source.println(System.out);
    }

    
    
}
