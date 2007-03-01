/*
 * JavaTypeArgumentBoundMethodModel.java
 * Copyright (c) 2004-2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Method with bound type arguments of class.
 * @author Ruslan Shevchenko
 */
public class JavaTypeArgumentBoundMethodModel extends JavaMethodModel implements JavaTypeArgumentBoundTopLevelBlockOwnerModel
{
    
    /**
     * Creates a new instance of JavaTypeArgumentBoundMethodModel
     */
    public JavaTypeArgumentBoundMethodModel(JavaMethodModel origin,JavaTypeArgumentsSubstitution substitution) {
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
               retval.add(new JavaTypeArgumentBoundTypeVariableModel(tv,newBounds));
            }
        }
        return retval;        
    }
    
    public JavaTypeModel  getResultType() throws TermWareException {
        if (boundResultType_==null){
          boundResultType_ = substitution_.substitute(origin_.getResultType());        
        }
        return boundResultType_;
    }
    
    public List<JavaFormalParameterModel>  getFormalParametersList() throws TermWareException
    {
      List<JavaFormalParameterModel> ol = origin_.getFormalParametersList();  
      ArrayList<JavaFormalParameterModel> retval = new ArrayList<JavaFormalParameterModel>(ol.size());
      for(JavaFormalParameterModel ofp: ol) {
          JavaFormalParameterModel newModel = new JavaFormalParameterModel(
                  ofp.getModifiers().getIntValue(),
                  ofp.getName(),
                  substitution_.substitute(ofp.getTypeModel()),
                  this,
                  ofp.getIndex()
                  );
          retval.add(newModel);
      }
      return retval;
    }
    
    public Map<String,JavaFormalParameterModel>  getFormalParametersMap() throws TermWareException
    {
      Map<String,JavaFormalParameterModel> retval = new TreeMap<String,JavaFormalParameterModel>();
      for(Map.Entry<String,JavaFormalParameterModel> e: origin_.getFormalParametersMap().entrySet())  {
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
    
    /*
    public  JavaMethodModel substituteTypeParameters(JavaTypeArgumentsSubstitution s) {
        //TODO: refine.
        return new JavaTypeArgumentBoundMethodModel(this,s);
    }*/
    
    public boolean canCheck() {
        return false; }
    
    public boolean check() {
        return true; }
    
    public boolean isSupportBlockModel()
    { return origin_.isSupportBlockModel(); }
    
    public JavaTypeArgumentBoundTopLevelBlockModel getTopLevelBlockModel() throws TermWareException, NotSupportedException {
        return new JavaTypeArgumentBoundTopLevelBlockModel(this,origin_.getTopLevelBlockModel(),substitution_);
    }
    
    
    public JavaMethodModel  getOrigin()
    { return origin_; }
    
    
    /**
     * TypeArgumentBoundMethodModel(originModel,substitution, context);
     */
    public Term getModelTerm() throws TermWareException, EntityNotFoundException
    {        
       Term mTerm=origin_.getModelTerm();
       Term sTerm = TermUtils.createJTerm(substitution_);
       Term jTerm = TermUtils.createJTerm(JavaPlaceContextFactory.createNewMethodContext(this));
       return TermUtils.createTerm("TypeArgumentBoundMethodModel",mTerm,sTerm,jTerm);
    }
    
    
    
    private JavaMethodModel origin_;
    private JavaTypeArgumentsSubstitution substitution_;
    
    private JavaTypeModel  boundResultType_=null;
    
}
