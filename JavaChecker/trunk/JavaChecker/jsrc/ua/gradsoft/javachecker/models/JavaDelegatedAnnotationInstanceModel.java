/*
 * JavaDelegatedAnnotationInstanceModel.java
 *
 */

package ua.gradsoft.javachecker.models;

import java.util.Map;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *
 * @author rssh
 */
public class JavaDelegatedAnnotationInstanceModel extends JavaAnnotationInstanceModel
{
    
    /** Creates a new instance of JavaDelegatedAnnotationInstanceModel */
    public JavaDelegatedAnnotationInstanceModel(JavaAnnotationInstanceModel origin,Object newTarget) {
        super(origin.getTargetElementType(),newTarget);
        origin_=origin;      
    }
    
    public JavaTypeModel getAnnotationModel() throws TermWareException, EntityNotFoundException
    { return origin_.getAnnotationModel(); }
    
    public Term getModelTerm() throws TermWareException, EntityNotFoundException
    {
        return origin_.getModelTerm();
    }

    public boolean hasElement(String elementName) throws TermWareException
    {
        return origin_.hasElement(elementName);
    }
    
    public JavaExpressionModel getElement(String elementName) throws NotSupportedException, TermWareException {
        return origin_.getElement(elementName);
    }
   
    public Map<String, JavaExpressionModel> getElements() throws TermWareException {
        return origin_.getElements();
    }  
    
    private JavaAnnotationInstanceModel origin_;   
}
