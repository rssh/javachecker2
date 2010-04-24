
package ua.gradsoft.models.php5;

/**
 *Binary operators
 */
public enum PhpBinaryOperator {

    LOGICAL_OR("logical_or","Logical_Or_Expression","||") {

        @Override
        public PhpValueModel doOp(PhpExpressionModel frs, PhpExpressionModel snd, PhpEvalEnvironment pee) {
            PhpValueModel vfrs = frs.eval(pee);
            if (vfrs.getBoolean()) {
                return PhpBooleanModel.TRUE;
            } else {
                return snd.eval(pee);
            }
        }
   
        
    },

    LOGICAL_AND("logical_and","Logical_And_Expression","&&") {

        @Override
        public PhpValueModel doOp(PhpExpressionModel frs, PhpExpressionModel snd, PhpEvalEnvironment pee) {
            PhpValueModel vfrs = frs.eval(pee);
            if (vfrs.getBoolean()) {
                return snd.eval(pee);
            } else {
                return PhpBooleanModel.FALSE;
            }
        }
    
        
        
    },

    LOGICAL_XOR("logical_xor","Logical_Xor_Expression","^^") {

        @Override
        public PhpValueModel doOp(PhpExpressionModel frs, PhpExpressionModel snd, PhpEvalEnvironment pee) {
            PhpValueModel vfrs = frs.eval(pee);
            PhpValueModel vsnd = frs.eval(pee);
            if (vfrs.getBoolean()) {
                if (vsnd.getBoolean()) {
                    return PhpBooleanModel.FALSE;
                } else {
                    return PhpBooleanModel.TRUE;
                }
            } else {
                if (vsnd.getBoolean()) {
                    return PhpBooleanModel.TRUE;
                } else {
                    return PhpBooleanModel.FALSE;
                }
            }
        }    
        
    },

    BITWISE_OR("bitwise_or","BitwiseOrExpression","|") {

        @Override
        public PhpValueModel doOp(PhpExpressionModel frs, PhpExpressionModel snd, PhpEvalEnvironment pee) {
            PhpValueModel vfrs = frs.eval(pee);
            PhpValueModel vsnd = snd.eval(pee);
            return new PhpIntegerModel(vfrs.getInt() | vsnd.getInt());
        }
        
    },

    BITWISE_XOR("bitwise_xor","BitwiseXorExpression","^") {

        @Override
        public PhpValueModel doOp(PhpExpressionModel frs, PhpExpressionModel snd, PhpEvalEnvironment pee) {
            PhpValueModel vfrs = frs.eval(pee);
            PhpValueModel vsnd = snd.eval(pee);
            return new PhpIntegerModel(vfrs.getInt() ^ vsnd.getInt());            
        }
    
    },

    BITWISE_AND("bitwise_and","BitwiseAndExpression","&") {

        @Override
        public PhpValueModel doOp(PhpExpressionModel frs, PhpExpressionModel snd, PhpEvalEnvironment pee) {
            PhpValueModel vfrs = frs.eval(pee);
            PhpValueModel vsnd = snd.eval(pee);
            return new PhpIntegerModel(vfrs.getInt() & vsnd.getInt());            
        }        
    
    },


    LEFT_SHIFT("left_shift","ShiftExpression","<<") {

        @Override
        public PhpValueModel doOp(PhpExpressionModel frs, PhpExpressionModel snd, PhpEvalEnvironment pee) {
            PhpValueModel vfrs = frs.eval(pee);
            PhpValueModel vsnd = snd.eval(pee);
            return new PhpIntegerModel(vfrs.getInt() << vsnd.getInt());            
        }        
    
    },

    RIGHT_SHIFT("right_shift","ShiftExpression",">>") {
    
        @Override
        public PhpValueModel doOp(PhpExpressionModel frs, PhpExpressionModel snd, PhpEvalEnvironment pee) {
            PhpValueModel vfrs = frs.eval(pee);
            PhpValueModel vsnd = snd.eval(pee);
            return new PhpIntegerModel(vfrs.getInt() >> vsnd.getInt());
        }        
    
    },


    PLUS("plus","AdditiveExpression","+") {

        @Override
        public PhpValueModel doOp(PhpExpressionModel frs, PhpExpressionModel snd, PhpEvalEnvironment pee) {
          PhpValueModel vfrs = frs.eval(pee);
          PhpValueModel vsnd = snd.eval(pee);
          PhpValueModel retval;
          if (vfrs.getType()==PhpType.INTEGER && vsnd.getType()==PhpType.INTEGER) {
             retval = new PhpIntegerModel(vfrs.getInt()+vsnd.getInt());
          } else {
              retval = new PhpFloatModel(vfrs.getFloat()+vsnd.getFloat());
          }
          return retval;
        }

    },
    MINUS("minus","AdditiveExpression","-") {

        @Override
        public PhpValueModel doOp(PhpExpressionModel frs, PhpExpressionModel snd, PhpEvalEnvironment pee) {
          PhpValueModel vfrs = frs.eval(pee);
          PhpValueModel vsnd = snd.eval(pee);
          PhpValueModel retval = null;
          if (vfrs.getType()==PhpType.INTEGER && vsnd.getType()==PhpType.INTEGER) {
            retval = new PhpIntegerModel(vfrs.getInt()-vsnd.getInt());
          } else {
            retval = new PhpFloatModel(vfrs.getFloat()-vsnd.getFloat());
          }
          return retval;
        }

    },
    DOT("dot","AdditiveExpression",".") {

        @Override
        public PhpValueModel doOp(PhpExpressionModel frs, PhpExpressionModel snd, PhpEvalEnvironment pee) {
          PhpValueModel vfrs = frs.eval(pee);
          PhpValueModel vsnd = snd.eval(pee);
          return new PhpStringModel(vfrs.getString(pee)+vsnd.getString(pee));
        }


    },
    MULTIPLY("mitiply","MultiplicativeExpression","*") {

        @Override
        public PhpValueModel doOp(PhpExpressionModel frs, PhpExpressionModel snd, PhpEvalEnvironment pee) {
          PhpValueModel vfrs = frs.eval(pee);
          PhpValueModel vsnd = snd.eval(pee);
          PhpValueModel retval = null;
          if (vfrs.getType()==PhpType.INTEGER && vsnd.getType()==PhpType.INTEGER) {
            retval = new PhpIntegerModel(vfrs.getInt()*vsnd.getInt());
          } else {
            retval = new PhpFloatModel(vfrs.getFloat()*vsnd.getFloat());
          }
          return retval;
        }
        
    },

    DIVIDE("divide","MultiplicativeExpression","/") {

        @Override
        public PhpValueModel doOp(PhpExpressionModel frs, PhpExpressionModel snd, PhpEvalEnvironment pee) {
            PhpValueModel vfrs = frs.eval(pee);
            PhpValueModel vsnd = snd.eval(pee);
            PhpValueModel retval = new PhpFloatModel(vfrs.getFloat()/vsnd.getFloat());
            if (Double.isInfinite(retval.getFloat())||Double.isNaN(retval.getFloat())) {
                pee.warning("division by zero");
            }
            return retval;
        }



    },
    MODULE("module","MultiplicativeExpression","%") {

        @Override
        public PhpValueModel doOp(PhpExpressionModel frs, PhpExpressionModel snd, PhpEvalEnvironment pee) {
            PhpValueModel vfrs = frs.eval(pee);
            if (pee.getEvalState()!=EvalState.OK) {
                return PhpNullValueModel.INSTANCE;
            }
            PhpValueModel vsnd = snd.eval(pee);
            if (pee.getEvalState()!=EvalState.OK) {
                return PhpNullValueModel.INSTANCE;
            }
            return new PhpIntegerModel(vfrs.getInt() % vsnd.getInt());
        }

        

    },
    INSTANCE_OF("instanceOf","InstanceOfExpression","instance of") {

        @Override
        public PhpValueModel doOp(PhpExpressionModel frs, PhpExpressionModel snd, PhpEvalEnvironment pee) {
            if (pee.getEvalState()!=EvalState.OK) {
                return PhpNullValueModel.INSTANCE;
            }
            PhpValueModel vfrs = frs.eval(pee);
            if (pee.getEvalState()!=EvalState.OK) {
                return PhpNullValueModel.INSTANCE;
            }
            PhpValueModel vsnd = snd.eval(pee);
            if (pee.getEvalState()!=EvalState.OK) {
                return PhpNullValueModel.INSTANCE;
            }
            PhpObjectModel frsObj = vfrs.getObject(pee);
            if (pee.getEvalState()!=EvalState.OK) {
                return PhpNullValueModel.INSTANCE;
            }
            if (vsnd.getType()==PhpType.STRING) {
               PhpClassDeclarationModel cd =  pee.findClassDeclarationModel(vsnd.getString(pee));
               if (cd==null) {
                   // model is not found
                   pee.error("class with name "+vsnd.getString(pee)+" does not exists");
                   return PhpBooleanModel.FALSE;
               }
               return PhpBooleanModel.create(frsObj.getClassDeclaration().isChildOf(cd));
            } else if (vsnd.getType()==PhpType.OBJECT) {
                PhpClassDeclarationModel cd = vsnd.getObject(pee).getClassDeclaration();
                return PhpBooleanModel.create(frsObj.getClassDeclaration().isChildOf(cd));
            } else {
                // error ?
                return PhpBooleanModel.FALSE;
            }
        }

    };
   
    PhpBinaryOperator(String name, String termName, String symbols)
    {
      this.name=name;
      this.termName=termName;
      this.symbols=symbols;
    }

    public String getName()
    { return name; }

    public String getTermName()
    { return termName; }

    public String getSymbols()
    { return symbols; }

    public static PhpBinaryOperator findOp(String symbols)
    {
      for (PhpBinaryOperator op: PhpBinaryOperator.values()) {
          if (op.symbols.equals(symbols)) {
              return op;
          }
      }  
      throw new IllegalArgumentException("Can't find operator "+symbols);
    }

    public abstract PhpValueModel doOp(PhpExpressionModel frs, PhpExpressionModel snd, PhpEvalEnvironment pee);

    private String name;
    private String termName;
    private String symbols;
}
