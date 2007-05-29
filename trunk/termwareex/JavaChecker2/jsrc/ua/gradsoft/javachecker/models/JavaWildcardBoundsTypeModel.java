/*
 * JavaWildcardBoundsTypeModel.java
 *
 * Copyright (c) 2004-2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.Collections;
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
 *Model for wildcard bounds.
 * @author Ruslan Shevchenko
 */
public class JavaWildcardBoundsTypeModel extends JavaTypeModel {
    
    /** Creates a new instance of JavaWildcardBoundsTypeModel
     *  with Object-like unbound capture.
     */
    public JavaWildcardBoundsTypeModel(JavaTypeModel where) throws TermWareException
    {
        super(where.getPackageModel());
        kind_=JavaWildcardBoundsKind.OBJECT;
        boundTypeModel_=JavaResolver.resolveJavaLangObject();
    }
    
    
    
    public JavaWildcardBoundsTypeModel(Term t, JavaTypeModel where, List<JavaTypeVariableAbstractModel> typeParameters) throws TermWareException {
        super(where.getPackageModel());
        if (t.getName().equals("WildcardBounds")) {
            String stype=t.getSubtermAt(0).getName();
            if (stype.equals("extends")) {
                kind_=JavaWildcardBoundsKind.EXTENDS;
            }else if (stype.equals("super")) {
                kind_=JavaWildcardBoundsKind.SUPER;
            }else{
                throw new AssertException("'extends' or 'super' is expected as the 0 subterm of "+TermHelper.termToString(t));
            }
            try {
              boundTypeModel_=JavaResolver.resolveTypeToModel(t.getSubtermAt(1),where,typeParameters);
            }catch(EntityNotFoundException ex){
                boundTypeModel_=JavaUnknownTypeModel.INSTANCE;
            }
        }else{
            throw new AssertException("WildcardBounds term is expected, instead "+TermHelper.termToString(t));
        }
    }
    
    public JavaWildcardBoundsTypeModel() throws TermWareException {
        super(Main.getFacts().getPackagesStore().findOrAddPackage("java.lang"));
        kind_=JavaWildcardBoundsKind.OBJECT;
        boundTypeModel_=null;
    }
    
    public JavaWildcardBoundsTypeModel(JavaWildcardBoundsKind kind, JavaTypeModel boundTypeModel) throws TermWareException {
        super(boundTypeModel.getPackageModel());
        kind_=kind;
        boundTypeModel_=boundTypeModel;
    }
  
    
    public String getName()
    {
        StringBuilder sb=new StringBuilder();
        sb.append("< ? ");
        switch(kind_) {
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
        switch(kind_) {
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
    
     public JavaTermModifiersModel getModifiersModel()
     { return JavaModelConstants.PUBLIC_MODIFIERS; }
    
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
  
      public boolean isTypeVariable()
      { return false; }
  
      public boolean isWildcardBounds()
      { return true; }

      public boolean isNull()
      { return false; }
            
      public boolean isUnknown()
      { return false; }
  
      public JavaWildcardBoundsKind getKind()
      {
          return kind_;
      }
      
      public boolean isLocal()
      { return false; }
      
      public boolean isAnonimous()
      { return false; }
      
      public JavaStatementModel getEnclosedStatement()
      { return null; }
      
      /**
       * get bound type model
       */
      public JavaTypeModel getBoundTypeModel()
      { return boundTypeModel_; }
      
      public JavaTypeModel  getSuperClass() throws TermWareException
      { if (kind_==JavaWildcardBoundsKind.EXTENDS) {
                return boundTypeModel_;
        }else{
            return JavaResolver.resolveJavaLangObject();
        }
      }
      
      public List<JavaTypeModel> getSuperInterfaces()
      { return Collections.emptyList(); }
      
      
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
  

    
  
    public boolean hasMethodModels()
    {
        return false;
    }
    
     public Map<String, List<JavaMethodModel>>   getMethodModels() throws NotSupportedException
     {
        throw new NotSupportedException(); 
     }
        
     public boolean hasMemberVariableModels()
     {
        return false; 
     }
  
      public Map<String, JavaMemberVariableModel> getMemberVariableModels() throws NotSupportedException
      {
        throw new NotSupportedException(); 
      }
      
      public List<JavaConstructorModel>  getConstructorModels()
      { return Collections.emptyList(); }

      public List<JavaInitializerModel> getInitializerModels()
      { return Collections.emptyList(); }
      
    public Map<String, JavaEnumConstantModel> getEnumConstantModels() throws NotSupportedException {
        throw new NotSupportedException();         
    }
      
    
     public boolean isNested()
     {
        return false;  
     }
  
      public boolean hasNestedTypeModels()
      {
        return false;
      }
  
      public Map<String,JavaTypeModel> getNestedTypeModels() throws NotSupportedException, TermWareException
      {
         throw new NotSupportedException();
      }
    
    
    public boolean hasTypeParameters() throws TermWareException
    {
        if (kind_==JavaWildcardBoundsKind.SUPER) {
            return false;
        }else{
            return boundTypeModel_.hasTypeParameters();
        }
    }
        
    
    public List<JavaTypeVariableAbstractModel>  getTypeParameters() throws TermWareException
    {
        if (kind_==JavaWildcardBoundsKind.SUPER) {
           return Collections.emptyList();
        }else{
            return boundTypeModel_.getTypeParameters();
        }
    }
    
    
    
    /**
     *@return empty map
     */
    public Map<String,JavaAnnotationInstanceModel> getAnnotationsMap()
    { return Collections.emptyMap(); }
    
    
    
    public boolean hasASTTerm()
    { return false; }
    
    public Term getASTTerm()
    { return null; }

    
    
    /**
     *WildCardBoundsModel(super|extends,TypeRef(boundTypeModel_))
     */
    public  Term getModelTerm() throws TermWareException
    {
        Term kt=null;
        if (kind_==JavaWildcardBoundsKind.SUPER) {
            kt=TermUtils.createAtom("super");
        }else{
            kt=TermUtils.createAtom("extends");
        }
        Term typeRef = TermUtils.createTerm("TypeRef",boundTypeModel_.getShortNameAsTerm(),TermUtils.createJTerm(boundTypeModel_));
        Term retval = TermUtils.createTerm("WildCardBoundsModel",kt,typeRef);
        return retval;
    }
    
    private JavaWildcardBoundsKind      kind_;
    private JavaTypeModel boundTypeModel_=null;
        

    
}
