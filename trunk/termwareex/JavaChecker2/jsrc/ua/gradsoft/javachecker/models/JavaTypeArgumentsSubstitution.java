/*
 * JavaTypeArgumentsSubstitution.java
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.JUtils;
import ua.gradsoft.javachecker.util.Holder;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Substitution of type variables.
 * @author Ruslan Shevchenko
 */
public class JavaTypeArgumentsSubstitution {
    
    public JavaTypeArgumentsSubstitution()
    {
     map_=new TreeMap<JavaTypeVariableAbstractModel,JavaTypeModel>(TypeVariableComparator.INSTANCE);   
    }
    
    public JavaTypeArgumentsSubstitution(List<JavaTypeVariableAbstractModel> typeVariables,
                                         List<JavaTypeModel> typeValues) throws TermWareException
    {
      map_=new TreeMap<JavaTypeVariableAbstractModel,JavaTypeModel>(TypeVariableComparator.INSTANCE);
      if (false/*typeVariables.size()!=typeValues.size()*/) {
          StringBuilder sb=new StringBuilder();
          sb.append("variables:(");
          boolean frs=true;
          for(JavaTypeVariableAbstractModel tv:typeVariables) {
              if (!frs) {
                  sb.append(",");
              }else{
                  frs=false;
              }
              sb.append(tv.getName());
          }
          sb.append(") values(");
          frs=true;
          for(JavaTypeModel t:typeValues) {
              if (!frs) {
                  sb.append(",");
              }else{
                  frs=false;
              }
              sb.append(t.getName());
          }
          sb.append(")");
          throw new AssertException("size of typeVariables must be the same as size of TypeValues:"+sb.toString());
      }
      Iterator<JavaTypeVariableAbstractModel> itVar=typeVariables.iterator();
      Iterator<JavaTypeModel> itVal=typeValues.iterator();
      while(itVar.hasNext()) {
          JavaTypeVariableAbstractModel var=itVar.next();
          JavaTypeModel val=itVal.next();
          map_.put(var,val);
      }
    }
    
    public JavaTypeModel substitute(JavaTypeModel x) throws TermWareException
    {
        Holder<Boolean> holder=new Holder<Boolean>();
        holder.setValue(false);
        return substitute(x,holder);
    }
    
    public JavaTypeModel substitute(JavaTypeModel x, Holder<Boolean> changed) throws TermWareException
    {        
      //System.out.println("substitute:"+x.getFullName()+" by "+this.toString());  
      JavaTypeModel retval=x;
      if (x.isTypeArgument()) {          
          JavaTypeVariableAbstractModel m = (JavaTypeVariableAbstractModel)x;
          JavaTypeModel sx = get(m);
          if (sx!=null) {
              changed.setValue(true);
              retval=sx;
          }else{
              retval=x;
          }         
      }else if(x.isWildcardBounds()){
          JavaWildcardBoundsTypeModel wx=(JavaWildcardBoundsTypeModel)x;
          if (wx.getKind()!=JavaWildcardBoundsKind.OBJECT) {
              JavaTypeModel bound = wx.getBoundTypeModel();
              boolean svChanged = changed.getValue();
              changed.setValue(false);
              JavaTypeModel substitutedBound = substitute(bound,changed);
              if (!changed.getValue()) {
                  retval = wx;
                  changed.setValue(svChanged);
              }else{
                  retval = new JavaWildcardBoundsTypeModel(wx.getKind(),substitutedBound);
              }
          }
      }else if(x.hasTypeParameters()) {       
         List<JavaTypeVariableAbstractModel> tps = x.getTypeParameters();         
         if (tps.isEmpty()) {
             retval = x;
         }else{         
            // boolean changed=true;                 
            changed.setValue(true);
            retval = new JavaTypeArgumentBoundTypeModel(x,this,x);          
         }
      }else if (x instanceof JavaTypeArgumentBoundTypeModel){         
          JavaTypeArgumentBoundTypeModel tx = (JavaTypeArgumentBoundTypeModel)x;
          // let's substitute all type arguments.          
          List<JavaTypeModel> oldTypeArguments;
          try {              
             oldTypeArguments = tx.getResolvedTypeArguments();
             //System.out.println("substitute type arguments, old="+oldTypeArguments.toString());
          }catch(EntityNotFoundException ex){
              InvalidJavaTermException ex1 = new InvalidJavaTermException(ex.getMessage(),tx.getTypeArguments());
              ex1.setFileAndLine(JUtils.getFileAndLine(tx.getTypeArguments()));          
              throw ex1;
          }
          List<JavaTypeModel> newTypeArguments = new ArrayList<JavaTypeModel>(oldTypeArguments.size());
          boolean svChanged=changed.getValue();
          changed.setValue(false);
          for(JavaTypeModel otm: oldTypeArguments) {                       
              JavaTypeModel ntm = substitute(otm,changed);
              newTypeArguments.add(ntm);
          }
          if (!changed.getValue()) {
              retval = x;
              changed.setValue(svChanged);
          }else{
              JavaTypeModel xo=tx.getOrigin();
      //        System.out.println("");
      //        System.out.println("substitute type arguments, new="+newTypeArguments.toString());
              retval = new JavaTypeArgumentBoundTypeModel(xo,newTypeArguments,x);
          }
      }else{
          retval=x;
      }
      //System.out.println("substitute:"+x.getFullName()+" by "+this.toString()+" returns "+retval.getFullName());        
      return retval;
    }
    
    public List<JavaTypeModel>  substitute(List<? extends JavaTypeModel> l) throws TermWareException
    {
      ArrayList<JavaTypeModel> retval = new ArrayList<JavaTypeModel>(l.size());  
      for(JavaTypeModel t: l){
          retval.add(substitute(t));
      }
      return retval;
    }
    
    
    
    /**
     *@return substitition for x or null if one is not found
     */
    public JavaTypeModel get(JavaTypeVariableAbstractModel x)
    {
      return map_.get(x);  
    }
    
    public void put(JavaTypeVariableAbstractModel v,JavaTypeModel t)
    {
      map_.put(v,t);  
    }
    
    public void putAll(JavaTypeArgumentsSubstitution otherSubstitution)
    {
        map_.putAll(otherSubstitution.map_);
    }
    
    
    public void print(PrintWriter out) 
    {
      out.print("[");
      boolean frs=true;
      for(Map.Entry<JavaTypeVariableAbstractModel,JavaTypeModel> e: map_.entrySet()) {
          if (frs) {
              frs=false;
          }else{
              out.print(",");
          }
          e.getKey().print(out);
          out.print("/");
          out.print(e.getValue().getName());
      }  
      out.print("]");  
    }
    
    public void print(PrintStream out) 
    {
      PrintWriter writer = new PrintWriter(out);
      print(writer);
      writer.flush();
      // must not be close.
    }
    
    /**
     * represet as string.
     */
    public String toString()
    {
        StringWriter swr=new StringWriter();
        PrintWriter writer = new PrintWriter(swr);
        print(writer);
        writer.close();
        return swr.toString();
    }
    
    static class TypeVariableComparator implements Comparator<JavaTypeVariableAbstractModel>
    {
        private TypeVariableComparator() {}
        
        public int compare(JavaTypeVariableAbstractModel x, JavaTypeVariableAbstractModel y)
        {
            return x.getName().compareTo(y.getName());            
        }
        
        public static final TypeVariableComparator INSTANCE = new TypeVariableComparator();
    }
    
    private Map<JavaTypeVariableAbstractModel,JavaTypeModel> map_; 
    
}
