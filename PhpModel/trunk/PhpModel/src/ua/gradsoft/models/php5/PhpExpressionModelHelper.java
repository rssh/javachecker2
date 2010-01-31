
package ua.gradsoft.models.php5;

import ua.gradsoft.termware.Term;

/**
 *Static helper classes for Php expressions
 * @author rssh
 */
public class PhpExpressionModelHelper {

    
    public static PhpExpressionModel create(Term t, PhpCompileEnvironment pce)
    {
        if (t.getName().equals("LogicalTextOrExpression")) {
            return new PhpLogicalTextOrExpressionModel(t,pce);
        } else if (t.getName().equals("LogicalTextXorExpression")) {
            return new PhpLogicalTextXorExpressionModel(t,pce);
        } else if (t.getName().equals("LogicalTextAndExpression")) {
            return new PhpLogicalTextAndExpressionModel(t,pce);
        } else if (t.getName().equals("AssignmentExpression")) {
            return new PhpAssignmentExpressionModel(t,pce);
        } else if (t.getName().equals("ConditionalExpression")) {
            return new PhpConditionalExpressionModel(t,pce);
        } else if (t.getName().equals("Logical_Or_Expression")) {
            return new PhpLogical_Or_ExpressionModel(t,pce);
        } else if (t.getName().equals("Logical_And_Expression")) {
            return new PhpLogical_Or_ExpressionModel(t,pce);
        } else if (t.getName().equals("BitwiseOrExpression")) {
            return new PhpBitwiseOrExpressionModel(t,pce);
        } else if (t.getName().equals("BitwiseXorExpression")) {
            return new PhpBitwiseXorExpressionModel(t,pce);
        }else if (t.getName().equals("BitwiseAndExpression")) {
            return new PhpEqualityExpressionModel(t,pce);
        }else if (t.getName().equals("PhpRelationalExpression")) {
            return new PhpRelationalExpressionModel(t,pce);
        }else if (t.getName().equals("ShiftExpression")) {
            return new PhpShiftExpressionModel(t,pce);
        }else if (t.getName().equals("AdditiveExpression")) {
            return new AdditiveExpressionModel(t,pce);
        }else if (t.getName().equals("MultiplicativeExpression")) {
            return new MultiplicativeExpressionModel(t,pce);
        }else if (t.getName().equals("CastExpression")) {
            return new CastExpressionModel(t,pce);
        }else if (t.getName().equals("UnaryExpression")) {
            return new UnaryExpressionModel(t,pce);
        }else if (t.getName().equals("PrefixIncDecExpression")) {
            return new PrefixIncDecExpression(t,pce);
        }else if (t.getName().equals("PostfixIncDecExpression0")) {
            return new PhpPostfixIncDecExpressionModel(t,pce);
        }else if (t.getName().equals("InstanceOfExpression")) {
            return new PhpInstanceOfExpressionModel(t,pce);
        }else if (t.getName().equals("PostfixExpression")) {
            return new PhpPostfixExpressionModel(t,pce);
        }else if (t.getName().equals("PrimaryExpression")) {
            return new PhpPrimaryExpressionModel(t,pce);
        }else if (t.getName().equals("Reference")) {
            return new PhpReferenceModel(t,pce);
            // TODO: all expressions, which AST transformed.
        }else if (t.getName().equals("Variable")) {
            return new PhpVariableModel(t,pce);
        }else if (t.getName().equals("Constant")) {
            return new PhpConstantModel(t,pce);
        }else if (t.getName().equals("InBracesExpression")) {
            return new PhpInbracesExpressionModel(t,pce);
        }else if (t.getName().equals("ClassInstantiation")) {
            
        }
    }


}
