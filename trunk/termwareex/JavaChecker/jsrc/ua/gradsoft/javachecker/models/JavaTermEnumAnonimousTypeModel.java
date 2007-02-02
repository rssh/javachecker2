/*
 * JavaTermEnumAnonimousTypeModel.java
 *
 * Created on п'€тниц€, 19, с≥чн€ 2007, 5:02
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.Map;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Model for type, which represent anonimous subtype of enum.
 * @author Ruslan Shevchenko
 */
public class JavaTermEnumAnonimousTypeModel extends JavaTermTypeAbstractModel
{
        

    public boolean isAnnotationType() {
        return false;
    }
        
    public boolean isClass() {
        return false;
    }
    
    public boolean isInterface() {
        return false;
    }

    public boolean isEnum() {
        return true;
    }
    
    
    public JavaTermEnumAnonimousTypeModel(String constantName,Term body,JavaTermEnumModel owner) throws TermWareException
    {
      super(0,body,owner.getPackageModel(),owner.getUnitModel());  
      constantName_=constantName;        
      fillModels(body);
      superClassTerm_ = owner.getShortNameAsTerm();
      resolvedSuperClass_ = owner;
      setParentType(owner);
      anonimousIndexInParent_=owner.nextAnonimousTypeIndex();        
      name_="$"+anonimousIndexInParent_;
      owner.addNestedType(name_,this);  
    }

    public boolean hasTypeParameters() {
        return false;
    }        


    public Map<String, JavaEnumConstantModel> getEnumConstantModels() throws NotSupportedException {
        return getEnclosedType().getEnumConstantModels();
    }
    
    /**
     * EnumAnonimousTypeModel(identifier,membersList,context)
     */
    public Term getModelTerm() throws TermWareException
    {
        Term identifierName=TermUtils.createIdentifier(constantName_);
        Term membersListModel=getMemberModelsList();
        JavaPlaceContext ctx=JavaPlaceContextFactory.createNewTypeContext(this);
        Term tctx=TermUtils.createJTerm(ctx);
        return TermUtils.createTerm("EnumAnonimousTypeModel",identifierName,membersListModel,tctx);
    }
    
    private void fillModels(Term t) throws TermWareException
    {
     Term membersList = t.getSubtermAt(0);
      
       while(!membersList.isNil()) {
            if (membersList.getName().equals("cons")) {
                Term declaration=membersList.getSubtermAt(0);
                if (!declaration.getName().equals("ClassOrInterfaceBodyDeclaration")) {
                    throw new AssertException("ClassOrInterfaceBodyDeclaration expected:"+TermHelper.termToString(declaration));
                }
                if (declaration.getArity() > 0) {
                    Term st=declaration.getSubtermAt(0);
                    if (st.getName().equals("Initializer")) {
                        addInitializer(st);
                    }else{
                        int modifiers=declaration.getSubtermAt(0).getSubtermAt(0).getInt();
                        declaration=declaration.getSubtermAt(1);
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
                }else{
                    // skip empty declaration, i. e. do nothing.
                }
                membersList=membersList.getSubtermAt(1);
            }else{
                throw new AssertException("membersList must be a list term");
            }
       }
         
    }
    
           
    private String constantName_;
    //private JavaTermEnumModel owner_;
    private int anonimousIndexInParent_;
    
    
}
