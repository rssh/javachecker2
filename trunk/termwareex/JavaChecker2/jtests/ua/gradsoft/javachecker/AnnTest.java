/*
 * AnnTest.java
 *
 * Created on April 28, 2007, 3:40 AM
 *
 */

package ua.gradsoft.javachecker;

import java.util.Map;
import junit.framework.TestCase;
import ua.gradsoft.javachecker.models.JavaAnnotationInstanceModel;
import ua.gradsoft.javachecker.models.JavaExpressionModel;
import ua.gradsoft.javachecker.models.JavaMethodModel;
import ua.gradsoft.javachecker.models.JavaResolver;
import ua.gradsoft.javachecker.models.JavaTypeModel;

/**
 *
 * @author rssh
 */
public class AnnTest extends TestCase
{
    

    public void testLoadingAnn1() throws Exception
    {
      JavaCheckerFacade.init();
      JavaCheckerFacade.addInputDirectory("testpackages/testdata9",true);
      JavaTypeModel x1Model = JavaResolver.resolveTypeModelFromPackage("X1","x");
      
      Map<String,JavaAnnotationInstanceModel> x1anns = x1Model.getAnnotationsMap();
      
      assertTrue("x1 must have annotation",x1anns.size()>0);
      
      JavaAnnotationInstanceModel a1Ci = x1Model.getAnnotation("ann.A1C");
     
      assertTrue("a1C must not be null",a1Ci!=null);
      
      JavaTypeModel a1CModel = a1Ci.getAnnotationModel();
      
     
      
      JavaAnnotationInstanceModel a1cRetention = a1CModel.getAnnotation("java.lang.annotation.Retention");
      assertTrue("Retention must be defied in a1c", a1cRetention!=null);
      
    }
    
    public void testLoadingAnn2() throws Exception
    {
        JavaCheckerFacade.init();
        JavaCheckerFacade.addInputDirectory("testpackages/testdata9",true);
        
        JavaTypeModel x2Model = JavaResolver.resolveTypeModelFromPackage("X2","x");
        
        JavaAnnotationInstanceModel a2Ci = x2Model.getAnnotation("ann.A2C");
        
        JavaExpressionModel v = a2Ci.getElement("value");
               
    }

    public void testLoadingAnnXI1() throws Exception
    {
        JavaCheckerFacade.init();
        JavaCheckerFacade.addInputDirectory("testpackages/testdata9",true);
        
        JavaTypeModel x1iModel = JavaResolver.resolveTypeModelFromPackage("XI1","x");
        
        JavaAnnotationInstanceModel a2Ci = x1iModel.getAnnotation("ann.A2C");
       
        JavaTypeModel i1Model = x1iModel.findNestedTypeModel("I1");
        
        assertTrue("i1Model exists",i1Model!=null);
        
        JavaAnnotationInstanceModel i1A1am = i1Model.getAnnotation("ann.A1C");
        
        assertTrue("XI1.I1 model has A1C annotation",i1A1am!=null);
        
        JavaTypeModel i2Model = x1iModel.findNestedTypeModel("I2");
        
        JavaAnnotationInstanceModel i2A2am = i2Model.getAnnotation("ann.A2C");
        
        assertTrue("XI1.I2 model has A2C annotation",i2A2am!=null);
        
        JavaAnnotationInstanceModel i2A3am = i2Model.getAnnotation("ann.A3C");
        
        assertTrue("XI1.I2 model has A2C annotation",i2A3am!=null);
        
        JavaTypeModel eModel = x1iModel.findNestedTypeModel("E");
        
        assertTrue("XI1.E model exists",true);
        
        JavaAnnotationInstanceModel eA1am = eModel.getAnnotation("ann.A1C");
        
        assertTrue("XI1.E model has A1C annotation",eA1am!=null);
                               
    }

    public void testLoadingAnnXM() throws Exception
    {
        JavaCheckerFacade.init();
        JavaCheckerFacade.addInputDirectory("testpackages/testdata9",true);
        
        JavaTypeModel xmModel = JavaResolver.resolveTypeModelFromPackage("XM","x");
        
        JavaAnnotationInstanceModel a3C1 = xmModel.getAnnotationsMap().get("ann.A3C");
        
        assertTrue("XM class must be annoitated by ann.A3C",a3C1!=null);
        
        JavaExpressionModel e=a3C1.getElement("x");
        assertTrue("ann.A3C IN Xm define x",e!=null);
        
        JavaAnnotationInstanceModel a4C1 = xmModel.getAnnotationsMap().get("ann.A4C");
        assertTrue("XM class must be annoitated by ann.A4C",a4C1!=null);
        
        JavaMethodModel mainModel = xmModel.getMethodModels().get("main").get(0);
        
        JavaAnnotationInstanceModel a1M1 = mainModel.getAnnotationsMap().get("ann.A1M");
        assertTrue("XM.main must be annotated by ann.A1M",a1M1!=null);
        
        JavaExpressionModel a1M1q = a1M1.getElement("q");
        assertTrue("A1M1.q must exists",a1M1q!=null);
        
        JavaMethodModel main1Model = xmModel.getMethodModels().get("main1").get(0);
        JavaAnnotationInstanceModel a1M2 = main1Model.getAnnotationsMap().get("ann.A1M");
        assertTrue("XM.main1 must be annotated by ann.A1M",a1M2!=null);
        
        Map<String,JavaExpressionModel> a1m2es = a1M2.getElements();
        
        assertTrue("a1m2es must contain q",a1m2es.containsKey("q"));
        
        assertTrue("a1m2es must not contain nq",!a1m2es.containsKey("nq"));
                                        
        
    }       
    
    
}
