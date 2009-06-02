/*
 * JavaArumentBoundTypeVariableModel.java
 *
 * Copyright (c) 2004-2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.Map;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Member value, bound by type argument
 * @author Ruslan Shevchenko
 */
public class JavaTypeArgumentBoundMemberVariableModel extends JavaMemberVariableModel
{
    
    
    /** Creates a new instance of JavaArgumentBoundTypeVariableModel */
    public JavaTypeArgumentBoundMemberVariableModel(JavaTypeArgumentBoundTypeModel newOwner, JavaMemberVariableModel origin) 
    {
       origin_=origin;
       owner_=newOwner;
    }

    
    public String getName()
    { return origin_.getName(); }
    
    public JavaTypeModel  getOwnerType()
    { 
       return owner_;  
    }
    
    /**
     *@return null
     */
    public JavaTopLevelBlockOwnerModel getTopLevelBlockOwner()
    {
       return null; 
    }
    
    public JavaTypeModel  getType() throws TermWareException, EntityNotFoundException
    {
      JavaTypeModel originTypeModel = origin_.getType();
      //return owner_.substituteTypeParameters(originTypeModel);
      return owner_.getSubstitution().substitute(originTypeModel);
    }

    public Map<String,JavaAnnotationInstanceModel> getAnnotationsMap() throws TermWareException
    { return origin_.getAnnotationsMap(); }
    
    public JavaModifiersModel  getModifiers()
    {
        return origin_.getModifiers();
    }
    
    public boolean isSupportInitializerExpression()
    { return origin_.isSupportInitializerExpression(); }

    public boolean isConstant() throws TermWareException, EntityNotFoundException
    {
      return origin_.isConstant();
    }
    
    public JavaExpressionModel getInitializerExpression() throws TermWareException, EntityNotFoundException
    {
       JavaExpressionModel oexpr = origin_.getInitializerExpression();
       if (oexpr==null) {
           return null;
       }else{
           return new JavaTypeArgumentBoundExpressionModel(oexpr,owner_);
       }
    }
    
    /**
     * TypeArgumentBoundMemberVariableModel(originModelTerm,ownerModelTerm)
     */
    public Term getModelTerm() throws TermWareException, EntityNotFoundException
    {
        Term originModelTerm = origin_.getModelTerm();
        Term ownerModelTerm = owner_.getModelTerm();
        Term retval=TermUtils.createTerm("TypeArgumentBoundMemberVariableModel",
                                          originModelTerm,ownerModelTerm);
        return retval;
    }
    
    private JavaMemberVariableModel origin_;
    private JavaTypeArgumentBoundTypeModel      owner_;
    
}
