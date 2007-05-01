/*
 * JavaTypeModelAttributes.java
 *
 * Created on April 22, 2007, 11:25 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ua.gradsoft.javachecker.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.JUtils;
import ua.gradsoft.javachecker.Main;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *
 * @author rssh
 */
public class JavaTypeModelAttributes {
    
    
    public JavaTypeModelAttributes(JavaTypeModel owner)
    { owner_=owner; }

    public JavaTypeModel  getTypeModel()
    { return owner_; }
    
  
    public Term  getTypeAttribute(String name) throws TermWareException
    {        
        if (!isLoaded()) {
            load();
        }
        Term retval = loadedTypeAttributes_.get(name);        
        return (retval!=null ? retval : TermUtils.createNil());
    }
       
    
    public Term findInheriedTypeAttribute(String name) throws TermWareException
    {      
        Term retval = getTypeAttribute(name);
        if (!retval.isNil()) {
            return retval;
        }
        if (owner_.isClass()) {            
            JavaTypeModel superOwner = null;
            try {
              superOwner=owner_.getSuperClass();
              retval = superOwner.getAttributes().findInheriedTypeAttribute(name);
            }catch(NotSupportedException ex){
                /* do nothing */;
            }catch(EntityNotFoundException ex){
                throw new AssertException("Unable to get super",ex);
            }
            if (!retval.isNil()) {
                loadedTypeAttributes_.put(name,retval);
                return retval;
            }
            try {
              List<JavaTypeModel> superInterfaces = owner_.getSuperInterfaces();
              for(JavaTypeModel si: superInterfaces) {
                retval = si.getAttributes().findInheriedTypeAttribute(name);
                if (!retval.isNil()) {
                    loadedTypeAttributes_.put(name,retval);
                    return retval;
                }
              }            
            }catch(NotSupportedException ex){
                ;
            }
        }else if (owner_.isInterface()) {
            try {
              List<JavaTypeModel> superInterfaces = owner_.getSuperInterfaces();
              for(JavaTypeModel si: superInterfaces) {
                retval = si.getAttributes().findInheriedTypeAttribute(name);
                if (!retval.isNil()) {
                    loadedTypeAttributes_.put(name,retval);
                    return retval;
                }
              }
            }catch(NotSupportedException ex){
                throw new AssertException("Impossible",ex);
            }
        }
        return retval;
    }
    
    
    public Term  getMethodAttribute(String name, JavaMethodSignature methodSignature)
    {
       return getMethodAttribute(name, generateMethodSignatureString(methodSignature));       
    }
       
    protected void finalize()
    {
     if (!Main.isInShutdown()) {
      synchronized(this) {  
          try {
            save();
          }catch(TermWareException ex){
              LOG.log(Level.WARNING,"exception during saving properties",ex);
          }
      }
     }
    }
        
    private Term getMethodAttribute(String name, String signature)
    {
      HashMap<String,Term> methodAttributes = loadedMethodsAttributes_.get(signature);
      if (methodAttributes==null) {
          return TermUtils.createNil();
      }
      Term retval = methodAttributes.get(name);
      if (retval==null) {
          retval=TermUtils.createNil();
      }
      return retval;
    }
    
    private boolean isLoaded()
    { return loadedTypeAttributes_==null; }
    
    private synchronized void load() throws TermWareException
    {
       if (!isLoaded()) {
           String dirName=JUtils.createDirectoryNameFromPackageName(Main.getTmpDir(),owner_.getPackageModel().getName());
           String fname = JUtils.createSourceFileNameFromClassName(owner_.getName(),".jcswp");
           String fullLoadName = dirName+File.separator+fname;
           File f = new File(fullLoadName);
           if (f.exists()) {
               ObjectInputStream oi = null;
               try {
                 oi=new ObjectInputStream(new FileInputStream(f));
               }catch(FileNotFoundException ex){
                   LOG.log(Level.WARNING,"File.exists() is true, but file not found",ex);
                   throw new AssertException("Impossible situation during opening file "+fullLoadName,ex);
               }catch(IOException ex){
                   throw new AssertException("Unable to create object stream from "+f.getAbsoluteFile(),ex);
               }
               Object o=null;
               try {
                 o = oi.readObject();
               }catch(IOException ex){
                   throw new AssertException("Error during reading object stream",ex);
               }catch(ClassNotFoundException ex){
                   throw new AssertException("Error during reading object stream",ex);
               }finally{
                   try {
                     oi.close();
                   }catch(IOException ex){
                     LOG.log(Level.WARNING,"exception during closing object stream",ex);    
                   }
               }
               if (o!=null) {
                 if (o instanceof JavaTypeModelAttributesData) {
                   JavaTypeModelAttributesData data = (JavaTypeModelAttributesData)o;
                   loadedTypeAttributes_=data.getTypeAttributes();    
                   loadedMethodsAttributes_=data.getMethodsAttributes();
                   loadedFieldsAttributes_=data.getFieldsAttributes();
                 }else{
                     throw new AssertException("Type of object in "+fullLoadName+" is not JavaTypeModelAttributesData");
                 }
               }
           }           
       } 
       if (loadedTypeAttributes_==null) {
           loadedTypeAttributes_=new HashMap<String,Term>();
           loadedMethodsAttributes_=new HashMap<String,HashMap<String,Term>>();
           loadedFieldsAttributes_=new HashMap<String,HashMap<String,Term>>();           
           loadConfigTypeAttributes();        
       }       
    }
    
    private synchronized void save() throws TermWareException
    {
        if (isLoaded()) {
           String dirName=JUtils.createDirectoryNameFromPackageName(Main.getTmpDir(),owner_.getPackageModel().getName());
           String fname = JUtils.createSourceFileNameFromClassName(owner_.getName(),".jcswp");
           String fullLoadName = dirName+File.separator+fname;
           File f = new File(fullLoadName);
           if (!f.exists()) {
               try {
                 f.createNewFile();
               }catch(IOException ex){
                 throw new AssertException("Can't create file "+f.getAbsolutePath(),ex);   
               }
           }
           
           ObjectOutputStream oo=null;
           try {
             oo=new ObjectOutputStream(new FileOutputStream(f));  
             JavaTypeModelAttributesData data = new JavaTypeModelAttributesData();
             data.setTypeAttributes(loadedTypeAttributes_);
             data.setMethodsAttributes(loadedMethodsAttributes_);
             data.setFieldsAttributes(loadedFieldsAttributes_);
             oo.writeObject(data);
           }catch(FileNotFoundException ex){
               throw new AssertException("Can't open file "+f.getAbsolutePath()+" for writing",ex);
           }catch(IOException ex){
               throw new AssertException("Can't output object to file "+f.getAbsolutePath(),ex);
           }finally{
             if (oo!=null) {
               try {  
                 oo.close();
               }catch(IOException ex){
                   LOG.log(Level.WARNING,"exception diring closing just-writed swp file",ex);
               }
             }
           }
        }
    }
    
    
    private String generateMethodSignatureString(JavaMethodSignature methodSignature)
    {
        JavaTypeModel returnType = methodSignature.getReturnType();
        throw new RuntimeException("Not implemented!");
    }

    private void loadConfigTypeAttributes()
    {
        throw new RuntimeException("Not implemented!");
        /*
        for(String basedirname: Main.getSourceMetainfoDirs()) {
            String dirname = JUtils.createDirectoryNameFromPackageName(basedirname,owner_.getPackageModel().getName());
            String fname = JUtils.createSourceFileNameFromClassName(owner_.getName(),".java");            
            String fullFileName = dirname+File.separator+fname;
            File f = new File(fullFileName);
            if (f.exists()) {
               Term t = JUtils.readSourceFile(f);
               Term ct = findTypeDeclaration(owner_.getName());
               
            }
        }
         */
    }
    
    
    private JavaTypeModel        owner_;
    private HashMap<String,Term> loadedTypeAttributes_=null;
    
    /**
     * HashMap<signature:string,HashMap<name:String,value:Term>>
     */
    private HashMap<String,HashMap<String,Term>> loadedMethodsAttributes_=null;
    
    
    /**
     *HashMap<varname:String,HashMap<name:String,value:Term>>
     */
    private HashMap<String,HashMap<String,Term>> loadedFieldsAttributes_=null;
    
    private final static Logger LOG = Logger.getLogger(JavaTypeModelAttributes.class.getName());
    
}
