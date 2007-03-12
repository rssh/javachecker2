/*
 * JavaTermAnnotationTypeModel.java
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Model of annotation type which holds term.
 *TODO: build body. now body is not build at all.
 * @author Ruslan Shevchenko
 */
public class JavaTermAnnotationTypeModel extends JavaTermTypeAbstractModel
{
            
    public JavaTermAnnotationTypeModel(int modifiers, Term t, JavaPackageModel packageModel, JavaUnitModel unitModel) throws TermWareException
    {
      super(modifiers,t,packageModel,unitModel);
      name_=t.getSubtermAt(0).getSubtermAt(0).getString();
      fillModels(t.getSubtermAt(1));
    }
    
    public boolean isClass()
    {
      return false;  
    }

    public boolean isInterface()
    {
      return false;  
    }
    
    
    public boolean isEnum()
    {
      return false;  
    }
    
    public boolean isAnnotationType()
    {
      return true;  
    }
        
    public boolean hasMethodModels()
    { return false; }
    
    public JavaTypeModel getSuperClass() throws TermWareException
    {
      try {  
        return JavaResolver.resolveTypeModelFromPackage("Annotation","java.lang"); 
      }catch(EntityNotFoundException ex){
          throw new AssertException(ex.getMessage());
      }
    }
    
    
    public List<JavaTypeModel>  getSuperInterfaces() 
    { return Collections.emptyList(); }
    
    
    public Map<String, List<JavaMethodModel>> getMethodModels() throws NotSupportedException
    {
      throw new NotSupportedException();
    }

    public Map<String, JavaEnumConstantModel> getEnumConstantModels() throws NotSupportedException {
        throw new NotSupportedException();
    }

    public boolean hasASTTerm()
    { return true; }
    
    public Term getASTTerm()
    { return t_; }
    
    /**
     * AnnotationTypeDeclarationModel(name,[...],context)
     *where AnnotationTypeBodyModel is list,
     */
    public Term getModelTerm() throws TermWareException, EntityNotFoundException
    {
        Term nameTerm=t_.getSubtermAt(0);
        Term typeBodyModel=getMemberModelsList();
        JavaPlaceContext context = JavaPlaceContextFactory.createNewTypeContext(this);
        Term tcontext = TermUtils.createJTerm(context);
        return TermUtils.createTerm("AnnotationTypeModel",nameTerm,typeBodyModel,tcontext);
    }
    
       
    public boolean hasTypeParameters()
    { return false; }

    private void fillModels(Term annotationTypeBody) throws TermWareException
    {      
      Term l = annotationTypeBody.getSubtermAt(0);
      while(!l.isNil()) {          
          Term mb=l.getSubtermAt(0);
          l=l.getSubtermAt(1);
          if (mb.getName().equals("AnnotationTypeMemberDeclaration")) {
              if (mb.getArity()==2) {          
                Term modifiersTerm=mb.getSubtermAt(0);
                int modifiers=modifiersTerm.getSubtermAt(0).getInt();
                Term declaration = mb.getSubtermAt(1);
                if (declaration.getName().equals("ClassOrInterfaceDeclaration")) {
                      addClassOrInterfaceDeclaration(modifiers,declaration);
                }else if(declaration.getName().equals("EnumDeclaration")) {
                      addEnumDeclaration(modifiers,declaration);
                }else if(declaration.getName().equals("FieldDeclaration")) {
                      addFieldDeclaration(modifiers,declaration);
                }else if(declaration.getName().equals("AnnotationTypeDeclaration")) {
                      addAnnotationTypeDeclaration(modifiers,declaration);
                }else{
                      throw new AssertException("Unknown declaration:"+TermHelper.termToString(declaration));
                 }
              }else if(mb.getArity()==3 || mb.getArity()==4){
                 Term modifiersTerm=mb.getSubtermAt(0);
                 int modifiers=modifiersTerm.getSubtermAt(0).getInt();                
                 Term typeTerm = mb.getSubtermAt(1);
                 Term identifierTerm = mb.getSubtermAt(2);
                 Term defaultValueTerm = TermUtils.createNil();
                 if (mb.getArity()==4) {
                        defaultValueTerm = mb.getSubtermAt(3);
                 }
                 addAnnotationMethodDeclaration(modifiers, typeTerm, identifierTerm, defaultValueTerm);
              }else if (mb.getArity()==0) {
                   // skip empty declaration   
              }else{
                  throw new AssertException("arity of AnnotationTypeMemberDeclaration must be 0, 2, 3 or 4 in "+TermHelper.termToString(mb));
              }              
          }else{
              throw new AssertException("AnnotationTypeMemberDeclaration expected, have "+TermHelper.termToString(mb));
          }
      }
    }
    
    protected void addAnnotationTypeDeclaration(int modifiers, Term t) throws TermWareException
    {
       JavaTermAnnotationTypeModel tm = new JavaTermAnnotationTypeModel(modifiers,t,getPackageModel(),getUnitModel());
       addNestedType(tm.getName(),tm);
    }

    protected void addAnnotationMethodDeclaration(int modifiers, Term typeTerm, Term identifierTerm, Term defaultValueTerm)
    {
        JavaTermAnnotationMethodModel jtamm = new JavaTermAnnotationMethodModel(this,modifiers,typeTerm,identifierTerm,defaultValueTerm);
        JavaMethodModel mm=jtamm;
        List<JavaMethodModel> ml=Collections.singletonList(mm);
        methodModels_.put(identifierTerm.getSubtermAt(0).getString(),ml);
    }
    
}
