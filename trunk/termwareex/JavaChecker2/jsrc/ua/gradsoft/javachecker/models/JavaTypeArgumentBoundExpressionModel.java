/*
 * JavaTypeArgumentBoundExpression.java
 *
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
        enclosedType_=null;
    }

    /** construct expression outside statement **/
    public JavaTypeArgumentBoundExpressionModel(JavaExpressionModel origin,JavaTypeArgumentBoundTypeModel enclosedType) {
        origin_=origin;
        statement_=null;
        enclosedType_=enclosedType;
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
      if (enclosedType_==null)  {
        try {  
          return getSubstitution().substitute(origin_.getEnclosedType());             
        }catch(TermWareException ex){
          throw new TermWareRuntimeException(ex);
        }
      }
      return enclosedType_;
    }

    public List<JavaExpressionModel> getSubExpressions() throws TermWareException, EntityNotFoundException
    {
      List<JavaExpressionModel> originSubExpressions = origin_.getSubExpressions();
      if (originSubExpressions.isEmpty()) {
          return originSubExpressions;
      }else{
          List<JavaExpressionModel> retval = new ArrayList<JavaExpressionModel>(originSubExpressions.size());
          for(JavaExpressionModel e: originSubExpressions) {
              retval.add(
                      statement_!=null ? new JavaTypeArgumentBoundExpressionModel(e,statement_)
                                       : new JavaTypeArgumentBoundExpressionModel(e,enclosedType_));
          }
          return retval;
      }
    }
    
    private JavaTypeArgumentsSubstitution getSubstitution() throws TermWareException
    {
      if (statement_!=null) {  
        return statement_.getArgumentBoundTopLevelBlockModel().getSubstitution();  
      }else{
        return enclosedType_.getSubstitution();  
      }
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
    private JavaTypeArgumentBoundTypeModel     enclosedType_;
            
    
}
