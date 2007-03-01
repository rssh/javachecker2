/*
 * JavaTypeArgumentBoundExpression.java
 *
 * Created on 21 Февраль 2007 г., 20:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ua.gradsoft.javachecker.models;

import java.util.ArrayList;
import java.util.List;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TermWareRuntimeException;

/**
 *Expression, where type arguments are bound.
 *(need for analyzis)
 * @author RSSH
 */
public class JavaTypeArgumentBoundExpressionModel implements JavaExpressionModel
{
    
    /** Creates a new instance of JavaTypeArgumentBoundExpression */
    public JavaTypeArgumentBoundExpressionModel(JavaExpressionModel origin,JavaTypeArgumentBoundStatementModel statement) {
        origin_=origin;
        statement_=statement;
    }
 
    public JavaExpressionKind  getKind()
    { return origin_.getKind(); }
    
    public JavaTypeModel  getType() throws TermWareException, EntityNotFoundException 
    {
      return getSubstitution().substitute(origin_.getType());  
    }
    
    public boolean isType() throws TermWareException, EntityNotFoundException 
    { return origin_.isType(); }
    
    public JavaStatementModel  getStatementModel()
    { return statement_; }
    
    public JavaTypeModel getEnclosedType() 
    {
      try {  
        return getSubstitution().substitute(origin_.getEnclosedType());             
      }catch(TermWareException ex){
          throw new TermWareRuntimeException(ex);
      }
    }

    public List<JavaExpressionModel> getSubExpressions() throws TermWareException, EntityNotFoundException
    {
      List<JavaExpressionModel> originSubExpressions = origin_.getSubExpressions();
      if (originSubExpressions.isEmpty()) {
          return originSubExpressions;
      }else{
          List<JavaExpressionModel> retval = new ArrayList<JavaExpressionModel>(originSubExpressions.size());
          for(JavaExpressionModel e: originSubExpressions) {
              retval.add(new JavaTypeArgumentBoundExpressionModel(e,statement_));
          }
          return retval;
      }
    }
    
    private JavaTypeArgumentsSubstitution getSubstitution()
    {
      return statement_.getArgumentBoundTopLevelBlockModel().getSubstitution();  
    }
    
    /**
     * TypeArgumentBoundExpressionModel(origin,substitution)
     */
    public Term getModelTerm() throws TermWareException, EntityNotFoundException
    {
      Term x=origin_.getModelTerm();
      Term y=TermUtils.createJTerm(getSubstitution());
      Term retval = TermUtils.createTerm("TypeArgumentBoundExpressionModel",x,y);
      return retval;
    }
    
    private JavaExpressionModel origin_;
    private JavaTypeArgumentBoundStatementModel statement_;
            
    
}
