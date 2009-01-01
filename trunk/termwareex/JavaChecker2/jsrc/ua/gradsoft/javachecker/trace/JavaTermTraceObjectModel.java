
package ua.gradsoft.javachecker.trace;

import java.util.Map;
import java.util.TreeMap;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.javachecker.models.JavaExpressionModel;
import ua.gradsoft.javachecker.models.JavaMemberVariableModel;
import ua.gradsoft.javachecker.models.JavaTermExpressionModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.javachecker.models.expressions.JavaObjectConstantExpressionModel;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *TraceObject, created form TermExpression
 * @author rssh
 */
public class JavaTermTraceObjectModel implements JavaTraceObjectModel
{

    public JavaTermTraceObjectModel(JavaTermExpressionModel expression) {
        expression_=expression;
    }

    public JavaExpressionModel getExpressionModel()
    { return expression_; }

    public JavaTypeModel getType() throws TermWareException, EntityNotFoundException
    { if (type_==null) {
        type_ =  expression_.getType();
      }
      return type_;
    }

    public boolean hasFields() throws TermWareException, EntityNotFoundException
    {
       return getType().hasMemberVariableModels();
    }

    public JavaTraceObjectModel getField(String name) throws TermWareException, EntityNotFoundException
    {
      lazyInitFields();
      JavaTraceObjectModel retval = fields_.get(name);
      if (retval==null) {
          // try to deduce.
          retval = lazyInitField(name);
      }
      return retval;
    }

    public void setField(String name, JavaTraceObjectModel value)
    {
        //TODO: check types.
      fields_.put(name, value);
    }

    private void lazyInitFields()
    {
      if (fields_==null) {
          fields_=new TreeMap<String,JavaTraceObjectModel>();
      }
    }

    private JavaTraceObjectModel lazyInitField(String name) throws TermWareException, EntityNotFoundException
    {
      lazyInitFields();
      JavaMemberVariableModel mv = null;
      JavaTraceObjectModel retval = null;
      try {
         mv = getType().getMemberVariableModels().get(name);
      }catch(NotSupportedException ex){
          throw new AssertException("member variables are not supported for type "+getType().getName());
      }
      if (mv==null) {
          throw new AssertException("field "+name+" does not exists in "+getType().getName());
      }
      if (mv.isSupportInitializerExpression()) {
          if (mv.getInitializerExpression()!=null) {
              JavaExpressionModel te = mv.getInitializerExpression();
              if (te instanceof JavaTermExpressionModel) {
                  JavaTermExpressionModel tte = (JavaTermExpressionModel)te;
                  retval = new JavaTermTraceObjectModel(tte);
              }else if (te instanceof JavaObjectConstantExpressionModel) {
                  JavaObjectConstantExpressionModel oce = (JavaObjectConstantExpressionModel)te;
                  retval = new JavaObjectConstantTraceObjectModel(oce);
              }else{
                  throw new AssertException("Can't find type of initialize expression");
              }
          }else{
              JavaExpressionModel e = mv.getType().getDefaultInitializerExpression();
              if (e instanceof JavaTermExpressionModel) {
                  retval = new JavaTermTraceObjectModel((JavaTermExpressionModel)e);
              }else if(e instanceof JavaObjectConstantExpressionModel){
                  retval = new JavaObjectConstantTraceObjectModel((JavaObjectConstantExpressionModel)e);
              }else{
                  throw new AssertException("Can't find type of default initializer expression");
              }
          }
      }else{
          JavaExpressionModel e = mv.getType().getDefaultInitializerExpression();
          if (e instanceof JavaTermExpressionModel) {
                  retval = new JavaTermTraceObjectModel((JavaTermExpressionModel)e);
          }else if(e instanceof JavaObjectConstantExpressionModel){
                  retval = new JavaObjectConstantTraceObjectModel((JavaObjectConstantExpressionModel)e);
          }else{
                  throw new AssertException("Can't find type of default initializer expression");
          }
      }
      fields_.put(name, retval);
      return retval;
    }

    private JavaTermExpressionModel expression_;
    private JavaTypeModel           type_=null;
    private Map<String, JavaTraceObjectModel> fields_;

}
