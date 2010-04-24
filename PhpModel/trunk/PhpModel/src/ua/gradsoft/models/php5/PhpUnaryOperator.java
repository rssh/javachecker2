
package ua.gradsoft.models.php5;

/**
 *PhpUnaryOperatoee
 * @author rssh
 */
public enum PhpUnaryOperator {

    REF("ref","UnaryExpression","&"){
        public PhpValueModel doOp(PhpExpressionModel x, PhpEvalEnvironment pee) {
            if (x.isReference(pee)) {
                return x.getReferenceModel(pee);
            }else{
                pee.error("ref must be applied to reference");
                return PhpNullValueModel.INSTANCE;
            }
        }
    },
    MINUS("minus","UnaryExpression","-") {
        public PhpValueModel doOp(PhpExpressionModel x, PhpEvalEnvironment pee) {
            PhpValueModel vx = x.eval(pee);
            if (vx.getType()==PhpType.FLOAT) {
                return new PhpFloatModel(-vx.getFloat());
            } else {
                return new PhpIntegerModel(vx.getInt());
            }
        }
    },
    NEG("invert","UnaryExpression","~") {
        public PhpValueModel doOp(PhpExpressionModel x, PhpEvalEnvironment pee) {
            PhpValueModel vx = x.eval(pee);
            return new PhpIntegerModel(~vx.getInt());
        }
    },
    NOT("not","UnaryExpression","!") {
        public PhpValueModel doOp(PhpExpressionModel x, PhpEvalEnvironment pee) {
            if (pee.getEvalState()!=EvalState.OK) {
                return PhpNullValueModel.INSTANCE;
            }
            PhpValueModel vx = x.eval(pee);
            if (pee.getEvalState()!=EvalState.OK) {
                return PhpNullValueModel.INSTANCE;
            }
            return PhpBooleanModel.create(!vx.getBoolean());
        }
    },
    PREFIX_INCREMENT("incr","PrefixIncDecExpression","++") {
        public PhpValueModel doOp(PhpExpressionModel x, PhpEvalEnvironment pee) {
            if (x.isReference(pee)) {
                PhpValueModel vx = x.eval(pee);
                if (pee.getEvalState()!=EvalState.OK) {
                    return PhpNullValueModel.INSTANCE;
                }
                int nextVal = vx.getInt()+1;
                PhpValueModel retval = new PhpIntegerModel(nextVal);
                x.getReferenceModel(pee).set(retval);
                return retval;
            } else {
                pee.error("increment must be applied to reference");
                return PhpNullValueModel.INSTANCE;
            }
        }
    },
    PREFIХ_DECREMENT("decr","PrefixIncDecExpression","--") {
        public PhpValueModel doOp(PhpExpressionModel x, PhpEvalEnvironment pee) {
            if (x.isReference(pee)) {
                PhpValueModel vx = x.eval(pee);
                if (pee.getEvalState()!=EvalState.OK) {
                    return PhpNullValueModel.INSTANCE;
                }
                int nextVal = vx.getInt()-1;
                PhpValueModel retval = new PhpIntegerModel(nextVal);
                x.getReferenceModel(pee).set(retval);
                return retval;
            } else {
                pee.error("decrement must be applied to reference");
                return PhpNullValueModel.INSTANCE;
            }
        }

    },
    POSTFIX_INCREMENET("potfix_incr","PosfixIncDec","_++") {
        public PhpValueModel doOp(PhpExpressionModel x, PhpEvalEnvironment pee) {
            if (x.isReference(pee)) {
                PhpValueModel retval = x.eval(pee);
                if (pee.getEvalState()!=EvalState.OK) {
                    return PhpNullValueModel.INSTANCE;
                }
                int nextVal = retval.getInt()+1;
                PhpValueModel nextval = new PhpIntegerModel(nextVal);
                x.getReferenceModel(pee).set(nextval);
                return retval;
            } else {
                pee.error("increment must be applied to reference");
                return PhpNullValueModel.INSTANCE;
            }
        }
    },
    POSTFIX_DECREMENT("potfix_decr","PosfixIncDec","_--") {
        public PhpValueModel doOp(PhpExpressionModel x, PhpEvalEnvironment pee) {
            if (x.isReference(pee)) {
                PhpValueModel retval = x.eval(pee);
                if (pee.getEvalState()!=EvalState.OK) {
                    return PhpNullValueModel.INSTANCE;
                }
                int nextVal = retval.getInt()-1;
                PhpValueModel nextval = new PhpIntegerModel(nextVal);
                x.getReferenceModel(pee).set(nextval);
                return retval;
            } else {
                pee.error("increment must be applied to reference");
                return PhpNullValueModel.INSTANCE;
            }
        }
    }
            ;

    PhpUnaryOperator(String name, String termName, String symbols)
    {
       this.name=name;
       this.termName = termName;
       this.symbols=symbols;
    }

    public String getName() {
        return name;
    }

    public String getSymbols() {
        return symbols;
    }

    public String getTermName() {
        return termName;
    }

    public static PhpUnaryOperator findOp(String symbols)
    {
        for(PhpUnaryOperator op: values()) {
            if (op.symbols.equals("symbols")) {
                return op;
            }
        }
        return null;
    }

    public abstract PhpValueModel doOp(PhpExpressionModel x, PhpEvalEnvironment pee);


    private String symbols;
    private String name;
    private String termName;
}
