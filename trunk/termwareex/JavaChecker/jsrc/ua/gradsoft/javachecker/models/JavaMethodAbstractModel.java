/*
 * JavaMethodAbstractModel.java
 *
 * Created on п'€тниц€, 27, лютого 2004, 12:27
 */

package ua.gradsoft.javachecker.models;

import java.util.List;
import ua.gradsoft.javachecker.JavaFacts;
import ua.gradsoft.termware.TermWareException;

/**
 * Model for method
 * @author  Ruslan Shevchenko
 */
public abstract class JavaMethodAbstractModel {
    
    /**
     * Creates a new instance of JavaMethodAbstractModel
     */
    public JavaMethodAbstractModel(JavaTypeModel typeModel) 
    {
     typeModel_=typeModel;
    }
    
    public abstract String getName();        
    
    public abstract List<JavaTypeVariableAbstractModel>  getTypeParameters() throws TermWareException;
    
    public abstract JavaTypeModel  getResultType() throws TermWareException;
    
    public abstract List<JavaTypeModel>  getFormalParametersTypes() throws TermWareException;
    
    public abstract boolean canCheck();
    
    public abstract boolean check() throws TermWareException;   
    
    public JavaFacts getJavaFacts()
    { return typeModel_.getJavaFacts(); }
    
    public JavaTypeModel getTypeModel()
    { return typeModel_; }
    
    private JavaTypeModel typeModel_;
}
