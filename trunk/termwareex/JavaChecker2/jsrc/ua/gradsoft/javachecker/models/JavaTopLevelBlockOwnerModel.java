/*
 * JavaBlockOwnerModel.java
 *
 * (C) Grad-Soft Ltd, Kiev, Ukarine.
 *http://www.gradsoft.ua
 */

package ua.gradsoft.javachecker.models;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.javachecker.attributes.AttributedEntity;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Generic owner of block (method, constructor or initializer)
 * @author Ruslan Shevchenko
 */
public interface JavaTopLevelBlockOwnerModel extends AttributedEntity
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
    
    /**
     * get List of formal parameters
     */
    public  List<JavaFormalParameterModel> getFormalParametersList() throws TermWareException, EntityNotFoundException;
    
    public Map<String, JavaFormalParameterModel>  getFormalParametersMap() throws TermWareException, EntityNotFoundException;

    
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
     * return annotations, associated with model oe costructor
     */
    public  Map<String,JavaAnnotationInstanceModel>  getAnnotationsMap() throws TermWareException;
    
    /**
     * return model term.
     */
    public Term  getModelTerm() throws TermWareException, EntityNotFoundException;
    
    /**
     * print signature (i. e. string, which depends from type parameters, name and formal parameter types)
     *ont pwr.
     */
    public void printSignature(PrintWriter pwr);
    
   
    /**
     * print signature, where type parameters are erased.
     */
    public void printErasedSignature(PrintWriter pwr);
    
}
