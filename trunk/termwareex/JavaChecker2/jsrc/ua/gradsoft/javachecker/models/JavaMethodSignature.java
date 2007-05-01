/*
 * JavaMethodSignature.java
 *
 * Created on April 23, 2007, 11:01 AM
 *
 */

package ua.gradsoft.javachecker.models;

import java.util.List;
import ua.gradsoft.termware.TermWareException;

/**
 *Interface, which determinate signature of Java method.
 * @author rssh
 */
public interface JavaMethodSignature
{

    /**
     *get name of method.
     */
    public  String  getName();
    
    /**
     *get type variables for this method
     */
    public List<JavaTypeVariableAbstractModel>  getTypeParameters() throws TermWareException;
    
    /**
     *get return type of method.
     */
    public  JavaTypeModel  getReturnType();
    
    /**
     * get list of argument types.
     */
    public  List<JavaTypeModel>  getArgumentTypes();
    
    /**
     *true, if method has variable number of arguments.
     */
    public  boolean  isVarArg();
}
