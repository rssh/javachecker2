/*
 * JavaTypeVariableAbstractModel.java
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import ua.gradsoft.javachecker.Main;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Abstract model for Java Type Variable
 * @author Ruslan Shevchenko
 */
public abstract class JavaTypeVariableAbstractModel extends JavaTypeModel {
    
    public JavaTypeVariableAbstractModel() {
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
     *throw NotSupportedException
     */    
    public JavaAnnotationInstanceModel getDefaultAnnotationInstanceModel() throws NotSupportedException 
    {
         throw new NotSupportedException();
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
     * throws NotSupportedException
     */
    public JavaTypeModel  getEnclosedType() throws NotSupportedException
    { throw new NotSupportedException(); }
    
    /**
     *@return referenced type. Works only if isArray()==true, otherwise
     *throws NotSupportedException
     */
    public JavaTypeModel  getReferencedType() throws NotSupportedException
    { throw new NotSupportedException(); }
    
    
    public JavaTypeModel getSuperClass()
    { return JavaNullTypeModel.INSTANCE; }
    
    public List<JavaTypeModel> getSuperInterfaces()
    { return Collections.emptyList(); }

        
    
    public boolean hasMethodModels() {
        return false; }
    
    /**
     *@return empty map;
     */
    public Map<String, List<JavaMethodModel>>   getMethodModels() throws NotSupportedException {
        return Collections.emptyMap();
    }
    
    
    public boolean hasMemberVariableModels() {
        return false; }
    
    public Map<String, JavaMemberVariableModel> getMemberVariableModels() throws NotSupportedException {
        throw new NotSupportedException(); }

    /**
     * throws NotSupportedException
     */
    public Map<String, JavaEnumConstantModel> getEnumConstantModels() throws NotSupportedException {
        throw new NotSupportedException();
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
    
    public Map<String,JavaTypeModel> getNestedTypeModels() throws NotSupportedException, TermWareException {
        throw new NotSupportedException();
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
