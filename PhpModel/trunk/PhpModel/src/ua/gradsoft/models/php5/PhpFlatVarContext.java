
package ua.gradsoft.models.php5;

import java.util.Map;

/**
 *Flat variable context
 * @author rssh
 */
public class PhpFlatVarContext {

    public void bind(String name, PhpValueModel model)
    {
       map.put(name, model); 
    }
    
    public PhpValueModel get(String name)
    {
       return map.get(name); 
    }
    
    public void set(String name, PhpValueModel model)
    {
        PhpValueModel v = map.get(name);
        if (v==null) {
            map.put(name, model);
        } else if (v instanceof PhpReferenceModel) {
            PhpReferenceModel rv = (PhpReferenceModel)v;
            rv.set(model);
        } else {
            // overwrite
            map.put(name, model);
        }
    }

    public Map<String, PhpValueModel> getMap()
    { return map; }

    private Map<String,PhpValueModel> map;
}
