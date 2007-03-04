/*
 * JavaClassTypeVariableModel.java
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.LinkedList;
import java.util.List;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *
 * @author Ruslan Shevchenko
 */
public class JavaClassTypeVariableModel extends JavaTypeVariableAbstractModel
{
    
    /** Creates a new instance of JavaClassTypeVariableModel */
    public JavaClassTypeVariableModel(TypeVariable<?> typeVariable) {
        typeVariable_=typeVariable;
    }
    
    public String getName()
    {
      return typeVariable_.getName();  
    }
    
    public JavaModifiersModel  getModifiersModel()
    {
      return JavaModelConstants.PUBLIC_MODIFIERS;  
    }
    
    public List<JavaTypeModel> getBounds() throws TermWareException
    {
        List<JavaTypeModel> retval=new LinkedList<JavaTypeModel>();
        Type[] bounds = typeVariable_.getBounds();
        for(int i=0; i<bounds.length; ++i) {
            retval.add(JavaClassTypeModel.createTypeModel(bounds[i]));
        }
        return retval;
    }

    public boolean check()
    {
       /* for now: do nothing */
        return true;
    }
    
    /**
     * TypeParameter(Identifier(name),boundsModelTerms)
     */
    public Term getModelTerm() throws TermWareException, EntityNotFoundException
    {        
        Term idt = TermUtils.createIdentifier(typeVariable_.getName());
        Term boundsModelTerm=JavaTypeModelHelper.createModelTermList(getBounds());
        Term typeParameter=TermUtils.createTerm("TypeParameter",idt,boundsModelTerm);
        return typeParameter;
    }

    
    private TypeVariable<?> typeVariable_;
    
}