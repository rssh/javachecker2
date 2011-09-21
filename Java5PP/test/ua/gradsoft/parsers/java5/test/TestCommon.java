/*
 * TestCommmon
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


public class TestCommon 
{
    
    public static void setUp()
    {                                                  
      if (!javaParserInitialized) {
        TermWare.getInstance().addParserFactory("Java",new JavaParserFactory());
        javaParserInitialized = true;
      }
    }
    private static boolean javaParserInitialized=false;
    
    
    public static Term readJavaSourceFromFile(String fname, boolean debug) throws Exception
    {
      Term source=null;
      Reader reader = new FileReader(fname);
      Term nil=TermWare.getInstance().getTermFactory().createNIL();
      source=TermWare.getInstance().getParserFactory("Java").createParser(reader,fname,nil,TermWare.getInstance()).readTerm();
      if (debug) {
        source.println(System.out);  
      }
      reader.close();
      return source;
    }

}
