/*
 * JavaResolver.java
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.List;
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
    
    public static JavaTypeModel resolveTypeToModel(Term t,JavaTypeModel where) throws EntityNotFoundException,TermWareException
    {
      return resolveTypeToModel(t,where,null);  
    }
    
    public static JavaTypeModel resolveTypeToModel(Term t,JavaTypeModel where,List<JavaTypeVariableAbstractModel> typeVariables) throws EntityNotFoundException,TermWareException
    {
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
                Term t1=t.getSubtermAt(0);
                JavaTypeModel tm=resolveTypeToModel(t1,where,typeVariables);
                int referenceLevel=t1.getSubtermAt(1).getInt();
                while(referenceLevel>0) {
                    tm=new JavaArrayTypeModel(tm);
                    --referenceLevel;
                }
                return tm;
            }else if (t.getName().equals("Identifier")) {
                String name=t.getSubtermAt(0).getName();
                return resolveTypeModelByName(name,where,typeVariables);
            }else if (t.getName().equals("ClassOrInterfaceType")) {
                //at first, try to find class in our package or imported classes
                Term ct=t.getSubtermAt(0); // must be identifier
                String name=ct.getSubtermAt(0).getName();
                JavaTypeModel frsTypeModel=null;
                boolean found=true;
                try {
                    frsTypeModel=resolveTypeModelByName(name,where,typeVariables);
                }catch(EntityNotFoundException ex){
                    found=false;
                }
                if (found) {
                    if (t.getArity()==1) {
                        return frsTypeModel;
                    }else{
                        try {
                            return resolveRestOfClassOrInterfaceType(frsTypeModel,t,1,where,typeVariables);
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
    
    
    private static JavaTypeModel  resolveRestOfClassOrInterfaceType(JavaTypeModel prevModel, Term t, int startIndex, JavaTypeModel where, List<JavaTypeVariableAbstractModel> typeVariables) throws EntityNotFoundException, TermWareException {
        JavaTypeModel curModel=prevModel;
        for(int i=startIndex; i<t.getArity(); ++i) {
            Term ct=t.getSubtermAt(i);
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
        }
        return curModel;
    }
    
    public static JavaTypeModel resolveTypeModelByName(String name,JavaTypeModel where,List<JavaTypeVariableAbstractModel> typeVariables) throws EntityNotFoundException, TermWareException
    {
        //0. try to find among type variables.
        if (typeVariables!=null) {
          for(JavaTypeVariableAbstractModel tv: typeVariables) {
            if (tv.getName().equals(name)) {
                return tv;
            }
          }
        }
        
        //1. try to find subtype of current type.
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
            }
        }
        // now get current package model and try to find class in current package.
        JavaPackageModel pm=where.getPackageModel();
        
        
        JavaTypeModel retval=pm.findTypeModel(name);
        if (retval!=null) {
            return retval;
        }
        
        JavaUnitModel um=where.getUnitModel();
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
        
        
        //we still here - it means that class was not found in import declarations.
        throw new EntityNotFoundException(" type ",name,"");
    }
    
    public static JavaTypeModel resolveTypeModelWithFullPackage(Term t, JavaTypeModel where) throws EntityNotFoundException, TermWareException
    {
        if (!t.getName().equals("ClassOrInterfaceType")) {
            throw new AssertException("argument of resolveTypeModelWithFullPackageName must be ClassOrInterfaceType, we have:"+TermHelper.termToString(t));
        }
        int fi=t.getArity();
        for(int i=0; i<t.getArity(); ++i) {
            if (!t.getSubtermAt(i).getName().equals("Identifier")) {
                fi=i;
                break;
            }
        }
        if (fi<=1) {
            throw new EntityNotFoundException("type",TermHelper.termToString(t),"");
        }
        String classShortName=t.getSubtermAt(fi-1).getSubtermAt(0).getString();
        StringBuffer sb=new StringBuffer();
        for(int i=0; i<fi-1; ++i) {
            Term idt=t.getSubtermAt(i);
            if (i!=0) {
                sb.append('.');
            }
            sb.append(idt.getSubtermAt(0).getString());
        }
        String packageName=sb.toString();
        JavaTypeModel retval=resolveTypeModelFromPackage(classShortName,packageName);
        if (fi!=t.getArity()) {
            return resolveRestOfClassOrInterfaceType(retval,t,fi,where,null);
        }
        return retval;
    }
    
    public static JavaTypeModel resolveTypeModelFromPackage(String classShortName,String packageName) throws EntityNotFoundException, TermWareException {
        JavaPackageModel packageModel = resolvePackage(packageName);
        return packageModel.findTypeModel(classShortName);
    }
    
    
    public static JavaPackageModel resolvePackage(String packageName) {
        return Main.getFacts().getPackagesStore().findOrAddPackage(packageName);
    }
    
    public static JavaTypeModel resolveJavaLangObject() throws TermWareException
    {
      try {  
        return resolveTypeModelFromPackage("Object","java.lang");
      }catch(EntityNotFoundException ex){
          throw new AssertException("java.lang.Object must be resolved");
      }
    }
    
}
