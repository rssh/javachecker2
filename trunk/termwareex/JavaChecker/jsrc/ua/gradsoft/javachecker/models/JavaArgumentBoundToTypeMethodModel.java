/*
 * JavaArgumentBoundToTypeMethodModel.java
 *
 * Copyright (c) 2004-2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.ArrayList;
import java.util.List;
import ua.gradsoft.termware.TermWareException;

/**
 *Java method, whith argument bounds of subclass.
 * @author Ruslan Shevchenko
 */
public class JavaArgumentBoundToTypeMethodModel extends JavaMethodAbstractModel
{
    
    /** Creates a new instance of JavaArgumentBoundToTypeMethodModel */
    public JavaArgumentBoundToTypeMethodModel(JavaArgumentBoundTypeModel typeModel,JavaMethodAbstractModel origin) {
        super(typeModel);
        origin_=origin;
    }

    public JavaArgumentBoundTypeModel getArgumentBoundTypeModel()
    { return (JavaArgumentBoundTypeModel)getTypeModel(); }
    
    public String getName()
    { return origin_.getName(); }

    public List<JavaTypeVariableAbstractModel> getTypeParameters() throws TermWareException
    {
      return origin_.getTypeParameters();  
    }
    
    public JavaTypeModel getResultType() throws TermWareException
    {
      if (resultType_==null) {
          resultType_=origin_.getResultType();
          resultType_=getArgumentBoundTypeModel().substituteTypeParameters(resultType_);
      }  
      return resultType_;
    }
    
    public List<JavaTypeModel> getFormalParametersTypes() throws TermWareException
    {
      if (formalParametersTypes_==null) {
          formalParametersTypes_=new ArrayList<JavaTypeModel>();
          for(JavaTypeModel m : origin_.getFormalParametersTypes()) {
              JavaTypeModel tm=getArgumentBoundTypeModel().substituteTypeParameters(m);
              formalParametersTypes_.add(tm);
          }
      }  
      return formalParametersTypes_;
    }
    
    public boolean canCheck()
    { return false; }
    
    public boolean check()
    { return true; }
        
    
    private JavaMethodAbstractModel origin_;
    private JavaTypeModel    resultType_=null;
    private List<JavaTypeModel> formalParametersTypes_=null;
    
    
}
