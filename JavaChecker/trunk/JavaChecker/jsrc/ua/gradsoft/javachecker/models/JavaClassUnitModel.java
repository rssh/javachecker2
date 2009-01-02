/*
 * JavaClassUnitModel.java
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.LinkedList;
import java.util.List;

/**
 * Formal 'CompilationUnitModel' which represent one class,
 * loaded from ClassLoader
 */
public class JavaClassUnitModel extends JavaUnitModel
{
    
    /** Creates a new instance of JavaClassUnitModel */
    public JavaClassUnitModel(Class theClass) {
        typeModels_=new LinkedList<JavaTypeModel>();
        JavaClassTypeModel tm=new JavaClassTypeModel(theClass);
        typeModels_.add(tm);
        tm.setUnitModel(this);
        packageName_=theClass.getPackage().getName();      
    }
    
    public String getPackageName()
    {
      return packageName_;  
    }
    
    public List<JavaTypeModel> getTypeModels()
    { return typeModels_; }
    
    private LinkedList<JavaTypeModel> typeModels_;
    private String packageName_;
    
}
