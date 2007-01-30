/*
 * JavaArumentBoundTypeVariableModel.java
 *
 * Copyright (c) 2004-2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Member value, bound by type argument
 * @author Ruslan Shevchenko
 */
public class JavaArgumentBoundMemberVariableModel extends JavaMemberVariableModel
{
    
    
    /** Creates a new instance of JavaArgumentBoundTypeVariableModel */
    public JavaArgumentBoundMemberVariableModel(JavaArgumentBoundTypeModel newOwner, JavaMemberVariableModel origin) 
    {
       origin_=origin;
       owner_=newOwner;
    }

    
    public String getName()
    { return origin_.getName(); }
    
    public JavaTypeModel  getOwner()
    { 
       return owner_;  
    }
    
    public JavaTypeModel  getTypeModel() throws TermWareException
    {
      JavaTypeModel originTypeModel = origin_.getTypeModel();
      return owner_.substituteTypeParameters(originTypeModel);
    }

    public JavaModifiersModel  getModifiersModel()
    {
        return origin_.getModifiersModel();
    }
    
    public boolean canCheck()
    { return false; }
    
    public boolean check()
    { return true; }
    
    /**
     * TypeArgumentBoundMemberVariableModel(originModelTerm,ownerModelTerm)
     */
    public Term getModelTerm() throws TermWareException
    {
        Term originModelTerm = origin_.getModelTerm();
        Term ownerModelTerm = owner_.getModelTerm();
        Term retval=TermUtils.createTerm("TypeArgumentBoundMemberVariableModel",
                                          originModelTerm,ownerModelTerm);
        return retval;
    }
    
    private JavaMemberVariableModel origin_;
    private JavaArgumentBoundTypeModel      owner_;
    
}
