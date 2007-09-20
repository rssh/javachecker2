/*
 * JavaTraceContext.java
 *
 * Created on August 26, 2007, 3:40 PM
 *
 */

package ua.gradsoft.javachecker.models;

import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import ua.gradsoft.javachecker.PartialConditionResult;
import ua.gradsoft.javachecker.annotations.Nullable;
import ua.gradsoft.termware.Term;

/**
 *This class keep context of program trace.
 * @author rssh
 */
public class JavaTraceContext {
    
    /** Creates a new instance of JavaTraceContext */
    public JavaTraceContext(JavaPlaceContext place) {
        place_=place;
        previous_=null;
        variables_ = new TreeMap<JavaVariableModel,JavaExpressionModel>(JavaVariableModelComparator.INSTANCE);
        forsedConditionsCNF_ = new LinkedList<Term>();
        typeSubstitution_ = new JavaTypeArgumentsSubstitution();
    }
    
    /**
     * return expression for variable v or null if one was not defined.
     */
    @Nullable
    public  JavaExpressionModel getVariable(JavaVariableModel v)
    { return variables_.get(v); }
    
    public  void  setVariable(JavaVariableModel v,JavaExpressionModel expr)
    {
      variables_.put(v,expr);  
    }
            
    public  boolean isDefined(JavaVariableModel v)
    {
        return variables_.containsKey(v);
    }
    
    /*
    public void addForsedCondition(JavaExpressionModel condition)
    {
       condition.forse(this); 
       List<Term> newCNF = createCNF(condition.getModelTerm());
       forsedConditionsCNF_.addAll(newCNF);
    }
    
    public PartialConditionResult checkInForsed(JavaExpressionModel condition)
    {
       PartialConditionResult retval = PartialConditionResult.UNKNOWN; 
       List<Term> conditionCNF =  createCNF(condition.getModelTerm());
       boolean isFirst=true;
       for(Term cn: conditionCNF) {
           if (isFirst) {
               retval=PartialConditionResult.TRUE;
               isFirst=false;
           }
           PartialConditionResult r = PartialConditionResult.UNKNOWN;
           for(Term forsed: forsedConditionsCNF) {
               r = checkInCNF(forsed,cn);
               if (r==PartialConditionResult.FALSE) {
                   return r;
               }
               if (r==PartialConditionResult.TRUE) {               
                   break;
               }
           } 
           if (retval==PartialConditionResult.TRUE) {
               if (r==PartialConditionResult.TRUE) {
                   // do nothing.
               }else if (r==PartialConditionResult.UNKNOWN) {
                   retval=PartialConditionResult.UNKNOWN;
               }else {
                   retval=r;
                   break;
               }               
           }else if (retval==PartialConditionResult.UNKNOWN) {
               if (r==PartialConditionResult.TRUE || r==PartialConditionResult.TRUE) {
                   // do nothing.
               }else{
                   retval=r;
                   break;
               }
           }
       }       
       return retval;
    }
     */
              
    public JavaTypeArgumentsSubstitution getTypeSubstitution()
    {
        return typeSubstitution_;
    }
    
    public void addTypeSubstitution(JavaTypeArgumentsSubstitution s)
    {
      typeSubstitution_.putAll(s);  
    }
    
    
    
    private JavaTraceContextVariableDependency dependencies_;
    
    private TreeMap<JavaVariableModel,JavaExpressionModel> variables_;
    
    private List<Term>  forsedConditionsCNF_;
    
    private JavaTypeArgumentsSubstitution typeSubstitution_;
    
    private JavaPlaceContext place_;
    private JavaTraceContext previous_;
}
