/*
 * JavaMethodModel.java
 *
 * Created on п'€тниц€, 27, лютого 2004, 12:27
 */

package ua.gradsoft.javachecker.models;

import java.util.List;
import java.util.Map;
import ua.gradsoft.javachecker.JavaFacts;
import ua.gradsoft.termware.TermWareException;

/**
 * Model for method
 * @author  Ruslan Shevchenko
 */
public abstract class JavaMethodModel implements JavaTopLevelBlockOwnerModel 
{
    
    /**
     * Creates a new instance of JavaMethodModel
     */
    public JavaMethodModel(JavaTypeModel typeModel) 
    {
     typeModel_=typeModel;
    }
    
    public abstract String getName();    
    
    public abstract JavaModifiersModel getModifiers();
    
    public abstract List<JavaTypeVariableAbstractModel>  getTypeParameters() throws TermWareException;
 
    public abstract JavaTypeModel  getResultType() throws TermWareException;
    
    public abstract List<JavaTypeModel> getFormalParametersTypes() throws TermWareException;
    
    public abstract Map<String,JavaFormalParameterModel>  getFormalParameters() throws TermWareException;
        
    
    public abstract boolean canCheck();
    
    public abstract boolean check() throws TermWareException;   
    
    public JavaFacts getJavaFacts()
    { return typeModel_.getJavaFacts(); }
    
    public JavaTypeModel getTypeModel()
    { return typeModel_; }
    
    private JavaTypeModel typeModel_;
}
