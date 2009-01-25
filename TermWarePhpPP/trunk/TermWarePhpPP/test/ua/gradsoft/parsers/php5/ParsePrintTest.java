package ua.gradsoft.parsers.php5;

import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import org.junit.Assert;
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
        doParsePrintParse("testdata/ta/t1.php",false);
    }

    @Test
    public void testExample2() throws Exception
    {
        doParsePrintParse("testdata/ta/cl1.php",false);
    }

    @Test
    public void testExample3() throws Exception
    {
        doParsePrintParse("testdata/ta/language.oop5.iterations.1.php",false);
    }

    @Test
    public void testExample4() throws Exception
    {
        doParsePrintParse("testdata/ta/language.control-structures.foreach.1.php",false);
    }

    @Test
    public void testExample5() throws Exception
    {
        doParsePrintParse("testdata/ta/language.control-structures.while.php",false);
        //String s = doParsePrint("testdata/ta/language.control-structures.while.php",true);
    }

    @Test
    public void testExample6() throws Exception
    {
        doParsePrintParse("testdata/ta/language.control-structures.switch.php",false);
        //String s = doParsePrint("testdata/ta/language.control-structures.switch.php",true);
    }

    @Test
    public void testArrays1() throws Exception
    {
        doParsePrintParse("testdata/ta/language.types.arrays.1.php",false);
        //String s = doParsePrint("testdata/ta/language.types.arrays.1.php",true);
    }
    
    @Test
    public void testTypeJuggling1() throws Exception
    {
        doParsePrintParse("testdata/ta/language.types.type-juggling.1.php",false);
        //String s = doParsePrint("testdata/ta/language.types.type-juggling.1.php",true);
    }

    @Test
    public void testTypeBoolean1() throws Exception
    {
        doParsePrintParse("testdata/ta/language.types.boolean.php",false);
        //String s = doParsePrint("testdata/ta/language.types.boolean.php",true);
    }

    @Test
    public void testScope1() throws Exception
    {
        doParsePrintParse("testdata/ta/language.variables.scope.1.php",false);
        //String s = doParsePrint("testdata/ta/language.variables.scope.1.php",true);
    }

    @Test
    public void testScope2() throws Exception
    {
        doParsePrintParse("testdata/ta/language.variables.scope.2.php",false);
        //String s = doParsePrint("testdata/ta/language.variables.scope.2.php",true);
    }

    @Test
    public void testScope3() throws Exception
    {
        doParsePrintParse("testdata/ta/language.variables.scope.3.php",false);
        //String s = doParsePrint("testdata/ta/language.variables.scope.3.php",true);
    }

    @Test
    public void testScope5() throws Exception
    {
        doParsePrintParse("testdata/ta/language.variables.scope.5.php",false);
        //String s = doParsePrint("testdata/ta/language.variables.scope.5.php",true);
    }

    @Test
    public void testConstants1() throws Exception
    {
        doParsePrintParse("testdata/ta/language.constants.1.php",false);
        //String s = doParsePrint("testdata/ta/language.constants.1.php",true);
    }

    @Test
    public void testConstants2() throws Exception
    {
        doParsePrintParse("testdata/ta/language.constants.2.php",false);
        //String s = doParsePrint("testdata/ta/language.constants.2.php",true);
    }

    @Test
    public void testOperatorsErrorControl1() throws Exception
    {
        doParsePrintParse("testdata/ta/language.operators.errorcontrol.php",false);
        //String s = doParsePrint("testdata/ta/language.operators.errorcontrol.php",true);
    }

    @Test
    public void testGoto1() throws Exception
    {
        doParsePrintParse("testdata/ta/control-structures.goto.php",false);
        //String s = doParsePrint("testdata/ta/control-structures.goto.php",true);
    }

    @Test
    public void testExceptions1() throws Exception
    {
        doParsePrintParse("testdata/ta/language.exceptions.1.php",false);
        //String s = doParsePrint("testdata/ta/language.exceptions.1.php",true);
    }

    @Test
    public void testCloning1() throws Exception
    {
        doParsePrintParse("testdata/ta/language.oop5.cloning.1.php",false);
        //String s = doParsePrint("testdata/ta/language.oop5.cloning.1.php",true);
    }

    @Test
    public void testFinal1() throws Exception
    {
        doParsePrintParse("testdata/ta/language.oop5.final.php",false);
        //String s = doParsePrint("testdata/ta/language.oop5.final.php",true);
    }

    @Test
    public void testAbstract1() throws Exception
    {
        doParsePrintParse("testdata/ta/language.oop5.abstract.php",false);
        //String s = doParsePrint("testdata/ta/language.oop5.abstract.php",true);
    }

    @Test
    public void testInterfaces1() throws Exception
    {
        doParsePrintParse("testdata/ta/language.oop5.interfaces.1.php",false);
        //String s = doParsePrint("testdata/ta/language.oop5.interfaces.1.php",true);
    }

    @Test
    public void testInterfaces4() throws Exception
    {
        //doParsePrintParse("testdata/ta/language.oop5.interfaces.4.php",false);
        String s = doParsePrint("testdata/ta/language.oop5.interfaces.4.php",true);
    }


    @Test
    public void testMakeJUnitHappy()
    {

    }

    public static void doParsePrintParse(String fname, boolean debug) throws Exception
    {
        String s = doParsePrint(fname,debug);
        Term nil = TermWare.getInstance().getTermFactory().createNil();
        Reader reader = new StringReader(s);
        Term source = TermWare.getInstance().getParserFactory("Php").createParser(reader,fname,nil,TermWare.getInstance()).readTerm();
        Assert.assertTrue(source!=null);
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
