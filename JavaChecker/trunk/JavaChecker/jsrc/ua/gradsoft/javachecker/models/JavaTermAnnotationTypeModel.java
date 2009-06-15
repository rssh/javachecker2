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
import ua.gradsoft.javachecker.models.expressions.JavaTermNullLiteralExpressionModel;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Model of annotation type which holds term.
 * @author Ruslan Shevchenko
 */
public class JavaTermAnnotationTypeModel extends JavaTermTypeAbstractModel
{
                    
    public JavaTermAnnotationTypeModel(Term modifiers, Term t, JavaPackageModel packageModel, JavaUnitModel unitModel) throws TermWareException
    {
      super(modifiers,t,packageModel,unitModel);
      name_=t.getSubtermAt(NAME_TERM_INDEX).getSubtermAt(0).getString();
      fillModels(t.getSubtermAt(BODY_TERM_INDEX));
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
        

    public JavaTypeModel getSuperClass() throws TermWareException
    {   
        return JavaResolver.resolveJavaLangObject();
    }
    
    
    public List<JavaTypeModel>  getSuperInterfaces() throws TermWareException
    { 
      try {  
        JavaTypeModel retval = JavaResolver.resolveTypeModelFromPackage("Annotation","java.lang.annotation");
        return Collections.singletonList(retval);
      }catch(EntityNotFoundException ex){
          throw new AssertException(ex.getMessage());
      }
    }
    
  
    public Map<String, JavaEnumConstantModel> getEnumConstantModels()  {
        return Collections.emptyMap();
    }
    
    public JavaAnnotationInstanceModel  getDefaultAnnotationInstanceModel() throws TermWareException, EntityNotFoundException
    {
        if (annotationDefaultInstanceModel_==null) {
            annotationDefaultInstanceModel_=new JavaAnnotationDefaultInstanceModel(this);
        }
        return annotationDefaultInstanceModel_;
    }

    public List<JavaConstructorModel>  getConstructorModels()
    { return Collections.emptyList(); }
    
    public boolean hasASTTerm()
    { return true; }
    
    public Term getASTTerm()
    { return t_; }
    
    /**
     * AnnotationTypeDeclarationModel(modifiers,name,[...],context)
     *where AnnotationTypeBodyModel is list,
     */
    public Term getModelTerm() throws TermWareException, EntityNotFoundException
    {
        Term modifiersTerm = getModifiersModel().getModelTerm();
        Term nameTerm=t_.getSubtermAt(0);
        Term typeBodyModel=getMemberModelsList();
        JavaPlaceContext context = JavaPlaceContextFactory.createNewTypeContext(this);
        Term tcontext = TermUtils.createJTerm(context);
        return TermUtils.createTerm("AnnotationTypeModel",modifiersTerm,nameTerm,typeBodyModel,tcontext);
    }
    
       
    public boolean hasTypeParameters()
    { return false; }


    @Override
    public JavaExpressionModel getDefaultInitializerExpression() throws TermWareException, EntityNotFoundException {
        return JavaTermNullLiteralExpressionModel.getNull();
    }


    private void fillModels(Term annotationTypeBody) throws TermWareException
    {      
      Term l = annotationTypeBody.getSubtermAt(0);
      while(!l.isNil()) {          
          Term mb=l.getSubtermAt(0);
          l=l.getSubtermAt(1);
          if (mb.getName().equals("AnnotationTypeMemberDeclaration")) {
              if (mb.getArity()==2) {          
                Term modifiers=mb.getSubtermAt(0);
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
                 Term typeTerm = mb.getSubtermAt(1);
                 Term identifierTerm = mb.getSubtermAt(2);
                 Term defaultValueTerm = TermUtils.createNil();
                 if (mb.getArity()==4) {
                        defaultValueTerm = mb.getSubtermAt(3);
                 }
                 addAnnotationMethodDeclaration(modifiersTerm, typeTerm, identifierTerm, defaultValueTerm);
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
    

    protected void addAnnotationMethodDeclaration(Term modifiers, Term typeTerm, Term identifierTerm, Term defaultValueTerm) throws TermWareException
    {
        JavaTermAnnotationMethodModel jtamm = new JavaTermAnnotationMethodModel(this,modifiers,typeTerm,identifierTerm,defaultValueTerm);     
        String name = identifierTerm.getSubtermAt(0).getString();
        List<JavaMethodModel> mml = Collections.<JavaMethodModel>singletonList(jtamm);
        methodModels_.put(name,mml);
    }
    
    private JavaAnnotationDefaultInstanceModel  annotationDefaultInstanceModel_=null;



    public static final int NAME_TERM_INDEX=0;
    public static final int BODY_TERM_INDEX=1;
    
}
