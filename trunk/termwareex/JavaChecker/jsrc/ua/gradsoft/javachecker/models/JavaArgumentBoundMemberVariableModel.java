/*
 * JavaArumentBoundTypeVariableModel.java
 *
 * Copyright (c) 2004-2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import ua.gradsoft.termware.TermWareException;

/**
 *Member value, bound by type arument
 * @author Ruslan Shevchenko
 */
public class JavaArgumentBoundMemberVariableModel extends JavaMemberVariableAbstractModel
{
    
    
    /** Creates a new instance of JavaArgumentBoundTypeVariableModel */
    public JavaArgumentBoundMemberVariableModel(JavaArgumentBoundTypeModel newOwner, JavaMemberVariableAbstractModel origin) 
    {
       origin_=origin;
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
    
    private JavaMemberVariableAbstractModel origin_;
    private JavaArgumentBoundTypeModel      owner_;
    
}
