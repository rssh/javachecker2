/*
 * JavaArgumentBoundToTypeMethodModel.java
 *
 * Copyright (c) 2004-2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.javachecker.util.ImmutableMappedList;
import ua.gradsoft.javachecker.util.Function;
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

    public JavaTypeArgumentBoundTypeModel getTypeArgumentBoundTypeModel()
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
          resultType_=getTypeArgumentBoundTypeModel().getSubstitution().substitute(origin_.getResultType());
          //resultType_=getTypeArgumentBoundTypeModel().substituteTypeParameters(resultType_);          
      }  
      return resultType_;
    }
    
    public List<JavaFormalParameterModel>  getFormalParametersList() throws TermWareException, EntityNotFoundException
    {
      List<JavaFormalParameterModel> originList = origin_.getFormalParametersList();  
      return new ImmutableMappedList<JavaFormalParameterModel,JavaFormalParameterModel>(originList,
                        new Function<JavaFormalParameterModel,JavaFormalParameterModel>(){
          public JavaFormalParameterModel function(JavaFormalParameterModel x)
          {
              return new JavaTypeSubstitutedFormalParameterModel(
                         x,JavaClassArgumentBoundMethodModel.this
                      );
          }          
      }
              );
    }
    
    public Map<String, JavaFormalParameterModel> getFormalParametersMap() throws TermWareException, EntityNotFoundException
    {
       Map<String,JavaFormalParameterModel> retval=new TreeMap<String,JavaFormalParameterModel>();
       for(Map.Entry<String, JavaFormalParameterModel> e:origin_.getFormalParametersMap().entrySet()) {
           JavaFormalParameterModel old=e.getValue();
           retval.put(e.getKey(),new JavaTypeSubstitutedFormalParameterModel(e.getValue(),JavaClassArgumentBoundMethodModel.this));
       }
       return retval;
    }
    
    
    
    public List<JavaTypeModel> getFormalParametersTypes() throws TermWareException, EntityNotFoundException
    {
     if (formalParametersTypes_==null) {   
        List<JavaTypeModel> ofps = origin_.getFormalParametersTypes();         
        formalParametersTypes_=getTypeArgumentBoundTypeModel().getSubstitution().substitute(ofps);
     }
     return formalParametersTypes_;
    }
    
    
    public Map<String,JavaAnnotationInstanceModel> getAnnotationsMap() throws TermWareException
    {
     return origin_.getAnnotationsMap();
    }
        
    public  boolean isSupportBlockModel()
    {
      return origin_.isSupportBlockModel();  
    }
    
    public JavaTopLevelBlockModel getTopLevelBlockModel() throws TermWareException, NotSupportedException
    {
      return new JavaTypeArgumentBoundTopLevelBlockModel(this,
                                                     origin_.getTopLevelBlockModel(),
                                                     getTypeArgumentBoundTypeModel().getSubstitution()           
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

    public  JavaTypeArgumentsSubstitution getSubstitution() throws TermWareException
    { return getTypeArgumentBoundTypeModel().getSubstitution(); }
    
    public JavaTopLevelBlockOwnerModel getOrigin()
    { return origin_; }
    
    private JavaMethodModel origin_;
    private JavaTypeModel    resultType_=null;
    private List<JavaTypeModel> formalParametersTypes_=null;
    
    
}
