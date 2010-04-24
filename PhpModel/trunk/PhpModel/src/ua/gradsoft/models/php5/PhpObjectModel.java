
package ua.gradsoft.models.php5;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermFactory;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;

/**
 *
 * @author rssh
 */
public class PhpObjectModel implements PhpValueModel
{

    public PhpObjectModel(PhpClassDeclarationModel cd,
                          Map<String,PhpValueModel> m)
    {
      phpClass=cd;
      memberVariables=m;
    }


    public boolean getBoolean() {
        return !memberVariables.isEmpty();
    }

    public double getFloat() {
        throw new PhpEvalException("Can't convert object to float");
    }

    public int getInt() {
        throw new PhpEvalException("Can't convert object to integer");
    }

    public PhpObjectModel getObject(PhpEvalEnvironment pee) {
        return this;
    }

    public PhpArrayModel getArray(PhpEvalEnvironment pee) {
        PhpArrayModel retval = new PhpArrayModel(pee);
        for(Map.Entry<String,PhpValueModel> e: memberVariables.entrySet()) {
            PhpVariableDeclarationModel v = phpClass.getMemberVariables().get(e.getKey());
            if (v==null) {
                retval.put(new PhpStringModel(e.getKey()),e.getValue());
            }else{
              switch(v.getVisibility()) {
                case PUBLIC:
                    retval.put(new PhpStringModel(e.getKey()),e.getValue());
                    break;
                case PROTECTED:
                    retval.put(new PhpStringModel("*"+e.getKey()),e.getValue());
                    break;
                case PRIVATE:
                    retval.put(new PhpStringModel(phpClass.getName()+e.getKey()),e.getValue());
                    break;
                default:
                    throw new PhpEvalException("Unkniewn visibility:"+v.getVisibility());
              }
            }
        }
        return retval;
    }


    public String getString(PhpEvalEnvironment pee) {
        PhpMethodModel method = phpClass.findMethod("__toString");
        if (method==null) {
          return "Object";
        } else {
          return method.eval(pee, this, Collections.<PhpValueModel>emptyList()).getString(pee);
        }
    }

    public PhpValueModel eval(PhpEvalEnvironment php) {
        return this;
    }

    public Term getTerm(PhpEvalEnvironment e) throws TermWareException {
        TermFactory tf = TermWare.getInstance().getTermFactory();
        Term args = tf.createTerm("SArgumentExpressionList",
                     tf.createConsTerm(
                      tf.createString(phpClass.getName()),
                      tf.createConsTerm(getArray(e).getTerm(e), tf.createNil())
                     )
                                 );
        Term fun = PhpTermUtils.createIdentifier("___createObject");
        Term[] body = new Term[2];
        body[0] = fun;
        body[1] = args;
        Term retval = PhpTermUtils.createContextTerm("MethodCall", body, this);
        return retval;
    }



    public PhpType getType() {
        return PhpType.OBJECT;
    }


    public PhpValueModel copyByReference(PhpEvalEnvironment pee) {
        return this;
    }

    public PhpValueModel copyByValue(PhpEvalEnvironment pee) {
        Map<String,PhpValueModel> newMemberVariables = new TreeMap<String,PhpValueModel>();
        for(Map.Entry<String,PhpValueModel> e: memberVariables.entrySet()) {
            newMemberVariables.put(e.getKey(), e.getValue().copyByValue(pee));
        }
        return new PhpObjectModel(phpClass,newMemberVariables);
    }

    public PhpClassDeclarationModel getClassDeclaration()
    {
      return phpClass;
    }


    public Map<String,PhpValueModel> getMemberVariables()
    { return memberVariables; }

    public PhpReferenceModel getReferenceModel(PhpEvalEnvironment php) {
        return new PhpDefaultReferenceModel(this);
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




    private PhpClassDeclarationModel phpClass;

    private Map<String,PhpValueModel> memberVariables;
}
