
package ua.gradsoft.javachecker.trace;

import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.models.JavaPrimitiveTypeModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.termware.TermWareException;

/**
 *Class for trace models of primitive types
 * @author rssh
 */
public abstract class JavaPrimitiveTraceModel implements JavaTraceObjectModel
{

    protected JavaPrimitiveTraceModel(JavaPrimitiveTypeModel typeModel)
    {
      typeModel_=typeModel;  
    }


    public JavaTraceObjectModel getField(String name) throws TermWareException, EntityNotFoundException, EvaluationException {
        throw new EvaluationException("getField is inappropriatvive for primitive type");
    }

    public JavaTypeModel getType() throws TermWareException, EntityNotFoundException {
        return typeModel_;
    }

    public boolean hasFields() throws TermWareException, EntityNotFoundException {
        return false;
    }

    public void setField(String name, JavaTraceObjectModel value) throws TermWareException, EntityNotFoundException, EvaluationException {
       throw new EvaluationException("setField is inappropriatvive for primitive type");
    }

    protected JavaPrimitiveTypeModel typeModel_;

}
