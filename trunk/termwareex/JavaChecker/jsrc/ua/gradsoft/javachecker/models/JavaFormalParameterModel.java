/*
 * JavaFormalParameterModel.java
 *
 * Created on νεδ³λÿ, 14, ρ³χνÿ 2007, 17:46
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

/**
 *
 * @author Ruslan Shevchenko
 */
public class JavaFormalParameterModel implements JavaVariableModel
{
    
    public JavaFormalParameterModel(int modifiers, String name, JavaTypeModel typeModel, JavaTopLevelBlockOwnerModel owner, int index)
    {
      modifiers_=new JavaModifiersModel(modifiers);
      name_=name;
      typeModel_=typeModel;
      owner_=owner;
      index_=index;
    }
    
    public JavaModifiersModel getModifiers()
    { return modifiers_; }
    
    public String getName()
    { return name_; }
    
    public JavaVariableKind getKind()
    { return JavaVariableKind.FORMAL_PARAMETER; }
    
    public JavaTypeModel getTypeModel()
    { return typeModel_; }
        
    
    public JavaTopLevelBlockOwnerModel  getOwner()
    { return owner_; }
    
    /**
     *@return index of this formal parameters in call, started from 0
     */
    public int getIndex()
    { return index_; }
    
    private JavaModifiersModel  modifiers_;
    private String name_;
    private JavaTypeModel   typeModel_;
    private JavaTopLevelBlockOwnerModel owner_;
    private int index_;
    
}
