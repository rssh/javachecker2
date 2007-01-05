/*
 * AnalyzedUnitRef.java
 *
 * Created on субота, 14, жовтня 2006, 17:45
 *
 * UBS application
 *
 * Class: __MAME__
 *
 * Created on: субота, 14, жовтня 2006, 17:45
 *
 * Owner: Ruslan Shevchenko
 *
 * Description:
 *
 * History:
 *
 * Copyright (c) 2004-2005 Infopulse Ukraine
 * Copyright (c) 2004-2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.io.File;
import java.lang.ref.SoftReference;
import ua.gradsoft.javachecker.*;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *
 * @author Ruslan Shevchenko
 */
public class AnalyzedUnitRef {
    
    /**
     * Creates a new instance of AnalyzedUnitRef
     */
    public AnalyzedUnitRef(AnalyzedUnitType unitType,
                               String  directory, 
                               String resource,
                               JavaUnitModel unitModel)
    {
        unitType_=unitType;
        directory_=directory;
        resource_=resource;
        javaUnitModelRef_=new SoftReference<JavaUnitModel>(unitModel);
    }
    
    JavaUnitModel getJavaUnitModel() throws TermWareException
    {
      JavaUnitModel retval=javaUnitModelRef_.get();
      if (retval!=null) {
          return retval;
      }
      switch(unitType_) {
          case SOURCE:
          {
              String fname=directory_+"/"+resource_;
              File f = new File(fname);
              Term t = JUtils.readSourceFile(f);
              JavaCompilationUnitModel cu = new JavaCompilationUnitModel(fname);
              String packageName=JUtils.getCompilationUnitPackageName(t);
              PackagesStore packagesStore=Main.getFacts().getPackagesStore();
              JavaPackageModel pm=packagesStore.findOrAddPackage(packageName);
              cu.setPackageModel(pm);              
              pm.addCompilationUnit(t,cu,this);              
              retval=cu;
          }
          break;
          case CLASS:
          {              
              ClassLoader cl=ClassLoader.getSystemClassLoader();           
              Class<?> theClass = null;
              try {
                  theClass=cl.loadClass(resource_);
              }catch(ClassNotFoundException ex){
                  throw new AssertException("previously resolved class:"+resource_+" can't be reloaded");
              }
              String packageName=theClass.getPackage().getName();
              PackagesStore packagesStore=Main.getFacts().getPackagesStore();
              JavaPackageModel pm=packagesStore.findOrAddPackage(packageName);
              JavaClassUnitModel cu=new JavaClassUnitModel(theClass);
              pm.addClassUnit(cu,this);
              retval=cu;              
          }
          break;
      }
      javaUnitModelRef_ = new SoftReference<JavaUnitModel>(retval);
      return retval;
    }
    
    private AnalyzedUnitType unitType_;
    private String           directory_;
    private String           resource_;
    private SoftReference<JavaUnitModel> javaUnitModelRef_;
    
}
