/*
 * JavaTermConstructorModel.java
 *
 * Copyright (c) 2004-2007 GradSoft  Ukraine
 * All Rights Reserved
 */
package ua.gradsoft.javachecker.models;

import java.lang.annotation.ElementType;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.JUtils;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Constructor for term.
 * @author Ruslan Shevchenko
 */
public class JavaTermConstructorModel extends JavaConstructorModel implements JavaTermTopLevelBlockOwnerModel
{
    
    /** Creates a new instance of JavaTermConstructorModel */
    public JavaTermConstructorModel(Term modifiers,Term constructor,JavaTermTypeAbstractModel owner) throws TermWareException
    {
        modifiers_=new JavaTermModifiersModel(modifiers,ElementType.CONSTRUCTOR,this);
        t_=constructor;
        owner_=owner;        
        build(constructor);             
    }
    
    public String getName()
    {
      return getTypeModel().getName();  
    }
    
    public JavaTypeModel getTypeModel()
    { return owner_; }
    
    public JavaTermTypeAbstractModel  getTermTypeAbstractModel()
    { return owner_; }
    
    
   public List<JavaTypeVariableAbstractModel>  getTypeParameters() throws TermWareException
   {
    Term tpt = t_.getSubtermAt(TYPE_PARAMETERS_TERM_INDEX);
    return JavaTopLevelBlockOwnerModelHelper.buildTypeParameters(tpt,getTypeModel());
   }

    public List<JavaFormalParameterModel> getFormalParametersList() throws TermWareException, EntityNotFoundException
    {    
        Term formalParametersList = t_.getSubtermAt(FORMAL_PARAMETERS_TERM_INDEX).getSubtermAt(0);  
        return JavaTopLevelBlockOwnerModelHelper.buildFormalParametersList(formalParametersList,this);     
    }
   
   
    public Map<String, JavaFormalParameterModel> getFormalParametersMap() throws TermWareException, EntityNotFoundException
    {
        Term formalParametersList = t_.getSubtermAt(FORMAL_PARAMETERS_TERM_INDEX).getSubtermAt(0);  
        return JavaTopLevelBlockOwnerModelHelper.buildFormalParametersMap(formalParametersList,this);
    }
    
    public  boolean isSupportBlockModel()
    { return true; }
    
    public  JavaTopLevelBlockModel getTopLevelBlockModel()
    {
      return blockModel_;  
    }
   
    private void build(Term t) throws TermWareException
    {
      Term blockTerm=t.getSubtermAt(BLOCK_TERM_INDEX);      
      if (!t.getSubtermAt(EXPLICIT_CONSTRUCTOR_INVOCATION_TERM_INDEX).isNil()) {
          blockTerm=TermUtils.createTerm("cons",t.getSubtermAt(EXPLICIT_CONSTRUCTOR_INVOCATION_TERM_INDEX),blockTerm);
      }
      blockModel_=new JavaTermTopLevelBlockModel(this,blockTerm);      
    }

    
    public Map<String,JavaAnnotationInstanceModel>  getAnnotationsMap()
    { return modifiers_.getAnnotationsMap(); }
    
    /**
     *ConstructorModel(modifiers,TypeParameters,identifier,FormalParameters,trowsNameList,BlockModel,context)
     */
    public Term getModelTerm() throws TermWareException, EntityNotFoundException
    {
      Term modifiersModelTerm = modifiers_.getModelTerm();  
      Term typeParametersModelTerm = TermUtils.buildTypeParametersModelTerm(getTypeParameters(),t_.getSubtermAt(TYPE_PARAMETERS_TERM_INDEX));
      Term identifierTerm = t_.getSubtermAt(IDENTIFIER_TERM_INDEX);
      Term formalParametersTerm = t_.getSubtermAt(FORMAL_PARAMETERS_TERM_INDEX);
      Term throwsNameList = t_.getSubtermAt(THROWS_NAMELIST_TERM_INDEX);
      Term blockModelTerm = blockModel_.getModelTerm();
      JavaPlaceContext context = JavaPlaceContextFactory.createNewConstructorContext(this);
      Term tcontext = TermUtils.createJTerm(context);
      Term modelTerm=TermUtils.createTerm("ConstructorModel",
                                             modifiersModelTerm,
                                             typeParametersModelTerm,
                                             identifierTerm,
                                             formalParametersTerm,
                                             throwsNameList,
                                             blockModelTerm,
                                             tcontext);
      return modelTerm;
    }
    
    private JavaTermTypeAbstractModel      owner_;
    private JavaTermModifiersModel modifiers_;
    private Term               t_;
    private JavaTermTopLevelBlockModel blockModel_;
    
    public static final int TYPE_PARAMETERS_TERM_INDEX=0;
    public static final int IDENTIFIER_TERM_INDEX=1;
    public static final int FORMAL_PARAMETERS_TERM_INDEX=2;
    public static final int THROWS_NAMELIST_TERM_INDEX=3;
    public static final int EXPLICIT_CONSTRUCTOR_INVOCATION_TERM_INDEX=4;
    public static final int BLOCK_TERM_INDEX=5;
    
    
}
