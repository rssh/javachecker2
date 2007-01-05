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

    public  JavaCompilationUnitModel(String fname)
    {
        fname_=fname;
        staticMethodImports_ = new TreeMap<String,String>();
        staticClassImports_ = new TreeSet<String>();
        classImports_ = new TreeMap<String,String>();
        packageImports_ = new TreeSet<String>();       
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
      if (t.getArity()==1) {
          // this is normal class declaration.
          Term nameTerm=t.getSubtermAt(0);
          String lastName=nameTerm.getSubtermAt(nameTerm.getArity()-1).getSubtermAt(0).getName();
          String packageName=JUtils.getJavaNameAsString(nameTerm,nameTerm.getArity()-1);
          classImports_.put(lastName,packageName);
      }else if(t.getArity()==2){
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
              String methodName=nameTerm.getSubtermAt(nameTerm.getArity()-1).getSubtermAt(0).getName();
              String className=JUtils.getJavaNameAsString(nameTerm,nameTerm.getArity()-1);
              staticMethodImports_.put(methodName,className);
          }else if(!isStatic && isAll){
              String packageName=JUtils.getJavaNameAsString(nameTerm);    
              packageImports_.add(packageName);
          }else{ //!isStatic && !isAll
              String className=nameTerm.getSubtermAt(nameTerm.getArity()-1).getSubtermAt(0).getName();
              String packageName=JUtils.getJavaNameAsString(nameTerm,nameTerm.getArity()-1);
              classImports_.put(className,packageName);
          }
      }else{
          throw new AssertException("arity of import declaration must be 1 or 2, term:"+TermHelper.termToString(t));
      }  
    }

    /**
     *@return map of static imports.
     * key is method name, value is full import class names.
     *i.e. (PI, java.lang.Math)
     */
    public  Map<String,String>  getStaticMethodImports()
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
    public Map<String,String>   getClassImports()
    { return classImports_; }
    
    /**
     *@return set of package imports.
     */
    public Set<String>  getPackageImports()
    { return packageImports_; }
    
    
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
    private Map<String,String>  classImports_;
    
    /**
     * key is package name.
     */
    private Set<String>  packageImports_;
    
    
    
}
