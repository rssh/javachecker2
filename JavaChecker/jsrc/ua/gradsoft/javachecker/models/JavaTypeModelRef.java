/*
 * JavaTypeModelRef.java
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.lang.ref.SoftReference;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Soft reference to JavaTypeModel, which hold
 *pointer to file (or entry) and soft reference to JavaTypeModel
 * @author Ruslan Shevchenko
 */
public class JavaTypeModelRef {
    
    /** Creates a new instance of JavaTypeModelRef */
    public JavaTypeModelRef(String name,AnalyzedUnitRef unitRef,JavaTypeModel typeModel) {
       name_=name;  
       unitRef_=unitRef;
       typeModelRef_=new SoftReference<JavaTypeModel>(typeModel);       
    }

    public String getName()
    { return name_; }
    
    public SoftReference<JavaTypeModel> getTypeModelRef()
    { return typeModelRef_; }
    
    public void  setTypeModelRef(JavaTypeModel typeModel)
    {
      typeModelRef_=new SoftReference<JavaTypeModel>(typeModel);  
    }
    
    public JavaTypeModel  getTypeModel() throws TermWareException
    {
       JavaTypeModel typeModel=typeModelRef_.get();
       if (typeModel!=null) {
           return typeModel;
       }
       JavaUnitModel um=unitRef_.getJavaUnitModel();
       for(JavaTypeModel tm: um.getTypeModels()) {
           if (tm.getName().equals(name_)) {
               typeModelRef_=new SoftReference<JavaTypeModel>(tm);
               return tm;
           }
       }
       //impossible, if all names are equals.
       throw new AssertException("Can't find typeModel in known compliation unit");       
    }
    
    private String           name_;
    private AnalyzedUnitRef  unitRef_;
    private SoftReference<JavaTypeModel>  typeModelRef_;
    
}
