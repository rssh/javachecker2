/*
 * JavaTermConstructorModel.java
 *
 * Copyright (c) 2004-2007 GradSoft  Ukraine
 * All Rights Reserved
 */
package ua.gradsoft.javachecker.models;

import java.util.List;
import java.util.Map;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;

/**
 *Constructor for term.
 * @author Ruslan Shevchenko
 */
public class JavaTermConstructorModel implements JavaConstructorModel, JavaTermTopLevelBlockOwnerModel
{
    
    /** Creates a new instance of JavaTermConstructorModel */
    public JavaTermConstructorModel(int modifiers,Term constructor,JavaTermTypeAbstractModel owner) throws TermWareException
    {
        modifiers_=new JavaModifiersModel(modifiers);
        t_=constructor;
        owner_=owner;
        build(constructor);
    }
    
    public JavaTypeModel getTypeModel()
    { return owner_; }
    
    public JavaTermTypeAbstractModel  getTermTypeAbstractModel()
    { return owner_; }
    
    public boolean canCheck()
    { return true; }
    
    public boolean check()
    { return true; }
    
   public List<JavaTypeVariableAbstractModel>  getTypeParameters() throws TermWareException
   {
    Term tpt = t_.getSubtermAt(TYPE_PARAMETERS_TERM_INDEX);
    return TermUtils.buildTypeParameters(tpt,getTypeModel());
   }
   
    public Map<String,JavaFormalParameterModel> getFormalParameters() throws TermWareException
    {
        Term formalParametersList = t_.getSubtermAt(FORMAL_PARAMETERS_TERM_INDEX).getSubtermAt(0);  
        return TermUtils.buildFormalParameters(formalParametersList,this);
    }
    
    public  boolean isSupportBlockModel()
    { return true; }
    
    public  JavaTopLevelBlockModel getTopLevelBlockModel()
    {
      return blockModel_;  
    }
   
    private void build(Term t) throws TermWareException
    {
      blockModel_=new JavaTermTopLevelBlockModel(this,t.getSubtermAt(BLOCK_TERM_INDEX));      
    }
    
    private JavaTermTypeAbstractModel      owner_;
    private JavaModifiersModel modifiers_;
    private Term               t_;
    private JavaTermTopLevelBlockModel blockModel_;
    
    public static final int TYPE_PARAMETERS_TERM_INDEX=0;
    public static final int FORMAL_PARAMETERS_TERM_INDEX=2;
    public static final int BLOCK_TERM_INDEX=5;
    
    
}
