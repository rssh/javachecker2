/*
 * JavaTermEnumModel.java
 *
 * Copyright (c) 2004-2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.Set;
import java.util.TreeSet;
import ua.gradsoft.javachecker.Main;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Model of Enum definition.
 *TODO: implement full model of enum constant (with value and class-body).
 */
public class JavaTermEnumModel extends JavaTermTypeAbstractModel {
    
    JavaTermEnumModel(int modifiers, Term t,JavaPackageModel packageModel) throws TermWareException {
        super(modifiers,t,packageModel);
        constantNames_=new TreeSet<String>();
        build(t);
    }
    
    public boolean isClass()  { return false; }
    
    public boolean isInterface()  { return false; }
    
    public boolean isEnum() { return true; }
    
    public boolean isAnnotationType() { return false; }
    
    public boolean hasTypeParameters() { return false; }
    
    
    public boolean check() throws TermWareException {
        boolean retval=true;
        retval=checkNamePatterns();
        if (super.canCheck()) {
            retval &= super.check();
        }
        return retval;
    }
    
    private boolean checkNamePatterns() throws TermWareException {
        boolean retval=true;
        if (Main.getFacts().isCheckEnabled("EnumConstantNamePatterns")) {
            for(String s: getConstantNames()) {
                if (!s.matches(Main.getFacts().getEnumConstantNamePattern())) {
                    Main.getFacts().violationDiscovered("EnumConstantNamePatterns","enum constant does not match pattern",this.getTerm());
                    retval=false;
                }
            }
        }
        return retval;
    }
    
    public boolean  containsConstant(String name) {
        return constantNames_.contains(name);
    }
    
    public Set<String>  getConstantNames() {
        return constantNames_;
    }
    
    private void build(Term t) throws TermWareException {
        //System.out.println("JavaTermEnumModel::build:"+TermHelper.termToString(t));
        
        Term identifierTerm=t.getSubtermAt(QENUM_IDENTIFIER_INDEX_);
        Term implementsList=t.getSubtermAt(IMPLEMENTS_INDEX_);
        Term enumBody=t.getSubtermAt(ENUMBODY_INDEX_);
        name_=identifierTerm.getSubtermAt(0).getString();
        Term curr=enumBody.getSubtermAt(0);
        while(!curr.isNil()) {
            Term et=curr.getSubtermAt(0);
            curr=curr.getSubtermAt(1);
            if (et.getName().equals("EnumConstant")) {
                constantNames_.add(et.getSubtermAt(0).getSubtermAt(0).getString());
            }else if(et.getName().equals("ClassOrInterfaceBodyDeclaration")){
                System.out.println("et="+TermHelper.termToString(et));
                if (et.getArity()>0) {
                    if (et.getSubtermAt(0).getName().equals("Initializer")) {
                        Term initializer=et.getSubtermAt(0);
                        if (initializer.getArity()==1) {
                            addInitializer(0,initializer.getSubtermAt(0));
                        }else if (initializer.getArity()==2) {
                            addInitializer(initializer.getSubtermAt(0).getInt(),initializer.getSubtermAt(1));
                        }else{
                            throw new AssertException("arity of initializer must be 1 or 2");
                        }
                    }else{
                        int modifiers=et.getSubtermAt(0).getSubtermAt(0).getInt();
                        Term declaration=et.getSubtermAt(1);
                        if (declaration.getName().equals("ClassOrInterfaceDeclaration")) {
                            addClassOrInterfaceDeclaration(modifiers,declaration);
                        }else if(declaration.getName().equals("EnumDeclaration")) {
                            addEnumDeclaration(modifiers,declaration);
                        }else if(declaration.getName().equals("ConstructorDeclaration")) {
                            addConstructorDeclaration(modifiers,declaration);
                        }else if(declaration.getName().equals("FieldDeclaration")) {
                            addFieldDeclaration(modifiers,declaration);
                        }else if(declaration.getName().equals("MethodDeclaration")) {
                            addMethodDeclaration(modifiers,declaration);
                        }else{
                            throw new AssertException("Unknown declaration:"+TermHelper.termToString(declaration));
                        }
                    }
                }
            }else{
                throw new AssertException("Incorrect element of EnumBody:"+TermHelper.termToString(et));
            }
        }
    }
    
    
    private Set<String>  constantNames_;
    
    private static final int QENUM_IDENTIFIER_INDEX_=0;
    private static final int IMPLEMENTS_INDEX_=1;
    private static final int ENUMBODY_INDEX_=2;
    
    
    
}
