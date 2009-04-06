package ua.gradsoft.javachecker.trace;

import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.models.JavaArrayTypeModel;
import ua.gradsoft.javachecker.models.JavaExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermExpressionModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.javachecker.models.TermUtils;
import ua.gradsoft.javachecker.models.expressions.JavaTermArrayInitializerExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermIntegerLiteralExpressionModel;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TermWareRuntimeException;

/**
 *Model for array
 * @author rssh
 */
public class JavaTraceArrayModel implements JavaTraceObjectModel
{

    public JavaTraceArrayModel(JavaTypeModel referencedType, int length)
    {
        this(null, referencedType, length);
    }

    public JavaTraceArrayModel(JavaTermExpressionModel expression, JavaTypeModel referencedType, int length)
    {
        expression_=expression;
        referencedType_=referencedType;
        elements_=new JavaTraceObjectModel[length];
        //JavaTraceNullModel = JavaTraceObjectsFactory.createNull();
        JavaTraceNullModel nullModel = new JavaTraceNullModel();
        for(int i=0; i<length; ++i) {
            elements_[i]=nullModel;
        }
    }

    public JavaExpressionModel getExpressionModel() throws TermWareException, EntityNotFoundException
    {
        if (expression_==null) {
            lazyInitExpressionModel();
        }
        return expression_;
    }

    public JavaTraceObjectModel getField(String name) throws TermWareException, EntityNotFoundException, EvaluationException {
        if (name.equals("length")) {
            return new JavaIntTraceModel(elements_.length);
        }else{
            throw new EvaluationException("No such field for array("+name+")");
        }
    }

    public JavaTypeModel getType() throws TermWareException, EntityNotFoundException {
        return new JavaArrayTypeModel(referencedType_,
                new JavaTermIntegerLiteralExpressionModel(elements_.length,null,null)
                );
    }

    public boolean hasFields() throws TermWareException, EntityNotFoundException {
        return true;
    }

    public void setField(String name, JavaTraceObjectModel value) throws TermWareException, EntityNotFoundException, EvaluationException {
        throw new EvaluationException("setField on array is not supported");
    }

    private void lazyInitExpressionModel() throws EntityNotFoundException
    {
       // array as expression is literal
       // { x1, x2,  x4 }
       // // may be change to Array Constructor in future.
      try {
       Term listTerm = TermUtils.createNil();
       for(int i=0; i<elements_.length; ++i) {
           Term t = elements_[i].getExpressionModel().getTerm();
           listTerm = TermUtils.createTerm("cons", t, listTerm);
       }
       listTerm = TermUtils.reverseListTerm(listTerm);
       Term arrayInitializer = TermUtils.createTerm("ArrayInitializer", listTerm);
       expression_ = new JavaTermArrayInitializerExpressionModel(arrayInitializer,null,null);
      } catch (TermWareException ex){
          throw new TermWareRuntimeException(ex);
      }
    }

    public void set(int i, JavaTraceObjectModel element) throws EvaluationException
    {
      if (i<0 || i>=elements_.length) {
          throw new InvokedEvaluationException(new IndexOutOfBoundsException());
      }
      elements_[i]=element;
    }

    public JavaTraceObjectModel get(int i) throws EvaluationException
    {
      if (i<0 || i>=elements_.length) {
          throw new InvokedEvaluationException(new IndexOutOfBoundsException());
      }
      return elements_[i];
    }

    private JavaTraceObjectModel[] elements_=null;
    private JavaTermExpressionModel expression_=null;
    private JavaTypeModel           referencedType_=null;

}
