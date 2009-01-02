
package x;

import java.util.HashMap;


public class PropertyManager {
    
    
    HashMap supportedProps = new HashMap();
    
    /**
     * Initialize this object with the properties taken from passed PropertyManager object.
     */
    public PropertyManager(PropertyManager propertyManager){
        
        HashMap properties = propertyManager.getProperties();
        supportedProps.putAll(properties);
    }
    
    private HashMap getProperties(){
        return supportedProps ;
    }
    
    public String toString(){
        return supportedProps.toString();
    }
    
}//PropertyManager
