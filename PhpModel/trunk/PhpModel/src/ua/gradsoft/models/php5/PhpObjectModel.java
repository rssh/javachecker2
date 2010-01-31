
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
            switch(phpClass.getVisibility(e.getKey())) {
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
                    throw new PhpEvalException("Unkniewn visibility:"+phpClass.getVisibility(e.getKey()));
            }
        }
        return retval;
    }


    public String getString(PhpEvalEnvironment pee) {
        PhpMethodModel method = phpClass.findMethod("__toString");
        if (method==null) {
          return "Object";
        } else {
          return method.eval(pee, Collections.singletonList(this));
        }
    }

    public PhpValueModel eval(PhpEvalEnvironment php) {
        return this;
    }

    public Term getTerm(PhpEvalEnvironment e) throws TermWareException {
        TermFactory tf = TermWare.getInstance().getTermFactory();
        Term args = tf.createTerm("SArgumentExpressionList",
                      tf.createConsTerm(getArray().getTerm(), tf.createNil())
                                 );

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



    private PhpClassDeclarationModel phpClass;

    private Map<String,PhpValueModel> memberVariables;
}
