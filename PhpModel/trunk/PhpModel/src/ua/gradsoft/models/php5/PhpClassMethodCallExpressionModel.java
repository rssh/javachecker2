
package ua.gradsoft.models.php5;

import java.util.List;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *MethodCallModel for call of static class
 * @author rssh
 */
public class PhpClassMethodCallExpressionModel extends PhpMethodCallExpressionModel
{

    public PhpClassMethodCallExpressionModel(
                                            PhpExpressionModel classNameExpression,
                                            PhpExpressionModel methodNameExpression,
                                            Term   paramsTerm,
                                            PhpCompileEnvironment pce)
                                                                throws TermWareException
    {
      this.classNameExpression=classNameExpression;
      this.methodNameExpression=methodNameExpression;
      this.params=parseParams(paramsTerm,pce);
    }

    @Override
    public Term getFirstSubterm(PhpEvalEnvironment pee) throws TermWareException {
        Term[] scBody = new Term[2];
        scBody[0]=classNameExpression.getTerm(pee);
        scBody[1]=methodNameExpression.getTerm(pee);
        Term sc = PhpTermUtils.createContextTerm("ScopeResolution", scBody, this);
        return sc;
    }

    public PhpValueModel eval(PhpEvalEnvironment php) {
        if (cachedClassDeclaration!=null) {
            if (cachedMethodModel!=null) {
                return evalMethod(cachedClassDeclaration, cachedMethodModel, php);
            } else {
                PhpMethodModel methodModel = findMethod(cachedClassDeclaration,php);
                if (methodModel==null) {
                    // error message is set in findMethod
                    return PhpNullValueModel.INSTANCE;
                }
                return evalMethod(cachedClassDeclaration, methodModel, php);
            }
        }else{
            PhpClassDeclarationModel classDeclaration=findClassDeclaration(php);
            if (classDeclaration==null) {
                return PhpNullValueModel.INSTANCE;
            }
            PhpMethodModel methodModel = findMethod(classDeclaration,php);
            if (methodModel==null) {
                return PhpNullValueModel.INSTANCE;
            }
            return evalMethod(cachedClassDeclaration, methodModel, php);
        }
    }


    private PhpValueModel evalMethod(PhpClassDeclarationModel classDeclaration,
                                     PhpMethodModel  method,
                                     PhpEvalEnvironment php)
    {
        if (!method.isStatic()) {
           php.error("Can't call non-static method in static context");
        }
        List<PhpValueModel> vp = evalParams(php);
        if (php.getEvalState()!=EvalState.OK) {
            return PhpNullValueModel.INSTANCE;
        }
        return method.eval(php, null, this.evalParams(php));
    }


    private PhpClassDeclarationModel  findClassDeclaration(PhpEvalEnvironment php)
    {
       String className=null;
       boolean isConstant=false;
       if (classNameExpression.isIdentifier()) {
           className=classNameExpression.getIdentifierName();
           if (className.equals("self")) {
               cachedClassDeclaration=php.getCurrentClassDeclaration();
               if (cachedClassDeclaration==null) {
                   // outside of scope
                   php.error("use of 'self' outside class declaration");
                   return null;
               }
               return cachedClassDeclaration;
           }
           isConstant=true;
       }else{
           className=classNameExpression.eval(php).getString(php);
           if (php.getEvalState()!=EvalState.OK) {
               return null;
           }
       }
       PhpClassDeclarationModel classDeclaration = php.findClassDeclarationModel(className);
       if (classDeclaration==null) {
           php.error("Can't find class "+className);
           return null;
       }
       if (isConstant) {
           cachedClassDeclaration = classDeclaration;
       }
       return classDeclaration;
    }

    /**
     * find methid in class declaration
     * @param classDeclaration
     * @return
     */
    private PhpMethodModel findMethod(PhpClassDeclarationModel classDeclaration, PhpEvalEnvironment php)
    {
      String methodName = null;
      boolean isConstant = false;
      if (methodNameExpression.isIdentifier()) {
          methodName=methodNameExpression.getIdentifierName();
          isConstant=true;
      }else{
          methodName = methodNameExpression.eval(php).getString(php);
      }
      if (php.getEvalState()!=EvalState.OK) {
          return null;
      }
      PhpMethodModel methodModel = cachedClassDeclaration.findMethod(methodName);
      if (methodName==null) {
          php.error("Can't find method "+methodName+" for class "+cachedClassDeclaration.getFullName());
                    return null;
      }
      if (isConstant && cachedClassDeclaration!=null) {
           cachedMethodModel=methodModel;
      }
      return methodModel;
    }



    protected PhpExpressionModel       classNameExpression;
    protected PhpExpressionModel       methodNameExpression;

    protected boolean                  classIsConstant;
    // valid only if
    protected PhpClassDeclarationModel cachedClassDeclaration;

    protected boolean                  methodIsConstant;
    protected PhpMethodModel           cachedMethodModel;
}
