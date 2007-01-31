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
         //System.out.println(TermHelper.termToString(mt));
         while(!l.isNil()) {
             Term t = l.getSubtermAt(0);
             if (t.getName().equals("ConstructorModel")) {
                    ct=t;
                    break;             
             }
             l=l.getSubtermAt(1);
         }
         assertTrue("Constructor model must be found in "+TermHelper.termToString(mt),ct!=null);                  
         Term blockModel = ct.getSubtermAt(JavaModelConstants.CONSTRUCTOR_MODEL_BLOCK_INDEX);
         assertTrue("Constructor block model must not be NIL",!blockModel.isNil());
       }
       
       public void testQueueModel() throws Exception
       {
           JavaTypeModel qtm=JavaResolver.resolveTypeModelFromPackage("Queue",packageName);
           Term mt = qtm.getModelTerm();
           Term ta = mt.getSubtermAt(JavaModelConstants.CLASSORINTERFACE_MODEL_TYPE_ARGUMENTS_INDEX);
           assertTrue("TypeArguments must not be nil",!ta.isNil());
           Term bt = mt.getSubtermAt(JavaModelConstants.CLASSORINTERFACE_MODEL_BODY_INDEX);
           Term l=bt.getSubtermAt(0);
           boolean lengthFound=false;
           boolean pushFound=false;
           boolean popFound=false;
           boolean isEmptyFound=false;
           boolean sizeFound=false;
           boolean clearFound=false;
           while(!l.isNil()) {
               Term t = l.getSubtermAt(0);
               l=l.getSubtermAt(1);
               if (t.getName().equals("MemberVariableModel")) {
                   Term idTerm=t.getSubtermAt(JavaModelConstants.MEMBER_VARIABLE_MODEL_NAME_INDEX);
                   String name=idTerm.getSubtermAt(0).getString();
                   assertEquals("Queue have one member variable:list","list",name);  
                   lengthFound=true;
               }else if (t.getName().equals("MethodModel")) {
                   System.out.println("MethodModel:"+TermHelper.termToString(t));
                   Term idTerm=t.getSubtermAt(JavaModelConstants.METHOD_MODEL_NAME_INDEX);
                   String name=idTerm.getSubtermAt(0).getString();
                   if (name.equals("push")) {
                       pushFound=true;
                   }else if (name.equals("pop")) {
                       popFound=true;
                   }else if (name.equals("isEmpty")) {
                       isEmptyFound=true;
                   }else if (name.equals("size")){
                       sizeFound=true;
                   }else if (name.equals("clear")){
                       clearFound=true;
                   }
               }               
           }
           assertTrue("field length must be found",lengthFound);
           assertTrue("method push must be found",pushFound);
           assertTrue("method pop must be found",pushFound);
           assertTrue("method isEmpty must be found",isEmptyFound);
           assertTrue("method size must be found",sizeFound);
           assertTrue("method clear must be found",clearFound);
       }
       
       public void testPeekableStack() throws Exception
       {
           JavaTypeModel tm=JavaResolver.resolveTypeModelFromPackage("PeekableStack",packageName);
           Term mt = tm.getModelTerm();
           Term ta = mt.getSubtermAt(JavaModelConstants.CLASSORINTERFACE_MODEL_TYPE_ARGUMENTS_INDEX);
           assertTrue("PeekableStack must have type arguments",!ta.isNil());
       }
    
       private static String packageName="com.heatonresearch.examples.collections";
}
