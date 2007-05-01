package ua.gradsoft.javachecker.models;
import java.io.Serializable;
import java.util.HashMap;
import ua.gradsoft.termware.Term;
/*
 * JavaTypeModelAttributesData.java
 *
 * Created on April 23, 2007, 3:02 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *Data object, which holds information about attributes.
 * @author rssh
 */
public class JavaTypeModelAttributesData implements Serializable
{
    
    
    
    
    
    public HashMap<String,Term>  getTypeAttributes()
    { return typeAttributes_; }
    
    public void  setTypeAttributes(HashMap<String,Term> typeAttributes)
    { typeAttributes_=typeAttributes; }
    
    public HashMap<String,HashMap<String,Term>> getMethodsAttributes()
    { return methodsAttributes_; }
    
    public void  setMethodsAttributes(HashMap<String,HashMap<String,Term>> methodsAttributes)
    { methodsAttributes_=methodsAttributes; }
    
    public HashMap<String,HashMap<String,Term>> getFieldsAttributes()
    { return fieldsAttributes_; }
    
    public void setFieldsAttributes(HashMap<String,HashMap<String,Term>> fieldAttributes)
    {  fieldsAttributes_=fieldAttributes; }
    
       
    /**
     * key is attribute name, value is attribute value.
     */
    private HashMap<String,Term>  typeAttributes_;
    
    /**
     * key is signature binary name, value is hashmap <key,vaue>
     */
    private HashMap<String,HashMap<String,Term>> methodsAttributes_;
    
    
    /**
     * key is field name, value is map<key,value>
     */
    private HashMap<String,HashMap<String,Term>>  fieldsAttributes_;
    
}
