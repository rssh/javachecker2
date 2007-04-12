/*
 * SelfLoadingTest.java
 *
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
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.termware.Term;

/**
 *Some tests accross JavaChecker2 sources.
 * @author Ruslan Shevchenko
 */
public class SelfLoadingTest extends TestCase
{
    
    
    protected void setUp() throws Exception
    {
        JavaCheckerFacade.init();
        JavaCheckerFacade.addInputDirectory("jsrc",true);
    }

    public void  testLoadJavaResolver() throws Exception
    {
        JavaTypeModel tm = JavaResolver.resolveTypeModelByFullClassName("ua.gradsoft.javachecker.models.JavaResolver");
        assertTrue("ua.gradsoft.javachecker.models.JavaResolver exists",true);
        tm = JavaResolver.resolveTypeModelByFullClassName("ua.gradsoft.javachecker.models.JavaTypeModel");
        assertTrue("ua.gradsoft.javachecker.models.JavaTypeModel exists",true);
    }
    
    public void testLoadJavaPackageModel() throws Exception
    {
        JavaTypeModel tm = JavaResolver.resolveTypeModelByFullClassName("ua.gradsoft.javachecker.models.JavaPackageModel");
        assertTrue("ua.gradsoft.javachecker.models.JavaPackageModel exists",true);
        Term mt = tm.getModelTerm();        
    }
    
    public void testLoadJavaTermEqualityExpression() throws Exception
    {
        JavaTypeModel tm = JavaResolver.resolveTypeModelByFullClassName("ua.gradsoft.javachecker.models.expressions.JavaTermEqualityExpressionModel");        
        assertTrue("JavaTermEqualityExpressionModel exists",tm!=null);
        
        JavaMemberVariableModel sem = tm.findMemberVariableModel("subExpressions_");
        
        JavaTypeModel semType = sem.getTypeModel();
                                
        //System.out.println("semType="+semType.getName());
        
        List<JavaMethodModel> methodModels = semType.findMethodModels("get");
        
        JavaMethodModel methodModel = methodModels.get(0);
        
        //System.out.println("semType.get="+methodModel.toString());                
        
        Term mt = tm.getModelTerm();
        
    }
    
}
