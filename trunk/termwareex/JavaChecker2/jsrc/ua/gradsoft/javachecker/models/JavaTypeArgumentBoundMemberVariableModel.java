/*
 * JavaArumentBoundTypeVariableModel.java
 *
 * Copyright (c) 2004-2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Member value, bound by type argument
 * @author Ruslan Shevchenko
 */
public class JavaTypeArgumentBoundMemberVariableModel extends JavaMemberVariableModel
{
    
    
    /** Creates a new instance of JavaArgumentBoundTypeVariableModel */
    public JavaTypeArgumentBoundMemberVariableModel(JavaTypeArgumentBoundTypeModel newOwner, JavaMemberVariableModel origin) 
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
      //return owner_.substituteTypeParameters(originTypeModel);
      return owner_.getSubstitution().substitute(originTypeModel);
    }

    public JavaModifiersModel  getModifiersModel()
    {
        return origin_.getModifiersModel();
    }
    
    
    /**
     * TypeArgumentBoundMemberVariableModel(originModelTerm,ownerModelTerm)
     */
    public Term getModelTerm() throws TermWareException, EntityNotFoundException
    {
        Term originModelTerm = origin_.getModelTerm();
        Term ownerModelTerm = owner_.getModelTerm();
        Term retval=TermUtils.createTerm("TypeArgumentBoundMemberVariableModel",
                                          originModelTerm,ownerModelTerm);
        return retval;
    }
    
    private JavaMemberVariableModel origin_;
    private JavaTypeArgumentBoundTypeModel      owner_;
    
}
