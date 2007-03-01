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
      * build list of type parameters from term.
      *Term must be NIL or TypeParameters
      */
     public static List<JavaTypeVariableAbstractModel>  buildTypeParameters(Term tpt, JavaTypeModel typeModel) throws TermWareException
    {         
        if (tpt.isNil()) {
            return Collections.emptyList();
        }
        if (!tpt.getName().equals("TypeParameters")) {
            throw new AssertException("TypeParameters required instead "+TermHelper.termToString(tpt));
        }
        Term tl=tpt.getSubtermAt(0);
        List<JavaTypeVariableAbstractModel> retval=new LinkedList<JavaTypeVariableAbstractModel>();
        while(!tl.isNil()) {
            Term ta=tl.getSubtermAt(0);
            tl=tl.getSubtermAt(1);
            if (!ta.getName().equals("TypeParameter")) {
                throw new AssertException("TypeParameter required instead "+TermHelper.termToString(ta));
            }
            JavaTermTypeVariableModel m=new JavaTermTypeVariableModel(ta,typeModel);
            retval.add(m);
        }
        return retval;
    }

     /**
      *TODO: change type names in bounds to type-ref
      */
     public static Term buildTypeParametersModelTerm(List<JavaTypeVariableAbstractModel> tvams, Term tpt)
     {
       return tpt;  
     }


     public static List<JavaFormalParameterModel> buildFormalParametersList(Term formalParametersList,JavaTopLevelBlockOwnerModel executable) throws TermWareException
    {
      ArrayList<JavaFormalParameterModel> retval = new ArrayList<JavaFormalParameterModel>();      
      int index=0;
      while(!formalParametersList.isNil()) {
          Term c = formalParametersList.getSubtermAt(0);
          formalParametersList = formalParametersList.getSubtermAt(1);
          JavaFormalParameterModel pm = buildFormalParameter(c,executable,index);
          retval.add(pm);
          ++index;
      }
      return retval;
    }            
     
     
     
     public static Map<String,JavaFormalParameterModel> buildFormalParametersMap(Term formalParametersList,JavaTopLevelBlockOwnerModel executable) throws TermWareException
    {
      TreeMap<String,JavaFormalParameterModel> retval = new TreeMap<String,JavaFormalParameterModel>();      
      int index=0;
      while(!formalParametersList.isNil()) {
          Term c = formalParametersList.getSubtermAt(0);
          formalParametersList = formalParametersList.getSubtermAt(1);
          JavaFormalParameterModel pm = buildFormalParameter(c,executable,index);
          retval.put(pm.getName(),pm);
          ++index;
      }
      return retval;
    }            

    private static JavaFormalParameterModel buildFormalParameter(Term fp,JavaTopLevelBlockOwnerModel executable,int index) throws TermWareException
    {
          int modifiers = fp.getSubtermAt(0).getInt();
          Term typeTerm = fp.getSubtermAt(1);
          JavaTypeModel tm = null;
          try {
             tm = JavaResolver.resolveTypeToModel(typeTerm,executable.getTypeModel(),executable.getTypeParameters());
          }catch(EntityNotFoundException ex){
             tm = JavaUnknownTypeModel.INSTANCE;             
          }
          Term vdi=fp.getSubtermAt(2);  
          String name=null;
          //System.out.println("vdi is:"+TermHelper.termToString(vdi));
          if (vdi.getName().equals("VariableDeclaratorId")) {
            int nArray = vdi.getSubtermAt(1).getInt();
            name = vdi.getSubtermAt(0).getSubtermAt(0).getString();
            while(nArray > 0) {
              tm = new JavaArrayTypeModel(tm);
              --nArray;
            }
          }else if(vdi.getName().equals("Identifier")){
            name = vdi.getSubtermAt(0).getString();
          }else{
              throw new AssertException("Invalid VariableDeclaratorId in formal parameters:"+TermHelper.termToString(vdi));
          }
          if ((modifiers & JavaModifiersModel.VARARGS)!=0) {
              tm = new JavaArrayTypeModel(tm);
          }
          JavaFormalParameterModel pm = new JavaFormalParameterModel(modifiers,name,tm,executable,index);        
          return pm;
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
