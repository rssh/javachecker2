/*
 * MTData6Test.java
 *
 * Created on середа, 31, січня 2007, 14:17
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker;

import java.util.List;
import junit.framework.TestCase;
import ua.gradsoft.javachecker.models.JavaModelConstants;
import ua.gradsoft.javachecker.models.JavaModifiersModel;
import ua.gradsoft.javachecker.models.JavaResolver;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.termware.Term;

/**
 *
 * @author Ruslan Shevchenko
 */
public class MTData6Test extends TestCase
{
    
    
       protected void setUp() throws Exception
       {
        JavaCheckerFacade.init();
        JavaCheckerFacade.addInputDirectory("testpackages/testdata6",true);
       }

       /**
        * This is the main test
        */
       public void testOutliner() throws Exception
       {
           JavaTypeModel tm=JavaResolver.resolveTypeModelByFullClassName("x.Outliner");
           JavaTypeModel tmSuper = tm.getSuperClass();           
           Term mt = tm.getModelTerm();
           Term mtSuper = tmSuper.getModelTerm();
           Term modifiers = mt.getSubtermAt(JavaModelConstants.CLASSORINTERFACE_MODEL_MODIFIERS_INDEX);
           assertTrue("modifiers are public",(JavaModifiersModel.PUBLIC & modifiers.getSubtermAt(0).getInt())!=0);
       }
       
       public void testAttributedIndexed() throws Exception
       {          
           JavaTypeModel tm=JavaResolver.resolveTypeModelByFullClassName("y.AttributedIndexed");
           JavaTypeModel tmSuper = tm.getSuperClass();
           List<JavaTypeModel> tmSuperInterfaces = tm.getSuperInterfaces();
           Term mttm=tm.getModelTerm();
       }

       public void testAttributed() throws Exception
       {
           JavaTypeModel tm=JavaResolver.resolveTypeModelByFullClassName("y.Attributed");
           JavaTypeModel tmSuper = tm.getSuperClass();
           List<JavaTypeModel> tmSuperInterfaces = tm.getSuperInterfaces();
           Term mttm=tm.getModelTerm();           
       }
       
    
       public void testZObject() throws Exception
       {
           JavaTypeModel tm=JavaResolver.resolveTypeModelByFullClassName("z.ZObject");
           Term mttm = tm.getModelTerm();
       }
       
       public void testZP1() throws Exception
       {
           JavaTypeModel tm=JavaResolver.resolveTypeModelByFullClassName("z.ZP1");
           Term mttm = tm.getModelTerm();
       }
       
       public void testZVector() throws Exception
       {
          JavaTypeModel tm=JavaResolver.resolveTypeModelByFullClassName("z.ZVector"); 
          Term mttm = tm.getModelTerm();
       }
       
       public void testZVectorExpr1() throws Exception
       {
           JavaTypeModel tm = JavaResolver.resolveTypeModelByFullClassName("z.ZVector");
           String sexpr = "super.subList(1,2)";
           
       }


       public void testWE() throws Exception
       {
          JavaTypeModel tm=JavaResolver.resolveTypeModelByFullClassName("w.WE"); 
          Term mttm = tm.getModelTerm();
       }

       public void testV() throws Exception
       {
          JavaTypeModel tm=JavaResolver.resolveTypeModelByFullClassName("v.V1"); 
          Term mttm = tm.getModelTerm();
       }

       public void testT() throws Exception
       {
          JavaTypeModel tm=JavaResolver.resolveTypeModelByFullClassName("t.y.B"); 
          Term mttm = tm.getModelTerm();
       }

       public void testR() throws Exception
       {
          JavaTypeModel tm=JavaResolver.resolveTypeModelByFullClassName("r.R");  
          Term mttm = tm.getModelTerm();
       }
       
       public void testS() throws Exception
       {
          JavaTypeModel tm=JavaResolver.resolveTypeModelByFullClassName("s.x.X");  
          Term mttm = tm.getModelTerm();
       }
       
       public void testEn() throws Exception
       {
          JavaTypeModel tm=JavaResolver.resolveTypeModelByFullClassName("en.En");  
          Term mttm = tm.getModelTerm();
       }

       public void testP() throws Exception
       {
          JavaTypeModel tm=JavaResolver.resolveTypeModelByFullClassName("p.P");  
          Term mttm = tm.getModelTerm();
       }
       
       
}
