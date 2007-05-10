/*
 * AttributesData.java
 *
 * Created on April 23, 2007, 3:02 PM
 *
 */
package ua.gradsoft.javachecker.attributes;
import java.io.Serializable;
import java.util.HashMap;
import ua.gradsoft.termware.Term;

/**
 *Data object, which holds information about attributes.
 * @author rssh
 */
public class AttributesData implements Serializable
{
                    
    public HashMap<String,Term>  getGeneralAttributes()
    { return generalAttributes_; }
    
    public void  setGeneralAttributes(HashMap<String,Term> generalAttributes)
    { generalAttributes_=generalAttributes; }
    
    public HashMap<String,AttributesData> getChilds()
    { return childs_; }
        
    public void  setChilds(HashMap<String,AttributesData> childs)
    { childs_=childs; }
    
    public AttributesData  getOrCreateChild(String name)
    {
       AttributesData retval = childs_.get(name);
       if (retval==null) {
           retval=new AttributesData();
           childs_.put(name,retval);
       }
       return retval;
    }
       
    /**
     * key is attribute name, value is attribute value.
     */
    private HashMap<String,Term>  generalAttributes_;
    
    /**
     * key is signature binary name or name of type, value is Attributes
     *of subtypes
     */           
    private HashMap<String,AttributesData> childs_;
            
}
