
package ua.gradsoft.models.php5;

/**
 *Possible assignment operators
 * @author rssh
 */
public enum PhpAssignmentOperator {

    ASSIGN("=") {
        public PhpValueModel doOp(PhpExpressionModel frs,
                         PhpExpressionModel snd,
                         PhpEvalEnvironment env) {
            PhpReferenceModel r = frs.getReferenceModel(env);
           {    
            r.set(snd.eval(env));
            return r;
           }
        }

    },

    PLUS_ASSIGN("+=") {
        public PhpValueModel doOp(PhpExpressionModel frs,
                         PhpExpressionModel snd,
                         PhpEvalEnvironment env) {
            PhpReferenceModel r = frs.getReferenceModel(env);
            r.set(PhpBinaryOperator.PLUS.doOp(frs, snd, env));
            return r;
        }
    },
    MINUS_ASSIGN("-=") {
        public PhpValueModel doOp(PhpExpressionModel frs,
                         PhpExpressionModel snd,
                         PhpEvalEnvironment env) {
            PhpReferenceModel r = frs.getReferenceModel(env);
            r.set(PhpBinaryOperator.MINUS.doOp(frs, snd, env));
            return r;
        }
    },
    MULTIPLY_ASSIGN("*=") {
        public PhpValueModel doOp(PhpExpressionModel frs,
                         PhpExpressionModel snd,
                         PhpEvalEnvironment env) {
            PhpReferenceModel r = frs.getReferenceModel(env);
            r.set(PhpBinaryOperator.MULTIPLY.doOp(frs, snd, env));
            return r;
        }
    },
    DIVIDE_ASSIGN("/=") {
        public PhpValueModel doOp(PhpExpressionModel frs,
                         PhpExpressionModel snd,
                         PhpEvalEnvironment env) {
            PhpReferenceModel r = frs.getReferenceModel(env);
            r.set(PhpBinaryOperator.DIVIDE.doOp(frs, snd, env));
            return r;
        }
    },
    DOT_ASSIGN(".=") {
        public PhpValueModel doOp(PhpExpressionModel frs,
                         PhpExpressionModel snd,
                         PhpEvalEnvironment env) {
            PhpReferenceModel r = frs.getReferenceModel(env);
            r.set(PhpBinaryOperator.DOT.doOp(frs, snd, env));
            return r;
        }
    },
    PERCENT_ASSIGN("%=") {
        public PhpValueModel doOp(PhpExpressionModel frs,
                         PhpExpressionModel snd,
                         PhpEvalEnvironment env) {
            PhpReferenceModel r = frs.getReferenceModel(env);
            r.set(PhpBinaryOperator.MODULE.doOp(frs, snd, env));
            return r;
        }
    },
    AND_ASSIGN("&=") {
        public PhpValueModel doOp(PhpExpressionModel frs,
                         PhpExpressionModel snd,
                         PhpEvalEnvironment env) {
            PhpReferenceModel r = frs.getReferenceModel(env);
            r.set(PhpBinaryOperator.BITWISE_AND.doOp(frs, snd, env));
            return r;
        }
    },
    OR_ASSIGN("|=") {
        public PhpValueModel doOp(PhpExpressionModel frs,
                         PhpExpressionModel snd,
                         PhpEvalEnvironment env) {
            PhpReferenceModel r = frs.getReferenceModel(env);
            r.set(PhpBinaryOperator.BITWISE_OR.doOp(frs, snd, env));
            return r;
        }
    },
    XOR_ASSIGN("^=") {
        public PhpValueModel doOp(PhpExpressionModel frs,
                         PhpExpressionModel snd,
                         PhpEvalEnvironment env) {
            PhpReferenceModel r = frs.getReferenceModel(env);
            r.set(PhpBinaryOperator.BITWISE_XOR.doOp(frs, snd, env));
            return r;
        }
    },
    LEFTSHIFT_ASSIGN("<<=") {
        public PhpValueModel doOp(PhpExpressionModel frs,
                         PhpExpressionModel snd,
                         PhpEvalEnvironment env) {
            PhpReferenceModel r = frs.getReferenceModel(env);
            r.set(PhpBinaryOperator.LEFT_SHIFT.doOp(frs,snd,env));
            return r;
        }
    },
    RIGHTSHIFT_ASSIGN(">>=") {
        public PhpValueModel doOp(PhpExpressionModel frs,
                         PhpExpressionModel snd,
                         PhpEvalEnvironment env) {
            PhpReferenceModel r = frs.getReferenceModel(env);
            r.set(PhpBinaryOperator.RIGHT_SHIFT.doOp(frs,snd,env));
            return r;
        }                                  

    };

    public static PhpAssignmentOperator find(String value)
    {
      for(PhpAssignmentOperator op: PhpAssignmentOperator.values()) {
          if (op.opString.equals(value)) {
              return op;
          }
      }
      return null;
    }
    
    public abstract PhpValueModel doOp(PhpExpressionModel frs, 
                                       PhpExpressionModel snd, 
                                       PhpEvalEnvironment env);

    PhpAssignmentOperator(String op)
    {
        opString=op;
    }
    
    public String getOpString()
    {
      return opString;  
    }

    private String opString;
}
