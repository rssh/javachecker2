package ua.gradsoft.javachecker;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import junit.framework.TestCase;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWare;

/**
 *Check number of rules for coin-tests
 */
public class CoinTest extends TestCase
{



    public void testStringsInSwitch() throws Exception
    {
        Main main = new Main();       

       // set options.
        main.setExplicitEnabledOnly(true);
        main.setExplicitEnabled(Collections.singleton("StringInSwitch"));
        main.setDump(false);

        main.init(new String[0]);

        main.addInputDirectory("testpackages/coin/stringswitch",true);
        
        

        main.process();

        List<DefectReportItem> defects = main.getFacts().getDefectReportItems();

        assertTrue(defects.size()>0);

        String category = defects.get(0).getCategory();
        assertEquals("coin",category);


    }

    public void testInstanceOfSwitchNo() throws Exception
    {
        Main main = new Main();

       // set options.
        main.setExplicitEnabledOnly(true);
        main.setExplicitEnabled(Collections.singleton("InstanceOfSwitch"));
        main.setDump(false);
        main.setShowFiles(true);

        main.init(new String[0]);
        main.addInputDirectory("testpackages/coin/stringswitch",true);

        main.process();

        List<DefectReportItem> defects = main.getFacts().getDefectReportItems();

        assertTrue(defects.size()==0);


    }

    public void testInstanceOfSwitch() throws Exception
    {
        Main main = new Main();

       // set options.
        main.setExplicitEnabledOnly(true);
        main.setExplicitEnabled(Collections.singleton("InstanceOfSwitch"));
        main.setDump(false);
        main.setShowFiles(true);

        main.init(new String[0]);
        main.addInputDirectory("testpackages/coin/instanceofswitch",true);

        main.process();

        List<DefectReportItem> defects = main.getFacts().getDefectReportItems();

        assertTrue(defects.size()>0);

        String category = defects.get(0).getCategory();
        assertEquals("coin",category);

    }


    public void testByteLiterals() throws Exception
    {
        Main main = new Main();

       // set options.
        main.setExplicitEnabledOnly(true);
        main.setExplicitEnabled(Collections.singleton("ByteLiteral"));
        main.setDump(false);
        Main.setShowFiles(true);

        main.init(new String[0]);
        Main.addInputDirectory("testpackages/coin/byteliteral",true);

        main.process();

        List<DefectReportItem> defects = Main.getFacts().getDefectReportItems();

        assertTrue(defects.size()>0);
        System.err.println("defects.size()="+defects.size());

        String category = defects.get(0).getCategory();
        assertEquals("coin",category);

    }

    public void testBigIntegerLiterals() throws Exception
    {
        Main main = new Main();

       // set options.
        main.setExplicitEnabledOnly(true);
        main.setExplicitEnabled(Collections.singleton("BigIntegerLiteral"));
        main.setDump(false);
        Main.setShowFiles(true);

        main.init(new String[0]);
        Main.addInputDirectory("testpackages/coin/bigintegerliteral",true);

        main.process();

        List<DefectReportItem> defects = Main.getFacts().getDefectReportItems();

        assertTrue(defects.size()>0);
        System.err.println("defects.size()="+defects.size());

        String category = defects.get(0).getCategory();
        assertEquals("coin",category);

    }



    public void testMultiCatch() throws Exception
    {
        Main main = new Main();

       // set options.
        main.setExplicitEnabledOnly(true);
        main.setExplicitEnabled(Collections.singleton("MultiCatch"));
        main.setDump(false);
        Main.setShowFiles(true);

        main.init(new String[0]);
        Main.addInputDirectory("testpackages/coin/multicath",true);

        Main.getFacts().clearDefectReportItems();

        main.process();

        List<DefectReportItem> defects = Main.getFacts().getDefectReportItems();

        assertTrue(defects.size()>0);
        System.err.println("defects.size()="+defects.size());

        String category = defects.get(0).getCategory();
        assertEquals("coin",category);

    }

    /*
    public void testElvis() throws Exception
    {
        Main main = new Main();

       // set options.
        main.setExplicitEnabledOnly(true);
        main.setExplicitEnabled(Collections.singleton("Elvis"));
        main.setDump(false);
        Main.setShowFiles(true);


        main.init(new String[0]);
        Main.addInputDirectory("testpackages/coin/elvis",true);

        Main.getFacts().clearDefectReportItems();

        main.process();

        List<DefectReportItem> defects = Main.getFacts().getDefectReportItems();

        assertTrue(defects.size()>0);
        System.err.println("defects.size()="+defects.size());

        String category = defects.get(0).getCategory();
        assertEquals("coin",category);

    }
     */

    public void testElvis1() throws Exception
    {
        Main main = new Main();

       // set options.
        main.setExplicitEnabledOnly(true);
        Set<String> checks = new TreeSet<String>();
        checks.addAll(Arrays.asList("Elvis1","NullSafe"));
        main.setExplicitEnabled(checks);
        main.setDump(false);
        Main.setShowFiles(true);


        main.init(new String[0]);
        Main.addInputDirectory("testpackages/coin/elvis",true);

        Main.getFacts().clearDefectReportItems();

        main.process();

        List<DefectReportItem> defects = Main.getFacts().getDefectReportItems();

        assertTrue(defects.size()>3);
        System.err.println("defects.size()="+defects.size());


    }


    public void testLoopWithRemove() throws Exception
    {
        Main main = new Main();

       // set options.
        main.setExplicitEnabledOnly(true);
        main.setExplicitEnabled(Collections.singleton("LoopWithRemove"));
        main.setDump(false);
        Main.setShowFiles(true);


        main.init(new String[0]);
        Main.addInputDirectory("testpackages/coin/forwithremove",true);

        Main.getFacts().clearDefectReportItems();

        main.process();

        List<DefectReportItem> defects = Main.getFacts().getDefectReportItems();

        assertTrue(defects.size()==2);
        System.err.println("defects.size()="+defects.size());


    }

    public void testLoopWithIndex() throws Exception
    {
        Main main = new Main();

       // set options.
        main.setExplicitEnabledOnly(true);
        main.setExplicitEnabled(Collections.singleton("ForAroundSizeOrLength"));
        main.setDump(false);
        Main.setShowFiles(true);


        main.init(new String[0]);
        Main.addInputDirectory("testpackages/coin/forindex",true);

        Main.getFacts().clearDefectReportItems();

        main.process();

        List<DefectReportItem> defects = Main.getFacts().getDefectReportItems();

        assertTrue(defects.size()>0);
        System.err.println("defects.size()="+defects.size());


    }


    public void testCatchInFinally() throws Exception
    {
        Main main = new Main();

       // set options.
        main.setExplicitEnabledOnly(true);
        main.setExplicitEnabled(Collections.singleton("CatchInFinally"));
        main.setDump(false);
        Main.setShowFiles(true);
        //TermSystem ts =TermWare.getInstance().resolveSystem("FindSubterm");
        //ts.setLoggedEntity("All");
        //ts.setLoggingMode(true);

        main.init(new String[0]);
        Main.addInputDirectory("testpackages/coin/catchinfinally",true);

        Main.getFacts().clearDefectReportItems();

        main.process();

        List<DefectReportItem> defects = Main.getFacts().getDefectReportItems();

        assertTrue(defects.size()>0);
        System.err.println("defects.size()="+defects.size());

        String category = defects.get(0).getCategory();
        assertEquals("coin",category);

    }

    public void testRethrow() throws Exception
    {
        Main main = new Main();

       // set options.
        main.setExplicitEnabledOnly(true);
        main.setExplicitEnabled(Collections.singleton("RethrowClause"));
        main.setDump(false);
        Main.setShowFiles(true);
        //TermSystem ts =TermWare.getInstance().resolveSystem("FindSubterm");
        //ts.setLoggedEntity("All");
        //ts.setLoggingMode(true);

        main.init(new String[0]);
        Main.addInputDirectory("testpackages/coin/rethrow",true);

        Main.getFacts().clearDefectReportItems();

        main.process();

        List<DefectReportItem> defects = Main.getFacts().getDefectReportItems();

        assertTrue(defects.size()>0);
        System.err.println("defects.size()="+defects.size());


    }

    public void testWideningOperator() throws Exception
    {
        Main main = new Main();

       // set options.
        main.setExplicitEnabledOnly(true);

        Set<String> enabled = new TreeSet<String>();
        enabled.addAll(Arrays.asList("WideningSemantics","WideningSyntax"));
        main.setExplicitEnabled(enabled);
        main.setDump(false);
        Main.setShowFiles(true);
        //TermSystem ts =TermWare.getInstance().resolveSystem("FindSubterm");
        //ts.setLoggedEntity("All");
        //ts.setLoggingMode(true);

        main.init(new String[0]);
        Main.addInputDirectory("testpackages/coin/wideningoperator",true);

        Main.getFacts().clearDefectReportItems();

        main.process();

        List<DefectReportItem> defects = Main.getFacts().getDefectReportItems();

        assertTrue(defects.size()>0);
        System.err.println("defects.size()="+defects.size());

    }

    public void testConvertableSizeLoop() throws Exception
    {
        Main main = new Main();

       // set options.
        main.setExplicitEnabledOnly(true);

        Set<String> enabled = new TreeSet<String>();
        enabled.addAll(Arrays.asList("ForAroundSizeConvertable","ForAroundLengthConvertable"));
        main.setExplicitEnabled(enabled);
        main.setDump(false);
        Main.setShowFiles(true);
        TermSystem ts =TermWare.getInstance().resolveSystem("FindSubterm");
        //ts.setLoggedEntity("All");
        //ts.setLoggingMode(true);

        main.init(new String[0]);
        Main.addInputDirectory("testpackages/coin/convertableloops",true);

        Main.getFacts().clearDefectReportItems();

        main.process();

        List<DefectReportItem> defects = Main.getFacts().getDefectReportItems();

        assertTrue(defects.size()>0);
        System.err.println("defects.size()="+defects.size());

    }

    public void testStringBuilderOrBufferAppend() throws Exception
    {
        Main main = new Main();

       // set options.
        main.setExplicitEnabledOnly(true);

        Set<String> enabled = new TreeSet<String>();
        enabled.addAll(Arrays.asList("StringBuilderOrBufferAppend"));
        main.setExplicitEnabled(enabled);
        main.setDump(false);
        Main.setShowFiles(true);
        //TermSystem ts =TermWare.getInstance().resolveSystem("FindSubterm");
        //ts.setLoggedEntity("All");
        //ts.setLoggingMode(true);

        main.init(new String[0]);
        Main.addInputDirectory("testpackages/coin/sbappend",true);

        Main.getFacts().clearDefectReportItems();

        main.process();

        List<DefectReportItem> defects = Main.getFacts().getDefectReportItems();

        assertTrue(defects.size()>1);
        System.err.println("defects.size()="+defects.size());

    }

    public void testAutoOrDiamond() throws Exception
    {
        Main main = new Main();

       // set options.
        main.setExplicitEnabledOnly(true);

        Set<String> enabled = new TreeSet<String>();
        enabled.addAll(Arrays.asList("AllVariableInitializers",
                                "AutoInitializers","DiamondInitializers"));
        main.setExplicitEnabled(enabled);
        main.setDump(true);
        Main.setShowFiles(true);
        //TermSystem ts =TermWare.getInstance().resolveSystem("FindSubterm");
        //ts.setLoggedEntity("All");
        //ts.setLoggingMode(true);

        main.init(new String[0]);
        Main.addInputDirectory("testpackages/coin/auto",true);

        Main.getFacts().clearDefectReportItems();

        main.process();

        List<DefectReportItem> defects = Main.getFacts().getDefectReportItems();

        assertTrue(defects.size()>1);
        System.err.println("defects.size()="+defects.size());

    }

    public void testForEach() throws Exception
    {
        Main main = new Main();

       // set options.
        main.setExplicitEnabledOnly(true);

        Set<String> enabled = new TreeSet<String>();
        enabled.addAll(Arrays.asList("AllForEachLoops"));
        main.setExplicitEnabled(enabled);
        main.setDump(false);
        Main.setShowFiles(true);
        //TermSystem ts =TermWare.getInstance().resolveSystem("FindSubterm");
        //ts.setLoggedEntity("All");
        //ts.setLoggingMode(true);

        main.init(new String[0]);
        Main.addInputDirectory("testpackages/coin/foreach",true);

        Main.getFacts().clearDefectReportItems();

        main.process();

        List<DefectReportItem> defects = Main.getFacts().getDefectReportItems();

        assertTrue(defects.size()>1);
        System.err.println("defects.size()="+defects.size());

    }

    public void testCLI() throws Exception
    {
        Main main = new Main();


       // set options.
        main.setExplicitEnabledOnly(true);

        Set<String> enabled = new TreeSet<String>();
        enabled.add("CollectionLiterals");
        main.setExplicitEnabled(enabled);
        main.setDump(true);
        Main.setShowFiles(true);

        main.init(new String[0]);

        TermSystem ts =TermWare.getInstance().resolveSystem("CollectionLiterals");
       // ts.setLoggedEntity("All");
       // ts.setLoggingMode(true);

        Main.addInputDirectory("testpackages/coin/cli",true);

        Main.getFacts().clearDefectReportItems();


        main.process();

        List<DefectReportItem> defects = Main.getFacts().getDefectReportItems();

        assertTrue(defects.size()>1);
        System.err.println("defects.size()="+defects.size());

    }



}
