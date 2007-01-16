/*
 * LoadingTest.java
 *
 * Created on понеділок, 15, січня 2007, 0:45
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker;

import java.util.List;
import java.util.Map;
import junit.framework.TestCase;
import ua.gradsoft.javachecker.models.JavaFormalParameterModel;
import ua.gradsoft.javachecker.models.JavaMemberVariableAbstractModel;
import ua.gradsoft.javachecker.models.JavaMethodAbstractModel;
import ua.gradsoft.javachecker.models.JavaResolver;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.javachecker.models.JavaTypeVariableAbstractModel;

/**
 *How sources are loading.
 * @author Ruslan Shevchenko
 */
public class LoadingTest extends TestCase
{
    
    public void testLoadingXyz() throws Exception
    {
        JavaCheckerFacade f = new JavaCheckerFacade();
        f.addInputDirectory("testpackages/testdata4");
        JavaTypeModel zModel=JavaResolver.resolveTypeModelFromPackage("Z","x.y");
        assertTrue("zModel!=null failed",zModel!=null);
        //System.out.println("resolved:"+zModel.getFullName());
        JavaTypeModel zSuper = zModel.getSuperClass();
        assertTrue("zSuper!=null failed",zSuper!=null);
        //System.out.println("super is:"+zSuper.getFullName());
        List<JavaMethodAbstractModel> lm1=zModel.findMethodModels("main");
        assertTrue("main method models are not found in Z",lm1.size()!=0);
        JavaMethodAbstractModel methodModel = lm1.get(0);
        JavaTypeModel sTypeModel = methodModel.getResultType();
        assertTrue("main result type is not void",sTypeModel.getName().equals("void"));
        //System.out.println("Z.main result type is "+sTypeModel.getFullName());
        Map<String,JavaFormalParameterModel> mfp=methodModel.getFormalParameters();
        JavaFormalParameterModel fp = mfp.get("args");
        assertTrue("main must have args parameter",fp!=null);
        JavaTypeModel argsTypeModel=fp.getTypeModel();
        assertEquals("java.lang.String[]",argsTypeModel.getFullName());        
        Map<String,JavaTypeModel> nested = zModel.getNestedTypeModels();
        JavaTypeModel eTypeModel = nested.get("E");
        assertTrue("Z must contains E",eTypeModel!=null);
        JavaTypeModel eParent = eTypeModel.getEnclosedType();
        assertTrue("Z must be enclosed for E",eParent==zModel);
        Map<String,JavaMemberVariableAbstractModel> mf = zModel.getMemberVariableModels();
        assertTrue("Z must have member variable",mf.size()>0);
        JavaMemberVariableAbstractModel xmf = mf.get("x");
        assertTrue("Z must have member variable x",xmf!=null);
        sTypeModel = xmf.getTypeModel();
        assertEquals("String",sTypeModel.getName());
        mf=eTypeModel.getMemberVariableModels();
        assertTrue("E must have member variable",mf.size()>0);
        xmf = mf.get("x");
        assertTrue("E must have member variable x",xmf!=null);
        sTypeModel = xmf.getTypeModel();
        assertEquals("String",sTypeModel.getName());
    }
    
    public void testResolveString() throws Exception
    {        
        JavaTypeModel stringTypeModel=JavaResolver.resolveTypeModelByName("String",JavaResolver.resolveJavaIoSerializable(),null,null);
        assertEquals("java.lang.String",stringTypeModel.getFullName());        
    }
    
    public void testLoadingXyZZ() throws Exception
    {
        JavaCheckerFacade f = new JavaCheckerFacade();
        f.addInputDirectory("testpackages/testdata5");
        JavaTypeModel zzModel = JavaResolver.resolveTypeModelFromPackage("ZZ","x.y");
        assertTrue("ZZ name is ZZ",zzModel.getName().equals("ZZ"));        
        List<JavaTypeVariableAbstractModel> tv=zzModel.getTypeParameters();
        assertTrue("ZZ must have type parameters",tv.size()>0);        
        List<JavaMethodAbstractModel> zzCml = zzModel.findMethodModels("createList");
        assertTrue("ZZ have method with name create list",zzCml.size()>0);
        JavaMethodAbstractModel zzCm = zzCml.get(0);
        JavaTypeModel zzCmrt = zzCm.getResultType();
        System.out.println("result type of zzCmrt:"+zzCmrt.getName());
        assertEquals("List<T>",zzCmrt.getName());
        
        JavaTypeModel zzzModel = zzModel.findNestedTypeModel("ZZZ");
        assertTrue("ZZZ name is not ZZZ",zzzModel.getName().equals("ZZZ"));        
        
        JavaTypeModel zzzSuper = zzzModel.getSuperClass();
        assertEquals("ZZ<Integer>",zzzSuper.getName());
        
        List<JavaMethodAbstractModel> zzzCml = zzzSuper.findMethodModels("createList");
        JavaMethodAbstractModel zzzCm = zzzCml.get(0);
        JavaTypeModel zzzCmrt = zzzCm.getResultType();
        assertEquals("List<java.lang.Integer>",zzzCmrt.getName());
        
        Map<String,JavaFormalParameterModel> zzzCmfpm = zzzCm.getFormalParameters();
        assertEquals(2,zzzCmfpm.size());
        JavaFormalParameterModel fml1=zzzCmfpm.get("l1");
        assertTrue("l1 is a first argument of createList",fml1.getIndex()==0);
        assertEquals("l1 type is java.lang.Integer","java.lang.Integer",fml1.getTypeModel().getFullName());
        
        List<JavaMethodAbstractModel> zzzPml = zzzModel.findMethodModels("printList");
        JavaMethodAbstractModel zzzPm = zzzPml.get(0);
        assertTrue("ZZZ.printList must be static",zzzPm.getModifiers().isStatic());
        
        JavaTypeModel zzzPmr = zzzPm.getResultType();
        assertTrue("return type of zzzPmr must be primitive",zzzPmr.isPrimitiveType());
        
        
        Map<String,JavaFormalParameterModel> zzzPmfpm = zzzPm.getFormalParameters();
        assertEquals("printList have 1 parameter",1,zzzPmfpm.size());
        
        JavaFormalParameterModel zzzPmfp=zzzPmfpm.get("l");
        JavaTypeModel fp=zzzPmfp.getTypeModel();
        assertEquals("printList l formal parameter type","List<T>",fp.getName());
        
    }
}
