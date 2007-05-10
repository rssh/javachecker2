/*
 * JavaConstructorModel.java
 *
 */

package ua.gradsoft.javachecker.models;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import ua.gradsoft.javachecker.EntityNotFoundException;
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
    
}
