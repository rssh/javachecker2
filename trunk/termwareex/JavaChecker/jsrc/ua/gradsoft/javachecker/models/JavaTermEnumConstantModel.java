/*
 * JavaTermEnumConstantModel.java
 *
 * Created on п'€тниц€, 19, с≥чн€ 2007, 5:38
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Model of enum constant
 * @author Ruslan Shevchenko
 */
public class JavaTermEnumConstantModel extends JavaEnumConstantModel
{
    
    JavaTermEnumConstantModel(Term enumConstantTerm, JavaTermEnumModel owner) throws TermWareException
    {
      
      owner_=owner;
      Term idTerm = enumConstantTerm.getSubtermAt(IDENTIFIER_TERM_INDEX);
      name_ = idTerm.getSubtermAt(0).getString();
      System.err.println("enum constant "+name_+", arity="+enumConstantTerm.getArity());  
      if (enumConstantTerm.getArity()>=CLASSORINTERFACE_BODY_TERM_INDEX+1) {
          subtype_=new JavaTermEnumAnonimousTypeModel(name_,enumConstantTerm.getSubtermAt(CLASSORINTERFACE_BODY_TERM_INDEX),owner); 
      }
    }
    
    public String getName()
    {
      return name_;  
    }
    
    
    public JavaTypeModel getTypeModel()
    {
      return (subtype_==null) ? owner_ : subtype_;  
    }
    
    public JavaTypeModel getOwner()
    { return owner_; }
        
    
    public boolean canCheck()
    { return false; }
    
    public boolean check()
    { return true; }
    
    private String               name_;
    private JavaTermEnumModel    owner_;
    private JavaTermEnumAnonimousTypeModel subtype_=null;
    
    public static final int IDENTIFIER_TERM_INDEX=0;
    public static final int ARGUMENTS_TERM_INDEX=1;
    public static final int CLASSORINTERFACE_BODY_TERM_INDEX=2;
}
