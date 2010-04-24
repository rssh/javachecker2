
package ua.gradsoft.models.php5;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Model for php CastExpression
 * @author rssh
 */
public class PhpCastExpressionModel implements PhpExpressionModel
{

    public PhpCastExpressionModel(Term t, PhpCompileEnvironment pce)
                                                         throws TermWareException
    {
      castPrimitiveTypeName = t.getSubtermAt(0).getName();
      unaryExpression = PhpExpressionModelHelper.create(t.getSubtermAt(1), pce);
    }

    public PhpValueModel eval(PhpEvalEnvironment php) {
        if (php.getEvalState()!=EvalState.OK) {
            return PhpNullValueModel.INSTANCE;
        }
        PhpValueModel iv = unaryExpression.eval(php);
        if (php.getEvalState()!=EvalState.OK) {
            return PhpNullValueModel.INSTANCE;
        }
        if (castPrimitiveTypeName.equals("boolean")
           ||castPrimitiveTypeName.equals("bool")) {
            if (iv.getBoolean()) {
                return PhpBooleanModel.TRUE;
            } else {
                return PhpBooleanModel.FALSE;
            }
        } else if (castPrimitiveTypeName.equals("integer")||
                castPrimitiveTypeName.equals("int")) {
            return new PhpIntegerModel(iv.getInt());
        } else if (castPrimitiveTypeName.equals("float")||
                   castPrimitiveTypeName.equals("double")||
                   castPrimitiveTypeName.equals("real")) {
            return new PhpIntegerModel(iv.getInt());
        } else if (castPrimitiveTypeName.equals("string")) {
            return new PhpStringModel(iv.getString(php));
        } else {
            php.error("unknown primitive type name:"+castPrimitiveTypeName);
            return PhpNullValueModel.INSTANCE;
        }
    }

    public PhpReferenceModel getReferenceModel(PhpEvalEnvironment php) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public boolean isReference(PhpEvalEnvironment php) {
        return false;
    }

    public String getIdentifierName() {
        throw new UnsupportedOperationException("Not supported.");
    }

    public boolean isIdentifier() {
        return false;
    }



    public Term getTerm(PhpEvalEnvironment pee) throws TermWareException {
        Term[] body = new Term[2];
        body[0] = PhpTermUtils.createString(castPrimitiveTypeName);
        body[1] = unaryExpression.getTerm(pee);
        return PhpTermUtils.createContextTerm("CastExpression", body, this);
    }


    private String castPrimitiveTypeName;
    private PhpExpressionModel unaryExpression;

}
