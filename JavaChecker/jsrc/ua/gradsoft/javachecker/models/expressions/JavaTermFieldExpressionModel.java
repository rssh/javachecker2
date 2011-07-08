/*
 * JavaTermFieldExpressionModel.java
 *
 */

package ua.gradsoft.javachecker.models.expressions;

import java.util.Collections;
import java.util.List;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.JUtils;
import ua.gradsoft.javachecker.models.JavaExpressionKind;
import ua.gradsoft.javachecker.models.JavaExpressionModel;
import ua.gradsoft.javachecker.models.JavaMemberVariableModel;
import ua.gradsoft.javachecker.models.JavaResolver;
import ua.gradsoft.javachecker.models.JavaTermExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermStatementModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.javachecker.models.TermUtils;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Field($x,$y)
 * @author Ruslan Shevchenko
 */
public class JavaTermFieldExpressionModel extends JavaTermExpressionModel
{
    
    /** Creates a new instance of JavaTermFieldExpressionModel
     *
     * @param t - term, must be Field(expr-term,identifier)
     * @param st
     * @param enclosedType
     * @throws ua.gradsoft.termware.TermWareException
     */
    public JavaTermFieldExpressionModel(Term t, JavaTermStatementModel st, JavaTypeModel enclosedType) throws TermWareException
    {
      super(t,st,enclosedType);  
      Term objectOrTypeTerm = t.getSubtermAt(0);
      objectOrType_ = JavaTermExpressionModel.create(objectOrTypeTerm,st,enclosedType);      
    }
    
    /** Creates a new instance of JavaTermFieldExpressionModel, when object inside field
     * is already resolved.
     */
    public JavaTermFieldExpressionModel(JavaTermExpressionModel subExpr, Term t, JavaTermStatementModel st, JavaTypeModel enclosedType) throws TermWareException
    {
      super(t,st,enclosedType);        
      objectOrType_ = subExpr;
    }
    
    
    public JavaExpressionKind  getKind()
    { return JavaExpressionKind.FIELD; }
    
    public JavaTypeModel getType() throws TermWareException, EntityNotFoundException
    { lazyInitFieldModel();
      return fieldModel_.getType();
    }
    
    public boolean isType()
    {
      return false;  
    }
    
    public List<JavaExpressionModel> getSubExpressions()
    {
       return Collections.<JavaExpressionModel>singletonList(objectOrType_); 
    }
    
    public JavaMemberVariableModel getFieldModel() throws TermWareException, EntityNotFoundException
    {
        lazyInitFieldModel();
        return fieldModel_;
    }
    
    /**
     *  FieldModel(objectOrType,identifier,fieldModel,ctx)
     */
    public Term  getModelTerm()  throws TermWareException, EntityNotFoundException
    {
        lazyInitFieldModel();
        Term x = objectOrType_.getModelTerm();
        Term y = t_.getSubtermAt(1);
        Term fmTerm = TermUtils.createJTerm(fieldModel_);
        Term tctx = TermUtils.createJTerm(createPlaceContext());
        Term retval = TermUtils.createTerm("FieldModel",x,y,fmTerm,tctx);
        return retval;
    }
    
    public boolean isConstantExpression() throws TermWareException, EntityNotFoundException
    {
      lazyInitFieldModel();
      if (objectOrType_.isConstantExpression()) {
          return true;
      }else if (fieldModel_.getModifiers().isFinal()) {
          if (fieldModel_.isSupportInitializerExpression()) {
              JavaExpressionModel initializerExpression = fieldModel_.getInitializerExpression();
              return initializerExpression.isConstantExpression();
          }else{
              return false;
          }
      }else{
          return false;
      }
    }
        
    
    
    
    private void lazyInitFieldModel() throws TermWareException, EntityNotFoundException
    {
      if (fieldModel_==null)  {
          JavaTypeModel tm = objectOrType_.getType();
          String name = t_.getSubtermAt(1).getSubtermAt(0).getString();
          try {
            fieldModel_=JavaResolver.resolveMemberVariableByName(name,tm);
          }catch(EntityNotFoundException ex){
              ex.setFileAndLine(JUtils.getFileAndLine(t_));
              throw ex;
          }
      }
    }
    
    
    
    private JavaMemberVariableModel fieldModel_=null;
    private JavaTermExpressionModel objectOrType_;
}
