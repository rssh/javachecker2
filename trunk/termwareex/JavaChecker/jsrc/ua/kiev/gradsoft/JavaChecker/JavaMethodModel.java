/*
 * JavaMethodModel.java
 *
 * Created on п'€тниц€, 27, лютого 2004, 12:27
 */

package ua.kiev.gradsoft.JavaChecker;

import ua.kiev.gradsoft.TermWare.*;
import ua.kiev.gradsoft.TermWare.exceptions.*;

/**
 *
 * @author  Ruslan Shevchenko
 */
public abstract class JavaMethodModel {
    
    /** Creates a new instance of JavaMethodModel */
    public JavaMethodModel(JavaTypeModel typeModel) 
    {
     typeModel_=typeModel;
    }
    
    public abstract String getMethodName();
    
    public abstract boolean isSynchronized()  throws TermWareException;
    
    public abstract boolean check() throws TermWareException;
    
    
    public JavaFacts getFacts()
    { return typeModel_.getFacts(); }
    
    public JavaTypeModel getTypeModel()
    { return typeModel_; }
    
    private JavaTypeModel typeModel_;
}
