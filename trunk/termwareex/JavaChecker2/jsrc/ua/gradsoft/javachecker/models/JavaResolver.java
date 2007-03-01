/*
 * JavaResolver.java
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.Main;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.javachecker.util.Pair;
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
        JavaUnitModel unitModel = where.getUnitModel();
        JavaPackageModel packageModel = where.getPackageModel();
        return resolveTypeToModel(t,unitModel,packageModel,where,typeVariables,null);
    }
    
    public static JavaTypeModel resolveTypeToModel(Term t, JavaStatementModel where) throws EntityNotFoundException, TermWareException {
        JavaTypeModel ownerType=where.getTopLevelBlockModel().getOwnerModel().getTypeModel();
        JavaTopLevelBlockOwnerModel blockOwner=where.getTopLevelBlockModel().getOwnerModel();
        List<JavaTypeVariableAbstractModel> typeVariables=blockOwner.getTypeParameters();
        Iterable<JavaTypeModel> localTypes = new LocalTypesIterable(where);
        return resolveTypeToModel(t,ownerType.getUnitModel(),ownerType.getPackageModel(),ownerType,typeVariables,localTypes);
    }
    
    
    public static JavaTypeModel resolveTypeToModel(Term t, JavaUnitModel unitModel, JavaPackageModel packageModel, JavaTypeModel where,List<JavaTypeVariableAbstractModel> typeVariables,Iterable<JavaTypeModel> localTypes) throws EntityNotFoundException,TermWareException {
        boolean printDetails=false;       
        
        if (printDetails) {
            String stv = ((typeVariables==null ? "null" :  typeVariables.toString()));
            LOG.log(Level.INFO,"resolveTypeToModel, t="+TermHelper.termToString(t)+" in "+ (where==null ? "null" : where.getFullName())+" stv="+stv);
            if (where!=null) {
                if (where.hasTypeParameters()) {
                    LOG.log(Level.FINE,where.getName()+"have type parameters:"+where.getTypeParameters().toString());
                }
            }
        }
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
            if (t.getName().equals("TypeRef")) {
                // already resolved type is a second argument.
                Term jt = t.getSubtermAt(1);
                if (!jt.isJavaObject()) {
                    throw new AssertException("second TypeRef argument must be java object");
                }
                Object o = jt.getJavaObject();
                return (JavaTypeModel)o;
            } else if (t.getName().equals("ReferenceType")) {
                Term t1=t.getSubtermAt(1);
                JavaTypeModel tm=resolveTypeToModel(t1,unitModel,packageModel,where,typeVariables,localTypes);
                int referenceLevel=t.getSubtermAt(0).getInt();
                while(referenceLevel>0) {
                    tm=new JavaArrayTypeModel(tm);
                    --referenceLevel;
                }
                return tm;
            }else if (t.getName().equals("Identifier")) {
                String name=t.getSubtermAt(0).getString();
                if (where==null) {
                    return resolveTypeModelByName(name,unitModel,packageModel,typeVariables);
                }else{
                    return resolveTypeModelByName(name,where,typeVariables,localTypes);
                }
            }else if (t.getName().equals("ClassOrInterfaceType")) {
                //at first, try to find class in our package or imported classes
                Term head=t.getSubtermAt(0);
                Term ct=head.getSubtermAt(0); // must be identifier
                String name=ct.getSubtermAt(0).getName();
                JavaTypeModel frsTypeModel=null;
                boolean found=true;
                try {
                    if (where==null) {
                        frsTypeModel=resolveTypeModelByName(name,unitModel,packageModel, typeVariables);
                    }else{
                        frsTypeModel=resolveTypeModelByName(name,where,typeVariables,localTypes);
                    }
                }catch(EntityNotFoundException ex){
                    found=false;
                }
                if (found) {
                    if (head.getSubtermAt(1).isNil()) {
                        return frsTypeModel;
                    }else{
                        try {
                            return resolveRestOfClassOrInterfaceType(frsTypeModel,head.getSubtermAt(1),unitModel,packageModel,where,typeVariables,localTypes);
                        }catch(EntityNotFoundException ex){
                            //System.err.println("failed resolveRestOfClassAndInterfaceType");
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
            }else if (t.getName().equals("NestedType")) {
                Term t1=t.getSubtermAt(0);
                Term nested=t.getSubtermAt(1);
                JavaTypeModel tm = resolveTypeToModel(t1, unitModel, packageModel, where, typeVariables, localTypes);
                return JavaResolver.resolveTypeToModel(nested,tm,typeVariables);
            }else if (t.getName().equals("FullTypeName")) {
                String packageName = t.getSubtermAt(0).getString();
                String classShortName = t.getSubtermAt(1).getSubtermAt(0).getString();
                return JavaResolver.resolveTypeModelFromPackage(classShortName,packageName);
            }else if (t.getName().equals("TypeRef")){
                Term tpref = t.getSubtermAt(1);
                Object o = tpref.getJavaObject();
                if (! (o instanceof JavaTypeModel)) {
                    throw new AssertException("second argument of TypeRef must be JavaTypeModel");
                }
                return (JavaTypeModel)o;
            }else{
                // this is not ReferenceType and not PrimaryType and not ClassOrInterface body
                throw new AssertException("Unknown type term:"+TermHelper.termToString(t));
            }
        }else{
            throw new AssertException("Primitive non-atom type term:"+TermHelper.termToString(t));
        }
        if (printDetails) {
            System.err.println("failed resolveTypeToModel, t="+TermHelper.termToString(t)+"+ in "+ (where==null ? "null" : where.getName()));
        }
        throw new EntityNotFoundException("type",TermHelper.termToString(t),"");
    }
    
    
    private static JavaTypeModel  resolveRestOfClassOrInterfaceType(JavaTypeModel prevModel, Term t, JavaUnitModel unitModel,JavaPackageModel packageModel,JavaTypeModel where, List<JavaTypeVariableAbstractModel> typeVariables,Iterable<JavaTypeModel> localTypes) throws EntityNotFoundException, TermWareException {
        boolean printDetails=false;
        if (printDetails) {
            System.err.println("resolveRestOfClassOrInterfaceType "+TermHelper.termToString(t)+", prevModel="+prevModel.getName());
        }
        JavaTypeModel curModel=prevModel;
        while(!t.isNil()) {
            Term ct=t.getSubtermAt(0);
            if (ct.getName().equals("Identifier")) {
                String name=ct.getSubtermAt(0).getString();
                //TODO: think about typeArguments among classOrInterfaceType ?
                if (curModel.hasNestedTypeModels()) {
                    try {
                        curModel=curModel.findNestedTypeModel(name);
                    }catch(NotSupportedException ex){
                        throw new AssertException("impossible, hasNestedTypeMopdels but getNestedTypeModels throws NotSupported");
                    }
                }else{
                    if (printDetails) {
                        System.err.println("failed resolveRestOfCalssAndInterfaceType");
                    }
                    throw new EntityNotFoundException("type",t.getSubtermAt(0).getName(),"");
                }
            }else if(ct.getName().equals("TypeArguments")) {
                if (printDetails) {
                    System.err.println("creating new ArgumentBoundTypeModel");
                }
                curModel=new JavaTypeArgumentBoundTypeModel(curModel,ct,where,typeVariables,null);
            }else{
                throw new AssertException("Only Identifiers or TypeArgumentrs are allowed in ClassOrInterfaceType sequence, we have "+TermHelper.termToString(ct));
            }
            t=t.getSubtermAt(1);
        }
        if (printDetails) {
            System.err.println("resolveRestOfCalssAndInterfaceType succesfull");
        }
        return curModel;
    }
    
    public static JavaTypeModel resolveTypeModelByName(String name, JavaTypeModel where,List<JavaTypeVariableAbstractModel> typeVariables,Iterable<JavaTypeModel> localTypes) throws EntityNotFoundException, TermWareException {
        boolean printDetails=false;
        if (printDetails) {
            String stv;
            if (typeVariables==null) {
                stv="null";
            }else{
                stv=typeVariables.toString();
            }
            //System.err.println("!!!resolveTypeModelByName("+name+","+where.toString()+","+stv+")");
            LOG.info("resolveTypeModelByName("+name+","+where.getFullName()+","+stv+")");
        }
        
        //0. may be this is where
        // if (name.equals(where.getName())) {
        //     return where;
        // }

        //0. may be model, where we resolve, have typeVariables
        if (where.hasTypeParameters()) {
            for(JavaTypeVariableAbstractModel tv:where.getTypeParameters()) {
                if (tv.getName().equals(name)) {
                    if (printDetails) {
                        LOG.info("found in type parameters");
                    }
                    return tv;
                }
            }
        }
        
        
        //1. try to find among type variables.
        if (typeVariables!=null) {
            for(JavaTypeVariableAbstractModel tv: typeVariables) {
                if (tv.getName().equals(name)) {
                    if (printDetails) {
                      LOG.info("found in type variables");
                    }
                    return tv;
                }
            }
        }
        
        
        
        //2. try to find among local types
        if (localTypes!=null) {
            for(JavaTypeModel tm: localTypes) {
                if (tm.getName().equals(name)) {
                    if (printDetails) {
                        LOG.info("found in local types");
                    }
                    return tm;
                }
            }
        }
        
        //2. try to find subtype of current type.
        if (where.hasNestedTypeModels()) {
            //System.err.println("try to search in nested type models");
            try {
                return where.findNestedTypeModel(name);
            }catch(EntityNotFoundException ex){
                
                ; /* do nothing */
            }catch(NotSupportedException ex){
                ; /* do nothing */
            }
            //System.err.println("failed");
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
                
                // and try to resolve one in enclosed.
                try {
                    return resolveTypeModelByName(name, enclosed, typeVariables, localTypes);
                }catch(EntityNotFoundException ex){
                    // do nothing.
                    ;
                }
                
            }
        }
        
        
        
        
        //3. try to find as nested in super-class
        JavaTypeModel superModel = null;
        try {
            superModel = where.getSuperClass();
        }catch(NotSupportedException ex) {
            ;
        }
        if (superModel!=null) {
            
            while(!superModel.isNull()
            && !(superModel.getFullName().equals("java.lang.Object"))
            && !(superModel.getFullName().equals("java.lang.Enum"))
            ) {
                if (superModel.hasNestedTypeModels()) {
                    try {
                        return superModel.findNestedTypeModel(name);
                    }catch(EntityNotFoundException ex){
                        ;
                    }catch(NotSupportedException ex){
                        ;
                    }
                }
                try {
                    superModel=superModel.getSuperClass();
                }catch(NotSupportedException ex){
                    break;
                }
            }
        }
        
        //or in super-interfaces
        //System.err.println("!!!check for nested types of interfaces "+name+" in "+where.getName());
        LinkedList<JavaTypeModel> allSuperInterfaces=new LinkedList<JavaTypeModel>();
        try {
            allSuperInterfaces.addAll(where.getSuperInterfaces());
        }catch(NotSupportedException ex){
            ;
        }
        while(!allSuperInterfaces.isEmpty()) {
            JavaTypeModel curr = allSuperInterfaces.removeFirst();
            if (curr.hasNestedTypeModels()) {
                try {
                    
                    JavaTypeModel retval = curr.findNestedTypeModel(name);
                    if (printDetails) {
                        LOG.log(Level.INFO,"found "+retval.getFullName()+" in "+where.getName());
                    }
                    return retval;
                }catch(EntityNotFoundException ex){
                    ;
                }catch(NotSupportedException ex){
                    ;
                }
                if (printDetails) {
                    LOG.log(Level.INFO,"failed search of "+name+" as nested in "+curr.getName());
                }
            }
            try {
                allSuperInterfaces.addAll(curr.getSuperInterfaces());
            }catch(NotSupportedException ex){
                ;
            }
            if (curr.isNested()) {
                // add nested type.
                try {
                    allSuperInterfaces.add(curr.getEnclosedType());
                }catch(NotSupportedException ex){
                    // impossible, do nothing.
                }
            }
        }
        
        
        
        
        // now get current package model and try to find class in current compilation unit in this
        // package.
        JavaPackageModel pm=where.getPackageModel();
        
        JavaUnitModel um=where.getUnitModel();
        if (um==null) {
            LOG.warning("um is null for "+where.getFullName());
            try {
                JavaTypeModel retval=pm.findTypeModel(name);
                if (retval!=null) {
                    return retval;
                }
            }catch(EntityNotFoundException ex){
                ; /* do nothing */
            }
        }else{
            return resolveTypeModelByName(name,um,pm,typeVariables);
        }
        
        
        
        
        try {
            //if all fail, try to find in "java.lang"
            return resolveTypeModelFromPackage(name,"java.lang");
        }catch(EntityNotFoundException ex){
            /* do nothing */
            ;
        }
        
        if (printDetails) {
            LOG.log(Level.INFO,"failed resolving of "+name+" in "+where.getName());
        }
        //we still here - it means that class was not found in import declarations.
        throw new EntityNotFoundException(" type ",name,"");
    }
    
    public static JavaTypeModel resolveTypeModelByName(String name, JavaUnitModel um, JavaPackageModel pm, List<JavaTypeVariableAbstractModel> typeVariables) throws TermWareException, EntityNotFoundException {
        boolean printDetails=false;
               
        if (printDetails) {
            LOG.log(Level.INFO,"resolveTypeModelByName("+name+","+um.toString()+","+pm.getName()+")");
        }
        
        
        if (typeVariables!=null) {
            for(JavaTypeVariableAbstractModel tv: typeVariables) {
                if (tv.getName().equals(name)) {
                    //System.err.println("found in type variables");
                    return tv;
                }
            }
        }
        
        
        
        if (um instanceof JavaCompilationUnitModel) {
            JavaCompilationUnitModel cu=(JavaCompilationUnitModel)um;
            //at first, try to find in class imports.
            JavaCompilationUnitModel.ClassImportSuffix suffix = cu.getClassImports().get(name);
            if (suffix!=null) {
                JavaTypeModel tm = suffix.getTypeModel();
                if (tm!=null) {
                    return tm;
                }else{
                    String fullClassName = suffix.getFullClassName();
                    tm = JavaResolver.resolveTypeModelByFullClassName(fullClassName);
                    suffix.setTypeModel(tm);
                    return tm;
                }
            }
        }
        
        // if not found in class imports, than in current package
        try {
            JavaTypeModel retval=pm.findTypeModel(name);
            if (retval!=null) {
                return retval;
            }
        }catch(EntityNotFoundException ex){
            ; /* do nothing */
        }
        
        
        
        // try to get Type Model from any aviable packages.
        if (um instanceof JavaCompilationUnitModel) {
            JavaCompilationUnitModel cu=(JavaCompilationUnitModel)um;
            for(Map.Entry<String,JavaCompilationUnitModel.PackageOrClassModelHolder> e:cu.getPackageOrClassImports().entrySet()){
                String cPackageOrClassName = e.getKey();
                JavaCompilationUnitModel.PackageOrClassModelHolder holder = e.getValue();
                if (holder.isInitialized()) {
                   if (holder.getPackageModel()!=null) { 
                     try {
                        return holder.getPackageModel().findTypeModel(name); 
                       //return resolveTypeModelFromPackage(name,cPackageOrClassName);
                     }catch(EntityNotFoundException ex){
                    /* do nothing */
                       ;
                     }
                   }else if (holder.getTypeModel()!=null) {
                       JavaTypeModel tm = holder.getTypeModel();
                       // tyr to find nested class here.
                       // without inheritance.
                       try {
                           return tm.findNestedTypeModel(name);
                       }catch(EntityNotFoundException ex){
                           /* do nothing */
                           ;
                       }catch(NotSupportedException ex){
                           /* strange, but skip */
                           ; 
                       }
                   }
                }else{                    
                    JavaTypeModel retval=null;                    
                    JavaPackageModel npm=Main.getFacts().getPackagesStore().findOrAddPackage(cPackageOrClassName);
                    try {                       
                        retval = npm.findTypeModel(name); 
                    }catch(EntityNotFoundException ex){
                        ;
                    }
                    if (retval!=null) {
                        // great, we find all waht we need.
                        holder.setPackageModel(npm);
                        return retval;
                    }else{
                        // hmm, problem here.
                        if (npm.getNumberOfLoadedReferences()==0) {
                            // nothig was loaded, so we can remove one
                            Main.getFacts().getPackagesStore().removePackage(cPackageOrClassName);
                        }
                    }
                    // if we still here, try to look to find class import
                    JavaTypeModel importedType=null;
                    try {
                       importedType=JavaResolver.resolveTypeModelByFullClassName(cPackageOrClassName); 
                    }catch(EntityNotFoundException ex){
                        // it's not our case;
                        ;
                    }
                    if (importedType!=null) {
                        try {
                            retval = importedType.findNestedTypeModel(name);
                        }catch(EntityNotFoundException ex){
                            ;
                        }catch(NotSupportedException ex){
                            ;
                        }
                        if (retval!=null) {
                            // great, save to holder.
                            holder.setTypeModel(importedType);
                            return retval;
                        }
                    }
                    
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
        
        if (printDetails) {
            System.err.println("failed search of type "+name+" outside class ");
        }
        //we still here - it means that class was not found in import declarations.
        throw new EntityNotFoundException(" type ",name,"");
    }
    
    public static JavaTypeModel resolveTypeModelWithFullPackage(Term t, JavaTypeModel where) throws EntityNotFoundException, TermWareException {
        if (!t.getName().equals("ClassOrInterfaceType")) {
            throw new AssertException("argument of resolveTypeModelWithFullPackageName must be ClassOrInterfaceType, we have:"+TermHelper.termToString(t));
        }
        JavaUnitModel unitModel=null;
        JavaPackageModel packageModel=null;
        if (where!=null) {
            unitModel=where.getUnitModel();
            packageModel=where.getPackageModel();
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
        Term revList=TermUtils.createNil();
        int index=0;
        boolean frs=true;
        while(!head.isNil()) {
            Term ct=head.getSubtermAt(0);
            revList=TermUtils.createTerm("cons",ct,revList);
            head=head.getSubtermAt(1);
            ++index;
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
        try {
            JavaTypeModel retval=resolveTypeModelFromPackage(classShortName,packageName);
            if (!head.isNil()) {
                return resolveRestOfClassOrInterfaceType(retval,head,unitModel,packageModel,where,null,null);
            }
            return retval;
        }catch(EntityNotFoundException ex){
            for(;;) {
                int lastDotIndex=packageName.lastIndexOf(".");
                if (lastDotIndex==-1) {
                    throw new EntityNotFoundException("type",TermHelper.termToString(t),"");
                }
                String candidateName = packageName.substring(lastDotIndex+1);
                packageName = packageName.substring(0,lastDotIndex);
                //System.err.println("candidateName:"+candidateName+",packageName="+packageName);
                Term ct = revList.getSubtermAt(0);
                revList = revList.getSubtermAt(1);
                head=TermUtils.createTerm("cons",ct,head);
                try {
                    JavaTypeModel retval=resolveTypeModelFromPackage(candidateName,packageName);
                    if (!head.isNil()) {
                        return resolveRestOfClassOrInterfaceType(retval,head,unitModel,packageModel,where,null,null);
                    }
                }catch(EntityNotFoundException ex1){
                    ; //ignore
                }
            }
        }
    }
    
    
    public static JavaTypeModel resolveTypeModelFromPackage(String classShortName,String packageName) throws EntityNotFoundException, TermWareException {
        //System.out.println("resolveTypeModelFromPackage:"+classShortName+","+packageName);
        JavaPackageModel packageModel = resolvePackage(packageName);
        return packageModel.findTypeModel(classShortName);
    }
    
    public static JavaTypeModel  resolveTypeModelByFullClassName(String name) throws EntityNotFoundException, TermWareException {
        int lastDotIndex=name.lastIndexOf('.');
        if (lastDotIndex!=-1) {
            String packageName = name.substring(0,lastDotIndex);
            String className = name.substring(lastDotIndex+1);
            try {
                return resolveTypeModelFromPackage(className,packageName);
            }catch(EntityNotFoundException ex){
                // this can be request to nested class.
                JavaTypeModel enclosingModel = resolveTypeModelByFullClassName(packageName);
                try {
                    return enclosingModel.findNestedTypeModel(className);
                }catch(NotSupportedException ex1){
                    throw new EntityNotFoundException("type",name,"");
                }
            }
        }else{
            throw new EntityNotFoundException("type",name,"");
        }
    }
    
    public static JavaPackageModel resolvePackage(String packageName) {
        return Main.getFacts().getPackagesStore().findOrAddPackage(packageName);
    }
    
    /**
     * resolve member variable by name in scope of class definition (outside block)
     */
    public static JavaMemberVariableModel resolveMemberVariableByName(String name,JavaTypeModel where) throws TermWareException, EntityNotFoundException {
        boolean printDetails=false;
        if(printDetails) {
            LOG.log(Level.INFO,"resolveMemberVariableByName("+name+","+where.getFullName()+")");
        }
        LinkedList<JavaTypeModel> toCheck = new LinkedList<JavaTypeModel>();
        toCheck.addFirst(where);
        while(!toCheck.isEmpty()) {
            JavaTypeModel checked = toCheck.removeLast();
            try {
                if (printDetails) {
                    LOG.log(Level.INFO,"check in "+checked.getFullName());
                }
                JavaMemberVariableModel retval = checked.findMemberVariableModel(name);
                if (printDetails) {
                    LOG.log(Level.INFO,"found");
                }
                return retval;
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
        if (printDetails) {
            LOG.log(Level.INFO,"not found("+name+","+where.getFullName()+")");
        }
        throw new EntityNotFoundException(" member variable ",name,where.getFullName());
    }
    
    
    /**
     *resolve any variable outside scope definition.
     *(this can be member variable, constant from static import,
     * variable of enclosed statement for local and anonimous classes)
     */
    public static JavaVariableModel resolveVariableByName(String name,JavaTypeModel where) throws TermWareException, EntityNotFoundException {
        boolean printDetails=false;
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
            LOG.warning("um is null for "+where.getFullName());
        } else if (um instanceof JavaCompilationUnitModel) {
            JavaCompilationUnitModel cum=(JavaCompilationUnitModel)um;
            
            Map<String,String> staticMemberImports = cum.getStaticMemberImports();
            for(Map.Entry<String,String> e: staticMemberImports.entrySet()) {
                if (e.getKey().equals(name)) {
                    JavaTypeModel tp=null;
                    try {
                        tp=resolveTypeModelByFullClassName(e.getValue());
                        return resolveMemberVariableByName(name,tp);
                    }catch(EntityNotFoundException ex){
                        // this was a method. ignore.
                        ;
                    }
                }
            }
            
            //search such fields in static class imports
            Set<String> staticClassImports = cum.getStaticClassImports();
            for (String s:staticClassImports) {
                JavaTypeModel st=null;
                try {
                    st=resolveTypeModelByFullClassName(s);
                }catch(EntityNotFoundException ex){
                    // class not found, i.e. this must not compile.
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
    
    
    public static JavaVariableModel resolveVariableByName(String name, JavaStatementModel statement) throws TermWareException, EntityNotFoundException {
        boolean printDetails=false;
        //if (name.equals("p")||name.equals("parent")||name.equals("left")) {
        //    printDetails=true;
        //}
        boolean quit=false;
        JavaStatementModel parentStatement=statement.getParentStatementModel();
        while(!quit) {
            if (printDetails) {
                StringBuilder sb = new StringBuilder();
                sb.append("resolveVariableByName("+name+"),statement "+statement);
                if (statement instanceof JavaTermStatementModel) {
                    JavaTermStatementModel ts = (JavaTermStatementModel)statement;
                    sb.append(":"+TermHelper.termToString(ts.getTerm()));
                }
                LOG.log(Level.INFO,sb.toString());
            }
            
            List<JavaLocalVariableModel> lv = statement.getLocalVariables();
            for(JavaLocalVariableModel v:lv) {
                if (printDetails) {
                    LOG.log(Level.INFO,"check for variable: "+v.getName());
                }
                if (v.getName().equals(name)) {
                    return v;
                }
            }
            if (parentStatement!=null) {
                if (printDetails) {
                    LOG.log(Level.INFO,"parentStatement.getKind()="+parentStatement.getKind());
                }
                JavaStatementKind parentKind=parentStatement.getKind();
                if (parentKind==JavaStatementKind.BLOCK || parentKind==JavaStatementKind.SWITCH_LABEL_BLOCK) {
                    statement=statement.getPreviousStatementModel();
                    if (statement==null) {
                        statement=parentStatement;
                        parentStatement=statement.getParentStatementModel();
                        if (parentKind==JavaStatementKind.SWITCH_LABEL_BLOCK) {
                            JavaStatementModel prev=statement.getPreviousStatementModel();
                            while (prev!=null) {
                                int prevSize = prev.getChildStatements().size();
                                if (prevSize==0) {
                                    prev=prev.getPreviousStatementModel();
                                }else{
                                    statement=prev.getChildStatements().get(prevSize-1);
                                    parentStatement=statement.getParentStatementModel();
                                    break;
                                }
                            }
                        }
                    }
                }else{
                    statement=parentStatement;
                    parentStatement=statement.getParentStatementModel();
                }
            }else{
                //System.out.println("parentStatement=null");
                // parentStatement is null
                if (statement.getPreviousStatementModel()!=null) {
                    // top-level block, where root statement is not block.
                    statement=statement.getPreviousStatementModel();
                }else{
                    quit=true;
                }
            }
        }
        
        // This can be variable in enclosed function.
        JavaTypeModel tm = statement.getTopLevelBlockModel().getOwnerModel().getTypeModel();
        if (tm.isLocal()||tm.isAnonimous()) {
            JavaStatementModel enclosed=tm.getEnclosedStatement();
            if (enclosed!=null) {
                try {
                    return resolveVariableByName(name,enclosed);
                }catch(EntityNotFoundException ex){
                    //ohh, this is not our case, skip
                    ;
                }
            }
        }
        
        
        // This is not local variable, let's check formal parameters.
        Map<String,JavaFormalParameterModel> fpm = statement.getTopLevelBlockModel().getOwnerModel().getFormalParametersMap();
        JavaFormalParameterModel fp = fpm.get(name);
        if (fp!=null) {
            return fp;
        }
        // Now this can be resolved only outside our top-level block.
        JavaVariableModel retval=resolveVariableByName(name,statement.getTopLevelBlockModel().getOwnerModel().getTypeModel());
        return retval;
    }
    
    public static JavaFormalParameterModel resolveFormalParameterByName(String name, JavaTopLevelBlockOwnerModel ownerModel) throws EntityNotFoundException, TermWareException {
        Map<String,JavaFormalParameterModel> fpm = ownerModel.getFormalParametersMap();
        JavaFormalParameterModel fp = fpm.get(name);
        if (fp!=null) {
            return fp;
        }
        throw new EntityNotFoundException("formal parameter",name,ownerModel.getTypeModel().getFullName()+"."+ownerModel.getName());
    }
    
    
    /**
     * resolve method call to where (in where and all superclasses) and build substitutuion of method type arguments if needed.
     */
    public static JavaMethodModel resolveMethod(String methodName,List<JavaTypeModel> argumentTypes, JavaTypeArgumentsSubstitution substitution,JavaTypeModel where) throws EntityNotFoundException, TermWareException {
        boolean printDetails=false;
    //    if (methodName.equals("findInLine")||methodName.equals("forName")) {
    //        printDetails=true;
    //    }
        if (printDetails) {
            StringBuilder sb=new StringBuilder();
            boolean frs=true;
            sb.append("resolveMethod "+methodName+"(");
            for(JavaTypeModel at: argumentTypes) {
                if (frs) frs=false; else sb.append(",");
                sb.append(at.getName());
            }
            sb.append(")");
            sb.append("  in "+where.getName());    
            if (where.hasTypeParameters()) {
                sb.append(", tp="+where.getTypeParameters() );
            }
            LOG.log(Level.INFO,sb.toString());
        }
        boolean found=false;
        LinkedList<JavaTypeModel> toCheck = new LinkedList<JavaTypeModel>();
        toCheck.add(where);
        List<Pair<JavaMethodModel,JavaTypeArgumentsSubstitution>> goodCandidates = new ArrayList<Pair<JavaMethodModel,JavaTypeArgumentsSubstitution>>();
        while(!toCheck.isEmpty()){
            JavaTypeModel currWhere=toCheck.removeFirst();
            List<JavaMethodModel> candidates=null;
            try{
                if (printDetails) {
                    LOG.log(Level.INFO,"Search candidates in "+currWhere.getFullName()+" ("+currWhere.toString()+")");
                }
                candidates = currWhere.findMethodModels(methodName);
            }catch(EntityNotFoundException ex) {
                // candidates are still null
            }catch(NotSupportedException ex) {
                // try to search in primitive type.
                // impossible, we can ignore one here
            }
            if (candidates!=null) {
                int nArguments=argumentTypes.size();
                for(JavaMethodModel candidate:candidates) {
                    if (printDetails){
                        StringWriter swr=new StringWriter();
                        PrintWriter wr=new PrintWriter(swr);
                        wr.print("found candidate:");
                        candidate.print(wr);
                        wr.flush();
                        String s = swr.toString();
                        wr.close();
                        LOG.log(Level.INFO,s);
                    }
                    
                    List<JavaFormalParameterModel> fpts = candidate.getFormalParametersList();
                    
                    //TODO: copy substitution.
                    JavaTypeArgumentsSubstitution newSubstitution=new JavaTypeArgumentsSubstitution();
                    newSubstitution.putAll(substitution);
                    if (match(fpts,argumentTypes,newSubstitution,printDetails)) {
                        // TODO: we must peek best instead returning first.
                        if (printDetails) {
                            StringWriter swr=new StringWriter();
                            PrintWriter wr=new PrintWriter(swr);
                            wr.print("matched, substitution=");
                            newSubstitution.print(wr);
                            wr.flush();
                            String s = swr.toString();
                            wr.close();
                            LOG.log(Level.INFO,s);
                        }
                        substitution.putAll(newSubstitution);
                        return candidate;
                    }else{
                        if (printDetails) {
                            LOG.log(Level.INFO,"not match");
                        }
                        //TODO: restore substituton
                    }
                }
            }
            // if we here, than nothing found here, try supers
            if (!currWhere.isNull() && !(currWhere.isEnum() && currWhere.getFullName().equals("java.lang.Enum"))) {
                try {
                    toCheck.add(currWhere.getSuperClass());
                }catch(NotSupportedException ex){
                    // impossible,
                    ;
                }
            }
            if(true /*currWhere.isInterface()||currWhere.getModifiersModel().isAbstract()*/) {
                try {
                    List<JavaTypeModel> interfaces = currWhere.getSuperInterfaces();
                    toCheck.addAll(currWhere.getSuperInterfaces());
                }catch(NotSupportedException ex){
                    //  ignore
                    ;
                }
            }
            if (currWhere.isTypeArgument()) {
                JavaTypeVariableAbstractModel tmaWhere = (JavaTypeVariableAbstractModel)currWhere;
                List<JavaTypeModel> bounds = tmaWhere.getBounds();
                if (printDetails) {                    
                    StringWriter swr = new StringWriter();
                    PrintWriter pwr = new PrintWriter(swr);
                    boolean frs=true;                    
                    for(JavaTypeModel bound: bounds) {
                        if (!frs) pwr.print(","); else frs=false;
                        pwr.print(bound.getFullName());
                    }
                    pwr.close();
                    LOG.log(Level.INFO,"typeArgument, bounds are:"+swr.toString());
                }
                if (bounds.isEmpty()) {
                    // check in object.
                    JavaTypeModel objectTypeModel = JavaResolver.resolveJavaLangObject();
                    toCheck.addLast(objectTypeModel);
                }else{
                    toCheck.addAll(bounds);
                }
            }
            
            //then may be this is a method of enclosing class ?
            if (currWhere.isNested()) {
                try {
                    toCheck.addLast(currWhere.getEnclosedType());
                }catch(NotSupportedException ex){
                    ;
                    //impossible, ignore.
                }
            }
            
            
        }
        
        //see static import
        JavaUnitModel um=where.getUnitModel();
        if (um==null) {
            // strange
            LOG.warning("um is null for "+where.getFullName());
        } else if (um instanceof JavaCompilationUnitModel) {
            JavaCompilationUnitModel cum = (JavaCompilationUnitModel)um;
            Map<String,String> methodClassImports = cum.getStaticMemberImports();
            if (methodClassImports.get(methodName)!=null) {
                String fullClassName = methodClassImports.get(methodName);
                //System.err.println("full class name is:"+fullClassName);
                JavaTypeModel importedType = null;
                try {
                    importedType = JavaResolver.resolveTypeModelByFullClassName(fullClassName);
                }catch(EntityNotFoundException ex){
                    // invalid import. ignore
                    ;
                }
                if (importedType!=null) {
                    List<JavaMethodModel> ml=null;
                    try{
                        ml=importedType.findMethodModels(methodName);
                    }catch(EntityNotFoundException ex){
                        // invalid model, ignore
                        ;
                    }catch(NotSupportedException ex){
                        // invalid model, ignore
                        ;
                    }
                    if (ml!=null) {
                        for(JavaMethodModel m: ml){
                            List<JavaFormalParameterModel> fps=m.getFormalParametersList();
                            //TODO: preserve substitution
                            if (match(fps,argumentTypes,substitution,printDetails)) {
                                return m;
                            }else{
                                //TODO: restore substitution
                            }
                        }
                    }
                }
            } // end of method class imports.
            
        }
        
        
        // we still here ?
        //  ok, it means that notning is found.
        //  let's prepare full method name and throw exception
        StringBuilder sb = new StringBuilder();
        sb.append(methodName);
        sb.append('(');
        boolean frs=true;
        for(JavaTypeModel tm: argumentTypes) {
            if (!frs) {
                sb.append(",");
            }else{
                frs=false;
            }
            sb.append(tm.getFullName());
        }
        sb.append(')');
        String fullMethodName=sb.toString();
        throw new EntityNotFoundException("method",fullMethodName, "in class "+where.getFullName());
    }
    
    
    public static JavaTypeModel resolveTypeTerm(Term t, JavaPlaceContext ctx) throws EntityNotFoundException, TermWareException {
        if (ctx.getStatementModel()!=null) {
            return resolveTypeToModel(t,ctx.getStatementModel());
        }else if (ctx.getTopLeveBlockOwnerModel()!=null) {
            return resolveTypeToModel(t,ctx.getTypeModel(),ctx.getTopLeveBlockOwnerModel().getTypeParameters());
        }else if (ctx.getTypeModel()!=null) {
            return resolveTypeToModel(t,ctx.getTypeModel(),null);
        }else if (ctx.getPackageModel()!=null) {
            throw new AssertException("Can't resolve type model without enclosing type");
        }else{
            return resolveTypeModelWithFullPackage(t,null);
        }
    }
    
    /**
     *resolve type of Java expression.
     *@param t - term, which must be java expression.
     *@param ctx - place context for resolving names.
     */
    public static JavaTypeModel resolveExpressionType(Term t, JavaPlaceContext ctx) throws EntityNotFoundException, TermWareException {
        return JavaExpressionHelper.resolveExpressionType(t,ctx);
    }
    
    public static JavaTypeModel resolveTypeModelByName(String name, JavaPlaceContext ctx) throws EntityNotFoundException, TermWareException {
        if (ctx.getStatementModel()!=null) {
            return resolveTypeModelByName(name,ctx.getTypeModel(),ctx.getTopLeveBlockOwnerModel().getTypeParameters(),new LocalTypesIterable(ctx.getStatementModel()));
        }else if (ctx.getTopLeveBlockOwnerModel()!=null) {
            return resolveTypeModelByName(name,ctx.getTypeModel(),ctx.getTopLeveBlockOwnerModel().getTypeParameters(),null);
        }else if (ctx.getTypeModel()!=null) {
            return resolveTypeModelByName(name,ctx.getTypeModel(),null,null);
        }else if (ctx.getPackageModel()!=null) {
            try {
                return resolveTypeModelFromPackage(name,ctx.getPackageModel().getName());
            }catch(EntityNotFoundException ex){
                ;
            }
        }
        return resolveTypeModelFromPackage(name,"java.lang");
    }
    
    public static JavaVariableModel resolveVariableByName(String name, JavaPlaceContext ctx) throws EntityNotFoundException, TermWareException {
        if (ctx.getStatementModel()!=null) {
            return resolveVariableByName(name,ctx.getStatementModel());
        }else if (ctx.getTopLeveBlockOwnerModel()!=null){
            try {
                return resolveFormalParameterByName(name,ctx.getTopLeveBlockOwnerModel());
            }catch(EntityNotFoundException ex){
                return resolveVariableByName(name,ctx.getTypeModel());
            }
        }else if (ctx.getTypeModel()!=null) {
            return resolveVariableByName(name,ctx.getTypeModel());
        }else{
            throw new AssertException("Can't resolve variable outsde type model");
        }
    }
    
    
    public static boolean match(JavaTypeModel pattern, JavaTypeModel x, JavaTypeArgumentsSubstitution substitution, boolean debug) throws TermWareException {
        if (pattern.isPrimitiveType() && !x.isPrimitiveType()) {
            return match1(pattern,JavaTypeModelHelper.unboxingConversion(x),substitution,debug);
        }else if (!pattern.isPrimitiveType() && x.isPrimitiveType()) {
            return match1(pattern,JavaTypeModelHelper.boxingConversion(x),substitution,debug);
        }else{
            return match1(pattern,x,substitution,debug);
        }
    }
    
    /**
     * match patern type with x and fill substitution if needed after aplying boxing/unboxing conventions.
     */
    public static boolean match1(JavaTypeModel pattern, JavaTypeModel x,JavaTypeArgumentsSubstitution substitution, boolean topDebug) throws TermWareException {
        boolean match1DebugEnabled=true;
        boolean debug = topDebug && match1DebugEnabled;
        if (debug) {
            LOG.log(Level.INFO,"match1(+"+pattern.getFullName()+","+x.getFullName()+")");
        }
        boolean retval=true;
        if (pattern.isTypeArgument()) {
            JavaTypeVariableAbstractModel vpattern = (JavaTypeVariableAbstractModel)pattern;
            JavaTypeModel matched=substitution.get(vpattern);
            if (matched!=null) {
                if (matched.isTypeArgument()) {
                   if (JavaTypeModelHelper.subtypeOrSame(x,pattern,debug)) {
                       retval=true;
                   }else{
                       //to prevent recursion we must deconstruct or put pattern here.
                       JavaTypeVariableAbstractModel vmatched = (JavaTypeVariableAbstractModel)matched;
                       List<JavaTypeModel> bounds = vmatched.getBounds();
                       for(JavaTypeModel bound: bounds) {
                           if (match(bound,x,substitution,debug)) {
                               retval=true;
                               break;
                           }
                       }                        
                   }
                }else{
                   retval=match(matched,x,substitution,debug);       
                }                                
            }else{
                List<JavaTypeModel> bounds = vpattern.getBounds();
                for(JavaTypeModel bound: bounds) {
                    if (!match(bound,x,substitution,debug)) {
                        retval=false;
                        break;
                    }
                }
                if (retval) {
                    if (x!=vpattern) {
                      substitution.put(vpattern,x);
                    }
                }
            }
        }else if (pattern.isWildcardBounds()) {
            JavaWildcardBoundsTypeModel wpattern = (JavaWildcardBoundsTypeModel)pattern;
            switch(wpattern.getKind()) {
                case OBJECT:
                    retval=true;
                    break;
                case EXTENDS:
                    retval=JavaTypeModelHelper.subtypeOrSame(x,wpattern.getBoundTypeModel(),debug);
                    break;
                case SUPER:
                    retval=JavaTypeModelHelper.subtypeOrSame(wpattern.getBoundTypeModel(),x,debug);
                    break;
                default:
                    throw new AssertException("Impossible: invalid JavaWildcardBoundsKind");
            }
        }else if (x.isNull()) {
            // null can be object of any type.
            retval=true;
        }else{
            retval=JavaTypeModelHelper.subtypeOrSame(x,pattern,debug);
            if (!retval) {
                // unchecked conversion.
               if (true/*JavaTypeModelHelper.isRowType(pattern)*/) {
                   //JavaTypeModel uncheckedX=JavaTypeModel.erasureConversion(x);
                   retval=JavaTypeModelHelper.subtypeOrSame(pattern,x,debug);
               }
            }
        }
        if (debug) {
            LOG.log(Level.INFO,"match return "+retval);
        }
        return retval;
    }
    
    /**
     * match over list and feel substitution if needed.
     */
    public static boolean match(List<JavaFormalParameterModel> patterns,List<JavaTypeModel> xs,JavaTypeArgumentsSubstitution substitution, boolean debug) throws TermWareException {
        Iterator<JavaFormalParameterModel> pit = patterns.iterator();
        Iterator<JavaTypeModel> xit = xs.iterator();
        boolean inVarArg=false;
        JavaTypeModel varArgPattern=null;
        boolean quit=false;
        boolean retval=true;
        boolean getNextX=true;
        JavaTypeModel x=null;
        while(!quit) {
            if (!inVarArg) {
                if (!pit.hasNext()) {
                    if (xit.hasNext()) {
                        retval=false;
                    }
                    break;
                }else if(!xit.hasNext()) {
                    // check, that it was varArgs,
                    //  if yes - one will associated with empty array
                    retval=(pit.next().getModifiers().isVarArgs());
                    break;
                }
                JavaFormalParameterModel p = pit.next();
                if (p.getModifiers().isVarArgs()) {
                    try {
                        varArgPattern=p.getTypeModel().getReferencedType();
                    }catch(NotSupportedException ex){
                        throw new AssertException("VarArgs parameter must be array");
                    }
                    inVarArg=true;
                }else{
                    if (getNextX) {
                        x = xit.next();
                    }
                    if (!match(p.getTypeModel(),x,substitution,debug)) {
                        retval=false;
                        break;
                    }else{
                        getNextX=true;
                    }
                }
            }else{
                if (!xit.hasNext()) {
                    break;
                }else{
                    if (getNextX) {
                        x = xit.next();
                    }
                    if (!match(varArgPattern,x,substitution,debug)) {
                        if (x.isArray()) {
                            JavaTypeModel x1;
                            try {
                                x1=x.getReferencedType();
                            }catch(NotSupportedException ex){
                                throw new AssertException("getReferencedType on Array is unsupported");
                            }
                            if (match(varArgPattern,x1,substitution,debug)) {
                                // ... matched with array
                                inVarArg=false;
                                getNextX=true;
                                continue;
                            }
                        }
                        if (!pit.hasNext()) {
                            retval=false;
                            break;
                        }else{
                            // just end og var-args.
                            //   try to match with next formal parameter.
                            // This behavious is not supported with JDK5, but can be
                            // potentially usefull for future versions.
                            inVarArg=false;
                            getNextX=false;
                        }
                    }else{
                        getNextX=true;
                    }
                }
            }
        }
        return retval;
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
    
    private static Logger LOG = Logger.getLogger(JavaResolver.class.getName());
    
}
