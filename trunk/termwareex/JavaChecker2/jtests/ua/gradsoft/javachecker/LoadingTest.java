/*
 * LoadingTest.java
 *
 * Created on ��������, 15, ���� 2007, 0:45
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker;

import java.util.List;
import java.util.Map;
import junit.framework.TestCase;
import ua.gradsoft.javachecker.models.JavaFormalParameterModel;
import ua.gradsoft.javachecker.models.JavaMemberVariableModel;
import ua.gradsoft.javachecker.models.JavaMethodModel;
import ua.gradsoft.javachecker.models.JavaPackageModel;
import ua.gradsoft.javachecker.models.JavaResolver;
import ua.gradsoft.javachecker.models.JavaTypeArgumentBoundTypeModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.javachecker.models.JavaTypeVariableAbstractModel;
import ua.gradsoft.javachecker.models.TermUtils;
import ua.gradsoft.termware.Term;

/**
 *How sources are loading.
 * @author Ruslan Shevchenko
 */
public class LoadingTest extends TestCase
{
    
    public void testLoadingXyz() throws Exception
    {
        JavaCheckerFacade.init();
        JavaCheckerFacade.addInputDirectory("testpackages/testdata4",true);
        JavaTypeModel zModel=JavaResolver.resolveTypeModelFromPackage("Z","x.y");
        assertTrue("zModel!=null failed",zModel!=null);
        //System.out.println("resolved:"+zModel.getFullName());
        JavaTypeModel zSuper = zModel.getSuperClass();
        assertTrue("zSuper!=null failed",zSuper!=null);
        //System.out.println("super is:"+zSuper.getFullName());
        List<JavaMethodModel> lm1=zModel.findMethodModels("main");
        assertTrue("main method models are not found in Z",lm1.size()!=0);
        JavaMethodModel methodModel = lm1.get(0);
        JavaTypeModel sTypeModel = methodModel.getResultType();
        assertTrue("main result type is not void",sTypeModel.getName().equals("void"));
        //System.out.println("Z.main result type is "+sTypeModel.getFullName());
        Map<String,JavaFormalParameterModel> mfp=methodModel.getFormalParametersMap();
        JavaFormalParameterModel fp = mfp.get("args");
        assertTrue("main must have args parameter",fp!=null);
        JavaTypeModel argsTypeModel=fp.getType();
        assertEquals("java.lang.String[]",argsTypeModel.getFullName());        
        Map<String,JavaTypeModel> nested = zModel.getNestedTypeModels();
        JavaTypeModel eTypeModel = nested.get("E");
        assertTrue("Z must contains E",eTypeModel!=null);
        JavaTypeModel eParent = eTypeModel.getEnclosedType();
        assertTrue("Z must be enclosed for E",eParent==zModel);
        Map<String,JavaMemberVariableModel> mf = zModel.getMemberVariableModels();
        assertTrue("Z must have member variable",mf.size()>0);
        JavaMemberVariableModel xmf = mf.get("x");
        assertTrue("Z must have member variable x",xmf!=null);
        sTypeModel = xmf.getType();
        assertEquals("String",sTypeModel.getName());
        mf=eTypeModel.getMemberVariableModels();
        assertTrue("E must have member variable",mf.size()>0);
        xmf = mf.get("x");
        assertTrue("E must have member variable x",xmf!=null);
        sTypeModel = xmf.getType();
        assertEquals("String",sTypeModel.getName());
    }
    
    public void testResolveString() throws Exception
    {        
        JavaTypeModel stringTypeModel=JavaResolver.resolveTypeModelByName("String",JavaResolver.resolveJavaIoSerializable(),null,null);
        assertEquals("java.lang.String",stringTypeModel.getFullName());        
    }
    
    public void testLoadingXyZZ() throws Exception
    {
        JavaCheckerFacade.init();
        JavaCheckerFacade.addInputDirectory("testpackages/testdata5",true);
        JavaTypeModel zzModel = JavaResolver.resolveTypeModelFromPackage("ZZ","x.y");
        assertTrue("ZZ name is ZZ",zzModel.getName().equals("ZZ"));        
        List<JavaTypeVariableAbstractModel> tv=zzModel.getTypeParameters();
        assertTrue("ZZ must have type parameters",tv.size()>0);        
        List<JavaMethodModel> zzCml = zzModel.findMethodModels("createList");
        assertTrue("ZZ have method with name create list",zzCml.size()>0);
        JavaMethodModel zzCm = zzCml.get(0);
        JavaTypeModel zzCmrt = zzCm.getResultType();
        //System.out.println("result type of zzCmrt:"+zzCmrt.getName());
        assertEquals("List<T>",zzCmrt.getName());
        
        JavaTypeModel zzzModel = zzModel.findNestedTypeModel("ZZZ");
        assertTrue("ZZZ name is not ZZZ",zzzModel.getName().equals("ZZZ"));        
        
        JavaTypeModel zzzSuper = zzzModel.getSuperClass();
        assertEquals("ZZ<Integer>",zzzSuper.getName());
        
        List<JavaMethodModel> zzzCml = zzzSuper.findMethodModels("createList");
        JavaMethodModel zzzCm = zzzCml.get(0);
        JavaTypeModel zzzCmrt = zzzCm.getResultType();
        assertEquals("List<java.lang.Integer>",zzzCmrt.getName());
        
        Map<String,JavaFormalParameterModel> zzzCmfpm = zzzCm.getFormalParametersMap();
        assertEquals(2,zzzCmfpm.size());
        JavaFormalParameterModel fml1=zzzCmfpm.get("l1");
        assertTrue("l1 is a first argument of createList",fml1.getIndex()==0);
        assertEquals("l1 type is java.lang.Integer","java.lang.Integer",fml1.getType().getFullName());
        
        List<JavaMethodModel> zzzPml = zzzModel.findMethodModels("printList");
        JavaMethodModel zzzPm = zzzPml.get(0);
        assertTrue("ZZZ.printList must be static",zzzPm.getModifiers().isStatic());
        
        JavaTypeModel zzzPmr = zzzPm.getResultType();
        assertTrue("return type of zzzPmr must be primitive",zzzPmr.isPrimitiveType());
        
        
        Map<String,JavaFormalParameterModel> zzzPmfpm = zzzPm.getFormalParametersMap();
        assertEquals("printList have 1 parameter",1,zzzPmfpm.size());
        
        JavaFormalParameterModel zzzPmfp=zzzPmfpm.get("l");
        JavaTypeModel fp=zzzPmfp.getType();
        assertEquals("printList l formal parameter type","List<T>",fp.getName());
        
    }
    
    public void testLoadingLC() throws Exception
    {
     JavaCheckerFacade.init();
     JavaCheckerFacade.addInputDirectory("jtests",true);
     JavaTypeModel lc1Model = JavaResolver.resolveTypeModelFromPackage("LC1","t1.testdata.localclass1");
     assertEquals("full name of LC1 model must match","t1.testdata.localclass1.LC1",lc1Model.getFullName());
     Map<String,JavaTypeModel> ntm = lc1Model.getNestedTypeModels();
     assertEquals("we must have 3 nested models there",3,ntm.size());
    }
    
    public void testLoadingEb() throws Exception
    {
      JavaCheckerFacade.init();
      JavaCheckerFacade.addInputDirectory("testpackages/testdata5",true);
      JavaTypeModel ebModel = JavaResolver.resolveTypeModelFromPackage("Eb","x");
      List<JavaTypeVariableAbstractModel> ebtp = ebModel.getTypeParameters();
      assertTrue("Eb has type parameters",ebtp.size()!=0);
      JavaTypeVariableAbstractModel k = ebtp.get(0);
      assertEquals("name of K","K",k.getName());
      List<JavaTypeModel> kBounds = k.getBounds();
      assertTrue("Eb.K must have bounds",kBounds.size()!=0);
      //System.out.println("Eb full name:"+ebModel.getFullName()+",bounds:"+kBounds.toString());
      JavaTypeModel kBound=kBounds.get(0);
      assertEquals("bound is Enum","Enum<K>",kBound.getName());
      
     // Term x = ebModel.getModelTerm();
    }
    
    
     
    public void testLoadingPPP() throws Exception
    {
      JavaCheckerFacade.init();
      JavaCheckerFacade.addInputDirectory("testpackages/testdata5",true);
      JavaTypeModel ppModel = JavaResolver.resolveTypeModelFromPackage("PP","x.y");
      assertEquals("PP.getName()==PP","PP",ppModel.getName());
      
      JavaTypeModel pppModel = ppModel.findNestedTypeModel("PPP");
      assertTrue("pppModel!=null",pppModel!=null);
      
      JavaTypeModel pppSuperModel = pppModel.getSuperClass();
     
      JavaMethodModel pppDupT=pppSuperModel.findMethodModels("dupT").get(0);
      assertTrue("dupT name is dupP",pppDupT.getName().equals("dupT"));
      
      JavaTypeModel pppDupTFptModel=pppDupT.getFormalParametersTypes().get(0);
      assertTrue("PPP<Pair<Number,Integer>>.dupT argument bound",pppDupTFptModel instanceof JavaTypeArgumentBoundTypeModel);

      //System.out.println("pppDupT.getName()="+pppDupTFptModel.getName());
      JavaTypeModel pppDupTResultModel=pppDupT.getResultType();
      assertEquals("PPP.dupT() result type","Pair<Pair<Number,Integer>,Pair<Number,Integer>>",pppDupTResultModel.getName());
      
    }
      
    public void testPrintPPPNames() throws Exception
    {
      JavaCheckerFacade.init();
      JavaCheckerFacade.addInputDirectory("testpackages/testdata5",true);
      JavaTypeModel ppModel = JavaResolver.resolveTypeModelFromPackage("PP","x.y");
      assertEquals("PP.getFullName()==x.y.PP","x.y.PP",ppModel.getFullName());
      
      JavaPackageModel xym = ppModel.getPackageModel();
      
      String xyName = xym.getName();
      Term t = JUtils.createRowJavaName(xyName);
      Term tl = t.getSubtermAt(0);
      assertTrue(tl.getSubtermAt(0).getName().equals("Identifier"));
      assertEquals(tl.getSubtermAt(0).getSubtermAt(0).getString(),"x");
      
      Term id = ppModel.getShortNameAsTerm();
      tl = TermUtils.addTermToList(tl,id);
      assertTrue(tl.getSubtermAt(0).getName().equals("Identifier"));
      assertEquals(tl.getSubtermAt(0).getSubtermAt(0).getString(),"x");
      
      
      t = ppModel.getFullNameAsTerm();
      tl = t.getSubtermAt(0);
      assertTrue(tl.getSubtermAt(0).getName().equals("Identifier"));
      assertEquals(tl.getSubtermAt(0).getSubtermAt(0).getString(),"x");
    }
    
    
}
