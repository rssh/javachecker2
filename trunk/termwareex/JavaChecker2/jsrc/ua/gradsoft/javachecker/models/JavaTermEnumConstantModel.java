/*
 * JavaTermEnumConstantModel.java
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.Collections;
import java.util.Map;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Model of enum constant
 * @author Ruslan Shevchenko
 */
public class JavaTermEnumConstantModel extends JavaEnumConstantModel
{
    
    JavaTermEnumConstantModel(Term enumConstantTerm, JavaTermEnumModel owner) throws TermWareException
    {      
      owner_=owner;
      identifierTerm_ = enumConstantTerm.getSubtermAt(IDENTIFIER_TERM_INDEX);
      name_ = identifierTerm_.getSubtermAt(0).getString();
      if (enumConstantTerm.getArity()>=ARGUMENTS_TERM_INDEX+1) {
          argumentsTerm_ = enumConstantTerm.getSubtermAt(ARGUMENTS_TERM_INDEX);
      }else{
          argumentsTerm_ = TermUtils.createNil();
      }
      //System.err.println("enum constant "+name_+", arity="+enumConstantTerm.getArity());  
      if (enumConstantTerm.getArity()>=CLASSORINTERFACE_BODY_TERM_INDEX+1) {
          subtype_=new JavaTermEnumAnonimousTypeModel(name_,enumConstantTerm.getSubtermAt(CLASSORINTERFACE_BODY_TERM_INDEX),owner); 
      }
    }
    
    public String getName()
    {
      return name_;  
    }
    
    
    public JavaTypeModel getTypeModel()
    {
      return (subtype_==null) ? owner_ : subtype_;  
    }
    
    public JavaTypeModel getOwner()
    { return owner_; }
        
    
    public Map<String,JavaAnnotationInstanceModel> getAnnotationsMap()
    { return Collections.emptyMap(); }
    
    /**
     * EnumConstantModel(identifierTerm,arguments,subtype)
     */
    public Term getModelTerm() throws TermWareException, EntityNotFoundException
    {
       Term subtypeModelTerm = (subtype_==null) ? TermUtils.createNil() : subtype_.getModelTerm();
       return TermUtils.createTerm("EnumConstantModel",identifierTerm_,argumentsTerm_,subtypeModelTerm);
    }
        
    public Term getIdentifierTerm()
    { return identifierTerm_; }
    
    private String               name_;
    private Term                 identifierTerm_;
    private Term                 argumentsTerm_;
    private JavaTermEnumModel    owner_;
    private JavaTermEnumAnonimousTypeModel subtype_=null;
    
    public static final int IDENTIFIER_TERM_INDEX=0;
    public static final int ARGUMENTS_TERM_INDEX=1;
    public static final int CLASSORINTERFACE_BODY_TERM_INDEX=2;
}
