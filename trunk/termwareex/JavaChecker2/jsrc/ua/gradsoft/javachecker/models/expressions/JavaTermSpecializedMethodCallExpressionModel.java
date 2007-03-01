/*
 * JavaTermSpecializedMethodCallExpressionModel.java
 *
 */

package ua.gradsoft.javachecker.models.expressions;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.models.JavaTypeArgumentBoundMethodModel;
import ua.gradsoft.javachecker.models.JavaExpressionKind;
import ua.gradsoft.javachecker.models.JavaExpressionModel;
import ua.gradsoft.javachecker.models.JavaMethodModel;
import ua.gradsoft.javachecker.models.JavaPlaceContext;
import ua.gradsoft.javachecker.models.JavaResolver;
import ua.gradsoft.javachecker.models.JavaTermExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermStatementModel;
import ua.gradsoft.javachecker.models.JavaTypeArgumentsSubstitution;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.javachecker.models.TermUtils;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Model for SpecializedMethodCall (i. e. <x>)
 * @author rssh@gradsoft.ua
 */
public final class JavaTermSpecializedMethodCallExpressionModel extends JavaTermExpressionModel
{
    
    /** Creates a new instance of JavaTermSpecializedMethodCallExpressionModel */
    public JavaTermSpecializedMethodCallExpressionModel(Term t, JavaTermStatementModel st, JavaTypeModel enclosedType) throws TermWareException
    {
      super(t,st,enclosedType);         
      Term objectTerm=t.getSubtermAt(0);
      Term typeBoundTerm=t.getSubtermAt(1); 
      Term nameTerm=t.getSubtermAt(2);
      Term argumentsTerm=t.getSubtermAt(3);      
      Term unboundMethodCallTerm=TermUtils.createTerm("MethodCall",objectTerm,nameTerm,argumentsTerm);
      unboundMethodCall_=new JavaTermMethodCallExpressionModel(unboundMethodCallTerm,st,enclosedType);
    }
    
    public JavaExpressionKind getKind()
    {
      return JavaExpressionKind.SPECIALIZED_METHOD_CALL;   
    }
    
    
    public JavaTypeModel getType() throws TermWareException, EntityNotFoundException
    {
        JavaMethodModel methodModel = getMethodModel();
        return methodModel.getResultType();
    }
        
    public JavaMethodModel getMethodModel() throws TermWareException, EntityNotFoundException
    {
        JavaMethodModel originMethodModel=unboundMethodCall_.getMethodModel();
        if (originMethodModel instanceof JavaTypeArgumentBoundMethodModel) {
            JavaTypeArgumentBoundMethodModel jam=(JavaTypeArgumentBoundMethodModel)originMethodModel;
            JavaMethodModel originOrigin = jam.getOrigin();
            List<JavaTypeModel> typeValues=new LinkedList<JavaTypeModel>();
            Term typeArgumentsTerm = t_.getSubtermAt(1);
            Term l=typeArgumentsTerm.getSubtermAt(0);
            while(!l.isNil()) {
                Term typeTerm = l.getSubtermAt(0);
                l=l.getSubtermAt(1);
                JavaPlaceContext ctx = createPlaceContext();
                JavaTypeModel tm = JavaResolver.resolveTypeTerm(typeTerm,ctx);
                typeValues.add(tm);
            }
            JavaTypeArgumentsSubstitution s = new JavaTypeArgumentsSubstitution(originOrigin.getTypeParameters(),typeValues);
            JavaTypeArgumentBoundMethodModel retval = new JavaTypeArgumentBoundMethodModel(originOrigin,s);
            return retval;
        }else{
            return originMethodModel;
        }
    }
    
    public boolean isType()
    {
      return false;  
    }
    
    public List<JavaExpressionModel>  getSubExpressions()
    {   
        return Collections.<JavaExpressionModel>singletonList(unboundMethodCall_); 
    }
    
    /**
     * SpecializedMethodCallModel(bounds,methodCallModelTerm,methodModel,ctx)
     */
    public Term getModelTerm() throws TermWareException, EntityNotFoundException
    {
      Term bounds=t_.getSubtermAt(1);
      Term methodCall=unboundMethodCall_.getModelTerm();
      JavaMethodModel mm = this.getMethodModel();
      Term tmm = TermUtils.createJTerm(mm);
      Term ctx = TermUtils.createJTerm(this.createPlaceContext());
      Term retval = TermUtils.createTerm("SpecializedMethodCallModel",bounds, methodCall,tmm,ctx);
      return retval;
    }
    
    private JavaTermMethodCallExpressionModel unboundMethodCall_;
    
}
