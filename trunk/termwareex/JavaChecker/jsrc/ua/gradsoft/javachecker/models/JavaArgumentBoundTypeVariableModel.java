/*
 * JavaArgumentBoundTypeVariableModel.java
 *
 * Created on ������, 17, ���� 2007, 20:32
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.List;

/**
 *TypeVariable, which is result by substitution of bounds by other type varibale.
 *(typical case: when bounds of method type parameter use type parameter of enclosing class)
 * @author Ruslan Shevchenko
 */
public class JavaArgumentBoundTypeVariableModel extends JavaTypeVariableAbstractModel
{
 
    public JavaArgumentBoundTypeVariableModel(JavaTypeVariableAbstractModel origin,List<JavaTypeModel> bounds)
    {
      origin_=origin;
      bounds_=bounds;
    }

    
    public String getName()
    { return origin_.getName(); }
    
    public List<JavaTypeModel> getBounds()
    { return bounds_; }
    
    
    public boolean canCheck()
    { return false; }
    
    public boolean check()
    { return true; }
    
    private JavaTypeVariableAbstractModel origin_;
    private List<JavaTypeModel> bounds_;
    
}
