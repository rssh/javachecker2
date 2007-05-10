/*
 * JavaTermInitializerModel.java
 *
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.JUtils;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Model for Java Initializer, based on Term
 * @author Ruslan Shevchenko
 */
public class JavaTermInitializerModel extends JavaInitializerModel implements JavaTermTopLevelBlockOwnerModel
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
    
    public String getName()  { return "Initializer"; }
    
    public JavaTermTypeAbstractModel getTermTypeAbstractModel()
    { return owner_; }
    
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
    
    
    /**
     * get initializer modifiers.
     */
    public JavaTermModifiersModel getModifiers()
    { return modifiersModel_; }
    
    /**
     *initializers does not have annotations, so return empty Map
     */
    public Map<String,JavaAnnotationInstanceModel> getAnnotationsMap()
    { return Collections.emptyMap(); }
    
    /**
     *return true
     */
    public boolean isSupportBlockModel()
    { return true; }
    
    /**
     * get block model
     */
    public JavaTopLevelBlockModel getTopLevelBlockModel()
    { return blockModel_; }
    
    /**
     *InitializerModel(modifiersTerm,blockModelTerm,ctx);
     */
    public Term getModelTerm() throws TermWareException, EntityNotFoundException
    {
        Term mt=modifiersModel_.getModelTerm();
        Term bmt = blockModel_.getModelTerm();
        JavaPlaceContext ctx=JavaPlaceContextFactory.createNewInitializerContext(this);
        Term tctx = TermUtils.createJTerm(ctx);
        return TermUtils.createTerm("InitializerModel",mt,bmt,tctx);
    }
    
    private void build(Term t) throws TermWareException
    {
        Term modifiersTerm = t.getSubtermAt(0);
        Term insideModifiers = modifiersTerm.getSubtermAt(0);
        if (insideModifiers.isInt()) {
          modifiersModel_ = new JavaTermModifiersModel(modifiersTerm.getSubtermAt(0).getInt());
        }else if (insideModifiers.getName().equals("cons")) {
            Term l = insideModifiers;
            while(!l.isNil()) {
                Term ct = l.getSubtermAt(0);
                l=l.getSubtermAt(1);
                if (ct.isInt()) {
                    modifiersModel_ = new JavaTermModifiersModel(ct.getInt());                    
                }else{
                    // Annotation is not defined in initailizers
                    throw new AssertException("Annotations is not defined in initializers:"+TermHelper.termToString(ct));
                }
            }
        }else{
            throw new InvalidJavaTermException("List inside modifiers is required",modifiersTerm);
        }
        Term blockTerm = t.getSubtermAt(1);
        Term listStatements = blockTerm.getSubtermAt(0);
        blockModel_ = new JavaTermTopLevelBlockModel(this,listStatements);
    }
      
    
    private JavaTermTypeAbstractModel owner_;
    private JavaTermModifiersModel modifiersModel_;
    private JavaTermTopLevelBlockModel blockModel_;
    
}
