/*
 * JavaTypeArgumentBoundMethodModel.java
 * Copyright (c) 2004-2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.javachecker.util.Function;
import ua.gradsoft.javachecker.util.ImmutableMappedMap;
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
        return JavaTypeArgumentBoundTopLevelBlockOwnerModelHelper.getTypeParameters(this);
    }
    
    public JavaTypeModel  getResultType() throws TermWareException {
        if (boundResultType_==null){
          boundResultType_ = substitution_.substitute(origin_.getResultType());        
        }
        return boundResultType_;
    }
    
    public List<JavaFormalParameterModel>  getFormalParametersList() throws TermWareException, EntityNotFoundException
    {                
      List<JavaFormalParameterModel> ol = origin_.getFormalParametersList();  
      ArrayList<JavaFormalParameterModel> retval = new ArrayList<JavaFormalParameterModel>(ol.size());
      for(JavaFormalParameterModel ofp: ol) {
          JavaFormalParameterModel newModel = new JavaTypeSubstitutedFormalParameterModel(ofp,this);
          retval.add(newModel);
      }
      return retval;
    }
    
    public Map<String, JavaFormalParameterModel>  getFormalParametersMap() throws TermWareException, EntityNotFoundException
    {
        return JavaTypeArgumentBoundTopLevelBlockOwnerModelHelper.getFormalParametersMap(this);
    }
    
    public  List<JavaTypeModel> getFormalParametersTypes() throws TermWareException, EntityNotFoundException
    {
      return substitution_.substitute(origin_.getFormalParametersTypes()); 
    }
    
    public Map<String,JavaAnnotationInstanceModel>  getAnnotationsMap() throws TermWareException
    { return origin_.getAnnotationsMap(); }
    
    public boolean isSupportBlockModel()
    { return origin_.isSupportBlockModel(); }
    
    public JavaTypeArgumentBoundTopLevelBlockModel getTopLevelBlockModel() throws TermWareException, NotSupportedException {
        return new JavaTypeArgumentBoundTopLevelBlockModel(this,origin_.getTopLevelBlockModel(),substitution_);
    }
    
    public JavaTypeArgumentBoundTypeModel getTypeArgumentBoundTypeModel() throws TermWareException
    {
       if (boundResultType_==null) {
           boundResultType_=substitution_.substitute(origin_.getTypeModel());
       } 
        return (JavaTypeArgumentBoundTypeModel)getTypeModel();
    }

    public boolean hasDefaultValue()
    { return origin_.hasDefaultValue(); }
    
    public JavaExpressionModel  getDefaultValue() throws NotSupportedException, TermWareException
    { return origin_.getDefaultValue(); }
    
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
    
    
    public JavaTypeArgumentsSubstitution getSubstitution()
    { return substitution_; }
    
    private JavaMethodModel origin_;
    private JavaTypeArgumentsSubstitution substitution_;    
    
    private JavaTypeModel  boundResultType_=null;
    
}
