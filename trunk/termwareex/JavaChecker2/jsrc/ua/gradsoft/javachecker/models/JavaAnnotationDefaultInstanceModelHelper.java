/*
 * JavaAnnotationDefaultInstanceModelHelper.java
 *
 * Created on June 7, 2007, 10:16 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ua.gradsoft.javachecker.models;

import java.lang.annotation.ElementType;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.javachecker.models.expressions.JavaObjectConstantExpressionModel;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *
 * @author rssh
 */
class JavaAnnotationDefaultInstanceModelHelper {

    
    static ElementType mapAnnotationElementType(JavaTypeModel annotationType) throws TermWareException
    {
      JavaExpressionModel expr = null;  
      try {  
        expr=annotationType.getAnnotation("java.lang.annotation.Target").getElement("value");   
      }catch(NotSupportedException ex){
         throw new AssertException("Annotation type must have TargetAnnotation "+annotationType.getFullName());
      }      
      if (!(expr instanceof JavaObjectConstantExpressionModel)) {
          throw new AssertException("annotation must be object constant");
      }else{
          JavaObjectConstantExpressionModel cexpr = (JavaObjectConstantExpressionModel)expr;
          Object o = cexpr.getConstant();
          if (o instanceof ElementType) {
              return (ElementType)o;
          }else{
              // ElementType from source.
              throw new RuntimeException("getting ElementType from Java Source not implemented yet.");
          }
      }
      //return annotationType.getJavaClass().getAnnotation(java.lang.annotation.Target.class);  
    }
    
  
    
}
