/*
 * JavaBlockOwnerModel.java
 *
 * Created on п'€тниц€, 12, с≥чн€ 2007, 1:31
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ua.gradsoft.javachecker.models;

import java.util.List;
import java.util.Map;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Generic owner of block (method, constructor or initializer)
 * @author Ruslan Shevchenko
 */
public interface JavaTopLevelBlockOwnerModel 
{

    public JavaTypeModel  getTypeModel();
    
    /**
     *return name of method or 'Initializer' or 'Constructor'
     */
    public String getName();
    
    /*
     * get list of type parameters.
     */
    public List<JavaTypeVariableAbstractModel>  getTypeParameters() throws TermWareException;
    
    
    public Map<String,JavaFormalParameterModel>  getFormalParameters() throws TermWareException;

    
    /**
     * return true if storing of block model is supported, otherwise false.
     */
    public boolean         isSupportBlockModel();
    
    /**
     * return block model, if storing of block model is supported, 
     *otherwise throws NotSupportedException
     */
    public JavaTopLevelBlockModel  getTopLevelBlockModel() throws TermWareException, NotSupportedException;
    
    /**
     * return model term.
     */
    public Term  getModelTerm() throws TermWareException;
    
}
