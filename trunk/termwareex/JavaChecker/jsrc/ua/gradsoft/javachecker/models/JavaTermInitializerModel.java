/*
 * JavaTermInitializerModel.java
 *
 * Created on п'€тниц€, 12, с≥чн€ 2007, 1:10
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Model for Java Initializer, based on Term
 * @author Ruslan Shevchenko
 */
public class JavaTermInitializerModel implements JavaInitializerModel, JavaTermTopLevelBlockOwnerModel
{
    
    /**
     *create intializer model
     *@param owner parent type.
     *@param modifiers  modifiers.
     *@param t block
     */ 
    public JavaTermInitializerModel(JavaTermTypeAbstractModel owner, Term t) throws TermWareException
    { owner_=owner;       
      build(t);
    }
    
    /**
     * get owner.
     */
    public JavaTypeModel  getTypeModel()
    { return owner_; }
    
    public JavaTermTypeAbstractModel getTermTypeAbstractModel()
    { return owner_; }
    
    /**
     * initializers does not have type parameters, so return empty list.
     */
    public List<JavaTypeVariableAbstractModel> getTypeParameters()
    {
      return JavaModelConstants.TYPEVARIABLE_EMPTY_LIST;  
    }

    /**
     * initializers does not have formal parameters, so return empty map.
     */
    public Map<String,JavaFormalParameterModel> getFormalParameters()
    {
      return new TreeMap<String,JavaFormalParameterModel>();  
    }
    
    
    /**
     * get initializer modifiers.
     */
    public JavaModifiersModel getModifiers()
    { return modifiersModel_; }
    
    
    public boolean isSupportBlockModel()
    { return true; }
    
    /**
     * get block model
     */
    public JavaTopLevelBlockModel getTopLevelBlockModel()
    { return blockModel_; }
    
    
    private void build(Term t) throws TermWareException
    {
        Term modifiersTerm = t.getSubtermAt(0);
        modifiersModel_ = new JavaModifiersModel(modifiersTerm.getInt());
        Term blockTerm = t.getSubtermAt(1);
        blockModel_ = new JavaTermTopLevelBlockModel(this,blockTerm);
    }
    
    private JavaTermTypeAbstractModel owner_;
    private JavaModifiersModel modifiersModel_;
    private JavaTermTopLevelBlockModel blockModel_;
    
}
