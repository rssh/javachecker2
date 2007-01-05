/*
 * JavaTermTypeAbstractModel.java
 *
 * Copyright (c) 2004-2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import ua.gradsoft.javachecker.CheckerComment;
import ua.gradsoft.javachecker.InvalidCheckerCommentException;
import ua.gradsoft.javachecker.Main;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.termware.JTerm;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Abstract class for Term models of Java Types
 * @author Ruslan Shevchenko
 */
public abstract class JavaTermTypeAbstractModel extends JavaTypeModel
{
    
    /** Creates a new instance of JavaTermTypeAbstractModel */
    public JavaTermTypeAbstractModel(int modifiers, Term t, JavaPackageModel packageModel) throws TermWareException
    {
        super(packageModel);
        modifiers_=new JavaModifiersModel(modifiers);
        t_=t;
        t_=TermHelper.setAttribute(t_,"model",new JTerm(this));
        Term commentTerm=TermHelper.getAttribute(t, "comment");
        if (!commentTerm.isNil()) {
          try {
            checkerComment_=CheckerComment.extract(commentTerm.getString());
          }catch(InvalidCheckerCommentException ex){
              packageModel.getFacts().violationDiscovered("InvalidCheckerComments",ex.getMessage(),t);
          }
        }

        methodModels_=new TreeMap<String,List<JavaMethodAbstractModel> >();
        fieldModels_=new TreeMap<String,JavaMemberVariableAbstractModel>();
        nestedTypes_=new TreeMap<String,JavaTypeModel>();
        constructors_=new LinkedList<JavaTermConstructorModel>();
        typeVariables_=new LinkedList<JavaTypeVariableAbstractModel>();
    }
    
    public String getName()
    {
        return name_;
    }
    
    public Term getShortNameAsTerm() throws TermWareException
    {
       return TermUtils.createIdentifier(name_);
    }
    
   public boolean isPrimitiveType()
   { return false; }
  
   public boolean isUnknown()
   { return false; }

    
    public boolean canCheck()
    { return true; }
    
    public boolean checkDisabled()
    {
        boolean disableAll=false;
        if (checkerComment_!=null) {
            disableAll=checkerComment_.isDisable("All");
        }
        return disableAll; 
    }
    
    public boolean check() throws TermWareException
    {
      if (checkDisabled()) {
          return true;
      }
        
      boolean retval=true;
      
      if (Main.getBTPatternChecker().isEnabled()) {
          Main.getBTPatternChecker().check(t_.termClone());
      }

      
      for(List<JavaMethodAbstractModel> l : methodModels_.values()) {
          for(JavaMethodAbstractModel m: l) {
              retval &= m.check();
          }
      }  
      
      for(JavaMemberVariableAbstractModel f: fieldModels_.values()) {
          retval &= f.check();
      }
      
      for(JavaTypeVariableAbstractModel tv: typeVariables_) {
          retval &= tv.check();
      }
      
      return retval;
    }
    
    public boolean hasMethodModels()
    {
        return true;
    }
    
    public Map<String,List<JavaMethodAbstractModel> > getMethodModels() throws NotSupportedException
    {
        return methodModels_;
    }
    
    public boolean hasMemberVariableModels()
    { return true; }
    
    public Map<String,JavaMemberVariableAbstractModel> getMemberVariableModels()
    { return fieldModels_; }

    public boolean isNested()
    { return parentType_!=null; }
    
    public JavaTypeModel getEnclosedType()
    { return parentType_; }
    
    public List<JavaTypeVariableAbstractModel> getTypeParameters()
    { return typeVariables_; }
    
    public boolean hasNestedTypeModels()
    { return true; }
    
    public Map<String,JavaTypeModel>  getNestedTypeModels()
    {
      return nestedTypes_;  
    }
    
    void setParentType(JavaTypeModel parentType)
    {
        parentType_=parentType;
    }
    
    public JavaModifiersModel getModifiersModel()
    { return modifiers_; }
    
    public boolean isArray()
    { return false; }
    
    /**
     * throw NotSupportedException
     */
    public JavaTypeModel  getReferencedType() throws NotSupportedException
    {
        throw new NotSupportedException();
    }
    
    public boolean isTypeArgument()
    { return false; }
    
    public boolean isWildcardBounds()
    { return false; }
    
    
    /**
    *add initializer without modifiers
    */
    public void addInitializer(Term initializer) 
    {
      addInitializer(0,initializer);
    }
    
    /**
    *@TODO: implement
    */
    public void addInitializer(int modifiers, Term initializer) 
    {
      /* do nothing yet */
    }
    
    
    
    /**
    * add included class.
    */
    public void addClassOrInterfaceDeclaration(int modifiers, Term declaration) throws TermWareException
    {
      JavaTermClassOrInterfaceModel newModel=new JavaTermClassOrInterfaceModel(modifiers,declaration,this.getPackageModel());
      newModel.setParentType(this);
      nestedTypes_.put(newModel.getName(),newModel);      
    }

    /**
    * add  enum in scope
    */
    public void addEnumDeclaration(int modifiers, Term declaration)  throws TermWareException
    {
      JavaTermEnumModel newModel=new JavaTermEnumModel(modifiers,declaration,this.getPackageModel());
      newModel.setParentType(this);
      nestedTypes_.put(newModel.getName(),newModel);      
    }

   /**
    * add 
    */
    public void addConstructorDeclaration(int modifiers, Term declaration)  throws TermWareException
    {
      JavaTermConstructorModel m=new JavaTermConstructorModel(modifiers,declaration,this);
      constructors_.add(m);
    }
    
    
    public void addMethodDeclaration(int modifiers, Term methodDeclaration) throws TermWareException
    {                       
     JavaMethodAbstractModel methodModel=new JavaTermMethodModel(modifiers,methodDeclaration,this);
     String methodName=methodModel.getName();
     List<JavaMethodAbstractModel> l=methodModels_.get(methodName);                 
     if (l==null) {
         l=new LinkedList<JavaMethodAbstractModel>();
         methodModels_.put(methodName,l);
     }
     l.add(methodModel);
    }

    public void addFieldDeclaration(int modifiers, Term fieldDeclaration) throws TermWareException
    {
      if (!fieldDeclaration.getName().equals("FieldDeclaration")) {
          throw new AssertException("FieldDeclaration expected, have:"+TermHelper.termToString(fieldDeclaration));
      }        
      Term fieldType=fieldDeclaration.getSubtermAt(0);
      Term variableDeclarators=fieldDeclaration.getSubtermAt(1);      
      while(!variableDeclarators.isNil()) {          
          JavaTermMemberVariableModel fieldModel=new JavaTermMemberVariableModel(modifiers,fieldType,variableDeclarators.getSubtermAt(0),this);
          fieldModels_.put(fieldModel.getName(),fieldModel);          
          variableDeclarators=variableDeclarators.getSubtermAt(1);
      }
    }
    
    public void addTypeParameter(Term typeParameter) throws TermWareException
    {
      System.out.println("addTypeParameter "+TermHelper.termToString(typeParameter));  
      JavaTermTypeVariableModel model=new JavaTermTypeVariableModel(typeParameter,this);
      typeVariables_.add(model);
    }

    public Term getTerm()
    { return t_; }
    
    private JavaTypeModel parentType_=null;
    private TreeMap<String,List<JavaMethodAbstractModel> > methodModels_;
    private TreeMap<String,JavaMemberVariableAbstractModel> fieldModels_;
    private List<Term>                                      initializers_;
    private List<JavaTermConstructorModel>                  constructors_;
    private List<JavaTypeVariableAbstractModel>             typeVariables_;
    
    private TreeMap<String,JavaTypeModel>                   nestedTypes_;
   
    
    private JavaModifiersModel modifiers_;
        
    protected Term   t_;
    protected CheckerComment   checkerComment_;
    protected String name_=null;
    
}
