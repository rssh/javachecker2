/*
 * LoadingTest.java
 *
 * Copyright (c) 2006-2011 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker;

import java.util.List;
import java.util.Map;
import junit.framework.TestCase;
import ua.gradsoft.javachecker.models.JavaExpressionKind;
import ua.gradsoft.javachecker.models.JavaExpressionModel;
import ua.gradsoft.javachecker.models.JavaFormalParameterModel;
import ua.gradsoft.javachecker.models.JavaMemberVariableModel;
import ua.gradsoft.javachecker.models.JavaMethodModel;
import ua.gradsoft.javachecker.models.JavaPackageModel;
import ua.gradsoft.javachecker.models.JavaResolver;
import ua.gradsoft.javachecker.models.JavaStatementKind;
import ua.gradsoft.javachecker.models.JavaStatementModel;
import ua.gradsoft.javachecker.models.JavaTypeArgumentBoundTypeModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.javachecker.models.JavaTypeVariableAbstractModel;
import ua.gradsoft.javachecker.models.JavaVariableModel;
import ua.gradsoft.javachecker.models.TermUtils;
import ua.gradsoft.javachecker.models.expressions.JavaTermFunctionCallExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermMethodCallExpressionModel;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *How sources are loading.
 * @author Ruslan Shevchenko
 */
public class Java7examplesTest extends TestCase
{
    
    public void testLoadingStringInSwitch() throws Exception
    {
        JavaCheckerFacade.init();
        JavaCheckerFacade.addInputDirectory("testpackages/jdk7/strsw",true);
        JavaTypeModel sModel=JavaResolver.resolveTypeModelFromPackage("StrSwt","x");
        assertTrue("sModel!=null failed",sModel!=null);
        List<JavaMethodModel> lm1=sModel.findMethodModels("main");
        assertTrue("main method models are not found in Z",lm1.size()!=0);
        JavaMethodModel methodModel = lm1.get(0);
        JavaTypeModel sTypeModel = methodModel.getResultType();
        assertTrue("main result type is not void",sTypeModel.getName().equals("void"));
        Term modelTerm = sModel.getModelTerm();
        //System.out.print("model term:");
        //modelTerm.println(System.out);
    }
    
}
