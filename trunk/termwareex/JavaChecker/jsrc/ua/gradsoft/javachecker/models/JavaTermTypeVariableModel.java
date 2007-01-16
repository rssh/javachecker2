/*
 * JavaTermTypeVariableModel.java
 *
 * Copyright (c) 2004-2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

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
        bounds_=new LinkedList<JavaTypeModel>();
        if (t.getName().equals("Identifier")) {
            name_=t.getSubtermAt(0).getString();                                                
        }else if(t.getName().equals("TypeParameter")){
            name_=t.getSubtermAt(0).getSubtermAt(0).getString();
            if (t.getArity()>1) {
                addTypeBounds(t.getSubtermAt(1),where);
            }
        }
        fileAndLine_=JUtils.getFileAndLine(t);
        if (fileAndLine_==null) {
            // impossible ?
            fileAndLine_=new FileAndLine("unknown",-1);
        }
    }
    
    private void addTypeBounds(Term t,JavaTypeModel where) throws TermWareException
    {
      while(!t.isNil()) {
          Term classOrInterfaceType=t.getSubtermAt(0);
          t=t.getSubtermAt(1);
          JavaTypeModel bound;
          try {
              bound=JavaResolver.resolveTypeToModel(classOrInterfaceType,where);              
          }catch(EntityNotFoundException ex){
              bound=JavaUnknownTypeModel.INSTANCE;
          }
          bounds_.add(bound);          
      }  
    }
    
    public String getName()
    { return name_; }
    
    public List<JavaTypeModel> getBounds() 
    {
      return bounds_;  
    }
    
    
     public boolean check() throws TermWareException {
       if (Main.getFacts().isCheckEnabled("TypeArgumentNamePatterns")) {
          if (!getName().matches(Main.getFacts().getTypeArgumentNamePattern())) {
              Main.getFacts().violationDiscovered("TypeArgumentNamePatterns","violation of type argument name pattern ", fileAndLine_);
          }
       }
       return true;
    }

    
    private String name_;
    private FileAndLine  fileAndLine_;
    private List<JavaTypeModel> bounds_;
    
}
