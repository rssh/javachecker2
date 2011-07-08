/*
 * JavaTypeVariableAbstractModel.java
 *
 * Copyright (c) 2006, 2007 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.Main;
import ua.gradsoft.javachecker.models.expressions.JavaTermNullLiteralExpressionModel;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Abstract model for Java Type Variable
 * @author Ruslan Shevchenko
 */
public abstract class JavaTypeVariableAbstractModel extends JavaTypeModel {
    
    public JavaTypeVariableAbstractModel()  {
        super(Main.getFacts().getPackagesStore().findOrAddPackage("java.lang"));
    }
    
    public abstract String getName();
    
    public abstract List<JavaTypeModel> getBounds() throws TermWareException;
    
    public  Term  getShortNameAsTerm() throws TermWareException
    { return TermUtils.createIdentifier(getName()); }

    public  Term  getFullNameAsTerm() throws TermWareException
    { return TermUtils.createIdentifier(getName()); }
    
    
    /**
     * return false;
     **/
    public boolean isClass() {
        return false; }

    /**
     * return false;
     **/    
    public boolean isInterface() {
        return false; }
    
    /**
     * return false;
     **/    
    public boolean isEnum()
    { return false; }
    
    /**
     * return false;
     **/    
    public boolean isAnnotationType()
    { return false; }
    
    /**
     *@return null;
     */    
    public JavaAnnotationInstanceModel getDefaultAnnotationInstanceModel() 
    {
        return null;
    }

    public JavaTermExpressionModel getDefaultInitializerExpression() throws TermWareException
    {
      Term nlTerm = TermUtils.createTerm("NullLiteral");
      return new JavaTermNullLiteralExpressionModel(nlTerm,null,this);
    }

    
    public boolean isPrimitiveType()
    { return false; }
        
    public boolean isArray()
    { return false; }
        
    public boolean isTypeVariable()
    { return true; }
    
    public boolean isWildcardBounds()
    { return false; }
    
    public boolean isNull()
    { return false; }
    
    public boolean isUnknown()
    { return true; }
    
    /**
     *@return null;
     */
    public JavaTypeModel  getEnclosedType()
    { return null; }
    
    /**
     *@return null
     */
    public JavaTypeModel  getReferencedType() 
    { return null; }
    
    
    public JavaTypeModel getSuperClass()
    { return JavaNullTypeModel.INSTANCE; }
    
    public List<JavaTypeModel> getSuperInterfaces()
    { return Collections.emptyList(); }

        
    
    public boolean hasMethodModels() {
        return false; }
    
    /**
     *@return empty map;
     */
    public Map<String, List<JavaMethodModel>>   getMethodModels() {
        return Collections.emptyMap();
    }
    
    
    public boolean hasMemberVariableModels() {
        return false; }
    
    public Map<String, JavaMemberVariableModel> getMemberVariableModels() {
        return Collections.emptyMap();
    }

    /**
     *@return empty map
     */
    public Map<String, JavaEnumConstantModel> getEnumConstantModels() {
        return Collections.emptyMap();
    }
       
    
    
    public boolean isNested() {
        return false; }
    
    public boolean hasNestedTypeModels() {
        return false; }
    
    public boolean isLocal()
    { return false; }
    
    public boolean isAnonimous()
    { return false; }
    
    public JavaStatementModel getEnclosedStatement()
    { return null; }
    
    public Map<String,JavaTypeModel> getNestedTypeModels() throws TermWareException {
        return Collections.emptyMap();
    }
    
    
    /**
     *@return false
     */
    public boolean hasTypeParameters()
    { return false; }
    
    /**
     *@return empty list
     */
    public List<JavaTypeVariableAbstractModel>  getTypeParameters() {
        return Collections.<JavaTypeVariableAbstractModel>emptyList();
    }

    /**
     *@return empty map
     */
    public Map<String,JavaAnnotationInstanceModel> getAnnotationsMap()
    { return Collections.emptyMap(); }
    
    /**
     *@return empty list
     */
    public List<JavaConstructorModel>  getConstructorModels()
    { return Collections.emptyList(); }

    /**
     *@return empty list
     */
    public List<JavaInitializerModel>  getInitializerModels()
    { return Collections.emptyList(); }
    
    
    public void print(PrintWriter writer)
    {
      try {  
      writer.print(getName());
      if (!getBounds().isEmpty()) {
          writer.print(" extends ");
          boolean frs=true;
          for(JavaTypeModel bound: getBounds()) {
              if (!frs) {
                  writer.print("&");
              }else{
                  frs=false;
              }
              writer.print(bound.getFullName());
          }
      }
      }catch(TermWareException ex){
          writer.print("error");
      }
    }
   
    
    public String toString()
    {
      StringWriter sw = new StringWriter(); 
      PrintWriter swr = new PrintWriter(sw);
      this.print(swr);
      swr.close();
      return sw.toString();
    }
    
    
    /**
     * TypeVariableModel(name,bounds)
     *where bounds are lists of typeref
     */
    public  Term getModelTerm() throws TermWareException, EntityNotFoundException
    {
        Term idTerm = TermUtils.createIdentifier(getName());
        Term boundsList = TermUtils.createNil();
        for(JavaTypeModel tm: getBounds()) {
            Term typeRef = TermUtils.createTerm("TypeRef",tm.getShortNameAsTerm(),TermUtils.createJTerm(tm));
            boundsList = TermUtils.createTerm("cons",typeRef,boundsList);
        }
        boundsList = TermUtils.reverseListTerm(boundsList);
        return TermUtils.createTerm("TypeVariableModel",idTerm,boundsList);
    }
    
    
    /**
     *@return false
     */
    public boolean hasASTTerm()
    { return false; }
    
    /**
     *@return null
     */
    public Term  getASTTerm()
    { return null; }
    
}
