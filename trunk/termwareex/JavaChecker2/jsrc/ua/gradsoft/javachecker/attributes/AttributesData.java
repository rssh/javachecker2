/*
 * AttributesData.java
 *
 * Created on April 23, 2007, 3:02 PM
 *
 */
package ua.gradsoft.javachecker.attributes;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import ua.gradsoft.javachecker.models.TermUtils;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;

/**
 *Data object, which holds information about attributes.
 * @author rssh
 */
public class AttributesData implements Serializable, AttributedEntity
{
               
    
    public boolean isEmpty()
    {
        return generalAttributes_.isEmpty() && childs_.isEmpty();
    }
    
    public HashMap<String,Term>  getGeneralAttributes()
    { return generalAttributes_; }
    
    public void  setGeneralAttributes(HashMap<String,Term> generalAttributes)
    { generalAttributes_=generalAttributes; }
    
    public HashMap<String,AttributesData> getChilds()
    { return childs_; }
        
    public void  setChilds(HashMap<String,AttributesData> childs)
    { childs_=childs; }
    
    public Term  getAttribute(String name)
    { Term retval = generalAttributes_.get(name); 
      return (retval==null) ? TermUtils.createNil() : retval;
    }
    
    public void  setAttribute(String name, Term value)
    { generalAttributes_.put(name,value); }
    
    public AttributedEntity getChildAttributes(String childName)
    {
        return getOrCreateChild(childName);
    }
    
    public AttributesData  getOrCreateChild(String name)
    {
       AttributesData retval = childs_.get(name);
       if (retval==null) {
           retval=new AttributesData();
           childs_.put(name,retval);
       }
       return retval;
    }
    
    public void merge(AttributesData other)
    {
       generalAttributes_.putAll(other.generalAttributes_);
       for(Map.Entry<String,AttributesData> e: other.childs_.entrySet())
       {
           AttributesData x = childs_.get(e.getKey());
           if (x==null) {
               childs_.put(e.getKey(),e.getValue());
           }else{
               x.merge(e.getValue());
           }
       }
    }
    
    public void print(PrintWriter out)
    {
      print(out,0);  
    }
    
    public void print(PrintWriter out, int level)
    {
       printIdent(out,level);
       out.println("general:{");
       level=level+1;
       for(Map.Entry<String,Term> e: generalAttributes_.entrySet()) {
           printIdent(out,level);
           out.print("(");
           out.print(e.getKey());
           out.print(",");
           out.print(TermHelper.termToString(e.getValue()));
           out.print(")");
           out.println();           
       }
       level=level-1;
       printIdent(out,level);
       out.println("}");
       printIdent(out,level);
       out.println("childs:{");
       level=level+1;
       for(Map.Entry<String,AttributesData> e: childs_.entrySet()) {
           printIdent(out,level);
           out.print(e.getKey());
           out.println();
           e.getValue().print(out,level+1);
           out.println();
       }
       out.println("}");
    }
    
    private void printIdent(PrintWriter out, int level)
    {
       for(int i=0; i<level; ++i) {
           out.print(' ');
       } 
    }
    
    /**
     * key is attribute name, value is attribute value.
     */
    private HashMap<String,Term>  generalAttributes_ = new HashMap<String,Term>();
    
    /**
     * key is signature binary name or name of type, value is Attributes
     *of subtypes
     */           
    private HashMap<String,AttributesData> childs_ = new HashMap<String,AttributesData>();
            
}
