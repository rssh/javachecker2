/*
 * JavaMethodModelAttributes.java
 *
 */

package ua.gradsoft.javachecker.models;

import java.io.Serializable;
import java.util.HashMap;
import ua.gradsoft.javachecker.attributes.JavaTypeModelAttributes;
import ua.gradsoft.termware.Term;

/**
 *Holder for method attributes.
 * @author rssh
 */
public class JavaMethodModelAttributes implements Serializable
{
    
    JavaMethodModelAttributes(JavaTypeModelAttributes owner,JavaMethodModel methodModel,HashMap<String,Term> attributes)
    {
      owner_=owner;  
      methodModel_=methodModel;
      attributes_=attributes;
    }
    
    public JavaMethodModel   getMethodModel()
    { return methodModel_; }
    
  
    public Term  getAttribute(String name)
    {
       Term retval = attributes_.get(name);
       if (retval==null) {
           retval=TermUtils.createNil();
       }
       return retval;
    }
    
    public void setAttribute(String name, Term value)
    {
       attributes_.put(name,value);     
    }
    
    private HashMap<String,Term> attributes_;
    private JavaMethodModel      methodModel_;
    private JavaTypeModelAttributes owner_;
    
}
