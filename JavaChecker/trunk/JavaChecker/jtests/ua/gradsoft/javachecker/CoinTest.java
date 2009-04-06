package ua.gradsoft.javachecker;

import java.util.Collections;
import java.util.List;
import junit.framework.TestCase;

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

    public void testLoopWithRemove() throws Exception
    {
        Main main = new Main();

       // set options.
        main.setExplicitEnabledOnly(true);
        main.setExplicitEnabled(Collections.singleton("LoopWithRemove"));
        main.setDump(true);
        Main.setShowFiles(true);


        main.init(new String[0]);
        Main.addInputDirectory("testpackages/coin/forwithremove",true);

        Main.getFacts().clearDefectReportItems();

        main.process();

        List<DefectReportItem> defects = Main.getFacts().getDefectReportItems();

        assertTrue(defects.size()>0);
        System.err.println("defects.size()="+defects.size());

        String category = defects.get(0).getCategory();
        assertEquals("coin",category);

    }



}
