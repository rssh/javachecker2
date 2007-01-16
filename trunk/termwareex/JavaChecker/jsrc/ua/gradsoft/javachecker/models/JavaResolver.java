/*
 * JavaResolver.java
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.Main;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Static class
 * @author Ruslan Shevchenko
 */
public class JavaResolver {
    
    public static JavaTypeModel resolveTypeToModel(Term t,JavaTypeModel where) throws EntityNotFoundException,TermWareException {
        return resolveTypeToModel(t,where,null);
    }
    
    public static JavaTypeModel resolveTypeToModel(Term t,JavaTypeModel where,List<JavaTypeVariableAbstractModel> typeVariables) throws EntityNotFoundException,TermWareException {
        return resolveTypeToModel(t,where,typeVariables,null);
    }
    
    public static JavaTypeModel resolveTypeToModel(Term t, JavaStatementModel where) throws EntityNotFoundException, TermWareException {
        JavaTypeModel ownerType=where.getTopLevelBlockModel().getOwnerModel().getTypeModel();
        JavaTopLevelBlockOwnerModel blockOwner=where.getTopLevelBlockModel().getOwnerModel();
        List<JavaTypeVariableAbstractModel> typeVariables=blockOwner.getTypeParameters();
        Iterable<JavaTypeModel> localTypes = new LocalTypesIterable(where);
        return resolveTypeToModel(t,ownerType,typeVariables,localTypes);
    }
    
    public static JavaTypeModel resolveTypeToModel(Term t,JavaTypeModel where,List<JavaTypeVariableAbstractModel> typeVariables,Iterable<JavaTypeModel> localTypes) throws EntityNotFoundException,TermWareException {
        if (t.isAtom()) {
            if (t.getName().equals("boolean")) {
                return JavaPrimitiveTypeModel.BOOLEAN;
            }else if (t.getName().equals("char")) {
                return JavaPrimitiveTypeModel.CHAR;
            }else if (t.getName().equals("byte")) {
                return JavaPrimitiveTypeModel.BYTE;
            }else if (t.getName().equals("short")) {
                return JavaPrimitiveTypeModel.SHORT;
            }else if (t.getName().equals("int")) {
                return JavaPrimitiveTypeModel.INT;
            }else if (t.getName().equals("long")) {
                return JavaPrimitiveTypeModel.LONG;
            }else if (t.getName().equals("float")) {
                return JavaPrimitiveTypeModel.FLOAT;
            }else if (t.getName().equals("double")) {
                return JavaPrimitiveTypeModel.DOUBLE;
            }else if (t.getName().equals("void")){
                return JavaPrimitiveTypeModel.VOID;
            }else{
                throw new AssertException("Atom type model must be primitive, instead:"+TermHelper.termToString(t));
            }
        }else if (t.isComplexTerm()) {
            if (t.getName().equals("ReferenceType")) {
                Term t1=t.getSubtermAt(1);
                JavaTypeModel tm=resolveTypeToModel(t1,where,typeVariables);
                int referenceLevel=t.getSubtermAt(0).getInt();
                while(referenceLevel>0) {
                    tm=new JavaArrayTypeModel(tm);
                    --referenceLevel;
                }
                return tm;
            }else if (t.getName().equals("Identifier")) {
                String name=t.getSubtermAt(0).getString();
                return resolveTypeModelByName(name,where,typeVariables,localTypes);
            }else if (t.getName().equals("ClassOrInterfaceType")) {
                //at first, try to find class in our package or imported classes
                Term head=t.getSubtermAt(0);
                Term ct=head.getSubtermAt(0); // must be identifier
                String name=ct.getSubtermAt(0).getName();
                JavaTypeModel frsTypeModel=null;
                boolean found=true;
                try {
                    frsTypeModel=resolveTypeModelByName(name,where,typeVariables,localTypes);
                }catch(EntityNotFoundException ex){
                    found=false;
                }
                if (found) {
                    if (head.getSubtermAt(1).isNil()) {
                        return frsTypeModel;
                    }else{
                        try {
                            return resolveRestOfClassOrInterfaceType(frsTypeModel,head.getSubtermAt(1),where,typeVariables,localTypes);
                        }catch(EntityNotFoundException ex){
                            found=false;
                        }
                    }
                }
                // still not found: try to search with package name
                try {
                    return resolveTypeModelWithFullPackage(t,where);
                }catch(EntityNotFoundException ex){
                    found=false;
                }
                // at least, all
            }else{
                // this is not ReferenceType and not PrimaryType and not ClassOrInterface body
                throw new AssertException("Unknown type term:"+TermHelper.termToString(t));
            }
        }else{
            throw new AssertException("Primitive non-atom type term:"+TermHelper.termToString(t));
        }
        throw new EntityNotFoundException("type",TermHelper.termToString(t),"");
    }
    
    
    private static JavaTypeModel  resolveRestOfClassOrInterfaceType(JavaTypeModel prevModel, Term t, JavaTypeModel where, List<JavaTypeVariableAbstractModel> typeVariables,Iterable<JavaTypeModel> localTypes) throws EntityNotFoundException, TermWareException {
        JavaTypeModel curModel=prevModel;
        while(!t.isNil()) {
            Term ct=t.getSubtermAt(0);
            if (ct.getName().equals("Idendifier")) {
                String name=t.getSubtermAt(0).getString();
                //TODO: think about typeArguments among classOrInterfaceType ?
                if (curModel.hasNestedTypeModels()) {
                    try {
                        curModel=curModel.findNestedTypeModel(name);
                    }catch(NotSupportedException ex){
                        throw new AssertException("impossible, hasNestedTypeMopdels but getNestedTypeModels throws NotSupported");
                    }
                }else{
                    throw new EntityNotFoundException("type",t.getSubtermAt(0).getName(),"");
                }
            }else if(ct.getName().equals("TypeArguments")) {
                curModel=new JavaArgumentBoundTypeModel(curModel,ct,where);
            }else{
                throw new AssertException("Only Identifiers or TypeArgumentrs are allowed in ClassOrInterfaceType sequence, we have "+TermHelper.termToString(ct));
            }
            t=t.getSubtermAt(1);
        }
        return curModel;
    }
    
    public static JavaTypeModel resolveTypeModelByName(String name,JavaTypeModel where,List<JavaTypeVariableAbstractModel> typeVariables,Iterable<JavaTypeModel> localTypes) throws EntityNotFoundException, TermWareException {
        //0. try to find among type variables.
        if (typeVariables!=null) {
            for(JavaTypeVariableAbstractModel tv: typeVariables) {
                if (tv.getName().equals(name)) {
                    return tv;
                }
            }
        }
        
        //1. may be model, where we resolve, have typeVariables
        if (where.hasTypeParameters()) {
            for(JavaTypeVariableAbstractModel tv:where.getTypeParameters()) {
                if (tv.getName().equals(name)) {
                    return tv;
                }
            }
        }
        
        //2. try to find among local types
        if (localTypes!=null) {
            for(JavaTypeModel tm: localTypes) {
                if (tm.getName().equals(name)) {
                    return tm;
                }
            }
        }
        
        //2. try to find subtype of current type.
        if (where.hasNestedTypeModels()) {
            try {
                return where.findNestedTypeModel(name);
            }catch(EntityNotFoundException ex){
                ; /* do nothing */
            }catch(NotSupportedException ex){
                ; /* do nothing */
            }
        }
        // if current class is nested, try to find next one.
        if (where.isNested()) {
            JavaTypeModel enclosed=where;
            while(enclosed.isNested()) {
                try {
                    return enclosed.getEnclosedType().findNestedTypeModel(name);
                }catch(EntityNotFoundException ex){
                    ; /* do nothing */
                }catch(NotSupportedException ex){
                    ; /* do nothing */
                }
                // or may be this is type parameter of enclosed ?
                if (enclosed.hasTypeParameters()) {
                    for(JavaTypeVariableAbstractModel tv: enclosed.getTypeParameters()) {
                        if (tv.getName().equals(name)) {
                            return tv;
                        }
                    }
                }
                
                try {
                    enclosed=enclosed.getEnclosedType();
                }catch(NotSupportedException ex){
                    // impossible, becouse encloused is nested.
                    throw new AssertException("getEnclosedType is not supported, when type is nested");
                }
            }
        }
        // now get current package model and try to find class in current package.
        JavaPackageModel pm=where.getPackageModel();
        
        try {
            JavaTypeModel retval=pm.findTypeModel(name);
            if (retval!=null) {
                return retval;
            }
        }catch(EntityNotFoundException ex){
            ; /* do nothing */
        }
        
        JavaUnitModel um=where.getUnitModel();
        if (um==null) {
            System.out.println("um is null for "+where.getFullName());
        }
        if (um instanceof JavaCompilationUnitModel) {
            JavaCompilationUnitModel cu=(JavaCompilationUnitModel)um;
            //at first, try to find in class imports.
            String packageName=cu.getClassImports().get(name);
            if (packageName!=null) {
                return resolveTypeModelFromPackage(name,packageName);
            }
            
            // try to get Type Model from any aviable packages.
            for(String cPackageName:cu.getPackageImports()){
                try {
                    return resolveTypeModelFromPackage(name,cPackageName);
                }catch(EntityNotFoundException ex){
                    /* do nothing */
                    ;
                }
            }
        }
        
        try {
            //if all fail, try to find in "java.lang"
            return resolveTypeModelFromPackage(name,"java.lang");
        }catch(EntityNotFoundException ex){
            /* do nothing */
            ;
        }
        
        
        //we still here - it means that class was not found in import declarations.
        throw new EntityNotFoundException(" type ",name,"");
    }
    
    public static JavaTypeModel resolveTypeModelWithFullPackage(Term t, JavaTypeModel where) throws EntityNotFoundException, TermWareException {
        if (!t.getName().equals("ClassOrInterfaceType")) {
            throw new AssertException("argument of resolveTypeModelWithFullPackageName must be ClassOrInterfaceType, we have:"+TermHelper.termToString(t));
        }
        Term head=t.getSubtermAt(0);
        Term prev=null;
        int fi=0;
        while(!head.isNil()) {
            Term ct=head.getSubtermAt(0);
            head=head.getSubtermAt(1);
            if (!ct.getName().equals("Identifier")) {
                
                break;
            }
            prev=ct;
            ++fi;
        }
        
        String classShortName=null;
        if (prev!=null) {
            classShortName=prev.getSubtermAt(0).getString();
        }else{
            throw new AssertException("No identifier inside ClassOrInterfceType");
        }
        
        StringBuffer sb=new StringBuffer();
        head=t.getSubtermAt(0);
        boolean frs=true;
        while(!head.isNil()) {
            Term ct=head.getSubtermAt(0);
            head=head.getSubtermAt(1);
            if (ct==prev) {
                break;
            }
            if (!frs) {
                sb.append('.');
            }else{
                frs=false;
            }
            sb.append(ct.getSubtermAt(0).getString());
        }
        String packageName=sb.toString();
        JavaTypeModel retval=resolveTypeModelFromPackage(classShortName,packageName);
        if (!head.isNil()) {
            return resolveRestOfClassOrInterfaceType(retval,head,where,null,null);
        }
        return retval;
    }
        
    
    public static JavaTypeModel resolveTypeModelFromPackage(String classShortName,String packageName) throws EntityNotFoundException, TermWareException {
        //System.out.println("resolveTypeModelFromPackage:"+classShortName+","+packageName);
        JavaPackageModel packageModel = resolvePackage(packageName);
        return packageModel.findTypeModel(classShortName);
    }
    
    public static JavaTypeModel  resolveTypeModelByFullClassName(String name) throws EntityNotFoundException
    {
        throw new RuntimeException("Not implemented");
    }
    
    public static JavaPackageModel resolvePackage(String packageName) {
        return Main.getFacts().getPackagesStore().findOrAddPackage(packageName);
    }
    
    /**
     * resolve member variable by name in scope of class definition (outside block)
     */
    public static JavaMemberVariableAbstractModel resolveMemberVariableByName(String name,JavaTypeModel where) throws TermWareException, EntityNotFoundException 
    {
      LinkedList<JavaTypeModel> toCheck = new LinkedList<JavaTypeModel>();  
      toCheck.addFirst(where);
      while(!toCheck.isEmpty()) {                
          JavaTypeModel checked = toCheck.removeLast();
          try {
              return checked.findMemberVariableModel(name);
          }catch(EntityNotFoundException ex){
              // ignore
              ;
          }catch(NotSupportedException ex){
              // ignore
              ;
          }
          try {
            JavaTypeModel checkedSuperClass = checked.getSuperClass();
            if (!checkedSuperClass.isNull()) {
              toCheck.addFirst(checkedSuperClass);
            }
          }catch(NotSupportedException ex){
              //do nothing.
              ;
          }
          try {
            List<JavaTypeModel> checkedSuperInterfaces = checked.getSuperInterfaces();
            for(JavaTypeModel si: checkedSuperInterfaces) {
              toCheck.addFirst(si);
            }
          }catch(NotSupportedException ex){
              //do nothing
              ;
          }
          if (checked.isNested()) {
              try {
                 toCheck.add(checked.getEnclosedType());
              }catch(NotSupportedException ex){
                  /*impossible ignore */
                  ;
              }
          }        
      }
      throw new EntityNotFoundException(" member variable ",name,where.getFullName());
    }
        
    
    /**
     *resolve any variable outside scope definition.
     *(this can be member variable, constant from static import,
     * variable of enclosed statement for local and anonimous classes)
     */
    public static JavaVariableModel resolveVariableByName(String name,JavaTypeModel where) throws TermWareException, EntityNotFoundException
    {
      // check member variable.  
      try {
          return resolveMemberVariableByName(name,where);
      }catch(EntityNotFoundException ex){
          ;
      }  

      // now, may be we have enclosed statement ?
      JavaTypeModel currWhere = where;
      while (currWhere.isNested()) {
         JavaStatementModel st = currWhere.getEnclosedStatement();
         if (st!=null) {
             try {
                 return resolveVariableByName(name,st);
             }catch(EntityNotFoundException ex){
                 // block which contains where does not define variable name.
                 ;
             }
         }
         try {
            currWhere=currWhere.getEnclosedType();
         }catch(NotSupportedException ex){
             // impossible, nested means exists enclosed type;
             break;
         }
      }
      
      
      // now try to get static imports.
     JavaUnitModel um=where.getUnitModel();
     if (um==null) {
         // strange
         System.out.println("um is null for "+where.getFullName());
     } else if (um instanceof JavaCompilationUnitModel) {
         JavaCompilationUnitModel cum=(JavaCompilationUnitModel)um;
         Set<String> staticClassImports = cum.getStaticClassImports();
         for (String s:staticClassImports) {
             JavaTypeModel st=null;
             try {
                st=resolveTypeModelByFullClassName(s); 
             }catch(EntityNotFoundException ex){
                 // class not found, i.e. this must not compiler.
                 // we can ignore this.
                 continue;
             }
             try {
                return resolveVariableByName(name,st);
             }catch(EntityNotFoundException ex){
                 // it's not our case, ignore
                 ;
             }
         }
         // if we still here, than static import does not work for us.
     }
      
     throw new EntityNotFoundException("variable",name,where.getFullName());
    }
    
    
    public static JavaVariableModel resolveVariableByName(String name, JavaStatementModel statement) throws TermWareException, EntityNotFoundException
    {
      boolean quit=false;  
      JavaStatementModel parentStatement=statement.getParentStatementModel();
      while(!quit) {
        List<JavaLocalVariableModel> lv = statement.getLocalVariables();
        for(JavaLocalVariableModel v:lv) {
            if (v.getName().equals(name)) {
                return v;
            }
        }
        if (parentStatement!=null) {          
          if (parentStatement.getKind()==JavaStatementKind.BLOCK) {              
              statement=statement.getPreviousStatementModel();
              if (statement==null) {
                  statement=parentStatement;
                  parentStatement=statement.getParentStatementModel();
              }
          }else{
              statement=parentStatement;
              parentStatement=statement.getPreviousStatementModel();
          }          
        }else{
            // parentStatement is null
            if (statement.getPreviousStatementModel()!=null) {
                // top-level block, where root statement is not block.               
                statement=statement.getPreviousStatementModel();
            }else{
                quit=true;
            }
        }
      }
      // This is not local variable, let's check formal parameters.
      Map<String,JavaFormalParameterModel> fpm = statement.getTopLevelBlockModel().getOwnerModel().getFormalParameters();
      JavaFormalParameterModel fp = fpm.get(name);
      if (fp!=null) {
          return fp;
      }
      // Now this can be resolved only outside our top-level block.
      return resolveVariableByName(name,statement.getTopLevelBlockModel().getOwnerModel().getTypeModel());
    }
    
    
    public static JavaTypeModel resolveJavaLangObject() throws TermWareException {
        try {
            return resolveTypeModelFromPackage("Object","java.lang");
        }catch(EntityNotFoundException ex){
            throw new AssertException("java.lang.Object must be resolved");
        }
    }
    
    public static JavaTypeModel resolveJavaLangCloneable() throws TermWareException {
        try {
            return resolveTypeModelFromPackage("Cloneable","java.lang");
        }catch(EntityNotFoundException ex){
            throw new AssertException("java.lang.Cloneable must be resolved");
        }
    }
    
    public static JavaTypeModel resolveJavaIoSerializable() throws TermWareException {
        try {
            return resolveTypeModelFromPackage("Serializable","java.io");
        }catch(EntityNotFoundException ex){
            throw new AssertException("java.io.Serializable must be resolved");
        }
    }
    
    public static JavaTypeModel resolveJavaLangAnnotation() throws TermWareException {
        try {
            return resolveTypeModelFromPackage("Annotation","java.lang");
        }catch(EntityNotFoundException ex){
            throw new AssertException("java.lang.Annotation must be resolved");
        }
    }
    
    
}
