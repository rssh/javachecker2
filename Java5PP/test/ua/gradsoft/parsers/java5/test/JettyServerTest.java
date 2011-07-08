/*
 * 
 */

package ua.gradsoft.parsers.java5.test;

import java.io.FileReader;
import java.io.Reader;
import junit.framework.TestCase;
import ua.gradsoft.parsers.java5.JavaParserFactory;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWare;

/**
 *Test for parising Server.jave from jetty distribution
 * @author rssh
 */
public class JettyServerTest extends TestCase
{

    protected void setUp()
    {
        TermWare.getInstance().addParserFactory("Java",new JavaParserFactory());
    }


    public void testServer() throws Exception
    {
      Term source=null;
      String fname = "testdata/jetty_6_1_11/Server.java";
      Reader reader = new FileReader(fname);
      Term nil=TermWare.getInstance().getTermFactory().createNIL();
      source=TermWare.getInstance().getParserFactory("Java").createParser(reader,fname,nil,TermWare.getInstance()).readTerm();
      //source.println(System.out);
      reader.close();
      assertTrue("received term is not compilation unit",source.getName().equals("CompilationUnit"));
    }

    public void testIO() throws Exception
    {
      Term source=null;
      String fname = "testdata/jetty_6_1_11/IO.java";
      Reader reader = new FileReader(fname);
      Term nil=TermWare.getInstance().getTermFactory().createNIL();
      source=TermWare.getInstance().getParserFactory("Java").createParser(reader,fname,nil,TermWare.getInstance()).readTerm();
      //source.println(System.out);
      reader.close();
      assertTrue("received term is not compilation unit",source.getName().equals("CompilationUnit"));
    }



}
