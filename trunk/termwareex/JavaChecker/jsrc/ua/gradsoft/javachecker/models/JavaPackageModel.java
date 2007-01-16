/*
 * JavaPackageModel.java
 *
 */

package ua.gradsoft.javachecker.models;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.JUtils;
import ua.gradsoft.javachecker.JavaFacts;
import ua.gradsoft.javachecker.Main;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;


/**
 *Model for Java Package.
 * @author  Ruslan Shevchenko
 */
public class JavaPackageModel {
    
    
    /** Creates a new instance of JavaPackageModel */
    public JavaPackageModel(String name,JavaFacts owner) {
        name_=name;
        owner_=owner;
        typeModelRefs_=new HashMap<String,JavaTypeModelRef>();
    }
    
    /**
     *@return name of package.
     */
    public String getName()
    {
      return name_;
    }
    
    /**
     * Add compilation unit to package, if one not already currently in soft hash.
     *In parallel parse <code> compilationUnit </code> and fill <code> compilationUnitModel </code>
     */
    public void addCompilationUnit(Term compilationUnit, JavaCompilationUnitModel compilationUnitModel, AnalyzedUnitRef unitRef) throws TermWareException
    {        
       if (!compilationUnit.getName().equals("CompilationUnit")) {
           throw new AssertException("Invalid compilation unit:"+TermHelper.termToString(compilationUnit));
       }       
       // get package declaration
       int i=0;
       Term t = compilationUnit.getSubtermAt(i);
       if (t.getName().equals("PackageDeclaration")) {
           ++i;
           // TODO: check package declaration
       }       
       // get import specifications
       boolean inImport=true;
       while(inImport && i<compilationUnit.getArity()) {
           t=compilationUnit.getSubtermAt(i);
           if (t.getName().equals("ImportDeclaration")) {
               compilationUnitModel.addImportDeclaration(t);
               ++i;
           }else{
               inImport=false;
           }
       }
       // get types       
       while (i<compilationUnit.getArity() && !inImport) {
           JavaTypeModel javaTypeModel=null;          
           if (t.getName().equals("TypeDeclaration")) {
             if (t.getArity()>0) {  
               int modifiers=t.getSubtermAt(MODIFIERS_TERM_INDEX).getSubtermAt(0).getInt();
               Term typeType=t.getSubtermAt(1);
               if (typeType.getName().equals("ClassOrInterfaceDeclaration")) {
                   javaTypeModel=new JavaTermClassOrInterfaceModel(modifiers,typeType,this);                                      
               }else if(typeType.getName().equals("EnumDeclaration")) {
                   javaTypeModel=new JavaTermEnumModel(modifiers,typeType,this);
               }else if(typeType.getName().equals("AnnotationTypeDeclaration")) {
                   javaTypeModel=new JavaTermAnnotationTypeModel(modifiers, typeType,this);
               }else{
                   throw new AssertException("Type must be one of ClassOrInterfaceDeclaration,EnumDeclaration,AnnotationTypeDeclaration");
               }               
               JavaTypeModelRef ref = typeModelRefs_.get(javaTypeModel.getName());
               if (ref==null) {
                 typeModelRefs_.put(javaTypeModel.getName(),new JavaTypeModelRef(javaTypeModel.getName(),unitRef,javaTypeModel));
               }else{
                 if (ref.getTypeModelRef()==null) {
                     ref.setTypeModelRef(javaTypeModel);
                 }  
               }
               compilationUnitModel.addTypeModel(javaTypeModel);               
             }
           }else{
               throw new AssertException("TypeDeclaration expected");
           }
           ++i;
       }       
    }
    
    public void addClassUnit(JavaClassUnitModel cl,AnalyzedUnitRef unitRef)
    {       
       for(JavaTypeModel tm : cl.getTypeModels()) {
          typeModelRefs_.put(tm.getName(),new JavaTypeModelRef(tm.getName(),unitRef,tm));         
       }
    }
    
    JavaFacts getFacts()
    { return owner_; }
    
    public JavaTypeModel findTypeModel(String name) throws TermWareException, EntityNotFoundException
    {
      JavaTypeModel retval=null;  
      // 1. try to get from hash.
      JavaTypeModelRef ref = typeModelRefs_.get(name);
      if (ref!=null) {
          retval=ref.getTypeModel();
          return retval;
      }
      
      // 2. try to load from source directories
      boolean found=false;
      for(String inputDir: owner_.getPackagesStore().getSourceDirs()) {
          String directory = JUtils.createDirectoryNameFromPackageName(inputDir,this.getName());
          String resource = JUtils.createSourceFileNameFromClassName(name);
          String fname=directory+File.separator+resource;
          File f = new File(fname);
          if (f.exists()) {
              Term t=JUtils.readSourceFile(f);
              JavaCompilationUnitModel cu = new JavaCompilationUnitModel(fname);
              cu.setPackageModel(this);              
              AnalyzedUnitRef newRef = new AnalyzedUnitRef(AnalyzedUnitType.SOURCE,directory,resource,cu);              
              this.addCompilationUnit(t,cu,newRef);
              for(JavaTypeModel tm: newRef.getJavaUnitModel().getTypeModels() ) {
                  typeModelRefs_.put(tm.getName(),new JavaTypeModelRef(name,newRef,tm));
                  if (tm.getName().equals(name)) {
                      retval=tm;                                            
                  }
                  tm.setUnitModel(cu);
              }
              if(retval!=null){
                return retval;
              }
              // other (non-public ? classed).
              // near impossible.
              
          } 
      }     
      
      // 3. try to load from class loader.
      try {              
        Class<?> theClass = ClassLoader.getSystemClassLoader().loadClass(name_+"."+name);      
        JavaClassUnitModel cu =new JavaClassUnitModel(theClass);        
        AnalyzedUnitRef newRef = new AnalyzedUnitRef(AnalyzedUnitType.CLASS,null,theClass.getName(),cu);
        this.addClassUnit(cu,newRef);
        for(JavaTypeModel tm: newRef.getJavaUnitModel().getTypeModels()) {
            typeModelRefs_.put(tm.getName(),new JavaTypeModelRef(name,newRef,tm));
            if (tm.getName().equals(name)) {
                retval=tm;                
            }
        }
        if (retval!=null) {
            return retval;
        }        
        return retval;
      }catch(ClassNotFoundException ex){
          // do nothing. if not found, than let it be not found.
      }
      
      throw new EntityNotFoundException("typeModel",name,"");
    }
    
    private String name_;
    private HashMap<String,JavaTypeModelRef> typeModelRefs_;       
    
    private JavaFacts owner_;
        
    
    public static final int MODIFIERS_TERM_INDEX=0;
    public static final int DECLARATION_TERM_INDEX=1;
            
    
}
