/*
 * TermUtils.java
 *
 * Copyright (c) 2004-2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermFactory;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;

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
    
    public static final Term createAtom(String name)
    {
        return getTermFactory().createAtom(name);
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
    
    public static final Term createTerm(String name, Term t1, int i2) throws TermWareException
    {
      return getTermFactory().createTerm(name,t1,getTermFactory().createInt(i2));
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
    
}
