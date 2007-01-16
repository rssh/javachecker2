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
    
    public JavaTypeModel substitute(JavaTypeModel x)
    {
      throw new RuntimeException("Not implemented");  
    }
    
    public List<JavaTypeModel>  substitute(List<JavaTypeModel> l)
    {
      ArrayList<JavaTypeModel> retval = new ArrayList<JavaTypeModel>(l.size());  
      for(JavaTypeModel t: l){
          retval.add(substitute(t));
      }
      return retval;
    }
    
    public List<JavaTypeVariableAbstractModel>  substituteTypeVariables(List<JavaTypeVariableAbstractModel> l)
    {
      ArrayList<JavaTypeVariableAbstractModel> retval = new ArrayList<JavaTypeVariableAbstractModel>(l.size());    
      for(JavaTypeVariableAbstractModel t: l){
          JavaTypeModel m = substitute(t);
          // result of TypeVariable substitution is always Type Variable
          retval.add((JavaTypeVariableAbstractModel)m);
      }
      return retval;      
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
