/*
 * JavaArgumentBoundToTypeMethodModel.java
 *
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
 *Java method, whith argument bounds of subclass.
 * @author Ruslan Shevchenko
 */
public class JavaClassArgumentBoundMethodModel extends JavaMethodModel implements JavaTypeArgumentBoundTopLevelBlockOwnerModel
{
    
    /** Creates a new instance of JavaArgumentBoundToTypeMethodModel */
    public JavaClassArgumentBoundMethodModel(JavaTypeArgumentBoundTypeModel typeModel,JavaMethodModel origin) {
        super(typeModel);
        origin_=origin;
    }

    public JavaTypeArgumentBoundTypeModel getArgumentBoundTypeModel()
    { return (JavaTypeArgumentBoundTypeModel)getTypeModel(); }
    
    public String getName()
    { return origin_.getName(); }
    
    public JavaModifiersModel getModifiers()
    { return origin_.getModifiers(); }

    public List<JavaTypeVariableAbstractModel> getTypeParameters() throws TermWareException
    {
      return origin_.getTypeParameters();  
    }
    
    public JavaTypeModel getResultType() throws TermWareException
    {
      if (resultType_==null) {
          resultType_=getArgumentBoundTypeModel().getSubstitution().substitute(origin_.getResultType());
          //resultType_=getArgumentBoundTypeModel().substituteTypeParameters(resultType_);          
      }  
      return resultType_;
    }
    
    public List<JavaFormalParameterModel>  getFormalParametersList() throws TermWareException, EntityNotFoundException
    {
      List<JavaFormalParameterModel> originList = origin_.getFormalParametersList();  
      List<JavaFormalParameterModel> retval = new ArrayList<JavaFormalParameterModel>(originList.size());
      for(JavaFormalParameterModel ofp: originList) {
          int modifiers = ofp.getModifiers().getIntValue();          
          //JavaTypeModel type = getArgumentBoundTypeModel().substituteTypeParameters(ofp.getTypeModel());
          JavaTypeModel type = getArgumentBoundTypeModel().getSubstitution().substitute(ofp.getTypeModel());
          retval.add(new JavaFormalParameterModel(modifiers,ofp.getName(),type,this,ofp.getIndex()));
      }
      return retval;
    }
    
    public Map<String,JavaFormalParameterModel> getFormalParametersMap() throws TermWareException, EntityNotFoundException
    {
       Map<String,JavaFormalParameterModel> retval=new TreeMap<String,JavaFormalParameterModel>();
       for(Map.Entry<String,JavaFormalParameterModel> e:origin_.getFormalParametersMap().entrySet()) {
           JavaFormalParameterModel old=e.getValue();
           int modifiers=old.getModifiers().getIntValue();
           //JavaTypeModel type=getArgumentBoundTypeModel().substituteTypeParameters(old.getTypeModel());
           JavaTypeModel type=getArgumentBoundTypeModel().getSubstitution().substitute(old.getTypeModel());
           retval.put(e.getKey(),new JavaFormalParameterModel(modifiers,e.getKey(),type,this,old.getIndex()));                   
       }
       return retval;
    }
    
    
    
    public List<JavaTypeModel> getFormalParametersTypes() throws TermWareException, EntityNotFoundException
    {
      List<JavaTypeModel> ofps = origin_.getFormalParametersTypes();         
      return getArgumentBoundTypeModel().getSubstitution().substitute(ofps);
    }
    
        
    public  boolean isSupportBlockModel()
    {
      return origin_.isSupportBlockModel();  
    }
    
    public JavaTopLevelBlockModel getTopLevelBlockModel() throws TermWareException, NotSupportedException
    {
      return new JavaTypeArgumentBoundTopLevelBlockModel(this,
                                                     origin_.getTopLevelBlockModel(),
                                                     getArgumentBoundTypeModel().getSubstitution()           
                                             );
    }
    
    /**
     * return ClassTypeArgumentBoundMethodModel(context)
     */
    public  Term getModelTerm() throws TermWareException
    {
        Term ctx=TermUtils.createJTerm(JavaPlaceContextFactory.createNewMethodContext(this));
        return TermUtils.createTerm("ClassTypeArgumentBoundMethodModel",ctx);
    }
    
    private JavaMethodModel origin_;
    private JavaTypeModel    resultType_=null;
    private List<JavaTypeModel> formalParametersTypes_=null;
    
    
}
