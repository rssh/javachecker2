/*
 * JavaCompilationUnitModel.java
 *
 * Copyright (c) 2004-2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import ua.gradsoft.javachecker.JUtils;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *
 * @author Ruslan Shevchenko
 */
public class JavaCompilationUnitModel extends JavaUnitModel
{
    
    /**
     *class import can consists not only from package name and imported
     * class, but from package name and some sequences of classes, which
     *are enclosing for imported.
     *So, we keep in class import full resolved type model, if resolving 
     *is possible. (if no - keep null and try to resolve later)    
     */
    public static class ClassImportSuffix
    {
        private String   fullClassName_;        
        private JavaTypeModel typeModel_;
        
        public ClassImportSuffix(String fullClassName)
        {
           fullClassName_=fullClassName;
           typeModel_=null;
        }
                
        public ClassImportSuffix(JavaTypeModel typeModel)
        {
           fullClassName_=typeModel.getFullName();
           typeModel_=typeModel;
        }
        
        public String getFullClassName()
        {
           return fullClassName_; 
        }
       
        public JavaTypeModel getTypeModel()
        {
           return typeModel_; 
        }
        
        void setTypeModel(JavaTypeModel typeModel)
        {
           typeModel_=typeModel; 
        }
        
    }

    public final static class PackageOrClassModelHolder
    {      
        private boolean initialized_;
        private JavaPackageModel packageModel_;
        private JavaTypeModel    typeModel_;
        
        public PackageOrClassModelHolder()
        {
            initialized_=false;
            packageModel_=null;
            typeModel_=null;
        }
        
        public boolean isInitialized()
        { return initialized_; }
        
        public JavaPackageModel getPackageModel()
        {
          return packageModel_;  
        }
        
        public void  setPackageModel(JavaPackageModel packageModel)
        {
            initialized_=true;
            packageModel_=packageModel;
        }
        
        public JavaTypeModel  getTypeModel()
        {
           return typeModel_; 
        }
        
        public void setTypeModel(JavaTypeModel typeModel)
        {
            initialized_=true;
            typeModel_=typeModel;
        }
        
    }
    
    public  JavaCompilationUnitModel(String fname)
    {
        fname_=fname;
        staticMethodImports_ = new TreeMap<String,String>();
        staticClassImports_ = new TreeSet<String>();
        classImports_ = new TreeMap<String,ClassImportSuffix>();
        packageOrClassImports_ = new TreeMap<String,PackageOrClassModelHolder>();       
        typeModels_ = new ArrayList<JavaTypeModel>();
    }
    
    public  JavaPackageModel getPackageModel()
    { return packageModel_; }
    
    public String getPackageName()
    { return packageModel_.getName(); }
    
    public  void  setPackageModel(JavaPackageModel packageModel)
    { packageModel_=packageModel; }
        
    public  void addImportDeclaration(Term t) throws TermWareException
    {
      if(t.getArity()==2){
          Term modifier=t.getSubtermAt(0);
          boolean isStatic=false;
          boolean isAll=false;
          while(!modifier.isNil()) {
              Term ct=modifier.getSubtermAt(0);
              if (ct.getName().equals("all")) {
                  isAll=true;
              }else if(ct.getName().equals("static")){
                  isStatic=true;
              }else{
                  throw new AssertException("only 'all' and 'static' are possible in import modifiers, we have "+TermHelper.termToString(ct));
              }
              modifier=modifier.getSubtermAt(1);
          }
          Term nameTerm=t.getSubtermAt(1);
          if (isStatic && isAll) {              
              String className=JUtils.getJavaNameAsString(nameTerm);
              staticClassImports_.add(className);
          }else if(isStatic && !isAll){
              int lastIndex=0;
              Term curr=nameTerm.getSubtermAt(0);
              String methodName=null;
              while(!curr.isNil()) {
                  if (curr.getSubtermAt(1).isNil()) {
                      methodName=curr.getSubtermAt(0).getSubtermAt(0).getString();
                  }else{
                      ++lastIndex;
                  }
                  curr=curr.getSubtermAt(1);
              }              
              String className=JUtils.getJavaNameAsString(nameTerm,lastIndex);
              staticMethodImports_.put(methodName,className);
          }else if(!isStatic && isAll){
              String packageOrClassName=JUtils.getJavaNameAsString(nameTerm);    
              PackageOrClassModelHolder holder = new PackageOrClassModelHolder();
              packageOrClassImports_.put(packageOrClassName,holder);
          }else{ //!isStatic && !isAll
              int lastIndex=0;
              Term curr=nameTerm.getSubtermAt(0);
              String className=null;
              while(!curr.isNil()) {
                  if (curr.getSubtermAt(1).isNil()) {
                      className=curr.getSubtermAt(0).getSubtermAt(0).getString();
                  }else{
                      ++lastIndex;                      
                  }
                  curr=curr.getSubtermAt(1);
              }                             
              String fullName=JUtils.getJavaNameAsString(nameTerm);
              // do not call resolver here to prevenet recursive loop.
              classImports_.put(className,new ClassImportSuffix(fullName));
          }
      }else{
          throw new AssertException("arity of import declaration must be 2, term:"+TermHelper.termToString(t));
      }  
    }

    /**
     *@return map of static imports.
     * key is method name, value is full import class names.
     *i.e. (PI, java.lang.Math)
     */
    public  Map<String,String>  getStaticMemberImports()
    { return staticMethodImports_; }
    
    /**
     *@return set of static class imports. values are full names of class.
     */
    public  Set<String>         getStaticClassImports()
    { return staticClassImports_; }
    
    
    /**
     *@return map of class imports.
     *key is name of class, value is package name.
     */
    public Map<String,ClassImportSuffix>   getClassImports()
    { return classImports_; }
    
    /**
     *@return set of package imports.
     */
    public Map<String,PackageOrClassModelHolder>  getPackageOrClassImports()
    { return packageOrClassImports_; }
    
    
    /**
     *return list of type models, defined in this compilation units.
     *(note, that nested types are included in enclosed type)
     */
    public List<JavaTypeModel>  getTypeModels()
    { return typeModels_; }
    
    
    public  void addTypeModel(JavaTypeModel typeModel)
    {
      typeModels_.add(typeModel);
    }
    
    /**
     * package, where this compilation unit is situated.
     */
    private JavaPackageModel packageModel_=null;
 
    /**
     * list of types, defined in this compilation unit.
     */
    private ArrayList<JavaTypeModel> typeModels_;
 
    
    /* name of file */
    private String fname_;
    
    /**
     * key is method name, value is full import class names.
     */
    private Map<String,String>  staticMethodImports_;
    
    /**
     * values are full import class names.
     */
    private Set<String>         staticClassImports_;
    
    /**
     * key is short class name, value is full name.
     */
    private Map<String,ClassImportSuffix>  classImports_;
    
    /**
     * key is package or class name.
     */
    private Map<String,PackageOrClassModelHolder>  packageOrClassImports_;
    
    
    
}
