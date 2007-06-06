/*
 * LocallyUnusedTest.java
 *
 * Created on June 5, 2007, 9:36 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ua.gradsoft.javachecker;

import java.util.List;
import junit.framework.TestCase;
import ua.gradsoft.javachecker.models.JavaResolver;
import ua.gradsoft.javachecker.models.JavaTypeModel;

/**
 *
 * @author rssh
 */
public class LocallyUnusedTest extends TestCase
{
    
    public void  testUnusedInX() throws Exception
    {
        Main main = new Main();
        String[] args=new String[0];   
        main.init(args);
        main.addInputDirectory("testpackages/testdata11/e1",true); 
        
        
        //TermSystem system0 = TermWare.getInstance().getRoot().resolveSystem("TrackOpenClose");
        //system0.setDebugEntity("SystemReductions");
        //system0.setDebugMode(true);

        //system0.getFacts().setDebugEntity("All");
        //system0.getFacts().setDebugMode(true);
        
        
        //main.setDump(true);
        main.process();   
              
        List<DefectReportItem> defects = main.getFacts().getDefectReportItems();
      
        int nUnusedFound=0;
        for(DefectReportItem defect: defects) {
            String description=defect.getDescription();         
            //System.out.println("!!!"+description);
            if (description.matches(".*unused.*")) {
                ++nUnusedFound;
            }
        }
        assertTrue("'unused' in defects must be 1. have "+nUnusedFound,nUnusedFound==1);                
    }   
    
    public void  testUusedInY() throws Exception
    {
        Main main = new Main();
        String[] args=new String[0];   
        main.init(args);
        main.addInputDirectory("testpackages/testdata11/e2",true); 
        
        //TermSystem system0 = TermWare.getInstance().getRoot().resolveSystem("TrackOpenClose");
        //system0.setDebugEntity("SystemReductions");
        //system0.setDebugMode(true);

        //system0.getFacts().setDebugEntity("All");
        //system0.getFacts().setDebugMode(true);
        
        
        //main.setDump(true);
        main.process();   
              
        List<DefectReportItem> defects = main.getFacts().getDefectReportItems();
      
        int nUnusedFound=0;
        for(DefectReportItem defect: defects) {
            String description=defect.getDescription();         
            //System.out.println("!!!"+description);
            if (description.matches(".*unused.*")) {
                ++nUnusedFound;
            }
        }
        assertTrue("'unused' in defects must be 0. have "+nUnusedFound,nUnusedFound==0);                
    }   
 
 
}
