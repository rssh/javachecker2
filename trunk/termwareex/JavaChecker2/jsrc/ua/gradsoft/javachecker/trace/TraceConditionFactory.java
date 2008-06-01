
package ua.gradsoft.javachecker.trace;

import java.util.List;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.models.InvalidJavaExpressionModelException;
import ua.gradsoft.javachecker.models.JavaExpressionKind;
import ua.gradsoft.javachecker.models.JavaExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaObjectConstantExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermBooleanLiteralExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermEqualityExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermInstanceOfExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermThisExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermUnaryExpressionModel;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Factory fro trace conditions
 * @author rssh
 */
public class TraceConditionFactory {
    
    public TraceCondition  createFromExpressionModel(JavaExpressionModel expr, JavaTraceContext traceContext) throws TermWareException, EntityNotFoundException
    {     
        
      switch(expr.getKind()) {
          case EXPRESSION:
          {
              List<JavaExpressionModel> subs = expr.getSubExpressions();
              if (subs.size()!=1) {                                   
                  throw new InvalidJavaExpressionModelException("Invalid expression: kind is EXPRESSION but subexpressions.size()!=1",expr);
              }else{
                  return createFromExpressionModel(subs.get(0),traceContext);
              }                   
          }
          case ASSIGMENT:
          {
              // value of right part.
              List<JavaExpressionModel> subs = expr.getSubExpressions();
              if (subs.size()!=2) {
                  throw new InvalidJavaExpressionModelException("Invalid expression: kind is ASSIGMENT but subexpressions.size()!=2",expr);
              }
              JavaExpressionModel left = subs.get(0);
              JavaExpressionModel right = subs.get(1);
              traceContext.evalAssigment(left, right);
             
              return createFromExpressionModel(right,traceContext);
          }
          case CONDITIONAL:
          {
              List<JavaExpressionModel> subs = expr.getSubExpressions();              
              JavaExpressionModel r = traceContext.reduce(subs.get(0));              
              switch (r.getKind()) {
                  case BOOLEAN_LITERAL:
                  {
                      JavaTermBooleanLiteralExpressionModel bl = (JavaTermBooleanLiteralExpressionModel)r;
                      if (bl.getBoolean()) {
                          return createFromExpressionModel(subs.get(1),traceContext);
                      }else{
                          return createFromExpressionModel(subs.get(2),traceContext);
                      }
                  }
                  case OBJECT_CONSTANT:
                  {
                      JavaObjectConstantExpressionModel cn = (JavaObjectConstantExpressionModel)r;
                      Object o = cn.getConstant();
                      if (o instanceof Boolean) {
                          Boolean b = (Boolean)o;
                          if (b) {
                              return createFromExpressionModel(subs.get(1),traceContext);
                          }else{
                              return createFromExpressionModel(subs.get(0),traceContext);
                          }
                      }else{
                          throw new InvalidJavaExpressionModelException("condition is evaluated to non-boolean constant",expr);
                      }                              
                  }
                  default:
                  // so - we don't know how thie expression will be evaluated: create OR node.
                  {
                      //return new ConditionalTraceCondition(subs.get(0),subs.get(1),subs.get(2));
                      throw new AssertException("ConditionalTraceException is not implemented");
                  }
              }//
          }          
          case  CONDITIONAL_AND:
          {
              List<JavaExpressionModel> sexprs = expr.getSubExpressions();
              TraceCondition first = createFromExpressionModel(sexprs.get(0),traceContext);
              TraceCondition second = createFromExpressionModel(sexprs.get(1),traceContext);
              return new AndTraceCondition(first,second);
          }        
          case  CONDITIONAL_OR:
          {
              List<JavaExpressionModel> sexprs = expr.getSubExpressions();
              JavaTraceContext firstContext = traceContext;
              JavaTraceContext secondContext = traceContext;              
              TraceCondition first = createFromExpressionModel(sexprs.get(0),firstContext);
              TraceCondition second = createFromExpressionModel(sexprs.get(1),secondContext);
              return new OrTraceCondition(first,second);
          }
          case INCLUSIVE_OR:
          case EXCLUSIVE_OR:
          case AND:              
              throw new AssertException("Invalid kind for boolean expression:"+expr.getKind());
          case EQUALITY:
          {
            try {  
              JavaTermEqualityExpressionModel e = (JavaTermEqualityExpressionModel)expr;
              List<JavaExpressionModel> subs = expr.getSubExpressions();
              return new EqualityTraceCondition(e.getEqualityOperatorKind(),subs.get(0),subs.get(1));
            }catch(EntityNotFoundException ex){
                throw new AssertException("Exception during analyzing of expression",ex);
            }
          }            
          case INSTANCEOF:
          {
              JavaTermInstanceOfExpressionModel texpr = (JavaTermInstanceOfExpressionModel)expr;
              return new InstanceOfTraceCondition(texpr);
          }              
          case RELATIONAL:
              throw new AssertException("RelationalTraceCondition is not implemented");
              //return new RelationalTraceCondition(expr,traceContext);
          case SHIFT:
          case ADDITIVE:
          case MULTIPLICATIVE:
              throw new AssertException("Invalid kind for boolean expression:"+expr.getKind());
          case UNARY:
          {
              JavaTermUnaryExpressionModel uexpr = (JavaTermUnaryExpressionModel)expr;
              switch(uexpr.getUnaryOperatorKind()) {
                  case NOT:
                      return createNotFromExpressionModel(uexpr.getSubExpressions().get(0),traceContext);
                  default:
                       throw new InvalidJavaExpressionModelException("invalid unary operatokl kind for boolean expression",expr);                      
              }
          }
          case PREINCREMENT:
          case PREDECREMENT:
          case CAST:
          case POSTFIX:
          case INTEGER_LITERAL:
          case FLOATING_POINT_LITERAL:
          case CHARACTER_LITERAL:
          case STRING_LITERAL:
            throw new InvalidJavaExpressionModelException("invalid kind for boolean expression",expr);                          
          case BOOLEAN_LITERAL:
          {
              JavaTermBooleanLiteralExpressionModel bexpr = (JavaTermBooleanLiteralExpressionModel)expr;
              if (bexpr.getBoolean()) {
                  return new TrueTraceCondition(bexpr.getTermStatementModel(),bexpr.getEnclosedType());
              }else{
                  return new FalseTraceCondition(bexpr.getTermStatementModel(),bexpr.getEnclosedType());
              }
          }
          case NULL_LITERAL:
              throw new InvalidJavaExpressionModelException("invalid kind for boolean expression",expr);
   //       case THIS:    
   //       {
   //           JavaTermThisExpressionModel texpr = (JavaTermThisExpressionModel)expr;
   //           if (JavaTypeModelHelper.isBoolean(texpr.getType())) {
   //               TraceContext changedTraceContext = new 
   //               return new ContextChangedTraceCondition( texpr.createPlaceContext());
   //               texpr.createPlaceContext();
   //               return new ThisTraceCondition(texpr.getPlaceContext(),traceContext);
   //           }else{
   //               throw new InvalidJavaExpressionModelException("invalid type for boolean expression",expr);
   //           }                      
   //       }
          default:
              throw new AssertException("Unknown or unimplemented kind:"+expr.getKind());
      }  
    }

    public TraceCondition  createNotFromExpressionModel(JavaExpressionModel expr, JavaTraceContext traceContext) throws TermWareException, EntityNotFoundException
    {
      switch(expr.getKind()) {
          case EXPRESSION:
              List<JavaExpressionModel> subs = expr.getSubExpressions();
              if (subs.size()!=1) {                                   
                  throw new InvalidJavaExpressionModelException("Invalid expression: kind is EXPRESSION but subexpressions.size()!=1",expr);
              }else{
                  return createNotFromExpressionModel(subs.get(0),traceContext);
              }                   
              // TODO:
          default:
              throw new AssertException("Unknown or unimplemented kind:"+expr.getKind());                            
      }  
    }
    

}
