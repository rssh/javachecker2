/*
 * JavaWildcardBoundsTypeModel.java
 *
 * Copyright (c) 2004-2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.List;
import java.util.Map;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.Main;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *
 * @author Ruslan Shevchenko
 */
public class JavaWildcardBoundsTypeModel extends JavaTypeModel {
    
    /** Creates a new instance of JavaWildcardBoundsTypeModel
     *  with Object-like unbound capture.
     */
    public JavaWildcardBoundsTypeModel(JavaTypeModel where) throws TermWareException
    {
        super(where.getPackageModel());
        type_=Type.OBJECT;
        boundTypeModel_=JavaResolver.resolveJavaLangObject();
    }
    
    
    
    public JavaWildcardBoundsTypeModel(Term t, JavaTypeModel where) throws TermWareException {
        super(where.getPackageModel());
        if (t.getName().equals("WildcardBounds")) {
            String stype=t.getSubtermAt(0).getName();
            if (stype.equals("extends")) {
                type_=Type.EXTENDS;
            }else if (stype.equals("super")) {
                type_=Type.SUPER;
            }else{
                throw new AssertException("'extends' or 'super' is expected as the 0 subterm of "+TermHelper.termToString(t));
            }
            try {
              boundTypeModel_=JavaResolver.resolveTypeToModel(t.getSubtermAt(1),where);
            }catch(EntityNotFoundException ex){
                boundTypeModel_=JavaUnknownTypeModel.INSTANCE;
            }
        }else{
            throw new AssertException("WildcardBounds term is expected, instead "+TermHelper.termToString(t));
        }
    }
    
    public JavaWildcardBoundsTypeModel() throws TermWareException {
        super(Main.getFacts().getPackagesStore().findOrAddPackage("java.lang"));
        type_=Type.OBJECT;
        boundTypeModel_=null;
    }
    
    public JavaWildcardBoundsTypeModel(Type type, JavaTypeModel boundTypeModel) throws TermWareException {
        super(boundTypeModel.getPackageModel());
        type_=type;
        boundTypeModel_=boundTypeModel;
    }
  
    
    public String getName()
    {
        StringBuilder sb=new StringBuilder();
        sb.append("< ? ");
        switch(type_) {
            case SUPER:
                sb.append("super ");
                sb.append(boundTypeModel_.getName());
                break;
            case EXTENDS:
                sb.append("extends ");
                sb.append(boundTypeModel_.getName());
                break;
            default:
                break;
        }
        sb.append(">");
        return sb.toString();
    }
    
    public Term getShortNameAsTerm() throws TermWareException
    {
        int arity=0;
        Term internalTerm=null;
        Term retval=null;
        switch(type_) {
            case EXTENDS:
                arity=2;
                internalTerm=TermUtils.createAtom("extends");
                break;
            case SUPER:
                arity=2;
                internalTerm=TermUtils.createAtom("super");
                break;
            default:
                arity=0;
                break;
        }
        if (arity==2) {
            Term boundTerm=boundTypeModel_.getShortNameAsTerm();
            retval=TermUtils.createTerm("WildcardBounds",internalTerm,boundTerm);
        }else{
            retval=TermUtils.createTerm("WildCardBounds");
        }
        return retval;
    }
    
      public  boolean isClass() 
      { return false; }
  
      public  boolean isInterface()
      { return false; }
  
      public  boolean isEnum()
      { return false; }
  
      public boolean isAnnotationType()
      { return false; }
    
      public boolean isPrimitiveType()
      { return false; }
  
      public boolean isArray()
      { return false; }
  
      public boolean isTypeArgument()
      { return false; }
  
      public boolean isWildcardBounds()
      { return true; }
  
      public boolean isUnknown()
      { return false; }
  
  /**
   *throws NotSupportedException
   */
  public JavaTypeModel  getEnclosedType() throws NotSupportedException, TermWareException
  { throw new NotSupportedException(); }
  
  /**
   *throws NotSupportedException
   */
  public JavaTypeModel  getReferencedType() throws NotSupportedException, TermWareException
{ throw new NotSupportedException(); }          
  

    
    public boolean canCheck()
    { return true; }
    
    /**
     *TODO: implement
     */
    public boolean check()
    {
      return true;  
    }
    
  
    public boolean hasMethodModels()
    {
        if (type_==Type.SUPER) {
            try {
               return JavaResolver.resolveJavaLangObject().hasMethodModels();
            }catch(TermWareException ex){
               return false;
            }
        }else{
            return boundTypeModel_.hasMethodModels();
        }
    }
    
     public Map<String,List<JavaMethodAbstractModel> >   getMethodModels() throws NotSupportedException
     {
        if (type_==Type.SUPER)  {           
            try {
              return JavaResolver.resolveJavaLangObject().getMethodModels();           
            }catch(TermWareException ex){
                throw new NotSupportedException();
            }
        }else{
            return boundTypeModel_.getMethodModels();
        }
     }
        
     public boolean hasMemberVariableModels()
     {
        if (type_==Type.SUPER) {
          try {  
            return JavaResolver.resolveJavaLangObject().hasMemberVariableModels();
          }catch(TermWareException ex){
              return false;
          }
        }else{
            return boundTypeModel_.hasMemberVariableModels();
        }
     }
  
      public Map<String,JavaMemberVariableAbstractModel> getMemberVariableModels() throws NotSupportedException
      {
        if (type_==Type.SUPER) {
          try{  
            return JavaResolver.resolveJavaLangObject().getMemberVariableModels();
          }catch(TermWareException ex){
            throw new NotSupportedException();
          }
        }else{
            return boundTypeModel_.getMemberVariableModels();
        }          
      }
  
    
     public boolean isNested()
     {
        return false;  
     }
  
      public boolean hasNestedTypeModels()
      {
        if (type_==Type.SUPER)  {
            return false;
        }else{
            return boundTypeModel_.hasNestedTypeModels();
        }
      }
  
      public Map<String,JavaTypeModel> getNestedTypeModels() throws NotSupportedException, TermWareException
      {
        if (type_==Type.SUPER)  {
            throw new NotSupportedException();
        }else{
            return boundTypeModel_.getNestedTypeModels();
        }          
      }

    
    
    public boolean hasTypeParameters() {
        if (type_==Type.SUPER) {
            return false;
        }else{
            return boundTypeModel_.hasTypeParameters();
        }
    }
    
    public List<JavaTypeVariableAbstractModel>  getTypeParameters() {
        if (type_==Type.SUPER) {
           return JavaModelConstants.TYPEVARIABLE_EMPTY_LIST; 
        }else{
            return boundTypeModel_.getTypeParameters();
        }
    }
    
    
  
    
    public enum Type {
        OBJECT, SUPER, EXTENDS
    };
    
    private Type          type_;
    private JavaTypeModel boundTypeModel_=null;
    
}
