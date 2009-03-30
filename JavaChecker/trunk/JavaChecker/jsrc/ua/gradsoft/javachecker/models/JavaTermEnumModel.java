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
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.javachecker.models.expressions.JavaTermNullLiteralExpressionModel;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TermWareRuntimeException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Model of Enum definition.
 */
public class JavaTermEnumModel extends JavaTermTypeAbstractModel {
    
    JavaTermEnumModel(Term modifiers, Term t,JavaPackageModel packageModel, JavaUnitModel unitModel) throws TermWareException {
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
    
    /**
     * get map of enum constants.
     */
    public Map<String, JavaEnumConstantModel> getEnumConstantModels()  {
        return enumConstants_;
    }
    
    /**
     *@return null
     */    
    public JavaAnnotationInstanceModel getDefaultAnnotationInstanceModel()  
    {
        return null;
    }    

    public JavaTermExpressionModel getDefaultInitializerExpression() throws TermWareException
    {
      Term nlTerm = TermUtils.createTerm("NullLiteral");
      return new JavaTermNullLiteralExpressionModel(nlTerm,null,this);
    }

    
    /**
     * EnumModel(modifiers,identifier,superInterfaces, enumConstantsList,membersList,context)
     */
    public Term getModelTerm() throws TermWareException, EntityNotFoundException
    {
        Term modifiersTerm=getModifiersModel().getModelTerm();
        Term identifierTerm=t_.getSubtermAt(IDENTIFIER_TERM_INDEX);
        List<JavaTypeModel> si = getSuperInterfaces();
        Term implementsList = t_.getSubtermAt(IMPLEMENTS_TERM_INDEX);
        if (implementsList.isComplexTerm()) {
            implementsList=implementsList.getSubtermAt(0);
        }
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
        Term membersList = getOtherMemberModelsList();
        JavaPlaceContext ctx = JavaPlaceContextFactory.createNewTypeContext(this);
        Term tctx=TermUtils.createJTerm(ctx);
        return TermUtils.createTerm("EnumModel",modifiersTerm,
                identifierTerm, implementsListModel,
                enumConstantsList, membersList,tctx
                );
    }
    
    private Term getOtherMemberModelsList() throws TermWareException, EntityNotFoundException
    {
       Term retval=TermUtils.createNil();       
       if (initializers_!=null) {
          for(JavaInitializerModel x: initializers_) {
              retval=TermUtils.createTerm("cons",x.getModelTerm(),retval);
          }          
       }   
       if (constructors_!=null) {
           for(JavaConstructorModel x: constructors_) {
               retval=TermUtils.createTerm("cons",x.getModelTerm(),retval);
           }
       }
       if (methodModels_!=null) {
           for(Map.Entry<String,List<JavaMethodModel>> e: methodModels_.entrySet()) {
               for(JavaMethodModel x: e.getValue()) {
                   retval=TermUtils.createTerm("cons",x.getModelTerm(),retval);
               }
           }
       }
       if (fieldModels_!=null) {
           for(Map.Entry<String, JavaMemberVariableModel> e:fieldModels_.entrySet()) {
               JavaMemberVariableModel x = e.getValue();
               if (! (x instanceof JavaEnumConstantModel)) {
                  retval=TermUtils.createTerm("cons",x.getModelTerm(),retval);
               }
           }
       }
       if (nestedTypes_!=null) {
           for(Map.Entry<String,JavaTypeModel> e: nestedTypes_.entrySet()) {
              JavaTypeModel x = e.getValue();
              if (!x.isAnonimous() && !x.isLocal()) {
                  retval=TermUtils.createTerm("cons",x.getModelTerm(),retval);
              }
           }
       }
       retval=TermUtils.reverseListTerm(retval);
       return retval;
    }

    
    private void build(Term t) throws TermWareException {
        //System.out.println("JavaTermEnumModel::build:"+TermHelper.termToString(t));
        
        Term identifierTerm=t.getSubtermAt(IDENTIFIER_TERM_INDEX);
        Term implementsList=t.getSubtermAt(IMPLEMENTS_TERM_INDEX);
        if (implementsList.isComplexTerm()) {
            implementsList=implementsList.getSubtermAt(0);
        }
        while(!implementsList.isNil()) {
            Term classOrInterfaceTerm = implementsList.getSubtermAt(0);
            addSuperInterface(classOrInterfaceTerm);            
            implementsList=implementsList.getSubtermAt(1);
        }
        
        Term enumBody=t.getSubtermAt(ENUMBODY_TERM_INDEX);
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
                        addInitializer(et.getSubtermAt(0));
                    }else{
                        Term modifiers=et.getSubtermAt(0);
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

    
    public static final int  IDENTIFIER_TERM_INDEX=0;
    public static final int  IMPLEMENTS_TERM_INDEX=1;
    public static final int  ENUMBODY_TERM_INDEX=2;
    
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
    
    public JavaTermModifiersModel getModifiers()
    { return JavaModelConstants.PUBLIC_STATIC_MODIFIERS; }
    
    public List<JavaTypeVariableAbstractModel>  getTypeParameters() 
    { return Collections.emptyList(); }
 
    public JavaTypeModel  getResultType() 
    { return new JavaArrayTypeModel(JavaTermEnumModel.this,null); }
    
    public List<JavaTypeModel> getFormalParametersTypes() 
    { return Collections.emptyList(); }
            
    public List<JavaFormalParameterModel> getFormalParametersList()
    { return Collections.emptyList(); }
    
    public Map<String, JavaFormalParameterModel>  getFormalParametersMap() 
    { return Collections.emptyMap(); }
    
    public List<JavaTypeModel>  getThrowsList()
    { return Collections.emptyList(); }
    
    public boolean canCheck()
    { return false; }
    
    public boolean check()
    { return true; }
    
    public boolean         isSupportBlockModel()
    { return false; }
    
    public JavaTopLevelBlockModel  getTopLevelBlockModel() throws NotSupportedException
    { throw new NotSupportedException(); }
    
    public Map<String,JavaAnnotationInstanceModel>  getAnnotationsMap()
    { return Collections.emptyMap(); }
    
    public boolean hasDefaultValue()
    { return false; }
    
    public JavaExpressionModel  getDefaultValue() throws NotSupportedException
    { throw new NotSupportedException(); }
    
    public boolean isSynthetic()
    { return true; }
    
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
      Term fp = TermUtils.createTerm("FormalParameters",ofp);
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
        parameter_=new JavaTermFormalParameterModel(TermUtils.createTerm("Modifiers",TermUtils.createInt(0),TermUtils.createNil()),
                                                "name", 
                                                JavaResolver.resolveTypeModelFromPackage("String","java.lang"), this, 0);
      }catch(EntityNotFoundException ex){
          throw new TermWareRuntimeException(ex);
      }
    }
    
    public String getName()
    { return "valueOf"; }
    
    public JavaTermModifiersModel getModifiers()
    { return JavaModelConstants.PUBLIC_STATIC_MODIFIERS; }
    
    public List<JavaTypeVariableAbstractModel>  getTypeParameters() 
    { return Collections.emptyList(); }
 
    public JavaTypeModel  getResultType() 
    { return JavaTermEnumModel.this; }
    
    public List<JavaTypeModel> getFormalParametersTypes() throws TermWareException
    { 
      return Collections.singletonList(parameter_.getType());
    }
            
    public List<JavaFormalParameterModel> getFormalParametersList()
    { return Collections.<JavaFormalParameterModel>singletonList(parameter_); }
    
    public Map<String, JavaFormalParameterModel>  getFormalParametersMap() 
    { return Collections.<String,JavaFormalParameterModel>singletonMap(parameter_.getName(),parameter_); }
    
    public List<JavaTypeModel>  getThrowsList()
    { return Collections.emptyList(); }
    
    public Map<String,JavaAnnotationInstanceModel>  getAnnotationsMap()
    { return Collections.emptyMap(); }
            
    public boolean canCheck()
    { return false; }
    
    public boolean check()
    { return true; }
    
    public boolean         isSupportBlockModel()
    { return false; }
    
    public JavaTopLevelBlockModel  getTopLevelBlockModel() throws NotSupportedException
    { throw new NotSupportedException(); }
    
    public boolean hasDefaultValue()
    { return false; }
    
    public JavaExpressionModel  getDefaultValue() throws NotSupportedException
    { throw new NotSupportedException(); }
    
    public boolean isSynthetic()
    { return true; }


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
      //vid = TermUtils.createTerm("cons",vid,TermUtils.createNil());
      vid = TermUtils.createTerm("VariableDeclaratorId",vid,TermUtils.createInt(0));
      //Term cls = TermUtils.createNil();
      Term cls = TermUtils.createTerm("Identifier",TermUtils.createString("String"));
      //cls = TermUtils.createTerm("cons",cls1,cls);
      //cls = TermUtils.createTerm("ClassOrInterfaceDeclaration",cls);
      Term fpm = TermUtils.createTerm("Modifiers",TermUtils.createInt(0),TermUtils.createNil());
      Term fp0 = TermUtils.createTerm("FormalParameter",fpm,cls,vid);
      Term ofp = TermUtils.createTerm("cons",fp0,TermUtils.createNil());
      Term fp = TermUtils.createTerm("FormalParameters",ofp);
      Term tht = TermUtils.createNil();
      Term blockModelTerm = TermUtils.createNil();
      JavaPlaceContext ctx = JavaPlaceContextFactory.createNewMethodContext(this);
      Term tctx = TermUtils.createJTerm(ctx);
      Term retval = TermUtils.createTerm("MethodModel",mt,tpt,rt,identifier,fp,tht,blockModelTerm,tctx);
      return retval;
    }    



    private JavaTermFormalParameterModel parameter_;
    
    
  }

    
}
