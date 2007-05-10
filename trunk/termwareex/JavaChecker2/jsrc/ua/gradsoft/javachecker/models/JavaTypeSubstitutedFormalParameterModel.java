/*
 * JavaTypeSubstitutedFormalParameterModel.java
 *
 * Created on May 3, 2007, 2:56 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ua.gradsoft.javachecker.models;

import java.util.Map;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.util.ImmutableMappedMap;
import ua.gradsoft.javachecker.util.Function;
import ua.gradsoft.termware.TermWareException;

/**
 *
 * @author rssh
 */
public class JavaTypeSubstitutedFormalParameterModel extends JavaFormalParameterModel
{
    
    /** Creates a new instance of JavaTypeSubstitutedFormalParameterModel */
    public JavaTypeSubstitutedFormalParameterModel(JavaFormalParameterModel origin,JavaTypeArgumentBoundTopLevelBlockOwnerModel blockOwner) {
        origin_=origin;
        blockOwner_=blockOwner;
    }
    
    public JavaModifiersModel getModifiersModel()
    {  return origin_.getModifiersModel(); }
    
    
    public String getName()
    { return origin_.getName(); }
         
    public JavaTypeModel getTypeModel() throws TermWareException, EntityNotFoundException
    { return blockOwner_.getTypeArgumentBoundTypeModel().getSubstitution().substitute(origin_.getTypeModel()); }
                   
    public JavaTopLevelBlockOwnerModel  getOwner()
    { return blockOwner_; }
  
        
    public Map<String,JavaAnnotationInstanceModel>  getAnnotationsMap()
    {
        return new ImmutableMappedMap(origin_.getAnnotationsMap(),
                              new Function<JavaAnnotationInstanceModel,JavaAnnotationInstanceModel>(){
            public JavaAnnotationInstanceModel function(JavaAnnotationInstanceModel x)
            {
                return new JavaDelegatedAnnotationInstanceModel(x,JavaTypeSubstitutedFormalParameterModel.this);
            }
        }                                          
                );
    }

    
    /**
     *@return index of this formal parameters in call, started from 0
     */
    public int getIndex()
    { return origin_.getIndex(); }
    
    
    
    private JavaFormalParameterModel origin_;
    private JavaTypeArgumentBoundTopLevelBlockOwnerModel blockOwner_;
}
