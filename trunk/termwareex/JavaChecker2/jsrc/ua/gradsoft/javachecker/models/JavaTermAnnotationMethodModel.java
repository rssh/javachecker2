/*
 * JavaTermAnnotationMethodModel.java
 *
 */

package ua.gradsoft.javachecker.models;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *
 * @author rssh
 */
public class JavaTermAnnotationMethodModel extends JavaMethodModel
{
    
    /** Creates a new instance of JavaTermAnnotationMethodModel */
    public JavaTermAnnotationMethodModel(JavaTermAnnotationFieldModel fieldModel) {
        super(fieldModel.getOwner());
        fieldModel_=fieldModel;
    }
    
    public String getName()
    { return fieldModel_.getName(); }
    
    public JavaTermModifiersModel getModifiers()
    { return fieldModel_.getModifiersModel(); }
    
    public List<JavaTypeVariableAbstractModel>  getTypeParameters() throws TermWareException
    { return Collections.emptyList(); }
 
    public JavaTypeModel  getResultType() throws TermWareException
    { return fieldModel_.getTypeModel(); }
   
    
    public List<JavaTypeModel> getFormalParametersTypes() throws TermWareException
    { return Collections.emptyList(); }
    
    public List<JavaFormalParameterModel> getFormalParametersList() throws TermWareException
    { return Collections.emptyList(); }
    
    public Map<String, JavaFormalParameterModel>  getFormalParametersMap() throws TermWareException
    { return Collections.emptyMap(); }
    
    public Term getModelTerm() throws TermWareException
    {
        return fieldModel_.getModelTerm();
    }
    
    public Map<String,JavaAnnotationInstanceModel> getAnnotationsMap()
    { return Collections.emptyMap(); }
    
    public boolean isSupportBlockModel()
    { return false; }
    
    public JavaTopLevelBlockModel getTopLevelBlockModel()
    {  return null; }
    
    private JavaTermAnnotationFieldModel fieldModel_;
}
