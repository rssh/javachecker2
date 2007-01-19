/*
 * VarResolvingTest.java
 *
 * Created on середа, 17, січня 2007, 3:06
 *
 * Copyright (c) 2006 GradSoft  Ukraine
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

/**
 *
 * @author Ruslan Shevchenko
 */
public class VarResolvingTest extends TestCase
{
    
    protected void setUp() throws Exception
    {
        JavaCheckerFacade.init();
        JavaCheckerFacade.addInputDirectory("testpackages/hiding");
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
        JavaTypeModel dd=xmd.getTypeModel();
        assertTrue("x type must be double",dd.getName().equals("double"));
        JavaMethodModel getxm=tm.findMethodModels("getX").get(0);
        tplb=getxm.getTopLevelBlockModel();
        statements=tplb.getStatements();
        last=statements.get(statements.size()-1);
        JavaVariableModel xmi = JavaResolver.resolveVariableByName("x",last);        
        assertEquals("x in getX is member",JavaVariableKind.MEMBER_VARIABLE,xmi.getKind());
        JavaTypeModel xt=xmi.getTypeModel();
        assertTrue("x type must be int",xt.getName().equals("int"));   
        JavaMethodModel dlxm=tm.findMethodModels("doLocalX").get(0);
        tplb=dlxm.getTopLevelBlockModel();
        statements=tplb.getStatements();
        last=statements.get(statements.size()-1);
        JavaVariableModel xmf = JavaResolver.resolveVariableByName("x",last); 
        assertEquals("x in doLocalX is local varibale",JavaVariableKind.LOCAL_VARIABLE,xmf.getKind());
        xt=xmf.getTypeModel();
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
        assertEquals("owner of x is Hd1","Hd1",xvmm.getOwner().getName());        
     }
    
     
}
