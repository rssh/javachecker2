
package ua.gradsoft.javachecker.trace;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.models.InvalidJavaExpressionModelException;
import ua.gradsoft.javachecker.models.JavaExpressionModel;
import ua.gradsoft.javachecker.models.JavaFormalParameterModel;
import ua.gradsoft.javachecker.models.JavaLocalVariableModel;
import ua.gradsoft.javachecker.models.JavaMemberVariableModel;
import ua.gradsoft.javachecker.models.JavaPlaceContext;
import ua.gradsoft.javachecker.models.JavaVariableModel;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Trace context, which can keep stat of variables.
 * @author rssh
 */
public class JavaTraceContext {
    
    public JavaTraceContext(JavaPlaceContext where)
    {
      where_=where;
      localVariables_=null;
      memberVariables_=null;
    }                
    
    /**
     * 
     *  merge traceContext with other 
     */
    public void merge(JavaTraceContext other) throws  TermWareException, ImpossibleTraceMerge
    {
      if (localVariables_!=null) {
          for(Map.Entry<String,JavaTraceObjectModel> e: localVariables_.entrySet() ) {
              JavaTraceObjectModel ov = other.localVariables_.get(e.getKey());
              if (ov!=null) {
                  e.getValue().merge(ov);
              }              
          }
          if (other.localVariables_!=null) {
            for(Map.Entry<String,JavaTraceObjectModel> e: other.localVariables_.entrySet()) {
                if (this.getLocalVariableValue(e.getKey())==null) {
                    localVariables_.put(e.getKey(), e.getValue().cloneTraceObjectModel());
                }              
            }
          }
      }else if (other.localVariables_!=null) {
          localVariables_=new HashMap<String,JavaTraceObjectModel>();
          for(Map.Entry<String,JavaTraceObjectModel> e: other.localVariables_.entrySet()) {
              localVariables_.put(e.getKey(), e.getValue().cloneTraceObjectModel());
          }
      }

      if (formalParameters_!=null) {
          for(Map.Entry<String,JavaTraceObjectModel> e: formalParameters_.entrySet() ) {
              JavaTraceObjectModel ov = other.formalParameters_.get(e.getKey());
              if (ov!=null) {
                  e.getValue().merge(ov);
              }              
          }
          if (other.formalParameters_!=null) {
            for(Map.Entry<String,JavaTraceObjectModel> e: other.formalParameters_.entrySet()) {
                if (this.getLocalVariableValue(e.getKey())==null) {
                    formalParameters_.put(e.getKey(), e.getValue().cloneTraceObjectModel());
                }              
            }
          }
      }else if (other.formalParameters_!=null) {
          formalParameters_=new HashMap<String,JavaTraceObjectModel>();
          for(Map.Entry<String,JavaTraceObjectModel> e: other.formalParameters_.entrySet()) {
              formalParameters_.put(e.getKey(), e.getValue().cloneTraceObjectModel());
          }
      }
      
      if (memberVariables_!=null) {
          for(Map.Entry<String,JavaTraceObjectModel> e: memberVariables_.entrySet() ) {
              JavaTraceObjectModel ov = other.memberVariables_.get(e.getKey());
              if (ov!=null) {
                  e.getValue().merge(ov);
              }              
          }
          if (other.memberVariables_!=null) {
            for(Map.Entry<String,JavaTraceObjectModel> e: other.memberVariables_.entrySet()) {
                if (this.getLocalVariableValue(e.getKey())==null) {
                    memberVariables_.put(e.getKey(), e.getValue().cloneTraceObjectModel());
                }              
            }
          }
      }else if (other.memberVariables_!=null) {
          memberVariables_=new HashMap<String,JavaTraceObjectModel>();
          for(Map.Entry<String,JavaTraceObjectModel> e: other.memberVariables_.entrySet()) {
             memberVariables_.put(e.getKey(), e.getValue().cloneTraceObjectModel());
          }
      }
      
      
    }
    
    public Object getLocalVariableValue(String name)
    {
        if (localVariables_!=null) {
          return localVariables_.get(name);
        }else{
            return null;
        }
    }
    
    public JavaPlaceContext where()
    { return where_; }
    
    public void  evalAssigment(JavaVariableModel variable, JavaExpressionModel expr) throws TermWareException
    {
       switch(variable.getKind()) {
           case MEMBER_VARIABLE:
           {
               evalVariableAssigment(variable,memberVariables_,expr);
           }
           break;
           case LOCAL_VARIABLE:
           {
               evalVariableAssigment(variable,localVariables_,expr);
           }
           break;
           case FORMAL_PARAMETER:
           {
               evalVariableAssigment(variable,formalParameters_,expr);
           }
           break;
           default:
               throw new AssertException("Unknown variable kind:"+variable.getKind());
       }
    }
    
    private void evalVariableAssigment(JavaVariableModel v, Map<String,JavaTraceObjectModel> state, JavaExpressionModel expr) throws TermWareException
    {
       synchronized(this) {              
          JavaTraceObjectModel om = state.get(v.getName());
          JavaExpressionModel rexpr = reduce(expr);
          if (om==null) {
             JavaTraceObjectModel newTraceObject = new JavaTraceObjectModel(v);
             newTraceObject.setExpression(rexpr);
             state.put(v.getName(), newTraceObject);
          }else{
             om.setExpression(rexpr);     
          }
       }        
    }
    
    public void evalAssigment(JavaExpressionModel left, JavaExpressionModel rigth) throws TermWareException, EntityNotFoundException
    {
      switch(left.getKind())  {
          case EXPRESSION:
              evalAssigment(left.getSubExpressions().get(0),rigth);
              break;
          case ASSIGMENT:
              evalAssigment(left.getSubExpressions().get(1),rigth);
              evalAssigment(left.getSubExpressions().get(0),left.getSubExpressions().get(1));
              break;
          case CONDITIONAL:
          case CONDITIONAL_OR:
          case CONDITIONAL_AND:
          case INCLUSIVE_OR:
          case EXCLUSIVE_OR:
          case AND:
          case EQUALITY:
          case INSTANCEOF:
          case RELATIONAL:
          case SHIFT:
          case ADDITIVE:
          case MULTIPLICATIVE:
          case UNARY:
          case PREINCREMENT:
          case PREDECREMENT:
          case CAST:
          case POSTFIX:
          case INTEGER_LITERAL:
          case FLOATING_POINT_LITERAL:
          case CHARACTER_LITERAL:
          case STRING_LITERAL:
          case BOOLEAN_LITERAL:
          case NULL_LITERAL:
             throw new InvalidJavaExpressionModelException("invalid expression for left part of assiment",left);
//          case THIS:
//  THIS_PREFIX,
//    SUPER,
//    SUPER_PREFIX,
//    CLASS_LITERAL,
//    ALLOCATION_EXPRESSION,
//    INNER_ALLOCATION,
//    IDENTIFIER,
//    FUNCTION_CALL,
//    METHOD_CALL,
//    SPECIALIZED_METHOD_CALL,            
//    FIELD,
//    ARRAY_INDEX,
//    NAME,
//    TYPE_NAME,
//    TYPE_FIELD,
//    STATIC_FIELD,
//    ARRAY_INITIALIZER,
//    SWITCH_CONSTANT,
//    OBJECT_CONSTANT,     
//    ANNOTATION,
//    ANNOTATION_MEMBER_VALUE_ARRAY_INITIALIZER
          default:
              throw new AssertException("Not implemented");
      }
    }
            
    
    public JavaExpressionModel reduce(JavaExpressionModel expr) throws TermWareException
    {
        return expr;
    }
    
    public JavaTraceContext cloneTraceContext() throws TermWareException
    {
      JavaTraceContext retval = new JavaTraceContext(this.where_);
      retval.localVariables_ = new HashMap<String,JavaTraceObjectModel>();
      for(Map.Entry<String,JavaTraceObjectModel> le: localVariables_.entrySet()) {
          JavaTraceObjectModel te = le.getValue().cloneTraceObjectModel();
          retval.localVariables_.put(le.getKey(), te);
      }
      retval.formalParameters_ = new HashMap<String,JavaTraceObjectModel>();
      for(Map.Entry<String,JavaTraceObjectModel> fe: formalParameters_.entrySet()) {
          JavaTraceObjectModel te = fe.getValue().cloneTraceObjectModel();
          retval.formalParameters_.put(fe.getKey(), te);
      }
      retval.memberVariables_ = new HashMap<String,JavaTraceObjectModel>();
      for(Map.Entry<String,JavaTraceObjectModel> me: memberVariables_.entrySet()) {
          JavaTraceObjectModel te = me.getValue().cloneTraceObjectModel();
          retval.memberVariables_.put(me.getKey(), te);
      } 
      return retval;
    }
    
    
    private HashMap<String,JavaTraceObjectModel> localVariables_;
    private HashMap<String,JavaTraceObjectModel> formalParameters_;
    private HashMap<String,JavaTraceObjectModel> memberVariables_; 
    
    private JavaPlaceContext where_;
    
}
