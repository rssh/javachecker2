/*
 * TermUtils.java
 *
 * Copyright (c) 2004-2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.List;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermFactory;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TermWareSymbols;

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

    public static final Term createLong(long x)
    {
      return getTermFactory().createLong(x);
    }


    public static final Term createDouble(double x)
    {
        return getTermFactory().createDouble(x);
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
    
    public static final Term createChar(char ch)
    {
        return getTermFactory().createChar(ch);
    }

    public static final Term createFloat(float x)
    {
        return getTermFactory().createFloat(x);
    }


    public static final Term appendTermToList(Term list, Term object) throws TermWareException
    {
      if (list.isNil()) {
          return getTermFactory().createTerm("cons",object,list);
      }else{      
        // Term svList = list;
        // while(!list.getSubtermAt(1).isNil()) {
        //      list=list.getSubtermAt(1);
        // }
        // list.setSubtermAt(1,getTermFactory().createTerm("cons",object,getTermFactory().createNil()));                      
        // return svList;         
          Term nl = TermUtils.createNil();
          while(!list.isNil()) {
              nl = TermUtils.createTerm("cons",list.getSubtermAt(0),nl);
              list=list.getSubtermAt(1);
          }
          nl = TermUtils.createTerm("cons",object,nl);
          return TermUtils.reverseListTerm(nl);
      }
    }
    
    public static final Term createList(Term[] array)
    {
        return getTermFactory().createList(array);
    }

     /**
      * TypeParameters(cons(TypeVariableModel,cons(TypeRef2,...,NIL)...))
      *[may be todo: insert origin name in typeref]
      */
     public static Term buildTypeParametersModelTerm(List<JavaTypeVariableAbstractModel> tvams, Term tpt) throws TermWareException, EntityNotFoundException
     {
       Term retval = TermUtils.createNil();
       for(JavaTypeVariableAbstractModel tv: tvams) {
           Term tvm = tv.getModelTerm();
           retval = TermUtils.createTerm("cons",tvm,retval);
       }
       retval = TermUtils.reverseListTerm(retval);
       if (!retval.isNil()) {
          retval = TermUtils.createTerm("TypeParameters",retval);
       }
       return retval;  
     }

     

     /**
      * FormalParameters([FormalParameterModel(..)..])
      */
     public static Term buildFormalParametersModelTerm(List<JavaFormalParameterModel> fpms,Term origin) throws TermWareException, EntityNotFoundException
     {
       Term retval = TermUtils.createNil();
       for(JavaFormalParameterModel fpm: fpms) {
           Term m = fpm.getModelTerm();
           retval = TermUtils.createTerm("cons",m,retval);
       }
       retval=TermUtils.reverseListTerm(retval);
       retval=TermUtils.createTerm("FormalParameters",retval);
       return retval;
     }
     
     public static Term buildThrowsNameListModelTerm(List<JavaTypeModel> throwsNameList, Term origin) throws TermWareException
     {
         Term retval = TermUtils.createNil();
         Term ct = TermUtils.createNil();
         if (!origin.isNil()) {
             ct=origin.getSubtermAt(0);
         }
         for(JavaTypeModel tm: throwsNameList)
         {
           Term tname = null; 
           if (ct.getNameIndex().equals(TermWareSymbols.CONS_INDEX)) {
               tname = ct.getSubtermAt(0);
               ct=ct.getSubtermAt(1);
           }else{
               tname = tm.getFullNameAsTerm();
           } 
           Term jterm = TermUtils.createJTerm(tm);
           Term refTerm = TermUtils.createTerm("TypeRef",tname,jterm);
           retval=TermUtils.createTerm("cons",refTerm,retval);
         }
         retval = TermUtils.reverseListTerm(retval);
         return retval;
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
