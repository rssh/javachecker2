/*
 * ExprTypeResolvingTest.java
 *
 * Created on середа, 24, січня 2007, 21:39
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker;

import java.io.StringReader;
import junit.framework.TestCase;
import ua.gradsoft.javachecker.models.JavaMethodModel;
import ua.gradsoft.javachecker.models.JavaPlaceContext;
import ua.gradsoft.javachecker.models.JavaPlaceContextFactory;
import ua.gradsoft.javachecker.models.JavaPrimitiveTypeModel;
import ua.gradsoft.javachecker.models.JavaResolver;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.termware.Term;

/**
 *
 * @author Ruslan Shevchenko
 */
public class ExprTypeResolvingTest extends TestCase
{
    
     protected void setUp() throws Exception
    {
        JavaCheckerFacade.init();
        JavaCheckerFacade.addInputDirectory("testpackages/testdata3",true);
    }

     public static void testIntConstantsExprType() throws Exception
     {
         JavaTypeModel tm = JavaResolver.resolveTypeModelByFullClassName("j2seexamples.MyMsg.Msg");
         JavaPlaceContext ctx = JavaPlaceContextFactory.createNewTypeContext(tm);
         String sexpr1="3+2";
         Term expr1 = JUtils.parseJavaLanguageElement(new StringReader(sexpr1),"Expression");
         JavaTypeModel tme1=JavaResolver.resolveExpressionType(expr1,ctx);
         assertTrue("Type of 3+2 must be int",tme1.equals(JavaPrimitiveTypeModel.INT));
         String sexpr2="3+2L";
         Term expr2 = JUtils.parseJavaLanguageElement(new StringReader(sexpr2),"Expression");
         JavaTypeModel tme2=JavaResolver.resolveExpressionType(expr2,ctx);
         assertTrue("Type of 3+2L must be long",tme2.equals(JavaPrimitiveTypeModel.LONG));
         String sexpr3="UPPER";
         Term expr3 = JUtils.parseJavaLanguageElement(new StringReader(sexpr3),"Expression");
         JavaTypeModel tme3=JavaResolver.resolveExpressionType(expr3,ctx);
         assertTrue("Type of UPPER must be int",tme3.equals(JavaPrimitiveTypeModel.INT));
         String sexpr4="System.out";
         Term expr4 = JUtils.parseJavaLanguageElement(new StringReader(sexpr4),"Expression");
         JavaTypeModel tme4=JavaResolver.resolveExpressionType(expr4,ctx);
         assertEquals("type of System.out","java.io.PrintStream",tme4.getFullName());
         String sexpr5="System.out.println(\"qqq\")";
         Term expr5 = JUtils.parseJavaLanguageElement(new StringReader(sexpr5),"Expression");
         JavaTypeModel tme5=JavaResolver.resolveExpressionType(expr5,ctx);
         assertEquals("type of System.out.println(..)","void",tme5.getName());
     }
    
      public static void testFullStaticImportExprType() throws Exception
      {
         JavaTypeModel tm = JavaResolver.resolveTypeModelByFullClassName("j2seexamples.MyMsg.Test"); 
         JavaPlaceContext ctx = JavaPlaceContextFactory.createNewTypeContext(tm);
         String sexpr1="MIXED";
         Term expr1 = JUtils.parseJavaLanguageElement(new StringReader(sexpr1),"Expression");
         JavaTypeModel tme1=JavaResolver.resolveExpressionType(expr1,ctx);
         assertTrue("Type of MIXED must be int",tme1.equals(JavaPrimitiveTypeModel.INT));         
         JavaMethodModel mainModel = tm.findMethodModels("main").get(0);
         String sexpr2="args[LOWER]";
         ctx = JavaPlaceContextFactory.createNewMethodContext(mainModel);
         Term expr2 = JUtils.parseJavaLanguageElement(new StringReader(sexpr2),"Expression");
         JavaTypeModel tme2=JavaResolver.resolveExpressionType(expr2,ctx);
         assertEquals("Type of args[LOWER] must be String","String",tme2.getName());         
      }
}
