
package ua.gradsoft.models.php5;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;

/**
 *Static helper classes for Php expressions
 * @author rssh
 */
public class PhpExpressionModelHelper {

    
    public static PhpExpressionModel create(Term t, PhpCompileEnvironment pce)
                                                         throws TermWareException
    {
        if (!t.isComplexTerm()) {
            if (t.isBoolean()) {
                return PhpBooleanModel.create(t.getBoolean());
            }else if (t.isInt()) {
                return new PhpIntegerModel(t.getInt());
            }else if (t.isDouble()) {
                return new PhpFloatModel(t.getDouble());
            }else if (t.isString()) {
                return new PhpStringModel(t.getString());
            }else{
                throw new InvalidPhpTermExpression("term ("+TermHelper.termToString(t)+") is not php expression",t);
            }
        } else if (t.getName().equals("LogicalTextOrExpression")) {
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
            return new PhpLogical_And_ExpressionModel(t,pce);
        } else if (t.getName().equals("BitwiseOrExpression")) {
            return new PhpBitwiseOrExpressionModel(t,pce);
        } else if (t.getName().equals("BitwiseXorExpression")) {
            return new PhpBitwiseXorExpressionModel(t,pce);
        }else if (t.getName().equals("BitwiseAndExpression")) {
            return new PhpBitwiseAndExpressionModel(t,pce);
        }else if (t.getName().equals("EqualityExpression")) {
            return new PhpEqualityExpressionModel(t,pce);
        }else if (t.getName().equals("RelationalExpression")) {
            return new PhpRelationalExpressionModel(t,pce);
        }else if (t.getName().equals("ShiftExpression")) {
            return new PhpShiftExpressionModel(t,pce);
        }else if (t.getName().equals("AdditiveExpression")) {
            return new PhpAdditiveExpressionModel(t,pce);
        }else if (t.getName().equals("MultiplicativeExpression")) {
            return new PhpMultiplicativeExpressionModel(t,pce);
        }else if (t.getName().equals("CastExpression")) {
            return new PhpCastExpressionModel(t,pce);
        }else if (t.getName().equals("UnaryExpression")) {
            return new PhpUnaryExpressionModel(t,pce);
        }else if (t.getName().equals("PrefixIncDecExpression")) {
            return new PhpPrefixIncDecExpressionModel(t,pce);
        }else if (t.getName().equals("PostfixIncDecExpression0")) {
            return new PhpPostfixIncDecExpressionModel(t,pce);
        }else if (t.getName().equals("InstanceOfExpression")) {
            return new PhpInstanceOfExpressionModel(t,pce);
        }else if (t.getName().equals("PostfixExpression")) {
            throw new InvalidPhpTermExpression("Postfix expression must be eliminated during parsing",t);
            //return new PhpPostfixExpressionModel(t,pce);
        }else if (t.getName().equals("MethodCall")) {
            return PhpMethodCallExpressionModel.create(t,pce);
        }else if (t.getName().equals("MemberSelector")) {
            return new PhpMemberSelectorExpressionModel(t,pce);
        }else if (t.getName().equals("ScopeResolution")) {
            return new PhpScopeResolutionExpressionModel(t,pce);
        }else if (t.getName().equals("ArrayIndex")) {
            return new PhpArrayIndexExpressionModel(t,pce);
        }else if (t.getName().equals("CurlyBrakets")) {
            return new PhpCurlyBracketsExpressionModel(t,pce);
        }else if (t.getName().equals("PrimaryExpression")) {
            return new PhpPrimaryExpressionModel(t,pce);
        }else if (t.getName().equals("Reference")) {
            return new PhpPrimitiveReferenceModel(t,pce);
            // TODO: all expressions, which AST transformed.
        }else if (t.getName().equals("Variable")) {
            return new PhpVariableModel(t,pce);
        //}else if (t.getName().equals("Constant")) {
        //    return new PhpConstantModel(t,pce);
        }else if (t.getName().equals("Identifier")) {
            return new PhpIdentifierModel(t,pce);
        }else if (t.getName().equals("SingleStringLiteral")) {
            return new PhpSingleStringLiteralModel(t,pce);
        }else if (t.getName().equals("DoubleStringLiteral")) {
            return new PhpDoubleStringLiteralModel(t,pce);
        }else if (t.getName().equals("HereDoct")){
            return new PhpHereDocModel(t,pce);
        }else if (t.getName().equals("InBracesExpression")) {
            return new PhpInbracesExpressionModel(t,pce);
        }else if (t.getName().equals("ClassInstantiation")) {
            return new PhpClassInstantiationModel(t,pce);
        }else if (t.getName().equals("Array")) {
            return new PhpArrayInstantiationModel(t,pce);
        }else{
            throw new InvalidPhpTermExpression(t.getName()+" is not expression",t);
        }
    }


}
