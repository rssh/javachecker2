package ua.gradsoft.javachecker;

import java.util.List;

/**
 *Check number of rules for coin-tests
 */
public class CoinTest extends TestCase
{


    protected void setUp() throws Exception
    {
        JavaCheckerFacade.init();
        JavaCheckerFacade.addInputDirectory("testpackages/coin",true);
    }

    public void testX()
    {
        Main main = new Main();
        main.addInputDirectory("testpackages/coin",true);
        main.process();

        List<DefectReportItem> defects = main.getFacts().getDefectReportItems();
    }


}
