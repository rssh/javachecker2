/*
 * TermUtils.java
 *
 * Copyright (c) 2004-2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.JUtils;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermFactory;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Various utilities for work with terms.
 * @author Ruslan Shevchenko
 */
public class TermUtils {

    public static final TermFactory getTermFactory()
    {
      return TermWare.getInstance().getTermFactory();  
    }
    
    public static final Term createNil()
    {
      return getTermFactory().createNil();  
    }
    
    public static final Term createInt(int x)
    {
      return getTermFactory().createInt(x);
    }
    
    public static final Term createAtom(String name)
    {
        return getTermFactory().createAtom(name);
    }
    
    public static final Term createString(String value)
    {
       return getTermFactory().createString(value); 
    }

    public static final Term createTerm(String name) throws TermWareException
    {
      Term[] z=new Term[0];  
      return getTermFactory().createComplexTerm(name,z);
    }
    
    
    public static final Term createTerm(String name, Term t1) throws TermWareException
    {
      return getTermFactory().createTerm(name,t1);
    }

    public static final Term createIdentifier(String name) throws TermWareException
    {
      return getTermFactory().createTerm("Identifier",getTermFactory().createString(name));
    }
    
    
    public static final Term createTerm(String name, Term t1,Term t2) throws TermWareException
    {
      return getTermFactory().createTerm(name,t1,t2);
    }

    public static final Term createTerm(String name, Term t1,Term t2, Term t3) throws TermWareException
    {
      return getTermFactory().createTerm(name,t1,t2,t3);
    }
    
    public static final Term createTerm(String name, Term ... subterms) throws TermWareException
    {
      return getTermFactory().createTerm(name,subterms);
    }
    
    public static final Term createTerm(String name, Term t1, int i2) throws TermWareException
    {
      return getTermFactory().createTerm(name,t1,getTermFactory().createInt(i2));
    }
    
    public static final Term createJTerm(Object o) throws TermWareException
    {
      return getTermFactory().createJTerm(o);
    }
    
    public static final Term createBoolean(boolean b)
    {
      return getTermFactory().createBoolean(b);  
    }
    
    public static final Term addTermToList(Term list, Term object) throws TermWareException
    {
      if (list.isNil()) {
          return getTermFactory().createTerm("cons",object,list);
      }else{        
          while(!list.getSubtermAt(1).isNil()) {
              list=list.getSubtermAt(1);
          }
          list.setSubtermAt(1,getTermFactory().createTerm("cons",object,getTermFactory().createNil()));                      
          return list;
      }
    }
    

     /**
      *TODO: change type names in bounds to type-ref
      */
     public static Term buildTypeParametersModelTerm(List<JavaTypeVariableAbstractModel> tvams, Term tpt)
     {
       return tpt;  
     }


     
    public static Term reverseListTerm(Term t) throws TermWareException
    {
      Term retval=TermUtils.createNil();
      while(!t.isNil()) {
          retval = getTermFactory().createConsTerm(t.getSubtermAt(0),retval);
          t=t.getSubtermAt(1);
      }
      return retval;
    }
     
}
