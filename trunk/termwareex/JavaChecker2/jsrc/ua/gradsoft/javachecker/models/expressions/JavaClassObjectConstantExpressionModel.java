/*
 * JavaClassObjectConstantExpressionModel.java
 *
 */

package ua.gradsoft.javachecker.models.expressions;

import java.util.Collections;
import java.util.List;
import ua.gradsoft.javachecker.models.JavaClassTypeModel;
import ua.gradsoft.javachecker.models.JavaExpressionKind;
import ua.gradsoft.javachecker.models.JavaExpressionModel;
import ua.gradsoft.javachecker.models.JavaPlaceContext;
import ua.gradsoft.javachecker.models.JavaPlaceContextFactory;
import ua.gradsoft.javachecker.models.JavaStatementModel;
import ua.gradsoft.javachecker.models.JavaTraceContext;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.javachecker.models.TermUtils;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *'Expression'  which is really a constant, situated in class
 * @author rssh
 */
public class JavaClassObjectConstantExpressionModel implements JavaObjectConstantExpressionModel
{
    
    /** Creates a new instance of JavaClassObjectConstantExpressionModel */
    public JavaClassObjectConstantExpressionModel(Object o, JavaTypeModel enclosedType) {
        o_=o;
        enclosedType_=enclosedType;
    }
    
    
    public JavaTypeModel  getType() throws TermWareException
    {
        return JavaClassTypeModel.createTypeModel(o_.getClass());
    }
    
    public boolean isType()
    { return false; }
    
    public JavaExpressionKind  getKind()
    {
      return JavaExpressionKind.OBJECT_CONSTANT;  
    }
   
    public List<JavaExpressionModel>  getSubExpressions()
    { return Collections.emptyList(); }
    
    public JavaTypeModel getEnclosedType()
    { return enclosedType_; }
    
    public JavaStatementModel  getStatementModel()
    { return null; }
    
    public Object getConstant()
    { return o_; }
 
    public Term getModelTerm() throws TermWareException
    {
        JavaPlaceContext ctx;
        if (enclosedType_!=null) {
            ctx = JavaPlaceContextFactory.createNewTypeContext(enclosedType_);
        }else{
            ctx = JavaPlaceContextFactory.createNewPackageContext("java.lang");
        }
        Term x1 = TermUtils.createJTerm(o_);
        Term x2 = TermUtils.createJTerm(ctx);
        Term retval = TermUtils.createTerm("ObjectConstantExpressionModel",x1,x2);
        return retval;
    }
    
    public boolean isConstantExpression()
    { return true; }
    
    public JavaExpressionModel eval(JavaTraceContext ctx)
    {
        return this;
    }
    
    private Object o_;
    private JavaTypeModel enclosedType_;
}
