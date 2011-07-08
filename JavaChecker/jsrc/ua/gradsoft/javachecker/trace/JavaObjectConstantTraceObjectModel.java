package ua.gradsoft.javachecker.trace;

import java.lang.reflect.Field;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.models.JavaExpressionModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.javachecker.models.expressions.JavaClassObjectConstantExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaObjectConstantExpressionModel;
import ua.gradsoft.termware.TermWareException;

/**
 *
 * @author rssh
 */
public class JavaObjectConstantTraceObjectModel implements JavaTraceObjectModel
{

    public JavaObjectConstantTraceObjectModel(JavaObjectConstantExpressionModel oce)
    { oce_ = oce; }


    public JavaExpressionModel getExpressionModel() {
        return oce_;
    }

    public JavaTraceObjectModel getField(String name) throws TermWareException, EntityNotFoundException, EvaluationException {
        Object o = oce_.getConstant();
        Class oClass = o.getClass();
        Field field = null;
        try {
           field = oClass.getField(name);
        } catch(NoSuchFieldException ex){
            throw new EvaluationException(ex);
        }
        try {
          o = field.get(o);
        }catch(IllegalAccessException ex){
            throw new EvaluationException(ex);
        }
        return new JavaObjectConstantTraceObjectModel(
                new JavaClassObjectConstantExpressionModel(o, getType())
               );
    }

    public JavaTypeModel getType() throws TermWareException, EntityNotFoundException {
        return oce_.getType();
    }

    public boolean hasFields() throws TermWareException, EntityNotFoundException {
        return (!oce_.getType().isPrimitiveType());
    }

    public void setField(String name, JavaTraceObjectModel value) throws TermWareException, EntityNotFoundException, EvaluationException {
        if (value.getExpressionModel() instanceof JavaObjectConstantExpressionModel) {
            JavaObjectConstantExpressionModel sfc = (JavaObjectConstantExpressionModel)value;
            Object fo = sfc.getConstant();
            Class oClass = oce_.getConstant().getClass();
            Field field = null;
            try {
               field = oClass.getField(name);
            } catch(NoSuchFieldException ex){
               throw new EvaluationException(ex);
            }
            try {
              field.set(oce_.getConstant(), fo);
            }catch(IllegalAccessException ex){
                throw new EvaluationException(ex);
            }
        }else{
            throw new EvaluationException("Can't evaluate setting of non-term field by term");
        }
    }



    private JavaObjectConstantExpressionModel oce_;
}
