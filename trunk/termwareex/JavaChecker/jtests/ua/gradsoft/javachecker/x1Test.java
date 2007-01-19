/*
 * x1Test.java
 *
 * Copyright (c) 2004-2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker;

import junit.framework.TestCase;
import java.util.List;
import ua.gradsoft.javachecker.models.*;


/**
 *
 * @author Ruslan Shevchenko
 */
public class x1Test extends TestCase
{
    
    public void  testX1() throws Exception
    {
        Main main = new Main();
        String[] args=new String[0];        
        main.init(args);
        main.addInputDirectory("testpackages/testdata1");        
        main.setDump(false);
        main.setNoClean(false);        
        main.process(args);        
        
        JavaPackageModel packageModel = JavaResolver.resolvePackage("ua.gradsoft.javachecker.x1");        
        assertTrue("must exists package model",packageModel!=null);
        
        JavaTypeModel x1ClassModel = JavaResolver.resolveTypeModelFromPackage("X1","ua.gradsoft.javachecker.x1");
        assertTrue("must exists type model",x1ClassModel!=null);
        
        assertTrue(" type model for X1 must be class",x1ClassModel.isClass());
                       
        List<JavaMethodModel> qqqModels = x1ClassModel.findMethodModels("qqq");
        assertTrue("must exists qqq method models", qqqModels!=null);
        
    }
    
    public void  testFl() throws Exception
    {
        Main main = new Main();
        String[] args=new String[0];   
        main.init(args);
        main.addInputDirectory("testpackages/testdata1");        
        //main.setDump(true);
        //main.setNoClean(true);
        main.process(args);        
        
//        for(String s: main.getFacts().getPackagesStore().getLoadedPackageModels().keySet()) {
//           System.out.println("::"+s); 
//        }
       
        
        JavaPackageModel packageModel=main.getFacts().getPackagesStore().getLoadedPackageModels().get("ua.gradsoft.javachecker.fl");
        assertTrue("must exists package model",packageModel!=null);
        
        JavaTypeModel fileAndLineClassModel = packageModel.findTypeModel("FL");
        assertTrue("must exists type model",fileAndLineClassModel!=null);
        
        List<JavaMethodModel> getLineModels = fileAndLineClassModel.findMethodModels("getLine");
        assertTrue("must exists getLine method models", getLineModels!=null);
        
    }
    
    public void testS1() throws Exception
    {
        Main main = new Main();
        String[] args=new String[0];   
        main.init(args);
        main.addInputDirectory("testpackages/style/s1");        
        //main.setDump(true);
        //main.setNoClean(true);
        main.process(args);   
        
        List<DefectReportItem> defects = main.getFacts().getDefectReportItems();
        assertTrue("defects was not found",defects.size()!=0);
        
        boolean classFound=false;
        for(DefectReportItem defect: defects) {
            String description=defect.getDescription();
            if (description.matches(".*class.*")) {
                classFound=true;
            }
        }
        assertTrue("worl class must be in defect",classFound);
    }
    
    public void testS2() throws Exception
    {
        Main main = new Main();
        String[] args=new String[0];   
        main.init(args);
        main.addInputDirectory("testpackages/style/s2"); 
        //main.setDump(true);
        main.process(args);   
        
        List<DefectReportItem> defects = main.getFacts().getDefectReportItems();

        int nVariablesFound=0;
        int nMethodsFound=0;
        for(DefectReportItem defect: defects) {
            String description=defect.getDescription();         
            //System.out.println("!!!"+description);
            if (description.matches(".*variable.*")) {
                ++nVariablesFound;
            }else if(description.matches(".*method.*")) {
                ++nMethodsFound;
            }
        }
        assertTrue("variables in defects must be > 2. have "+nVariablesFound,nVariablesFound>2);
        assertTrue("methods in defects must be 2. have "+nMethodsFound,nMethodsFound==2);
                
    }
    
    public void testS3() throws Exception
    {
        Main main = new Main();
        String[] args=new String[0];   
        main.init(args);
        main.addInputDirectory("testpackages/style/s3"); 
        //main.setDump(true);
        main.process(args);   
        
        List<DefectReportItem> defects = main.getFacts().getDefectReportItems();
      
        int nEnumsFound=0;
        for(DefectReportItem defect: defects) {
            String description=defect.getDescription();         
            //System.out.println("!!!"+description);
            if (description.matches(".*enum.*")) {
                ++nEnumsFound;
            }
        }
        assertTrue("enums in defects must be > 1. have "+nEnumsFound,nEnumsFound>1);        
                
    }
    
    public void testS4() throws Exception
    {
        Main main = new Main();
        String[] args=new String[0];   
        main.init(args);
        main.addInputDirectory("testpackages/style/s4"); 
        main.setDump(true);
        main.process(args);   
        
        List<DefectReportItem> defects = main.getFacts().getDefectReportItems();
      
        int nTypeArgumentsFound=0;
        for(DefectReportItem defect: defects) {
            String description=defect.getDescription();         
            //System.out.println("!!!"+description);
            if (description.matches(".*type argu.*")) {
                ++nTypeArgumentsFound;
            }
        }
        assertTrue("type arguments in defects must be > 0. have "+nTypeArgumentsFound,nTypeArgumentsFound>0);        
                
    }
    
}
