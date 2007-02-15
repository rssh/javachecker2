/*
 * t2Test.java
 *
 * Created on середа, 20, грудня 2006, 4:55
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker;

import junit.framework.TestCase;
import ua.gradsoft.javachecker.models.JavaPackageModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;

/**
 *
 * @author Ruslan Shevchenko
 */
public class t2Test extends TestCase
{
    
    public void  testT2() throws Exception
    {
        Main main = new Main();
        String[] args=new String[0];   
        main.init(args);
        main.addInputDirectory("testpackages/testdata2");        
        //main.setDump(true);
        //main.setNoClean(true);
        main.process(args);        
        
//        for(String s: main.getFacts().getPackagesStore().getLoadedPackageModels().keySet()) {
//           System.out.println("::"+s); 
//        }
       
        
         JavaPackageModel packageModel=main.getFacts().getPackagesStore().getLoadedPackageModels().get("ua.gradsoft.javachecker.test");
//        assertTrue("must exists package model",packageModel!=null);
        
        JavaTypeModel classModel = packageModel.findTypeModel("T2");
         assertTrue("must exists type model",classModel!=null);
        
//        List<JavaMethodAbstractModel> getLineModels = fileAndLineClassModel.findMethodModels("getLine");
//        assertTrue("must exists getLine method models", getLineModels!=null);
        
    }

    
    
}
