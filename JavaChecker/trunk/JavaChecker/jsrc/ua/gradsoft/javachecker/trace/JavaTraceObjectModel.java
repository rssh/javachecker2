
package ua.gradsoft.javachecker.trace;

import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.models.JavaExpressionModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.termware.TermWareException;

/**
 *trace object model
 * @author rssh
 */
public interface JavaTraceObjectModel {

    
    public JavaExpressionModel getExpressionModel();
    
    public JavaTypeModel getType() throws TermWareException, EntityNotFoundException;
    
    public boolean hasFields() throws TermWareException, EntityNotFoundException;
    
    public JavaTraceObjectModel getField(String name)  throws TermWareException, EntityNotFoundException, EvaluationException;
    
    public void setField(String name, JavaTraceObjectModel value) throws TermWareException, EntityNotFoundException, EvaluationException;

}
