/*
 * JavaTypeArgumentBoundConstructorModel.java
 *
 * Created on May 9, 2007, 5:11 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ua.gradsoft.javachecker.models;

import java.util.List;
import java.util.Map;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *
 * @author rssh
 */
public class JavaTypeArgumentBoundConstructorModel extends JavaConstructorModel implements JavaTypeArgumentBoundTopLevelBlockOwnerModel
{
        
    
    /** Creates a new instance of JavaTypeArgumentBoundConstructorModel */
    public JavaTypeArgumentBoundConstructorModel(JavaConstructorModel origin, JavaTypeArgumentBoundTypeModel owner) {
        origin_=origin;      
        owner_=owner;
    }
    
    public String getName()
    { return origin_.getName(); }
    
    public JavaTypeModel getTypeModel()
    { return owner_; }
    
    public JavaTypeArgumentBoundTypeModel  getTypeArgumentBoundTypeModel()
    { return owner_; }
    
    public List<JavaTypeVariableAbstractModel>  getTypeParameters() throws TermWareException {
        return JavaTypeArgumentBoundTopLevelBlockOwnerModelHelper.getTypeParameters(this);
    }
    
    public List<JavaFormalParameterModel>  getFormalParametersList() throws TermWareException, EntityNotFoundException
    { return JavaTypeArgumentBoundTopLevelBlockOwnerModelHelper.getFormalParametersList(this);  }
            

    public Map<String,JavaFormalParameterModel>  getFormalParametersMap() throws TermWareException, EntityNotFoundException
    { return JavaTypeArgumentBoundTopLevelBlockOwnerModelHelper.getFormalParametersMap(this);  }
 
    public Map<String,JavaAnnotationInstanceModel> getAnnotationsMap() throws TermWareException
    { return origin_.getAnnotationsMap(); }

    
    
    public boolean isSupportBlockModel()
    { return origin_.isSupportBlockModel(); }
    
  public JavaTypeArgumentBoundTopLevelBlockModel getTopLevelBlockModel() throws TermWareException, NotSupportedException {
        return new JavaTypeArgumentBoundTopLevelBlockModel(this,origin_.getTopLevelBlockModel(),owner_.getSubstitution());
   }    
    
    /**
     * TypeArgumentBoundConstructorModel(originModel,substitution, context);
     */

    public Term getModelTerm() throws TermWareException, EntityNotFoundException
    {
       Term originModel = origin_.getModelTerm();
       Term substitution = TermUtils.createJTerm(owner_.getSubstitution());
       Term context = TermUtils.createJTerm(JavaPlaceContextFactory.createNewTopLevelBlockOwnerContext(this));
       Term retval = TermUtils.createTerm("TypeArgumentBoundConstructorModel",originModel,substitution,context);
       return retval;
    }
    
    public JavaTypeArgumentsSubstitution getSubstitution() throws TermWareException
    { return owner_.getSubstitution(); }
    
    public JavaTopLevelBlockOwnerModel getOrigin()
    { return origin_; }
    
    private JavaConstructorModel origin_;   
    private JavaTypeArgumentBoundTypeModel owner_;
    
}
