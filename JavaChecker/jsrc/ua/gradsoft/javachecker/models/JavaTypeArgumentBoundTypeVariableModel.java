/*
 * JavaTypeArgumentBoundTypeVariableModel.java
 *
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.List;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *TypeVariable, which is result by substitution of bounds by other type varibale.
 *(typical case: when bounds of method type parameter use type parameter of enclosing class)
 * @author Ruslan Shevchenko
 */
public class JavaTypeArgumentBoundTypeVariableModel extends JavaTypeVariableAbstractModel
{
 
    public JavaTypeArgumentBoundTypeVariableModel(JavaTypeVariableAbstractModel origin,List<JavaTypeModel> bounds)
    {
      origin_=origin;
      bounds_=bounds;   
    }

    
    public String getName()
    { return origin_.getName(); }
    
    public String getErasedName()
    { return origin_.getErasedName(); }
    
    public JavaModifiersModel  getModifiersModel()
    { return origin_.getModifiersModel(); }
    
    public List<JavaTypeModel> getBounds()
    { return bounds_; }       
    
    private JavaTypeVariableAbstractModel origin_;
    private List<JavaTypeModel> bounds_;
    
}
