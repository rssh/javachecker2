/*
 * JavaTypeModel.java
 *
 * Created on п'€тниц€, 27, лютого 2004, 11:56
 */

package ua.kiev.gradsoft.JavaChecker;

import java.util.*;

import ua.kiev.gradsoft.TermWare.*;
import ua.kiev.gradsoft.TermWare.exceptions.*;

/**
 *Model of Java Type.
 * @author  Ruslan Shevchenko
 */
public abstract class JavaTypeModel {
    
    JavaTypeModel(JavaPackageModel packageModel)
    {
      packageModel_=packageModel;  
    }
    
    public abstract String getTypeName();
    
    public abstract boolean hasMethodModels();
    
    /**
     * return Map<methodName,Vector<Models> >
     */
    public abstract Map    getMethodModels() throws NotSupportedException;
    
    public abstract Collection  findMethodModels(String name) throws EntityNotFoundException, NotSupportedException;
    
    public abstract boolean canCheck();
    public abstract boolean check()  throws TermWareException;
    
    
    public JavaFacts getFacts()
    { return packageModel_.getFacts(); }

    private JavaPackageModel packageModel_;
}
