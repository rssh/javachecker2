/*
 * JavaTermTypeVariableModel.java
 *
 * Copyright (c) 2004-2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.FileAndLine;
import ua.gradsoft.javachecker.JUtils;
import ua.gradsoft.javachecker.Main;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *
 * @author Ruslan Shevchenko
 */
public class JavaTermTypeVariableModel extends JavaTypeVariableAbstractModel
{
    

    
    /**
     * Creates a new instance of JavaTermTypeVariableModel
     */
    public JavaTermTypeVariableModel(Term t,JavaTypeModel where) throws TermWareException
    {
        boolean printDetails=false;               
        //bounds_=new LinkedList<JavaTypeModel>();
        bounds_=null;
        termBounds_=TermUtils.createNil();
        if (t.getName().equals("Identifier")) {
            name_=t.getSubtermAt(0).getString();                                                
        }else if(t.getName().equals("TypeParameter")){
            name_=t.getSubtermAt(0).getSubtermAt(0).getString();
            if (t.getArity()>1) {
                termBounds_=t.getSubtermAt(1);
                //addTypeBounds(t.getSubtermAt(1),where);
            }
        }
        where_=where;
        fileAndLine_=JUtils.getFileAndLine(t);
        if (fileAndLine_==null) {
            // impossible ?
            fileAndLine_=new FileAndLine("unknown",-1);
        }
    }
    
    public JavaTermModifiersModel getModifiersModel()
    {
      return JavaModelConstants.PUBLIC_MODIFIERS; 
    }
    
    /**
     *addTypeBound.
     *t must be in form "TypeBound([b1,b2,...])"
     */
    private void addTypeBounds(Term t,JavaTypeModel where) throws TermWareException
    {            
      t=t.getSubtermAt(0);
      while(!t.isNil()) {
          Term classOrInterfaceType=t.getSubtermAt(0);
          t=t.getSubtermAt(1);
          JavaTypeModel bound;          
          try {              
              bound=JavaResolver.resolveTypeToModel(classOrInterfaceType,where,Collections.<JavaTypeVariableAbstractModel>singletonList(this));              
          }catch(EntityNotFoundException ex){
              InvalidJavaTermException ex1 = new InvalidJavaTermException(ex.getMessage(),classOrInterfaceType);
              ex1.setFileAndLine(JUtils.getFileAndLine(classOrInterfaceType));
              throw ex1;
          }
          bounds_.add(bound);          
      }  
    }
    
    public String getName()
    { return name_; }
    
    public String getErasedName()
    { return "Object"; }
    
    public List<JavaTypeModel> getBounds() throws TermWareException 
    {
      if (bounds_==null) {
          bounds_=new LinkedList<JavaTypeModel>();
          if (!termBounds_.isNil()) {
            addTypeBounds(termBounds_,where_);
          }
      }
      return bounds_;  
    }
    
        
    /**
     * TypeVariableModel(name,bounds)
     *where bounds are lists of typeref
     */
    public  Term getModelTerm() throws TermWareException
    {
        Term idTerm = TermUtils.createIdentifier(name_);
        Term boundsList = TermUtils.createNil();
        for(JavaTypeModel tm: getBounds()) {
            Term typeRef = TermUtils.createTerm("TypeRef",tm.getShortNameAsTerm(),TermUtils.createJTerm(tm));
            boundsList = TermUtils.createTerm("cons",typeRef,boundsList);
        }
        boundsList = TermUtils.reverseListTerm(boundsList);
        return TermUtils.createTerm("TypeVariableModel",idTerm,boundsList);
    }
     
     
    private String name_;
    private FileAndLine  fileAndLine_;
    private List<JavaTypeModel> bounds_;
    private Term           termBounds_;
    private JavaTypeModel  where_;
    
}
