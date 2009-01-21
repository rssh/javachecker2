package ua.gradsoft.parsers.php5;

import java.io.FileReader;
import java.io.Reader;
import org.junit.Before;
import org.junit.Test;
import ua.gradsoft.printers.php5.PhpPrinterFactory;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWare;

/**
 *
 * @author rssh
 */
public class ParsePrintTest
{
    
    @Before
    public void setUp()
    {
        TermWare.getInstance().addParserFactory("Php", new PhpParserFactory());
        TermWare.getInstance().addPrinterFactory("Php", new PhpPrinterFactory());
    }
    
    @Test
    public void testExample1() throws Exception
    {
        String s = doParsePrint("testdata/ta/t1.php",true);
    }

    @Test
    public void testMakeJUnitHappy()
    {

    }

    public static String doParsePrint(String fname, boolean debug) throws Exception
    {
        Term source = null;
        Reader reader = new FileReader(fname);
        Term nil = TermWare.getInstance().getTermFactory().createNil();
        source = TermWare.getInstance().getParserFactory("Php").createParser(reader,fname,nil,TermWare.getInstance()).readTerm();
        String s0 = TermHelper.termToPrettyString(source);
        if (debug) {
            System.out.println("fname:"+fname);
            System.out.println(s0);
        }
        String s1 = TermHelper.termToPrettyString(source,"Php",TermWare.getInstance().getTermFactory().createNIL());
        if (debug){
             System.out.println("in PHP:");
             System.out.println(s1);
        }
        return s1;
    }

}
