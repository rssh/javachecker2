/*
 * JavaTermArrayInitializerExpressionModel.java
 * 
 */

package ua.gradsoft.javachecker.models.expressions;

import java.util.ArrayList;
import java.util.List;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.models.JavaArrayTypeModel;
import ua.gradsoft.javachecker.models.JavaExpressionKind;
import ua.gradsoft.javachecker.models.JavaExpressionModel;
import ua.gradsoft.javachecker.models.JavaPrimitiveTypeModel;
import ua.gradsoft.javachecker.models.JavaResolver;
import ua.gradsoft.javachecker.models.JavaTermExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermStatementModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.javachecker.models.TermUtils;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Array initialize.
 * @author RSSH
 */
public class JavaTermArrayInitializerExpressionModel extends JavaTermExpressionModel
{
    
    /** Creates a new instance of JavaTermArrayInitializerExpressionModel */
    public JavaTermArrayInitializerExpressionModel(Term t, JavaTermStatementModel st, JavaTypeModel enclosedType) throws TermWareException
    {
      super(t,st,enclosedType);
      Term l = t.getSubtermAt(0);  
      subexpressions_=new ArrayList<JavaExpressionModel>();
      while(!l.isNil()) {
          Term ct=l.getSubtermAt(0);
          l=l.getSubtermAt(1);
          JavaTermExpressionModel e = JavaTermExpressionModel.create(ct,st,enclosedType);
          subexpressions_.add(e);
      }
    }
    
    public JavaExpressionKind  getKind()
    { return JavaExpressionKind.ARRAY_INITIALIZER; }
 
    public boolean isType()
    { return false; }
 
    public JavaTypeModel  getType() throws TermWareException, EntityNotFoundException
    { 
      JavaTypeModel referencedType=null;  
      if (subexpressions_.size()==0) {
          referencedType = JavaResolver.resolveJavaLangObject();          
      }else{
          referencedType = subexpressions_.get(0).getType();
      }
      return new JavaArrayTypeModel(referencedType,new JavaClassObjectConstantExpressionModel((Integer)subexpressions_.size(),JavaPrimitiveTypeModel.INT));
    }
    
    public List<JavaExpressionModel>  getSubExpressions()
    { return subexpressions_; }
    
    public boolean isConstantExpression()
    { return true; }
    
    /***
     * ArrayInitializerModel([expr1,..exprN], ctx)
     */
    public Term getModelTerm() throws TermWareException, EntityNotFoundException
    {
      Term l = TermUtils.createNil();
      for(JavaExpressionModel e: subexpressions_) {
          Term ct = e.getModelTerm();
          l=TermUtils.createTerm("cons",ct,l);
      }
      l=TermUtils.reverseListTerm(l);
      Term ctx = TermUtils.createJTerm(createPlaceContext());
      Term retval = TermUtils.createTerm("ArrayInitializerModel",l,ctx);
      return retval;
    }

    private List<JavaExpressionModel> subexpressions_;
}
