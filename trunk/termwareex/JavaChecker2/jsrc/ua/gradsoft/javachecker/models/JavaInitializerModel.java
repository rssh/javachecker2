/*
 * JavaIntializerModel.java
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.attributes.AttributedEntity;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Model for Java initializer
 * @author Ruslan Shevchenko
 */
public abstract class JavaInitializerModel implements JavaTopLevelBlockOwnerModel
{
    
    /**
     * get initializer modifiers.
     */
    public abstract JavaModifiersModel getModifiers();
    
    
    /**
     *get model term.
     */
    public abstract Term getModelTerm() throws TermWareException, EntityNotFoundException;
    
    
    public void printSignature(PrintWriter out)
    {
      out.print("{initializer}");  
    }

    public void printErasedSignature(PrintWriter out)
    {
      out.print("{initializer}");  
    }
    
    
    public Term  getAttribute(String name) throws TermWareException
    {
      return getTypeModel().getAttributes().getTopLevelBlockOwnerAttribute(this,name);
    }
    
    public void  setAttribute(String name, Term value) throws TermWareException
    {
       getTypeModel().getAttributes().setTopLevelBlockOwnerAttribute(this,name,value);
    }
    
    public String getName()  { return "__Initializer"; }
    
    
    
    /**
     *@return child attributes.
     */
    public AttributedEntity  getChildAttributes(String childName) throws TermWareException
    {
       return getTypeModel().getAttributes().getTopLevelBlockOwnerChildAttributes(this,childName);
    }
    
    /**
     * initializers does not have type parameters, so return empty list.
     */
    public List<JavaTypeVariableAbstractModel> getTypeParameters()
    {
      return Collections.emptyList(); 
    }

    /**
     * initializers does not have formal parameters, so return empty list.
     */
    public List<JavaFormalParameterModel> getFormalParametersList()
    {
      return Collections.emptyList();  
    }
    
    
    /**
     * initializers does not have formal parameters, so return empty map.
     */
    public Map<String, JavaFormalParameterModel> getFormalParametersMap()
    {
      return Collections.emptyMap();  
    }

    
}
