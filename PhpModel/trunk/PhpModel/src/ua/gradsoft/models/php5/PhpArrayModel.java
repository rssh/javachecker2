
package ua.gradsoft.models.php5;

import java.util.Map;
import java.util.TreeMap;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermFactory;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;

/**
 *Model for PHP Array
 */
public class PhpArrayModel implements PhpValueModel
{

    public PhpArrayModel(PhpEvalEnvironment pee)
    { 
       this.map = new TreeMap<PhpValueModel,PhpValueModel>(new LooseComparator(pee));
    }

    public PhpArrayModel(Map<PhpValueModel,PhpValueModel> map)
    {
      this.map=map;  
    }

    public boolean getBoolean() {
        return map.size()!=0;
    }

    public double getFloat() {
        throw new PhpEvalException("Can't convert from array to float");
    }

    public int getInt() {
        throw new PhpEvalException("Can't convert from array to integer");
    }

    public PhpObjectModel getObject(PhpEvalEnvironment pee) {
        Map<String,PhpValueModel> newMap = new TreeMap<String,PhpValueModel>();
        for(Map.Entry<PhpValueModel,PhpValueModel> me: map.entrySet()) {
            newMap.put(me.getKey().getString(pee), me.getValue());
        }
        PhpClassDeclarationModel stdClass = pee.findClassDeclarationModel("stdClass");
        return new PhpObjectModel(stdClass,newMap);
    }

    public PhpArrayModel getArray(PhpEvalEnvironment pee) {
        return this;
    }


    public String getString(PhpEvalEnvironment pee) {
        return "Array";
    }

    public PhpType getType() {
        return PhpType.ARRAY;
    }

    public PhpValueModel getValue() {
        return this;
    }

    public int size()
    { return map.size(); }

    public PhpValueModel eval(PhpEvalEnvironment php) {
        return this;
    }

    public Term getTerm(PhpEvalEnvironment pee) throws TermWareException
    {
        TermFactory tf = TermWare.getInstance().getTermFactory();
        Term arr = tf.createNil();
        for(Map.Entry<PhpValueModel,PhpValueModel> e: map.entrySet()) {
            Term[] kv = new Term[2];
            kv[0] = e.getKey().getTerm(pee);
            kv[1] = e.getKey().getTerm(pee);
            Term entryTerm = tf.createTerm("ArrayKeyOrValue", kv);
            arr = tf.createTerm("cons", entryTerm, arr);
        }
        return tf.createTerm("SArray",arr);
    }

    public PhpValueModel copyByReference(PhpEvalEnvironment env) {
        return this;
    }

    public PhpValueModel copyByValue(PhpEvalEnvironment env) {
        Map<PhpValueModel, PhpValueModel> m =
                new TreeMap<PhpValueModel, PhpValueModel>(new LooseComparator(env));
        m.putAll(map);
        return new PhpArrayModel(m);
    }

    public Map<PhpValueModel,PhpValueModel> getMap()
    {
      return map;
    }

    public void put(PhpValueModel key, PhpValueModel value)
    { map.put(key, value); }



    private Map<PhpValueModel,PhpValueModel> map;
}
