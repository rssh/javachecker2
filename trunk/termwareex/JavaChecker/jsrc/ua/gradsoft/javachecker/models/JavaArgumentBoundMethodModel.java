/*
 * JavaArgumentBoundMethodModel.java
 * Copyright (c) 2004-2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Method with bound type arguments of class.
 * @author Ruslan Shevchenko
 */
public class JavaArgumentBoundMethodModel extends JavaMethodModel implements JavaArgumentBoundTopLevelBlockOwnerModel
{
    
    /** Creates a new instance of JavaArgumentBoundMethodModel */
    public JavaArgumentBoundMethodModel(JavaMethodModel origin,JavaTypeArgumentsSubstitution substitution) {
        super(origin.getTypeModel());
        origin_=origin;
        substitution_=substitution;
    }
    
    public String getName() {
        return origin_.getName(); }
    
    public JavaModifiersModel getModifiers()
    { return origin_.getModifiers(); }
    
    public List<JavaTypeVariableAbstractModel>  getTypeParameters() throws TermWareException {
        List<JavaTypeVariableAbstractModel> retval = new ArrayList<JavaTypeVariableAbstractModel>();
        for(JavaTypeVariableAbstractModel tv: origin_.getTypeParameters()) {
            List<JavaTypeModel> newBounds = substitution_.substitute(tv.getBounds());
            if (newBounds!=tv.getBounds()) {
               retval.add(new JavaArgumentBoundTypeVariableModel(tv,newBounds));
            }
        }
        return retval;        
    }
    
    public JavaTypeModel  getResultType() throws TermWareException {
        return substitution_.substitute(origin_.getResultType());        
    }
    
    
    public Map<String,JavaFormalParameterModel>  getFormalParameters() throws TermWareException
    {
      Map<String,JavaFormalParameterModel> retval = new TreeMap<String,JavaFormalParameterModel>();
      for(Map.Entry<String,JavaFormalParameterModel> e: origin_.getFormalParameters().entrySet())  {
          JavaFormalParameterModel oldModel = e.getValue();          
          JavaFormalParameterModel newModel = new JavaFormalParameterModel(oldModel.getModifiers().getIntValue(),
                  e.getKey(),
                  substitution_.substitute(oldModel.getTypeModel()),
                  this,
                  oldModel.getIndex()
                    );
          retval.put(e.getKey(),newModel);
      }
      return retval;
    }
    
    public  List<JavaTypeModel> getFormalParametersTypes() throws TermWareException
    {
      return substitution_.substitute(origin_.getFormalParametersTypes()); 
    }
    
    public  JavaMethodModel substituteTypeParameters(JavaTypeArgumentsSubstitution s) {
        //TODO: refine.
        return new JavaArgumentBoundMethodModel(this,s);
    }
    
    public boolean canCheck() {
        return false; }
    
    public boolean check() {
        return true; }
    
    public boolean isSupportBlockModel()
    { return origin_.isSupportBlockModel(); }
    
    public JavaArgumentBoundTopLevelBlockModel getTopLevelBlockModel() throws NotSupportedException {
        return new JavaArgumentBoundTopLevelBlockModel(this,origin_.getTopLevelBlockModel(),substitution_);
    }
    
    /**
     * TypeArgumentBoundMethodModel(originModel,substitution, context);
     */
    public Term getModelTerm() throws TermWareException
    {        
       Term mTerm=origin_.getModelTerm();
       Term sTerm = TermUtils.createJTerm(substitution_);
       Term jTerm = TermUtils.createJTerm(JavaPlaceContextFactory.createNewMethodContext(this));
       return TermUtils.createTerm("TypeArgumentBoundMethodModel",mTerm,sTerm,jTerm);
    }
    
    
    
    private JavaMethodModel origin_;
    private JavaTypeArgumentsSubstitution substitution_;
}
