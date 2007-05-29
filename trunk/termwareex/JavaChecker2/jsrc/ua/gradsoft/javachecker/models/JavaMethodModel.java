/*
 * JavaMethodModel.java
 *
 */

package ua.gradsoft.javachecker.models;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.JavaFacts;
import ua.gradsoft.javachecker.attributes.AttributedEntity;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 * Model for method
 * @author  Ruslan Shevchenko
 */
public abstract class JavaMethodModel implements JavaTopLevelBlockOwnerModel 
{
    
    /**
     * Creates a new instance of JavaMethodModel
     */
    public JavaMethodModel(JavaTypeModel typeModel) 
    {
     typeModel_=typeModel;
    }
    
    /**
     *get name of method.
     *@return method name
     */
    public abstract String getName();    
    
    
    public abstract JavaModifiersModel getModifiers();
    
    public abstract List<JavaTypeVariableAbstractModel>  getTypeParameters() throws TermWareException;
 
    public abstract JavaTypeModel  getResultType() throws TermWareException;
    
    /**
     *return list of forma-parameters types.  Note, that type for varargs parameters are arrays.
     */
    public abstract List<JavaTypeModel> getFormalParametersTypes() throws TermWareException, EntityNotFoundException;
    
    /**
     *get list of formal parameters. 
     */
    public abstract List<JavaFormalParameterModel> getFormalParametersList() throws TermWareException, EntityNotFoundException;
    
    /**
     * get map of formal parameters.
     */
    public abstract Map<String, JavaFormalParameterModel>  getFormalParametersMap() throws TermWareException, EntityNotFoundException;
                
    
    /**
     * get Map of annotation.
     */
    public abstract Map<String,JavaAnnotationInstanceModel> getAnnotationsMap() throws TermWareException;
            
    
    public void printSignature(PrintWriter out) 
    {
        JavaTopLevelBlockOwnerModelHelper.printTypeParametersSignature(out,this);
        out.print(getName());
        JavaTopLevelBlockOwnerModelHelper.printFormalParametersSignature(out,this);
    }
    
    public void print(PrintStream out) 
    {
        PrintWriter writer = new PrintWriter(out);
        printSignature(writer);
        writer.flush();
 //       writer.close();
    }
    
    public JavaFacts getJavaFacts()
    { return typeModel_.getJavaFacts(); }
    
    public JavaTypeModel getTypeModel()
    { return typeModel_; }
    
 
    
    public Term getAttribute(String name) throws TermWareException
    {
      return getTypeModel().getAttributes().getMethodAttribute(this,name); 
    }
    
    public void setAttribute(String name, Term value) throws TermWareException
    {
      getTypeModel().getAttributes().setMethodAttribute(this,name,value);  
    }
    
    public AttributedEntity  getChildAttributes(String childName) throws TermWareException
    {
       return getTypeModel().getAttributes().getData().getOrCreateChild(JavaTopLevelBlockOwnerModelHelper.getStringSignature(this)).getOrCreateChild(childName); 
    }
    
    private JavaTypeModel typeModel_;
}
