/*
 * TermUtils.java
 *
 * Copyright (c) 2004-2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

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
    
     /**
      * build list of type parameters from term.
      *Term must be NIL or TypeParameters
      */
     public static List<JavaTypeVariableAbstractModel>  buildTypeParameters(Term tpt, JavaTypeModel typeModel) throws TermWareException
    {         
        if (tpt.isNil()) {
            return JavaModelConstants.TYPEVARIABLE_EMPTY_LIST;
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


     public static Map<String,JavaFormalParameterModel> buildFormalParameters(Term formalParametersList,JavaTopLevelBlockOwnerModel executable) throws TermWareException
    {
      TreeMap<String,JavaFormalParameterModel> retval = new TreeMap<String,JavaFormalParameterModel>();      
      int index=0;
      while(!formalParametersList.isNil()) {
          Term c = formalParametersList.getSubtermAt(0);
          formalParametersList = formalParametersList.getSubtermAt(1);
          int modifiers = c.getSubtermAt(0).getInt();
          Term typeTerm = c.getSubtermAt(1);
          JavaTypeModel tm = null;
          try {
             tm = JavaResolver.resolveTypeToModel(typeTerm,executable.getTypeModel(),executable.getTypeParameters());
          }catch(EntityNotFoundException ex){
             tm = JavaUnknownTypeModel.INSTANCE;             
          }
          Term vdi=c.getSubtermAt(2);  
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
          JavaFormalParameterModel pm = new JavaFormalParameterModel(modifiers,name,tm,executable,index);
          retval.put(name,pm);
          ++index;
      }
      return retval;
    }            

     
}
