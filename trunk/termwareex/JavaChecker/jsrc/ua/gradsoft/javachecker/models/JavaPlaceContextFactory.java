/*
 * JavaPlaceContextFactory.java
 *
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.Iterator;
import java.util.List;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.Main;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Factory of context
 * @author Ruslan Shevchenko
 */
public class JavaPlaceContextFactory {
    
    public static JavaPlaceContext  createInPackageContext(String packageName) throws TermWareException
    {
        JavaPlaceContext retval = new JavaPlaceContext();
        JavaPackageModel packageModel = Main.getFacts().getPackagesStore().findOrAddPackage(packageName);
        retval.setPackageModel(packageModel);
        return retval;
    }
    
    public static JavaPlaceContext  createInTypeContext(String className, JavaPlaceContext previous) throws TermWareException
    {
      if (previous.getPackageModel()==null)  {
          throw new AssertException("package does not set, before class entering");
      }
      JavaPlaceContext retval = new JavaPlaceContext();
      retval.setPackageModel(previous.getPackageModel());
      if (previous.getTypeModel()==null) {
        JavaTypeModel where = null;  
        try {          
          where = JavaResolver.resolveTypeModelFromPackage(className,previous.getPackageModel().getName());
        }catch(EntityNotFoundException ex){
           throw new AssertException("type "+className+" is not found in package "+previous.getPackageModel().getName()); 
           //LOG.warn("Type "+className+" is not found in package "+previous.getPackageModel().getName()); 
           //where = JavaUnknownTypeModel.INSTANCE;
        }
        retval.setTypeModel(where);
      }else{
        JavaTypeModel prevWhere = previous.getTypeModel();
        JavaTypeModel where = null;
        try {
            where = prevWhere.findNestedTypeModel(className);
        }catch(EntityNotFoundException ex){
            throw new AssertException("nested type "+className+" is not found in "+prevWhere.getFullName());
        }catch(NotSupportedException ex){
            throw new AssertException("search for nested types in "+className+" is unsupported");            
        }
        retval.setTypeModel(where);
      }
      return retval;
    }
    
    /**
     * create context for entering method.
     *@param name -- name of method to enter.
     *@param formalParameterTypes -- list of types of formal parameter. types must be the same or extend list of types
     *in method definition. (i.e. types of method definition must be assignable from appropriative
     * types in params)
     *@param previous -- previous context.
     */
    /*
    public static JavaPlaceContext createInMethodContext(String name, List<JavaTypeModel> formalParameterTypes, JavaPlaceContext previous) throws TermWareException
    {
      if (previous.getPackageModel()==null)  {
          throw new AssertException("package does not set, before method entering");
      }
      if (previous.getTypeModel()==null) {
          throw new AssertException("class does not set, before method entering");      
      }
      if (previous.getMethodModel()!=null) {
          throw new AssertException("nested methods are not supported in Java yet.");      
      }      
      List<JavaMethodAbstractModel> methodModels=null;
      try {
         methodModels=previous.getTypeModel().findMethodModels(name);
      }catch(EntityNotFoundException ex){
          throw new AssertException("method "+name+" is not found in class "+previous.getTypeModel().getName());
      }catch(NotSupportedException ex){
          throw new AssertException("type "+previous.getTypeModel().getName()+" can't have methods");
      }
      JavaMethodAbstractModel foundMethodModel = null;
      for(JavaMethodAbstractModel m: methodModels) {         
         List<JavaFormalParameterModel> fpts = m.getFormalParametersList();
         if (fpts.size()==formalParameterTypes.size()) {
             Iterator<JavaTypeModel> fpti = fpts.iterator();
             Iterator<JavaTypeModel> apti = formalParameterTypes.iterator();
             boolean ftMatches = true;
             while(fpti.hasNext()) {
                 JavaTypeModel fpt=fpti.next();
                 JavaTypeModel apt=apti.next();
                 // TODO: implement autoboxinf
                 if (!JavaTypeModelHelper.subtypeOrSame(fpt,apt))  {
                     ftMatches=false;
                     break;
                 }
             }
             if (ftMatches) {
                 foundMethodModel=m;
                 break;
             }
         }
      }
      if (foundMethodModel==null) {
          throw new AssertException("method model for appropriative parameters is not found");
      }
      JavaPlaceContext retval=new JavaPlaceContext();
      retval.setPackageModel(previous.getPackageModel());
      retval.setTypeModel(previous.getTypeModel());
      retval.setMethodModel(foundMethodModel);  
      return retval;
    }
     */
    
    
    
}
