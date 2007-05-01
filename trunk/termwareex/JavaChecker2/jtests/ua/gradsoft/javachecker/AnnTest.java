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
import ua.gradsoft.javachecker.models.JavaResolver;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.javachecker.models.expressions.JavaObjectConstantExpressionModel;

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
      
      Map<String,JavaAnnotationInstanceModel> x1anns = x1Model.getAnnotations();
      
      assertTrue("x1 must have annotation",x1anns.size()>0);
      
      JavaAnnotationInstanceModel a1Ci = x1Model.getAnnotation("A1C");
     
      assertTrue("a1C must not be null",a1Ci!=null);
      
      JavaTypeModel a1CModel = a1Ci.getAnnotationModel();
      
     
      
      JavaAnnotationInstanceModel a1cRetention = a1CModel.getAnnotation("Retention");
      assertTrue("Retention must be defied in a1c", a1cRetention!=null);
      
    }
    
    public void testLoadingAnn2() throws Exception
    {
        JavaCheckerFacade.init();
        JavaCheckerFacade.addInputDirectory("testpackages/testdata9",true);
        
        JavaTypeModel x2Model = JavaResolver.resolveTypeModelFromPackage("X2","x");
        
        JavaAnnotationInstanceModel a2Ci = x2Model.getAnnotation("A2C");
        
        JavaExpressionModel v = a2Ci.getElement("value");
               
    }
    
}
