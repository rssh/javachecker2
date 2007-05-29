/*
 * CloseableTrackingTest.java
 *
 */

package ua.gradsoft.javachecker;

import java.util.List;
import junit.framework.TestCase;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.transformers.general.EqTransformer;

/**
 *Test closeable tracking.
 * @author RSSH
 */
public class CloseableTrackingTest extends TestCase
{
    
    public void testForgottenFile1() throws Exception
    {
        Main main = new Main();
        String[] args=new String[0];   
        main.init(args);
        main.addInputDirectory("testpackages/testdata8/e1", true); 

      //  TermSystem system0 = TermWare.getInstance().getRoot().resolveSystem("TrackOpenClose");
      //  system0.setDebugEntity("SystemReductions");
      //  system0.setDebugMode(true);
        
        
      //  TermSystem system = TermWare.getInstance().getRoot().resolveSystem("ResourceLeak");
      //  system.setDebugEntity("All");
      //  system.setDebugMode(true);

      //  TermSystem listSystem = TermWare.getInstance().getRoot().resolveSystem("List");
      //  listSystem.setDebugEntity("All");
      //  listSystem.setDebugMode(true);
        
     //   TermSystem generalSystem = TermWare.getInstance().getRoot().resolveSystem("general");
     //   generalSystem.setDebugEntity("All");
     //   generalSystem.setDebugMode(true);
        
        
        
        //main.setDump(true);
        main.process();   
      
        
        List<DefectReportItem> defects = main.getFacts().getDefectReportItems();
      
        int nClosedFound=0;
        for(DefectReportItem defect: defects) {
            String description=defect.getDescription();         
            //System.out.println("!!!"+description);
            if (description.matches(".*closed.*")) {
                ++nClosedFound;
            }
        }
        assertTrue("'closed' in defects must be > 0. have "+nClosedFound,nClosedFound>0);        
                
    }
    
    public void testForgottenFile2() throws Exception
    {
        Main main = new Main();
        String[] args=new String[0];   
        main.init(args);
        main.addInputDirectory("testpackages/testdata8/e2",true); 
        
        //main.setDump(true);
        main.process();   
              
        List<DefectReportItem> defects = main.getFacts().getDefectReportItems();
      
        int nClosedFound=0;
        for(DefectReportItem defect: defects) {
            String description=defect.getDescription();         
            //System.out.println("!!!"+description);
            if (description.matches(".*closed.*")) {
                ++nClosedFound;
            }
        }
        assertTrue("'closed' in defects must be > 0. have "+nClosedFound,nClosedFound>0);        
                
    }

    public void testClosedFile3() throws Exception
    {
        Main main = new Main();
        String[] args=new String[0];   
        main.init(args);
        main.addInputDirectory("testpackages/testdata8/e3",true); 

        //TermSystem system0 = TermWare.getInstance().getRoot().resolveSystem("TrackOpenClose");
        //system0.setDebugEntity("SystemReductions");
        //system0.setDebugMode(true);

        //system0.getFacts().setDebugEntity("All");
        //system0.getFacts().setDebugMode(true);
        
        
        //main.setDump(true);
        main.process();   
              
        List<DefectReportItem> defects = main.getFacts().getDefectReportItems();
      
        int nClosedFound=0;
        for(DefectReportItem defect: defects) {
            String description=defect.getDescription();         
            //System.out.println("!!!"+description);
            if (description.matches(".*closed.*")) {
                ++nClosedFound;
            }
        }
        assertTrue("'closed' in defects must be == 0. have "+nClosedFound,nClosedFound==0);        
                
    }

    public void testUnclosedFile4() throws Exception
    {
        Main main = new Main();
        String[] args=new String[0];   
        main.init(args);
        main.addInputDirectory("testpackages/testdata8/e4",true); 

        
        //main.setDump(true);
        main.process();   
              
        List<DefectReportItem> defects = main.getFacts().getDefectReportItems();
      
        int nClosedFound=0;
        for(DefectReportItem defect: defects) {
            String description=defect.getDescription();         
            //System.out.println("!!!"+description);
            if (description.matches(".*closed.*")) {
                ++nClosedFound;
            }
        }
        assertTrue("'closed' in defects must be > 0. have "+nClosedFound,nClosedFound>0);        
                
    }
    
    public void  testNonCloseableStringWriter() throws Exception
    {
        Main main = new Main();
        String[] args=new String[0];   
        main.init(args);
        main.addInputDirectory("testpackages/testdata8/e5",true); 
        
        //TermSystem system0 = TermWare.getInstance().getRoot().resolveSystem("TrackOpenClose");
        //system0.setDebugEntity("SystemReductions");
        //system0.setDebugMode(true);

        //system0.getFacts().setDebugEntity("All");
        //system0.getFacts().setDebugMode(true);
        
        
        //main.setDump(true);
        main.process();   
              
        List<DefectReportItem> defects = main.getFacts().getDefectReportItems();
      
        int nClosedFound=0;
        for(DefectReportItem defect: defects) {
            String description=defect.getDescription();         
            //System.out.println("!!!"+description);
            if (description.matches(".*closed.*")) {
                ++nClosedFound;
            }
        }
        assertTrue("'closed' in defects must be 0. have "+nClosedFound,nClosedFound==0);                
    }

    public void  testCorrecltyClosedPrintWriter() throws Exception
    {
        Main main = new Main();
        String[] args=new String[0];   
        main.init(args);
        main.addInputDirectory("testpackages/testdata8/e6",true); 
        
        //TermSystem system0 = TermWare.getInstance().getRoot().resolveSystem("TrackOpenClose");
        //system0.setDebugEntity("SystemReductions");
        //system0.setDebugMode(true);

        //system0.getFacts().setDebugEntity("All");
        //system0.getFacts().setDebugMode(true);
        
        
        main.setDump(true);
        main.process();   
              
        List<DefectReportItem> defects = main.getFacts().getDefectReportItems();
      
        int nClosedFound=0;
        for(DefectReportItem defect: defects) {
            String description=defect.getDescription();         
            //System.out.println("!!!"+description);
            if (description.matches(".*closed.*")) {
                ++nClosedFound;
            }
        }
        assertTrue("'closed' in defects must be 0. have "+nClosedFound,nClosedFound==0);                
    }

    public void  testIncorrecltyUnclosedPrintWriter() throws Exception
    {
        Main main = new Main();
        String[] args=new String[0];   
        main.init(args);
        main.addInputDirectory("testpackages/testdata8/e7",true); 
        
        //TermSystem system0 = TermWare.getInstance().getRoot().resolveSystem("TrackOpenClose");
        //system0.setDebugEntity("SystemReductions");
        //system0.setDebugMode(true);

        //system0.getFacts().setDebugEntity("All");
        //system0.getFacts().setDebugMode(true);
        
        
        //main.setDump(true);
        main.process();   
              
        List<DefectReportItem> defects = main.getFacts().getDefectReportItems();
      
        int nClosedFound=0;
        for(DefectReportItem defect: defects) {
            String description=defect.getDescription();         
            //System.out.println("!!!"+description);
            if (description.matches(".*closed.*")) {
                ++nClosedFound;
            }
        }
        assertTrue("'closed' in defects must be 1. have "+nClosedFound,nClosedFound==1);                
    }
    
    public void  testCorrecltyUnclosedPrintWriter() throws Exception
    {
        Main main = new Main();
        String[] args=new String[0];   
        main.init(args);
        main.addInputDirectory("testpackages/testdata8/e8",true); 
        
        //TermSystem system0 = TermWare.getInstance().getRoot().resolveSystem("TrackOpenClose");
        //system0.setDebugEntity("SystemReductions");
        //system0.setDebugMode(true);

        //system0.getFacts().setDebugEntity("All");
        //system0.getFacts().setDebugMode(true);
        
        
        main.setDump(true);
        main.process();   
              
        List<DefectReportItem> defects = main.getFacts().getDefectReportItems();
      
        int nClosedFound=0;
        for(DefectReportItem defect: defects) {
            String description=defect.getDescription();         
            //System.out.println("!!!"x+description);
            if (description.matches(".*closed.*")) {
                ++nClosedFound;
            }
        }
        assertTrue("'closed' in defects must be 0. have "+nClosedFound,nClosedFound==0);                
    }

    public void  testCorrecltyClosedOOStream() throws Exception
    {
        Main main = new Main();
        String[] args=new String[0];   
        main.init(args);
        main.addInputDirectory("testpackages/testdata8/e9",true); 
        
        //TermSystem system0 = TermWare.getInstance().getRoot().resolveSystem("TrackOpenClose");
        //system0.setDebugEntity("SystemReductions");
        //system0.setDebugMode(true);

        //system0.getFacts().setDebugEntity("All");
        //system0.getFacts().setDebugMode(true);
        
        
        //main.setDump(true);
        main.process();   
              
        List<DefectReportItem> defects = main.getFacts().getDefectReportItems();
      
        int nClosedFound=0;
        for(DefectReportItem defect: defects) {
            String description=defect.getDescription();         
            //System.out.println("!!!"+description);
            if (description.matches(".*closed.*")) {
                ++nClosedFound;
            }
        }
        assertTrue("'closed' in defects must be 0. have "+nClosedFound,nClosedFound==0);                
    }
    
    
}
