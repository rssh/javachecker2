/*
 * JavaConstructorModel.java
 *
 */

package ua.gradsoft.javachecker.models;

import java.util.List;
import java.util.Map;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.termware.TermWareException;

/**
 *Interface for constructor
 * @author Ruslan Shevchenko
 */
public interface JavaConstructorModel extends JavaTopLevelBlockOwnerModel
{
    
    public List<JavaTypeVariableAbstractModel>  getTypeParameters() throws TermWareException;
    
    public Map<String,JavaFormalParameterModel> getFormalParametersMap() throws TermWareException, EntityNotFoundException;    
    
}
