/*
 * JavaConstructorModel.java
 *
 */

package ua.gradsoft.javachecker.models;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.attributes.AttributedEntity;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Interface for constructor
 * @author Ruslan Shevchenko
 */
public abstract class JavaConstructorModel implements JavaTopLevelBlockOwnerModel
{

   
    
    /**
     * get Map of declared annotations, binded to this constructor.
     *(note, that inherited annotations are not here).
     *Key is full name of annotation class.
     * @return map of annotations.
     */
    public abstract Map<String,JavaAnnotationInstanceModel>  getAnnotationsMap() throws TermWareException;
    
    
    public abstract List<JavaTypeVariableAbstractModel>  getTypeParameters() throws TermWareException;
    
    public abstract List<JavaFormalParameterModel>  getFormalParametersList() throws TermWareException, EntityNotFoundException;    
    
    public abstract Map<String, JavaFormalParameterModel> getFormalParametersMap() throws TermWareException, EntityNotFoundException;    
      
    
    public void printSignature(PrintWriter out) {
        JavaTopLevelBlockOwnerModelHelper.printTypeParametersSignature(out,this);
        out.print(getTypeModel().getFullName());
        JavaTopLevelBlockOwnerModelHelper.printFormalParametersSignature(out,this);
    }
    
    public Term getAttribute(String name)
    {
      return getTypeModel().getAttributes().getConstructorAttribute(this,name);  
    }
    
    public void setAttribute(String name,Term value)
    {
      getTypeModel().getAttributes().setConstructorAttribute(this,name,value);  
    }
    
    public AttributedEntity getChildAttributes(String childName) throws TermWareException
    {
      return getTypeModel().getAttributes().getData().getChildAttributes(JavaTopLevelBlockOwnerModelHelper.getStringSignature(this)).getChildAttributes(childName);  
    }
    
}
