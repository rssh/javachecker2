/*
 * JavaArgumentBoundMethodModel.java
 * Copyright (c) 2004-2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.List;
import ua.gradsoft.termware.TermWareException;

/**
 *Method with bound type arguments.
 * @author Ruslan Shevchenko
 */
public class JavaArgumentBoundMethodModel extends JavaMethodAbstractModel
{
    
    /** Creates a new instance of JavaArgumentBoundMethodModel */
    public JavaArgumentBoundMethodModel(JavaMethodAbstractModel origin,List<JavaTypeVariableAbstractModel> typeVariables,List<JavaTypeModel> values) {
        super(origin.getTypeModel());
        origin_=origin;
        typeVariables_=typeVariables;
        typeValues_=values;
    }
    
    public String getName()
    { return origin_.getName(); }
    
    public List<JavaTypeVariableAbstractModel>  getTypeParameters() throws TermWareException
    { return origin_.getTypeParameters(); }
    
    public JavaTypeModel  getResultType() throws TermWareException
    { return origin_.getResultType(); }
    
    public List<JavaTypeModel>  getFormalParametersTypes() throws TermWareException
    { return origin_.getFormalParametersTypes(); }
    
    public  JavaMethodAbstractModel substituteTypeParameters(List<JavaTypeVariableAbstractModel> typeVariables, List<JavaTypeModel> typeValues)
    {
        //TODO: refine.
        return new JavaArgumentBoundMethodModel(this,typeVariables,typeValues);
    }
    
    public boolean canCheck()
    { return false; }
    
    public boolean check()
    { return true; }
    
    private JavaMethodAbstractModel origin_;
    private List<JavaTypeVariableAbstractModel> typeVariables_;
    private List<JavaTypeModel> typeValues_;
}
