/*
 * JavaTermEnumModel.java
 *
 * Copyright (c) 2004-2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.Main;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TermWareRuntimeException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Model of Enum definition.
 *TODO: implement full model of enum constant (with value and class-body).
 */
public class JavaTermEnumModel extends JavaTermTypeAbstractModel {
    
    JavaTermEnumModel(int modifiers, Term t,JavaPackageModel packageModel, JavaUnitModel unitModel) throws TermWareException {
        super(modifiers,t,packageModel,unitModel);
        enumConstants_=new TreeMap<String,JavaEnumConstantModel>();
        build(t);
        // TODO: fix case, when valueof is overloaded in user code.
        ValuesMethodModel vmm = new ValuesMethodModel();
        methodModels_.put(vmm.getName(),Collections.<JavaMethodModel>singletonList(vmm));
        ValueOfMethodModel vomm = new ValueOfMethodModel();
        methodModels_.put(vomm.getName(),Collections.<JavaMethodModel>singletonList(vomm));
    }
    
    public boolean isClass()  { return false; }
    
    public boolean isInterface()  { return false; }
    
    public boolean isEnum() { return true; }
    
    public boolean isAnnotationType() { return false; }
    
    public boolean hasTypeParameters() { return false; }
    
        
    
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
    public Term getModelTerm() throws TermWareException, EntityNotFoundException
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
      try {   
        return JavaResolver.resolveTypeModelByFullClassName("java.lang.Enum");
      }catch(EntityNotFoundException ex){
          throw new AssertException("java.lang.Enum is not found");
      }
    }
      
    
    private Map<String,JavaEnumConstantModel>  enumConstants_;

    
    private static final int QENUM_IDENTIFIER_INDEX_=0;
    private static final int IMPLEMENTS_INDEX_=1;
    private static final int ENUMBODY_INDEX_=2;
    
public final class ValuesMethodModel extends JavaMethodModel 
{    
    /**
     * Creates a new instance of JavaMethodModel
     */
    public ValuesMethodModel() 
    {
      super(JavaTermEnumModel.this);
    }
    
    public String getName()
    { return "values"; }
    
    public JavaModifiersModel getModifiers()
    { return JavaModelConstants.PUBLIC_STATIC_MODIFIERS; }
    
    public List<JavaTypeVariableAbstractModel>  getTypeParameters() 
    { return Collections.emptyList(); }
 
    public JavaTypeModel  getResultType() 
    { return new JavaArrayTypeModel(JavaTermEnumModel.this); }
    
    public List<JavaTypeModel> getFormalParametersTypes() 
    { return Collections.emptyList(); }
            
    public List<JavaFormalParameterModel> getFormalParametersList()
    { return Collections.emptyList(); }
    
    public Map<String,JavaFormalParameterModel>  getFormalParametersMap() 
    { return Collections.emptyMap(); }
            
    public boolean canCheck()
    { return false; }
    
    public boolean check()
    { return true; }
    
    public boolean         isSupportBlockModel()
    { return false; }
    
    public JavaTopLevelBlockModel  getTopLevelBlockModel() throws NotSupportedException
    { throw new NotSupportedException(); }
    
    /**
     * MethodModel(modifiers,typeParameters,ResultType,name,formalParameters,throws,block,context)
     */
    public Term getModelTerm() throws TermWareException, EntityNotFoundException
    {
      Term mt = getModifiers().getModelTerm();
      Term tpt = TermUtils.createNil();
      JavaTypeModel rtm = getResultType();
      Term rt = TermUtils.createTerm("TypeRef",rtm.getShortNameAsTerm(),TermUtils.createJTerm(rtm));
      Term identifier = TermUtils.createIdentifier(getName());
      Term ofp = TermUtils.createNil();
      Term fp = ofp; /*buildFormalParametersModelTerm(formalParametersMap_,ofp);*/
      Term tht = TermUtils.createNil();
      Term blockModelTerm = TermUtils.createNil();
      JavaPlaceContext ctx = JavaPlaceContextFactory.createNewMethodContext(this);
      Term tctx = TermUtils.createJTerm(ctx);
      Term retval = TermUtils.createTerm("MethodModel",mt,tpt,rt,identifier,fp,tht,blockModelTerm,tctx);
      return retval;
    }    
    
  }

public final class ValueOfMethodModel extends JavaMethodModel 
{    
    /**
     * Creates a new instance of JavaMethodModel
     */
    public ValueOfMethodModel() throws TermWareException
    {
      super(JavaTermEnumModel.this);
      try {
        parameter_=new JavaFormalParameterModel(0, "name", JavaResolver.resolveTypeModelFromPackage("String","java.lang"), this, 0);
      }catch(EntityNotFoundException ex){
          throw new TermWareRuntimeException(ex);
      }
    }
    
    public String getName()
    { return "valueOf"; }
    
    public JavaModifiersModel getModifiers()
    { return JavaModelConstants.PUBLIC_STATIC_MODIFIERS; }
    
    public List<JavaTypeVariableAbstractModel>  getTypeParameters() 
    { return Collections.emptyList(); }
 
    public JavaTypeModel  getResultType() 
    { return JavaTermEnumModel.this; }
    
    public List<JavaTypeModel> getFormalParametersTypes() throws TermWareException
    { 
      return Collections.singletonList(parameter_.getTypeModel());
    }
            
    public List<JavaFormalParameterModel> getFormalParametersList()
    { return Collections.singletonList(parameter_); }
    
    public Map<String,JavaFormalParameterModel>  getFormalParametersMap() 
    { return Collections.singletonMap(parameter_.getName(),parameter_); }
            
    public boolean canCheck()
    { return false; }
    
    public boolean check()
    { return true; }
    
    public boolean         isSupportBlockModel()
    { return false; }
    
    public JavaTopLevelBlockModel  getTopLevelBlockModel() throws NotSupportedException
    { throw new NotSupportedException(); }
    
    /**
     * MethodModel(modifiers,typeParameters,ResultType,name,formalParameters,throws,block,context)
     */
    public Term getModelTerm() throws TermWareException, EntityNotFoundException
    {
      Term mt = getModifiers().getModelTerm();
      Term tpt = TermUtils.createNil();
      JavaTypeModel rtm = getResultType();
      Term rt = TermUtils.createTerm("TypeRef",rtm.getShortNameAsTerm(),TermUtils.createJTerm(rtm));
      Term identifier = TermUtils.createIdentifier(getName());
      Term vid = TermUtils.createIdentifier("name");
      vid = TermUtils.createTerm("cons",vid,TermUtils.createNil());
      vid = TermUtils.createTerm("VariableDeclaratorId",vid);
      Term cls = TermUtils.createNil();
      Term cls1 = TermUtils.createTerm("Identifier",TermUtils.createString("String"));
      cls = TermUtils.createTerm("cons",cls1,cls);
      cls = TermUtils.createTerm("ClassOrInterfaceDeclaration",cls);
      Term fp0 = TermUtils.createTerm("FormalParameter",TermUtils.createInt(0),cls,vid);
      Term ofp = TermUtils.createTerm("cons",fp0,TermUtils.createNil());
      Term fp = TermUtils.createTerm("FormalParameters",ofp);
      Term tht = TermUtils.createNil();
      Term blockModelTerm = TermUtils.createNil();
      JavaPlaceContext ctx = JavaPlaceContextFactory.createNewMethodContext(this);
      Term tctx = TermUtils.createJTerm(ctx);
      Term retval = TermUtils.createTerm("MethodModel",mt,tpt,rt,identifier,fp,tht,blockModelTerm,tctx);
      return retval;
    }    
    
    private JavaFormalParameterModel parameter_;
    
    
  }

    
}
