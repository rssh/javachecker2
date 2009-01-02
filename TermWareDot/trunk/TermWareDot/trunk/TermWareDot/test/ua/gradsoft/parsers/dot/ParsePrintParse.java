
package ua.gradsoft.parsers.dot;

import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import ua.gradsoft.printers.dot.DotPrinterFactory;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWare;

/**
 *
 * @author rssh
 */
public class ParsePrintParse 
{

    @Before public void setUp()
    {
        TermWare.getInstance().addParserFactory("Dot",new DotParserFactory());
        TermWare.getInstance().addPrinterFactory("Dot",new DotPrinterFactory());
    }

    @Test public void testGraphOne1() throws Exception
    {      
        doTestParsePrintParse("testdata/graphOne1.dot",false);
    }
    
    @Test public void testGraphOne2() throws Exception
    {     
        doTestParsePrintParse("testdata/graphOne2.dot",false);
    }

    @Test public void testGraphBusinessRules() throws Exception
    {     
        doTestParsePrintParse("testdata/GraphBusinessRules.dot",false);
    }

    @Test public void testGraphJPE() throws Exception
    {     
        doTestParsePrintParse("testdata/graphJPE.dot",false);
    }
        
    
    @Test public void testGraphN2S() throws Exception
    {     
        doTestParsePrintParse("testdata/graphN2S.dot",false);
    }

    @Test public void testGraphTw() throws Exception
    {     
        doTestParsePrintParse("testdata/graphTw.dot",false);
    }
    
    @Test public void testCluster1() throws Exception
    {     
        doTestParsePrintParse("testdata/cluster1.dot",false);
    }

    @Test 
    public void testCluster2() throws Exception
    {     
        doTestParsePrintParse("testdata/cluster2.dot",false);
    }
    
            
    private void doTestParsePrintParse(String fname, boolean debug) throws Exception
    {
      Term source=null;     
      Reader reader = new FileReader(fname);
      Term nil=TermWare.getInstance().getTermFactory().createNIL();
      source=TermWare.getInstance().getParserFactory("Dot").createParser(reader,fname,nil,TermWare.getInstance()).readTerm();
      String s0=TermHelper.termToPrettyString(source);
      if (debug) {
        System.out.println("!!!!----!!!!!");
        System.out.println(s0);
      }
      String s = TermHelper.termToPrettyString(source,"Dot",TermWare.getInstance().getTermFactory().createNIL());
      if (debug) {
        System.out.println("!!!!----!!!!!");
        System.out.println(s);
      }
      Term source1=TermWare.getInstance().getParserFactory("Dot").createParser(new StringReader(s),"s",nil,TermWare.getInstance()).readTerm();
      assertTrue("printed source is not parsed",!source1.isNil());
    }

    
    
}
