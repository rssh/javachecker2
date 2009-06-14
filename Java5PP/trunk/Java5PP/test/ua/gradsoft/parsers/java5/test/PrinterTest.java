/*
 * PrinterTest.java
 *
 * Copyright (c) 2004-2009 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.parsers.java5.test;

import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import junit.framework.TestCase;
import ua.gradsoft.parsers.java5.JavaParserFactory;
import ua.gradsoft.printers.java5.JavaPrinterFactory;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWare;

/**
 *Check printing of Java Sources
 * @author Ruslan Shevchenko
 */
public class PrinterTest extends TestCase
{
    
    /** Creates a new instance of PrinterTest */
    public PrinterTest() {
    }
    
     protected void setUp()
    {                                                  
        TermWare.getInstance().addParserFactory("Java",new JavaParserFactory());
        TermWare.getInstance().addPrinterFactory("Java",new JavaPrinterFactory());
    }
   
    
    public void testFX1() throws Exception
    {
      Term source=null;
      String fname = "test/data/ua/gradsoft/test/x1/X1.java";
      Reader reader = new FileReader(fname);
      Term nil=TermWare.getInstance().getTermFactory().createNIL();
      source=TermWare.getInstance().getParserFactory("Java").createParser(reader,fname,nil,TermWare.getInstance()).readTerm();
      String s0=TermHelper.termToPrettyString(source);
      System.out.println("!!!!----!!!!!");
      System.out.println(s0);
      String s = TermHelper.termToPrettyString(source,"Java",TermWare.getInstance().getTermFactory().createNIL());
      System.out.println("!!!!----!!!!!");
      System.out.println(s);
      Term source1=TermWare.getInstance().getParserFactory("Java").createParser(new StringReader(s),"s",nil,TermWare.getInstance()).readTerm();
      assertTrue("printed source is not parsed",!source1.isNil());
    }
 
     
    public void testFFpt() throws Exception
    {
      Term source=null;
      String fname = "test/data/ua/gradsoft/test/x1/FPt.java";
      Reader reader = new FileReader(fname);
      Term nil=TermWare.getInstance().getTermFactory().createNIL();
      source=TermWare.getInstance().getParserFactory("Java").createParser(reader,fname,nil,TermWare.getInstance()).readTerm();
      String s0=TermHelper.termToPrettyString(source);
      System.out.println("!!!!----!!!!!");
      System.out.println(s0);
      String s = TermHelper.termToPrettyString(source,"Java",TermWare.getInstance().getTermFactory().createNIL());
      System.out.println("!!!!----!!!!!");
      System.out.println(s);
      Term source1=TermWare.getInstance().getParserFactory("Java").createParser(new StringReader(s),"s",nil,TermWare.getInstance()).readTerm();
      assertTrue("printed source is not parsed",!source1.isNil());
    }
      
 
     
    public void testConfiguration1() throws Exception
    {
      Term source=null;
      String fname = "testdata/jpe/Configuration.java";
      Reader reader = new FileReader(fname);
      Term nil=TermWare.getInstance().getTermFactory().createNIL();
      source=TermWare.getInstance().getParserFactory("Java").createParser(reader,fname,nil,TermWare.getInstance()).readTerm();
      String s0=TermHelper.termToPrettyString(source);
      //System.out.println("!!!!----!!!!!");
      //System.out.println(s0);
      String s = TermHelper.termToPrettyString(source,"Java",TermWare.getInstance().getTermFactory().createNIL());
      //System.out.println("!!!!----!!!!!");
      //System.out.println(s);
      Term source1=TermWare.getInstance().getParserFactory("Java").createParser(new StringReader(s),"s",nil,TermWare.getInstance()).readTerm();
      assertTrue("printed source is not parsed",!source1.isNil());
    }
      

    public void testII() throws Exception
    {
      Term source=null;
      String fname = "testdata/ii/II.java";
      Reader reader = new FileReader(fname);
      Term nil=TermWare.getInstance().getTermFactory().createNIL();
      source=TermWare.getInstance().getParserFactory("Java").createParser(reader,fname,nil,TermWare.getInstance()).readTerm();
      String s0=TermHelper.termToString(source);
      //String s0=TermHelper.termToPrettyString(source);
      //System.out.println("!!!!----!!!!!");
      //System.out.println(s0);
      String s = TermHelper.termToPrettyString(source,"Java",TermWare.getInstance().getTermFactory().createNIL());
      //System.out.println("!!!!----!!!!!");
      //System.out.println(s);
      Term source1=TermWare.getInstance().getParserFactory("Java").createParser(new StringReader(s),"s",nil,TermWare.getInstance()).readTerm();
      assertTrue("printed source is not parsed",!source1.isNil());
    }

    public void testAI() throws Exception
    {
      Term source=null;
      String fname = "testdata/ai/Constants.java";
      Reader reader = new FileReader(fname);
      Term nil=TermWare.getInstance().getTermFactory().createNIL();
      source=TermWare.getInstance().getParserFactory("Java").createParser(reader,fname,nil,TermWare.getInstance()).readTerm();
      //String s0=TermHelper.termToString(source);
      String s0=TermHelper.termToPrettyString(source);
      System.out.println("!!!!----!!!!!");
      System.out.println(s0);
      String s = TermHelper.termToPrettyString(source,"Java",TermWare.getInstance().getTermFactory().createNIL());
      System.out.println("!!!!----!!!!!");
      System.out.println(s);
      Term source1=TermWare.getInstance().getParserFactory("Java").createParser(new StringReader(s),"s",nil,TermWare.getInstance()).readTerm();
      assertTrue("printed source is not parsed",!source1.isNil());
    }



    
}
