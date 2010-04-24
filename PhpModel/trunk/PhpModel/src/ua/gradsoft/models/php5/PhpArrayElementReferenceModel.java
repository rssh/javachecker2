
package ua.gradsoft.models.php5;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *reference to array model.
 * @author rssh
 */
public class PhpArrayElementReferenceModel implements PhpReferenceModel
{

    public PhpArrayElementReferenceModel(PhpValueModel key, PhpArrayModel array)
    {
        this.key=key;
        this.array=array;
        //this.proxyReference=null;
    }

    public PhpType getType() {
       return getValue().getType();
    }


    public PhpValueModel copyByReference(PhpEvalEnvironment pee) {
       return this;
    }

    public PhpValueModel copyByValue(PhpEvalEnvironment pee) {
       return getValue().copyByValue(pee);
    }

    public PhpArrayModel getArray(PhpEvalEnvironment pee) {
       return getValue().getArray(pee);
    }

    public boolean getBoolean() {
       return getValue().getBoolean();
    }

    public double getFloat() {
       return getValue().getFloat();
    }

    public int getInt() {
       return getValue().getInt();
    }

    public PhpObjectModel getObject(PhpEvalEnvironment pee) {
       return getValue().getObject(pee);
    }

    public String getString(PhpEvalEnvironment pee) {
       return getValue().getString(pee);
    }

    public PhpValueModel eval(PhpEvalEnvironment php) {
        return this;
    }

    @Override
    public void set(PhpValueModel value) {
        array.put(key, value);
      //  if (proxyReference!=null) {
      //      proxyReference.set(value);
      //  }
    }

    private PhpValueModel getValue()
    {
        PhpValueModel v = array.getMap().get(key);
        if (v==null) {
            v=new PhpNullValueModel();
        }
        return v;
    }

    public Term getTerm(PhpEvalEnvironment pee) throws TermWareException
    {
        return getValue().getTerm(pee);
    }


    public PhpReferenceModel getReferenceModel(PhpEvalEnvironment php) {
        return this;
    }

    public boolean isReference(PhpEvalEnvironment php) {
        return true;
    }

    public String getIdentifierName() {
        throw new UnsupportedOperationException("Not supported.");
    }

    public boolean isIdentifier() {
        return false;
    }



    //public void setReference(PhpReferenceModel ref) {
    //    throw new UnsupportedOperationException("Not supported yet.");
    //}



    PhpValueModel key;
    PhpArrayModel array;
    //PhpReferenceModel proxyReference;

}
