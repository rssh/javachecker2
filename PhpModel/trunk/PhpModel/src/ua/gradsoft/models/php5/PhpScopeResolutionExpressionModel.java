package ua.gradsoft.models.php5;


import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;


/**
 *Model for ScopeResolution expression
 * @author rssh
 */
public class PhpScopeResolutionExpressionModel implements PhpExpressionModel
{

    public PhpScopeResolutionExpressionModel(Term t, PhpCompileEnvironment php)
    {
      classNameExpression = PhpExpressionModelHelper.create(t.getSubtermAt(0), php);
      constantNameExpression = PhpExpressionModelHelper.create(t.getSubtermAt(1), php);
      cachedClassDeclaration=null;
      cachedValue=null;
    }

    public PhpValueModel eval(PhpEvalEnvironment php) {
        if (cachedValue!=null) {
            return cachedValue;
        }
        PhpClassDeclarationModel classDeclaration = evalClassDeclaration();
        if (classDeclaration==null) {
            return PhpNullValueModel.INSTANCE;
        }
        String constantName;
        boolean nameIsConstant=false;
        if (constantNameExpression.isIdentifier()) {
            constantName=constantNameExpression.getIdentifierName();
            nameIsConstant=false;
        }else{
            constantName=constantNameExpression.eval(php).toString();
        }
        PhpConstantDeclarationModel constantDeclaration = classDeclaration.getConstants().get(constantName);
        if (constantDeclaration==null) {
            php.error("Can't find constant with name "+constantName+" in class "+classDeclaration.getFullName());
            return PhpNullValueModel.INSTANCE;
        }
        // now switch to class local context and
        PhpValueModel retval=null;
        php.pushNewContext();
        try {
             retval=constantDeclaration.eval(php);
        }finally{
            php.popContext();
        }
        if (cachedClassdeclaration!=null && nameIsConstant) {
            cachedValue=retval;
        }
        return retval;
    }

    public String getIdentifierName() {
        throw new UnsupportedOperationException("Not supported.");
    }

    // constant can not be passed by reference.
    public PhpReferenceModel getReferenceModel(PhpEvalEnvironment php) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public boolean isIdentifier() {
        return false;
    }

    public boolean isReference(PhpEvalEnvironment php) {
        return false;
    }

    public Term getTerm(PhpEvalEnvironment pee) throws TermWareException {
        Term[] body=new Term[2];
        body[0]=classNameExpression.getTerm(pee);
        body[1]=constantNameExpression.getTerm(pee);
        return PhpTermUtils.createContextTerm("ScopeResolution", body, this);
    }

    protected PhpExpressionModel         classNameExpression;
    protected PhpClassDeclarationModel   cachedClassDeclaration;
    protected PhpExpressionModel         constantNameExpression;
    protected PhpValueModel              cachedValue;

}
