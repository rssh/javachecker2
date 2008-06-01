/*
 */

package ua.gradsoft.javachecker.trace;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.javachecker.models.JavaExpressionModel;
import ua.gradsoft.javachecker.models.JavaMemberVariableModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.javachecker.models.JavaVariableModel;
import ua.gradsoft.javachecker.models.expressions.JavaEqualityOperatorKind;
import ua.gradsoft.javachecker.models.expressions.JavaObjectConstantExpressionModel;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Java trace object
 * @author rssh
 */
public class JavaTraceObjectModel {

  
    
    public JavaTraceObjectModel(JavaVariableModel variableModel)
    {
      variableModel_=variableModel;  
      fields_ = null;  
      andObjectTraceConditions_=null;
      expression_=null;
    }
       
    public JavaVariableModel  getVariableModel()
    {
      return variableModel_;  
    }
    
    public JavaExpressionModel getExpression()
    {
      return expression_;  
    }
    
    public void setExpression(JavaExpressionModel expression)
    {
      expression_=expression;  
      andObjectTraceConditions_=null;
    }
    
    public JavaTraceObjectModel  cloneTraceObjectModel()
    {
       JavaTraceObjectModel retval = new JavaTraceObjectModel(variableModel_);
       if (fields_!=null) {
           retval.fields_ = new TreeMap<String,JavaTraceObjectModel>();
           for(Map.Entry<String,JavaTraceObjectModel> e: fields_.entrySet()) {
               retval.fields_.put(e.getKey(),e.getValue().cloneTraceObjectModel());
           }
       }
       if (andObjectTraceConditions_!=null) {
           retval.andObjectTraceConditions_ = new LinkedList<TraceCondition>();
           retval.andObjectTraceConditions_.addAll(andObjectTraceConditions_);
       }
       retval.expression_ = expression_;
       return retval;
    }
    
    public void setField(String name, JavaTraceObjectModel objectModel) throws TermWareException, EntityNotFoundException, NotSupportedException
    {
      synchronized(this) {  
        if (fields_==null) {
          fields_ = new TreeMap<String,JavaTraceObjectModel>();
        }  
      
        JavaTraceObjectModel om = fields_.get(name);
        JavaTraceObjectModel fom = null;
        JavaMemberVariableModel fvm = null;
        if (om==null) {
            // we have no information
            JavaTypeModel tp = variableModel_.getType();
            fvm = tp.findMemberVariableModel(name);      
            fom = new JavaTraceObjectModel(fvm);
        }else{
            fom = om;
            JavaVariableModel vm = om.getVariableModel();
            if (vm instanceof JavaMemberVariableModel) {
              fvm = (JavaMemberVariableModel)vm;
            }else{
                throw new AssertException("variable model for field must be instance of JavaMemberVarableModel");
            }
        }
        fields_.put(name, fom);            
        fom.assign(objectModel);        
      }
      
    }
    
    public void assign(JavaTraceObjectModel objectModel) throws TermWareException
    {
        synchronized(this) {
           fields_ = objectModel.fields_;
           expression_ = objectModel.expression_;
           andObjectTraceConditions_ = objectModel.andObjectTraceConditions_;
        }
    }
    
    public void merge(JavaTraceObjectModel objectModel) throws TermWareException, ImpossibleTraceMerge
    {
        synchronized(this) {
           if (fields_!=null) {
               if (objectModel.fields_!=null) {
                   for(Map.Entry<String,JavaTraceObjectModel> e: fields_.entrySet()) {
                       JavaTraceObjectModel other = objectModel.fields_.get(e.getKey());
                       if (other!=null) {
                           e.getValue().merge(other);
                       }
                   }
                   for(Map.Entry<String,JavaTraceObjectModel> e: objectModel.fields_.entrySet()) {
                       if (fields_.get(e.getKey())==null) {
                           fields_.put(e.getKey(), e.getValue().cloneTraceObjectModel());
                       }
                   }
               }
           }else if (objectModel.fields_!=null) {
               fields_ = new HashMap<String,JavaTraceObjectModel>();
               for(Map.Entry<String,JavaTraceObjectModel> e: objectModel.fields_.entrySet()) {
                   fields_.put(e.getKey(), e.getValue().cloneTraceObjectModel());
               }               
           }
           
           if (expression_!=null) {
               if (objectModel.expression_!=null) {
                 try{  
                  //TODO: write compatible operations on expression.
                  if (expression_.isConstantExpression() && objectModel.expression_.isConstantExpression()) {
                      JavaObjectConstantExpressionModel ce1 = (JavaObjectConstantExpressionModel)expression_;
                      JavaObjectConstantExpressionModel ce2 = (JavaObjectConstantExpressionModel)objectModel.expression_;
                      Object o1 = ce1.getConstant();
                      Object o2 = ce2.getConstant();
                      if (o1==null) {
                          if (o2!=null) {
                              throw new ImpossibleTraceMerge();
                          }
                      }else if (o2==null) {
                          throw new ImpossibleTraceMerge();
                      }else{
                          if (!o1.equals(o2)) {
                              throw new ImpossibleTraceMerge();
                          }
                      }
                  }else{
                      TraceCondition eqTrc = new EqualityTraceCondition(JavaEqualityOperatorKind.EQUALS, expression_,objectModel.expression_);
                      if (andObjectTraceConditions_==null) {
                          andObjectTraceConditions_ = new LinkedList<TraceCondition>();
                      }
                      andObjectTraceConditions_.add(eqTrc);
                  } 
                 }catch(EntityNotFoundException ex){
                     throw new AssertException("Error during merge",ex);
                 }
               }else{
                   //do nothing/
               }
           }else if(objectModel.expression_!=null) {
               expression_=objectModel.expression_;
           }        
        }
        
        // TODO: make andObjectTraceCondition a set without duplicates.
        //    (write comparator for TraceCondition)
        if (andObjectTraceConditions_!=null) {
            if (objectModel.andObjectTraceConditions_!=null) {
              andObjectTraceConditions_.addAll(objectModel.andObjectTraceConditions_);
            }            
        }else if (objectModel.andObjectTraceConditions_!=null) {
            andObjectTraceConditions_=new LinkedList<TraceCondition>();
            andObjectTraceConditions_.addAll(objectModel.andObjectTraceConditions_);
        }
        
    }
    
    private JavaVariableModel variableModel_;
    
    private Map<String,JavaTraceObjectModel>  fields_;
    
    private List<TraceCondition>  andObjectTraceConditions_;
    
    private JavaExpressionModel expression_;
    
}
