/*
 * JavaTermSwitchConstantExpressionModel.java
 *
 */

package ua.gradsoft.javachecker.models.expressions;

import java.util.Collections;
import java.util.List;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.models.JavaExpressionKind;
import ua.gradsoft.javachecker.models.JavaExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermStatementModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.javachecker.models.TermUtils;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Model for constant expression in switch
 * @author RSSH
 */
public class JavaTermSwitchConstantExpressionModel extends JavaTermExpressionModel
{

    public JavaTermSwitchConstantExpressionModel(JavaExpressionModel switchExpr, Term t, JavaTermStatementModel st, JavaTypeModel enclosedType) throws TermWareException
    {
      super(t,st,enclosedType);
      switchExpr_=switchExpr;
      isDefault_ = (t.isAtom() && t.getName().equals("default"));
    }
    
    public JavaExpressionKind  getKind()
    { return JavaExpressionKind.SWITCH_CONSTANT; }
    
    public JavaTypeModel getType() throws TermWareException, EntityNotFoundException
    { return switchExpr_.getType(); }
    
    public boolean isType()
    { return false; }
    
    
    public List<JavaExpressionModel> getSubExpressions()
    { if (isDefault()) {
        return Collections.emptyList(); 
      }else{
          return Collections.singletonList(switchExpr_);
      }
    }
    
    public boolean isDefault()
    { return isDefault_; }
    
    /**
     * CaseConstantModel(t, @type)
     */
    public Term getModelTerm() throws  TermWareException, EntityNotFoundException
    {
      Term jtype = TermUtils.createJTerm(getType());
      Term retval = TermUtils.createTerm("CaseConstantModel",t_,jtype);
      return retval;
    }
    
    
    private JavaExpressionModel  switchExpr_;
    private boolean              isDefault_;
    
    
    
}
