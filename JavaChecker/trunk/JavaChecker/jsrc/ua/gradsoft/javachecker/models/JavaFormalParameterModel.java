/*
 * JavaFormalParameterModel.java
 *
 * Created on May 3, 2007, 2:20 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ua.gradsoft.javachecker.models;

import java.util.Map;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.attributes.AttributedEntity;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Model for formal parameter
 * @author rssh
 */
public abstract class JavaFormalParameterModel implements JavaVariableModel
{
    
    public abstract JavaModifiersModel getModifiers();
    
    
    public abstract String getName();
  
    
    public JavaVariableKind getKind()
    { return JavaVariableKind.FORMAL_PARAMETER; }
    
    public abstract JavaTypeModel getType() throws TermWareException, EntityNotFoundException;
               
    public abstract JavaTopLevelBlockOwnerModel  getTopLevelBlockOwner();
    
    public JavaTypeModel  getOwnerType()
    {
      return getTopLevelBlockOwner().getTypeModel();  
    }
  
    
        
    public abstract Map<String,JavaAnnotationInstanceModel>  getAnnotationsMap();
        
    
    /**
     *@return index of this formal parameters in call, started from 0
     */
    public abstract int getIndex();
    
    
    /**
     * FormalParameterModel(Modifiers,TypeRef(),Identifier,ctx)
     */
    public Term getModelTerm() throws TermWareException, EntityNotFoundException
    {
        JavaTypeModel type=getType();
        Term modifiersTerm = getModifiers().getModelTerm();
        Term typeRef=TermUtils.createTerm("TypeRef",type.getFullNameAsTerm(),TermUtils.createJTerm(type));
        Term identifier=TermUtils.createIdentifier(getName());
        JavaPlaceContext ctx = JavaPlaceContextFactory.createNewTopLevelBlockOwnerContext(getTopLevelBlockOwner());        
        Term tctx = TermUtils.createJTerm(ctx);
        Term retval = TermUtils.createTerm("FormalParameterModel",modifiersTerm,typeRef,identifier,tctx);
        return retval;
    }
    
    
    public Term getAttribute(String name) throws TermWareException
    {
      return getTopLevelBlockOwner().getChildAttributes(getName()).getAttribute(name);  
    }
    
    public void setAttribute(String name, Term value) throws TermWareException
    {
      getTopLevelBlockOwner().getChildAttributes(getName()).setAttribute(name,value);
    }
    
    public AttributedEntity  getChildAttributes(String childName) 
    {
      return null;  
    }
    
}
