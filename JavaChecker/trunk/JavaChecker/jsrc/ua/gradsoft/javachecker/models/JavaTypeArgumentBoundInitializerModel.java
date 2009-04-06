/*
 * JavaTypeArgumentBoundInitializerModel.java
 *
 * Created on May 22, 2007, 1:43 PM
 *
 */

package ua.gradsoft.javachecker.models;

import java.util.Map;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Initializer of argument bound model
 */
public class JavaTypeArgumentBoundInitializerModel extends JavaInitializerModel implements JavaTypeArgumentBoundTopLevelBlockOwnerModel
{
    
    /** Creates a new instance of JavaTypeArgumentBoundInitializerModel */
    public JavaTypeArgumentBoundInitializerModel(JavaInitializerModel origin, JavaTypeArgumentBoundTypeModel owner) {
        origin_=origin;
        owner_=owner;
    }
    
    public JavaModifiersModel getModifiers()
    { return origin_.getModifiers(); }
    
    public JavaTypeModel getTypeModel()
    { return owner_; }
    
    public JavaTypeArgumentBoundTypeModel getTypeArgumentBoundTypeModel()
    { return owner_; }

    public JavaTypeArgumentsSubstitution getSubstitution() throws TermWareException {
        return owner_.getSubstitution();
    }
    
    public JavaInitializerModel getOrigin()
    { return origin_; }
    
   public boolean isSupportBlockModel()
    { return origin_.isSupportBlockModel(); }
    
   public JavaTypeArgumentBoundTopLevelBlockModel getTopLevelBlockModel() throws TermWareException {
       JavaTopLevelBlockModel bm = origin_.getTopLevelBlockModel();
       if (bm==null) {
           return null;
       }
       return new JavaTypeArgumentBoundTopLevelBlockModel(this,bm,owner_.getSubstitution());
   }    

        
    
    /**
     *TypeArgumentBoundInitializer(origin,substitutione,context)
     */
    public Term getModelTerm() throws TermWareException, EntityNotFoundException
    {
       Term originTerm = origin_.getModelTerm();
       Term substitution = TermUtils.createJTerm(owner_.getSubstitution());
       JavaPlaceContext ctx = JavaPlaceContextFactory.createNewInitializerContext(this);
       Term tctx = TermUtils.createJTerm(ctx);
       Term retval = TermUtils.createTerm("TypeArgumentBoundInitializer",originTerm,substitution,tctx);
       return retval;
    }
    
    public Map<String,JavaAnnotationInstanceModel> getAnnotationsMap() throws TermWareException
    { return origin_.getAnnotationsMap(); }
    
    private JavaInitializerModel  origin_;
    private JavaTypeArgumentBoundTypeModel owner_;
    
}
