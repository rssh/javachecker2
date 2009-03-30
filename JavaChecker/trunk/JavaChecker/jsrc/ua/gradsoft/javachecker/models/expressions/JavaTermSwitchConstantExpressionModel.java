/*
 * JavaTermSwitchConstantExpressionModel.java
 *
 */

package ua.gradsoft.javachecker.models.expressions;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.javachecker.models.InvalidJavaTermException;
import ua.gradsoft.javachecker.models.JavaEnumConstantModel;
import ua.gradsoft.javachecker.models.JavaExpressionKind;
import ua.gradsoft.javachecker.models.JavaExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermStatementModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.javachecker.models.TermUtils;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

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
      if(! t.getName().equals("SwitchLabel")) {
          throw new AssertException("SwitchLabel required instead "+TermHelper.termToString(t));
      }
      Term lt = t.getSubtermAt(0);
      isDefault_ = (lt.isAtom() && lt.getName().equals("default"));     
      labelExpr_=null;
    }
    
    public JavaExpressionKind  getKind()
    { return JavaExpressionKind.SWITCH_CONSTANT; }
    
    public JavaTypeModel getType() throws TermWareException, EntityNotFoundException
    { return switchExpr_.getType(); }
    
    public boolean isType()
    { return false; }
    
    
    public List<JavaExpressionModel> getSubExpressions() throws TermWareException, EntityNotFoundException
    { 
      if (isDefault()) {
        return Collections.emptyList(); 
      }else{
          lazyInitLabelExpr();
          return Collections.singletonList(labelExpr_);
      }
    }
    
    public boolean isConstantExpression()
    {
        return true;
    }
    
    public boolean isDefault()
    { return isDefault_; }
    
    /**
     * CaseConstantModel(SwitchLabel(labelModel), labelIdentifier|expr|default, @type, ctx)
     */
    public Term getModelTerm() throws  TermWareException, EntityNotFoundException
    {
      lazyInitLabelExpr();  
      Term jtype = TermUtils.createJTerm(getType());      
      Term tctx = TermUtils.createJTerm(createPlaceContext());
      Term retval = null;
      if (labelExpr_==null) {
        retval = TermUtils.createTerm("CaseConstantModel",t_,t_.getSubtermAt(0),jtype,tctx);
      } else {
        Term modelTerm = labelExpr_.getModelTerm();
        Term expr = null;
        if (labelExpr_.getType().isEnum()) {
            if (modelTerm.getName().equals("StaticFieldModel")) {
                //StaticFieldModel(typeRef,identifier,memberVariable,ctx)
                expr = modelTerm.getSubtermAt(1);                
            }else{
                throw new AssertException("StaticField required, have:"+TermHelper.termToString(modelTerm));
            }
        }else{
            expr = t_.getSubtermAt(0);
        }
        Term switchLabel = TermUtils.createTerm("SwitchLabel",modelTerm);          
        retval = TermUtils.createTerm("CaseConstantModel",switchLabel,expr,jtype,tctx);  
      }
      return retval;
    }

    private void lazyInitLabelExpr() throws TermWareException, EntityNotFoundException
    {
      if (isDefault_) {
          return;
      }else{
          if (!t_.getName().equals("SwitchLabel")) {
              throw new AssertException("Invalid switch label:"+TermHelper.termToString(t_));
          }
          Term labelTerm = t_.getSubtermAt(0);
          if (switchExpr_.getType().isEnum()) {
              // we must resolve enum constants here.
              JavaTypeModel tm = switchExpr_.getType();
              Map<String,JavaEnumConstantModel> encs = null;
              encs = tm.getEnumConstantModels();
              if (!labelTerm.getName().equals("Identifier")) {
                  throw new InvalidJavaTermException("Identifer required",labelTerm);
              }
              String evs = labelTerm.getSubtermAt(0).getString();
              JavaEnumConstantModel cexpr = encs.get(evs);
              if (cexpr==null) {
                  throw new InvalidJavaTermException("Invalid constant for type "+switchExpr_.getType()+":"+TermHelper.termToString(labelTerm),labelTerm);
              }else{
                  Term nct = TermUtils.createTerm("StaticField",switchExpr_.getType().getFullNameAsTerm(),labelTerm);
                  labelExpr_ = new JavaTermStaticFieldExpressionModel(cexpr,nct,getTermStatementModel(),getEnclosedType());
              }
          }else{
              labelExpr_=JavaTermExpressionModel.create(labelTerm,getTermStatementModel(),getEnclosedType());
          }
      }  
    }
  
    private JavaExpressionModel  switchExpr_;
    private JavaExpressionModel  labelExpr_;
    private boolean              isDefault_;
    
    
    
}
