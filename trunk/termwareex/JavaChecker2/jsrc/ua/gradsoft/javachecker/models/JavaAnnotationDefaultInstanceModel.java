/*
 * JavaAnnotationDefaultInstanceModel.java
 *
 * Created on June 7, 2007, 10:12 PM
 *
 */

package ua.gradsoft.javachecker.models;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Instance of default annotation.
 *(target is null, values are default values)
 * @author rssh
 */
public class JavaAnnotationDefaultInstanceModel extends JavaAnnotationInstanceModel {
    
    /**
     * Creates a new instance of JavaAnnotationDefaultInstanceModel
     */
    public JavaAnnotationDefaultInstanceModel(JavaTypeModel annotationType) throws TermWareException
    {
        super(JavaAnnotationDefaultInstanceModelHelper.mapAnnotationElementType(annotationType),null);
        annotationType_=annotationType;
    }
    
    public JavaTypeModel  getAnnotationModel()
    { return annotationType_; }
    
    public boolean hasElement(String name) throws TermWareException
    {
        return getElements().containsKey(name);
    }
    
    public JavaExpressionModel  getElement(String name) throws NotSupportedException, TermWareException
    { 
      JavaExpressionModel retval = getElements().get(name);
      if (retval==null) {
          throw new NotSupportedException();
      }
      return retval;
    }
    
    
    public Map<String,JavaExpressionModel>  getElements() throws TermWareException
    {
        if (elements_==null) {
            elements_=new TreeMap<String,JavaExpressionModel>();            
            Map<String,List<JavaMethodModel> > methods = null;
            try {
               methods=annotationType_.getMethodModels();
            }catch(NotSupportedException ex){
                throw new AssertException("annotationType_"+annotationType_.getFullName()+" must support quiering of methods");
            }
            for(List<JavaMethodModel> lms:methods.values()) {
                for(JavaMethodModel m: lms) {
                    if (m.hasDefaultValue()) {
                        try {
                          elements_.put(m.getName(),m.getDefaultValue());
                        }catch(NotSupportedException ex){
                            throw new AssertException("get default value for method "+m.getName()+" in "+annotationType_.getFullName()+" must be supported");
                        }
                    }
                }
            }
        }
        return elements_;                
    }
    
    /**
     * DefaultAnnotationInstance($obj)
     */
    public Term getModelTerm() throws TermWareException {
        Term obj = TermUtils.createJTerm(annotationType_);
        Term retval = TermUtils.createTerm("DefaultAnnotationInstance",obj);
        return retval;
    }
    
    
    private JavaTypeModel annotationType_;
    private Map<String,JavaExpressionModel> elements_=null;
    
}
