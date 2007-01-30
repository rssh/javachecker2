/*
 * JavaTermEnumModel.java
 *
 * Copyright (c) 2004-2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
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
        enumConstants_=new TreeMap<String,JavaEnumConstantModel>();
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
        return getConstantNames().contains(name);
    }
    
    public Set<String>  getConstantNames() {
        return enumConstants_.keySet();
    }
    
    public Map<String, JavaEnumConstantModel> getEnumConstantModels()  {
        return enumConstants_;
    }
    
    /**
     * EnumModel(identifier,superInterfaces, EnumConstantsList,membersList,context)
     */
    public Term getModelTerm() throws TermWareException
    {
        Term identifierTerm=t_.getSubtermAt(QENUM_IDENTIFIER_INDEX_);
        List<JavaTypeModel> si = getSuperInterfaces();
        Term implementsList = t_.getSubtermAt(IMPLEMENTS_INDEX_);
        Iterator<JavaTypeModel> siit = si.iterator();
        Term implementsListModel = TermUtils.createNil();
        while(!implementsList.isNil()) {
            Term siname = implementsList.getSubtermAt(0);
            implementsList=implementsList.getSubtermAt(1);
            JavaTypeModel tm = siit.next();
            Term ttm = TermUtils.createJTerm(tm);
            Term typeRefTerm = TermUtils.createTerm("TypeRef",siname,ttm);
            implementsListModel = TermUtils.createTerm("cons",typeRefTerm,implementsListModel);            
        }
        implementsListModel = TermUtils.reverseListTerm(implementsListModel);
        Term enumConstantsList = TermUtils.createNil();
        for(Map.Entry<String,JavaEnumConstantModel> e:enumConstants_.entrySet()) {
            Term enumConstantModelTerm = e.getValue().getModelTerm();
            enumConstantsList = TermUtils.createTerm("cons",enumConstantModelTerm,enumConstantsList);
        }
        enumConstantsList = TermUtils.reverseListTerm(enumConstantsList);
        Term membersList = getMemberModelsList();
        JavaPlaceContext ctx = JavaPlaceContextFactory.createNewTypeContext(this);
        Term tctx=TermUtils.createJTerm(ctx);
        return TermUtils.createTerm("EnumDeclarationModel",
                identifierTerm, implementsListModel,
                enumConstantsList, membersList,tctx
                );
    }
    
    private void build(Term t) throws TermWareException {
        //System.out.println("JavaTermEnumModel::build:"+TermHelper.termToString(t));
        
        Term identifierTerm=t.getSubtermAt(QENUM_IDENTIFIER_INDEX_);
        Term implementsList=t.getSubtermAt(IMPLEMENTS_INDEX_);
        while(!implementsList.isNil()) {
            Term classOrInterfaceTerm = implementsList.getSubtermAt(0);
            addSuperInterface(classOrInterfaceTerm);
            implementsList=implementsList.getSubtermAt(1);
        }
        
        Term enumBody=t.getSubtermAt(ENUMBODY_INDEX_);
        name_=identifierTerm.getSubtermAt(0).getString();
        Term curr=enumBody.getSubtermAt(0);
        while(!curr.isNil()) {
            Term et=curr.getSubtermAt(0);
            curr=curr.getSubtermAt(1);
            if (et.getName().equals("EnumConstant")) {
                JavaTermEnumConstantModel ec = new JavaTermEnumConstantModel(et,this);
                enumConstants_.put(ec.getName(),ec);
                fieldModels_.put(ec.getName(),ec);
            }else if(et.getName().equals("ClassOrInterfaceBodyDeclaration")){                
                if (et.getArity()>0) {
                    if (et.getSubtermAt(0).getName().equals("Initializer")) {
                        addInitializer(et);
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
    
    public  JavaTypeModel getSuperClass() throws TermWareException
    {
      return JavaResolver.resolveJavaLangObject();  
    }
      
    
    private Map<String,JavaEnumConstantModel>  enumConstants_;

    
    private static final int QENUM_IDENTIFIER_INDEX_=0;
    private static final int IMPLEMENTS_INDEX_=1;
    private static final int ENUMBODY_INDEX_=2;
    
    
    
}
