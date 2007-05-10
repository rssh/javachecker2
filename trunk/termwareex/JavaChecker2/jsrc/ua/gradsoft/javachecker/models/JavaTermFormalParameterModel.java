/*
 * JavaTermFormalParameterModel.java
 *
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.lang.annotation.ElementType;
import java.util.Map;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Model for Java Formal Parameter
 * @author Ruslan Shevchenko
 */
public class JavaTermFormalParameterModel extends JavaFormalParameterModel
{
    
    public JavaTermFormalParameterModel(Term modifiers, String name, JavaTypeModel typeModel, JavaTopLevelBlockOwnerModel owner, int index) throws TermWareException
    {
      modifiers_=new JavaTermModifiersModel(modifiers,ElementType.PARAMETER,this);
      name_=name;
      typeModel_=typeModel;
      owner_=owner;
      index_=index;
    }

    
    public JavaTermModifiersModel getModifiersModel()
    { return modifiers_; }
    
    public String getName()
    { return name_; }
    
    public JavaVariableKind getKind()
    { return JavaVariableKind.FORMAL_PARAMETER; }
    
    public JavaTypeModel getTypeModel()
    { return typeModel_; }
        
    
    public JavaTopLevelBlockOwnerModel  getOwner()
    { return owner_; }
    
    
    public Map<String,JavaAnnotationInstanceModel>  getAnnotationsMap()
    { return modifiers_.getAnnotationsMap(); }
    
    /**
     *@return index of this formal parameters in call, started from 0
     */
    public int getIndex()
    { return index_; }
    
    private JavaTermModifiersModel  modifiers_;
    private String name_;
    private JavaTypeModel   typeModel_;
    private JavaTopLevelBlockOwnerModel owner_;
    private int index_;
    
}
