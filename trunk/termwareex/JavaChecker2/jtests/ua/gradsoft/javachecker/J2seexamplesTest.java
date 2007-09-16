/*
 * MethodResolvingTest.java
 *
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker;

import java.util.LinkedList;
import java.util.List;
import junit.framework.TestCase;
import ua.gradsoft.javachecker.models.JavaCompilationUnitModel;
import ua.gradsoft.javachecker.models.JavaMemberVariableModel;
import ua.gradsoft.javachecker.models.JavaMethodModel;
import ua.gradsoft.javachecker.models.JavaPrimitiveTypeModel;
import ua.gradsoft.javachecker.models.JavaResolver;
import ua.gradsoft.javachecker.models.JavaStatementModel;
import ua.gradsoft.javachecker.models.JavaTopLevelBlockModel;
import ua.gradsoft.javachecker.models.JavaTypeArgumentsSubstitution;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.javachecker.models.JavaUnitModel;
import ua.gradsoft.javachecker.models.JavaVariableKind;
import ua.gradsoft.javachecker.models.JavaVariableModel;

/**
 *Resolving some methods.
 * @author Ruslan Shevchenko
 */
public class J2seexamplesTest extends TestCase
{
    
    protected void setUp() throws Exception
    {
        JavaCheckerFacade.init();
        JavaCheckerFacade.addInputDirectory("testpackages/testdata3",true);
    }

    public void testMethodResolvingEx6() throws Exception
    {
        JavaTypeModel hideTypeModel = JavaResolver.resolveTypeModelFromPackage("Hide","j2seexamples.ex6");
        JavaUnitModel um = hideTypeModel.getUnitModel();
        JavaCompilationUnitModel cum = (JavaCompilationUnitModel)um;
        List<JavaTypeModel> tms = cum.getTypeModels();
        assertTrue("tms!=null",tms!=null);
        // now bart is loaded, so we can resolve one
        JavaTypeModel bartTypeModel = JavaResolver.resolveTypeModelFromPackage("Bart","j2seexamples.ex6");
        JavaTypeModel milhouseTypeModel = JavaResolver.resolveTypeModelFromPackage("Milhouse","j2seexamples.ex6");
        List<JavaTypeModel> fps = new LinkedList<JavaTypeModel>();
        fps.add(milhouseTypeModel);
        JavaMethodModel doh = JavaResolver.resolveMethod("doh",fps,new JavaTypeArgumentsSubstitution(),bartTypeModel);
        JavaTypeModel dohOwner = doh.getTypeModel();
        assertEquals("doh(Milestone) defined in","Bart",dohOwner.getName());
        fps = new LinkedList<JavaTypeModel>();
        fps.add(JavaPrimitiveTypeModel.CHAR);
        doh = JavaResolver.resolveMethod("doh",fps,new JavaTypeArgumentsSubstitution(),bartTypeModel);
        dohOwner = doh.getTypeModel();
        assertEquals("doh(char) defined in","Homer",dohOwner.getName());
    }
    
    public void testMethodResolvingEx1() throws Exception
    {
     JavaTypeModel hypotTypeModel = JavaResolver.resolveTypeModelByFullClassName("j2seexamples.ex1.Hypot");
     List<JavaTypeModel> fps = new LinkedList<JavaTypeModel>();
     fps.add(JavaPrimitiveTypeModel.DOUBLE);
     JavaMethodModel m = JavaResolver.resolveMethod("sqrt",fps,new JavaTypeArgumentsSubstitution(),hypotTypeModel);     
     JavaTypeModel mOwner = m.getTypeModel();
     assertEquals("sqrt defined in Math","Math",mOwner.getName());
    }
    
    public void testVarResolvingEx2() throws Exception
    {
      JavaTypeModel siTypeModel = JavaResolver.resolveTypeModelByFullClassName("j2seexamples.ex2.StaticImporter");
      JavaMethodModel weMethodModel = siTypeModel.findMethodModels("writeError").get(0);
      JavaTopLevelBlockModel block = weMethodModel.getTopLevelBlockModel();
      JavaStatementModel st = block.getStatements().get(block.getStatements().size()-1);
      JavaVariableModel v = JavaResolver.resolveVariableByName("err",st);
      assertTrue("err in writeError is formal parameter",v.getKind()==JavaVariableKind.FORMAL_PARAMETER);
      JavaMethodModel mainModel = siTypeModel.findMethodModels("main").get(0);
      block=mainModel.getTopLevelBlockModel();
      st = block.getStatements().get(block.getStatements().size()-1);
      v = JavaResolver.resolveVariableByName("err",st);
      assertTrue("err in main is member variable",v.getKind()==JavaVariableKind.MEMBER_VARIABLE);
    }
    
    public void testEnumResolvingEx3() throws Exception
    {
      JavaTypeModel eiTypeModel = JavaResolver.resolveTypeModelByFullClassName("j2seexamples.ex3.EnumImporter");  
      JavaTypeModel studentsTypeModel = JavaResolver.resolveMemberVariableByName("students",eiTypeModel).getType();
      assertTrue("EnumImporter.students must be array",studentsTypeModel.isArray());
      JavaTypeModel studentTypeModel = studentsTypeModel.getReferencedType();
      JavaTypeModel gradeTypeModel = studentTypeModel.findMemberVariableModel("grade").getType();
      assertTrue("Strudent.grade must be an enum",gradeTypeModel.isEnum());
      JavaMethodModel printGradesModel = eiTypeModel.getMethodModels().get("printGrades").get(0);
      JavaTopLevelBlockModel block = printGradesModel.getTopLevelBlockModel();      
      JavaStatementModel st = block.getStatements().get(0);
      JavaVariableModel v = JavaResolver.resolveVariableByName("INCOMPLETE",st);
      JavaTypeModel vTypeModel = v.getType();
      assertTrue("v type must be enum",vTypeModel.isEnum());
    }
    
    public void testEnumAnonimousClassesEx7() throws Exception
    {
      JavaTypeModel opTypeModel = JavaResolver.resolveTypeModelByFullClassName("j2seexamples.ex7.Operation");
      JavaMemberVariableModel mv = JavaResolver.resolveMemberVariableByName("DIVIDED_BY",opTypeModel);
      JavaTypeModel mvt = mv.getType();
      assertTrue("type of OPERATION.DIVIDE_BY must be enum",mvt.isEnum());
      String divideTypeName=mvt.getName();
      assertTrue("type of OPERATION.DIVIDE_BT must have not null name",divideTypeName!=null);            
      assertTrue("type of OPERATION.DIVIDE_BY must start with $",divideTypeName.startsWith("$"));
      assertTrue("type of OPERATION.DIVIDE_BY must be nested type",mvt.isNested());
    }
    
    public void testLoadingEx5() throws Exception
    {
      JavaTypeModel meta3TypeModel = JavaResolver.resolveTypeModelByFullClassName("j2seexamples.ex5.Meta3");
      JavaTypeModel myAnnoTypeModel = JavaResolver.resolveTypeModelByFullClassName("j2seexamples.ex5.MyAnno");
      assertTrue("myAnno must be annotation",myAnnoTypeModel.isAnnotationType());
    }
        
    
}
