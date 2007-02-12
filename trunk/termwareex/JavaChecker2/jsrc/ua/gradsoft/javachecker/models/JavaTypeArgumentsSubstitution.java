/*
 * JavaTypeArgumentsSubstitution.java
 *
 * Created on субота, 13, січня 2007, 15:10
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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
      if (typeVariables.size()!=typeValues.size()) {
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
      if (x.isTypeArgument()) {
          JavaTypeVariableAbstractModel m = (JavaTypeVariableAbstractModel)x;
          JavaTypeModel sx = get(m);
          return (sx!=null) ? sx : x;
      }else if(x.isWildcardBounds()){
          JavaWildcardBoundsTypeModel wx=(JavaWildcardBoundsTypeModel)x;
          if (wx.getKind()!=JavaWildcardBoundsKind.OBJECT) {
              JavaTypeModel bound = wx.getBoundTypeModel();
              JavaTypeModel substitutedBound = substitute(bound);
              if (bound==substitutedBound) {
                  return wx;
              }else{
                  return new JavaWildcardBoundsTypeModel(wx.getKind(),substitutedBound);
              }
          }
      }else if(x.hasTypeParameters()) {
         List<JavaTypeVariableAbstractModel> tps = x.getTypeParameters();
         if (tps.isEmpty()) {
             return x;
         }else{
            // boolean changed=true;          
            return new JavaArgumentBoundTypeModel(x,this,x);          
         }
      }else{
          return x;
      }
      return x;
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
