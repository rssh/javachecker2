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
import ua.gradsoft.termware.TermWareException;

/**
 *Model for formal parameter
 * @author rssh
 */
public abstract class JavaFormalParameterModel implements JavaVariableModel
{
    
    public abstract JavaModifiersModel getModifiersModel();
    
    
    public abstract String getName();
  
    
    public JavaVariableKind getKind()
    { return JavaVariableKind.FORMAL_PARAMETER; }
    
    public abstract JavaTypeModel getTypeModel() throws TermWareException, EntityNotFoundException;
               
    public abstract JavaTopLevelBlockOwnerModel  getOwner();
  
        
    public abstract Map<String,JavaAnnotationInstanceModel>  getAnnotationsMap();

    
    /**
     *@return index of this formal parameters in call, started from 0
     */
    public abstract int getIndex();
    
    
}
