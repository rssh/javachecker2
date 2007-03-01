/*
 * x1test.java
 *
 * Owner: Ruslan Shevchenko
 *
 * Copyright (c) 2006 GradSoft  Ukraine
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


/**
 *
 * @author Ruslan Shevchenko
 */
public class JCTest extends TestCase
{
    
    /** Creates a new instance of x1test */
    public JCTest() {
        
    }
    
    protected void setUp()
    {                                                  
        TermWare.getInstance().addParserFactory("Java",new JavaParserFactory());
    }
    
    
    public void testJC0() throws Exception
    {
      Term source=null;
      String fname = "testdata/jc/JavaArgumentBoundTypeModel0.java";
      Reader reader = new FileReader(fname);
      Term nil=TermWare.getInstance().getTermFactory().createNIL();
      source=TermWare.getInstance().getParserFactory("Java").createParser(reader,fname,nil,TermWare.getInstance()).readTerm();
      //source.println(System.out);  
      reader.close();
      assertTrue("received term is not compilation unit",source.getName().equals("CompilationUnit"));
    }

    public void testAw() throws Exception
    {
      Term source=null;
      String fname = "testdata/aw/AWTKeyStroke.java";
      Reader reader = new FileReader(fname);
      Term nil=TermWare.getInstance().getTermFactory().createNIL();
      source=TermWare.getInstance().getParserFactory("Java").createParser(reader,fname,nil,TermWare.getInstance()).readTerm();
      //source.println(System.out);  
      assertTrue("received term is not compilation unit",source.getName().equals("CompilationUnit"));
    }

    public void testCr() throws Exception
    {
      Term source=null;
      String fname = "testdata/cr/ORBUtilSystemException.java";
      Reader reader = new FileReader(fname);
      Term nil=TermWare.getInstance().getTermFactory().createNIL();
      source=TermWare.getInstance().getParserFactory("Java").createParser(reader,fname,nil,TermWare.getInstance()).readTerm();
      //source.println(System.out);  
      assertTrue("received term is not compilation unit",source.getName().equals("CompilationUnit"));
    }
    
    
}
