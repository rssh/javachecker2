/*
 * MTCollectionsTest.java
 *
 * Created on вівторок, 30, січня 2007, 16:42
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker;

import junit.framework.TestCase;
import ua.gradsoft.javachecker.models.JavaModelConstants;
import ua.gradsoft.javachecker.models.JavaResolver;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;

/**
 *Test model terms
 * @author Ruslan Shevchenko
 */
public class MTCollectionsTest extends TestCase
{
    
        protected void setUp() throws Exception
       {
        JavaCheckerFacade.init();
        JavaCheckerFacade.addInputDirectory("testpackages/testdata7/Collections");
       }

       public void testQueueExceptionModelTerm() throws Exception
       {           
         JavaTypeModel qetm = JavaResolver.resolveTypeModelFromPackage("QueueException",packageName);
         Term mt = qetm.getModelTerm();
         Term bt = mt.getSubtermAt(JavaModelConstants.CLASSORINTERFACE_MODEL_BODY_INDEX);
         Term l = bt.getSubtermAt(0);
         Term ct = null;
         System.out.println(TermHelper.termToString(mt));
         while(!l.isNil()) {
             Term t = l.getSubtermAt(0);
             if (t.getName().equals("ConstructorModel")) {
                    ct=t;
                    break;             
             }
             l=l.getSubtermAt(1);
         }
         assertTrue("Constructor model must be found in "+TermHelper.termToString(mt),ct!=null);                  
       }
    
       private static String packageName="com.heatonresearch.examples.collections";
}
