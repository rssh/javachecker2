/*
 * JavaClassAnnotationInstanceModel.java
 *
 */

package ua.gradsoft.javachecker.models;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.javachecker.models.expressions.JavaClassObjectConstantExpressionModel;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Model for annotation instance, based on reflection API
 */
public class JavaClassAnnotationInstanceModel extends JavaAnnotationInstanceModel
{
    
    /** Creates a new instance of JavaClassAnnotationInstanceModel */
    public JavaClassAnnotationInstanceModel(ElementType et, Annotation a, JavaClassTypeModel tm) {
        super(et,tm);
        annotation_=a;
    }
    
    public JavaTypeModel  getAnnotationModel() throws TermWareException
    {
        return JavaClassTypeModel.createTypeModel(annotation_.annotationType());
    }
    
    public boolean hasElement(String elementName)
    {
        try {
            Field field = annotation_.annotationType().getField(elementName);
        }catch(NoSuchFieldException ex){
            return false;
        }
        return true;
    }
    
    public JavaExpressionModel  getElement(String elementName) throws NotSupportedException, TermWareException
    {                    
      try {  
        Field field = annotation_.annotationType().getField(elementName);
        Object o = field.get(annotation_);
        return new JavaClassObjectConstantExpressionModel(o,getAnnotationModel());
      }catch(NoSuchFieldException ex){
          throw new NotSupportedException();
      }catch(IllegalAccessException ex){
          throw new AssertException("illegal access to annotation field",ex);
      }
    }
    
    public Map<String,JavaExpressionModel>  getElements() throws TermWareException
    {
        TreeMap<String,JavaExpressionModel> retval = new TreeMap<String,JavaExpressionModel>();
        Field[] fields = annotation_.annotationType().getFields();
        JavaTypeModel enclosedType = getAnnotationModel();
        try {
          for(int i=0; i<fields.length; ++i) {
            String name = fields[i].getName();
            Object value = fields[i].get(annotation_);
            retval.put(name,new JavaClassObjectConstantExpressionModel(value,enclosedType));
          }
          return retval;
        }catch(IllegalAccessException ex){
            throw new AssertException("illegal access to annotation field",ex);
        }
    }
    
    
    /**
     * ClassAnnotationInstanceModel(this)
     */
    public Term getModelTerm() throws TermWareException
    {
       Term jterm = TermUtils.createJTerm(this);
       return TermUtils.createTerm("ClassAnnotationInstanceModel",jterm);
    }
    
    private Annotation annotation_;
}
