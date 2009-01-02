/*
 * JavaTermArrayIndexExpressionModel.java
 *
 */

package ua.gradsoft.javachecker.models.expressions;

import java.util.ArrayList;
import java.util.List;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.javachecker.models.InvalidJavaTermException;
import ua.gradsoft.javachecker.models.JavaExpressionKind;
import ua.gradsoft.javachecker.models.JavaExpressionModel;
import ua.gradsoft.javachecker.models.JavaExpressionModelHelper;
import ua.gradsoft.javachecker.models.JavaTermExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermStatementModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.javachecker.models.TermUtils;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *
 * @author RSSH
 */
public class JavaTermArrayIndexExpressionModel extends JavaTermExpressionModel
{
    
    /** Creates a new instance of JavaTermArrayIndexExpressionModel */
    public JavaTermArrayIndexExpressionModel(Term t, JavaTermStatementModel st, JavaTypeModel enclosedType) throws TermWareException 
    {
      super(t,st,enclosedType);  
      subExpressions_=new ArrayList<JavaExpressionModel>(2);
      JavaExpressionModel eo = JavaTermExpressionModel.create(t.getSubtermAt(0),st,enclosedType);
      subExpressions_.add(eo);
      JavaExpressionModel ei = JavaTermExpressionModel.create(t.getSubtermAt(1),st,enclosedType);
      subExpressions_.add(ei);      
    }
    
    
    public  JavaExpressionKind  getKind()
    {
      return JavaExpressionKind.ARRAY_INDEX;  
    }
    
    public boolean isType()
    { return false; }
    
    public JavaTypeModel  getType() throws TermWareException, EntityNotFoundException
    {
        JavaExpressionModel je=getIndexed();
        JavaTypeModel tm = je.getType();
        if (!tm.isArray()) {
            throw new InvalidJavaTermException("first argument od array index is not array",t_.getSubtermAt(0));            
        }
        try {
          return tm.getReferencedType();
        }catch(NotSupportedException ex){
            // impossible.
            throw new AssertException("reference type of array is not supportede",ex);
        }
    }
    
    
    public List<JavaExpressionModel> getSubExpressions()
    { return subExpressions_; }
    
    public boolean isConstantExpression() throws TermWareException, EntityNotFoundException
    {
      return JavaExpressionModelHelper.subExpressionsAreConstants(this);  
    }
      
    
    
    /**
     * ArrayIndexExpressionModel(array,index,ctx)
     */
    public Term getModelTerm() throws TermWareException, EntityNotFoundException
    {
      Term x=subExpressions_.get(0).getModelTerm();
      Term y=subExpressions_.get(1).getModelTerm();
      Term tctx = TermUtils.createJTerm(createPlaceContext());
      Term retval = TermUtils.createTerm("ArrayIndexExpressionModel",x,y,tctx);
      return retval;
    }

    
    public JavaExpressionModel getIndexed()
    {
      return subExpressions_.get(0);  
    }
    
    public JavaExpressionModel getIndex()
    {
      return subExpressions_.get(1);  
    }
    
    
    
    
    private List<JavaExpressionModel> subExpressions_;
}
