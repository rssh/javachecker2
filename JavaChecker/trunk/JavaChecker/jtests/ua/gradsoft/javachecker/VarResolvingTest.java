/*
 * VarResolvingTest.java
 *
 *
 * Copyright (c) 2006-2010 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker;

import java.util.List;
import junit.framework.TestCase;
import ua.gradsoft.javachecker.models.JavaMemberVariableModel;
import ua.gradsoft.javachecker.models.JavaMethodModel;
import ua.gradsoft.javachecker.models.JavaResolver;
import ua.gradsoft.javachecker.models.JavaStatementModel;
import ua.gradsoft.javachecker.models.JavaTopLevelBlockModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.javachecker.models.JavaVariableKind;
import ua.gradsoft.javachecker.models.JavaVariableModel;
import ua.gradsoft.termware.Term;

/**
 *
 * @author Ruslan Shevchenko
 */
public class VarResolvingTest extends TestCase
{
    
    protected void setUp() throws Exception
    {
        JavaCheckerFacade.init();
        JavaCheckerFacade.addInputDirectory("testpackages/hiding",true);
    }
    
    public void testHd1() throws Exception
    {
        JavaTypeModel tm = JavaResolver.resolveTypeModelFromPackage("Hd1","hd");
        JavaMethodModel dsm=tm.findMethodModels("doSomething").get(0);
        JavaTopLevelBlockModel tplb=dsm.getTopLevelBlockModel();
        List<JavaStatementModel> statements=tplb.getStatements();
        JavaStatementModel last=statements.get(statements.size()-1);
        JavaVariableModel xmd = JavaResolver.resolveVariableByName("x",last);
        assertTrue("x in doSomething is formal parametet",xmd.getKind()==JavaVariableKind.FORMAL_PARAMETER);
        JavaTypeModel dd=xmd.getType();
        assertTrue("x type must be double",dd.getName().equals("double"));
        JavaMethodModel getxm=tm.findMethodModels("getX").get(0);
        tplb=getxm.getTopLevelBlockModel();
        statements=tplb.getStatements();
        last=statements.get(statements.size()-1);
        JavaVariableModel xmi = JavaResolver.resolveVariableByName("x",last);        
        assertEquals("x in getX is member",JavaVariableKind.MEMBER_VARIABLE,xmi.getKind());
        JavaTypeModel xt=xmi.getType();
        assertTrue("x type must be int",xt.getName().equals("int"));   
        JavaMethodModel dlxm=tm.findMethodModels("doLocalX").get(0);
        tplb=dlxm.getTopLevelBlockModel();
        statements=tplb.getStatements();
        last=statements.get(statements.size()-1);
        JavaVariableModel xmf = JavaResolver.resolveVariableByName("x",last); 
        assertEquals("x in doLocalX is local varibale",JavaVariableKind.LOCAL_VARIABLE,xmf.getKind());
        xt=xmf.getType();
        assertTrue("x type must be float",xt.getName().equals("float"));   
    }
    
     public void testHd2() throws Exception
     {
        JavaTypeModel hd2tm = JavaResolver.resolveTypeModelFromPackage("Hd2","hd"); 
        JavaMethodModel mm=hd2tm.findMethodModels("sumSuper").get(0);
        JavaTopLevelBlockModel tplb=mm.getTopLevelBlockModel();
        List<JavaStatementModel> statements=tplb.getStatements();
        JavaStatementModel last=statements.get(statements.size()-1);
        JavaVariableModel xvm = JavaResolver.resolveVariableByName("x",last);
        assertEquals("x in sumSuper is member",JavaVariableKind.MEMBER_VARIABLE,xvm.getKind());
        JavaMemberVariableModel xvmm=(JavaMemberVariableModel)xvm;
        assertEquals("owner of x is Hd1","Hd1",xvmm.getOwnerType().getName());        
     }
    
     public void testSystemOutPrintln() throws Exception
     {
         JavaTypeModel tm = JavaResolver.resolveTypeModelByFullClassName("java.io.PrintStream");
         List<JavaMethodModel> printlnModels=tm.findMethodModels("println");
         assertTrue("we must have few models of println",printlnModels.size()>1);
         //for(JavaMethodModel tmm: printlnModels) {
         //    tmm.print(System.out);
         //    System.out.println();
         //}
     }

     public void testHd3() throws Exception
     {
        JavaTypeModel hd3tm = JavaResolver.resolveTypeModelFromPackage("Hd3","hd");                 
        JavaMethodModel mm = hd3tm.findMethodModels("main").get(0);
        JavaTopLevelBlockModel tplb=mm.getTopLevelBlockModel();
        List<JavaStatementModel> statements=tplb.getStatements();
        JavaStatementModel last=statements.get(statements.size()-1);
        JavaVariableModel xvm = JavaResolver.resolveVariableByName("otherArgs",last);
        assertEquals("otherArgs is local variable",JavaVariableKind.LOCAL_VARIABLE,xvm.getKind());
        
        // now check then we can resolve otherArgs form anopnimous class.
        //  (building of ModelTerm will resolve all references inside)
        Term mt = hd3tm.getModelTerm();
        
     }
     
     public void testHd4() throws Exception
     {
        JavaTypeModel hd4tm = JavaResolver.resolveTypeModelFromPackage("Hd4","hd");                 
        
        // now check then we can resolve y from switch statement
        //  (building of ModelTerm will resolve all references inside)
        Term mt = hd4tm.getModelTerm();
        
     }
     
     public void testHd5() throws Exception
     {
        JavaTypeModel hd5tm = JavaResolver.resolveTypeModelFromPackage("Hd5","hd");                 
        
        // now check then we can resolve y from switch statement
        //  (building of ModelTerm will resolve all references inside)
        Term mt = hd5tm.getModelTerm();
        
     }
     
     
     
}
