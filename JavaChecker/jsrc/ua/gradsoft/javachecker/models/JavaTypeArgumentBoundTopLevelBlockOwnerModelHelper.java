/*
 * JavaTypeArgumentBoundTopLevelBlockOwnerModelHelper.java
 *
 * Created on May 9, 2007, 7:53 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ua.gradsoft.javachecker.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.util.Function;
import ua.gradsoft.javachecker.util.ImmutableMappedMap;
import ua.gradsoft.termware.TermWareException;

/**
 *Helper of type argument bound executable.
 * @author rssh
 */
public class JavaTypeArgumentBoundTopLevelBlockOwnerModelHelper {
    

 public static List<JavaTypeVariableAbstractModel>  getTypeParameters(JavaTypeArgumentBoundTopLevelBlockOwnerModel x) throws TermWareException {
        JavaTopLevelBlockOwnerModel origin = x.getOrigin();
        JavaTypeArgumentsSubstitution substitution = x.getSubstitution();
        List<JavaTypeVariableAbstractModel> retval = new ArrayList<JavaTypeVariableAbstractModel>();
        for(JavaTypeVariableAbstractModel tv: origin.getTypeParameters()) {
            List<JavaTypeModel> newBounds = substitution.substitute(tv.getBounds());
            if (newBounds!=tv.getBounds()) {
               retval.add(new JavaTypeArgumentBoundTypeVariableModel(tv,newBounds));
            }
        }
        return retval;        
    }

 
   public static  List<JavaFormalParameterModel>  getFormalParametersList(JavaTypeArgumentBoundTopLevelBlockOwnerModel x) throws TermWareException, EntityNotFoundException
    {                
      List<JavaFormalParameterModel> ol = x.getOrigin().getFormalParametersList();  
      ArrayList<JavaFormalParameterModel> retval = new ArrayList<JavaFormalParameterModel>(ol.size());
      for(JavaFormalParameterModel ofp: ol) {
          JavaFormalParameterModel newModel = new JavaTypeSubstitutedFormalParameterModel(ofp,x);
          retval.add(newModel);
      }
      return retval;
    }
    

    
   public static Map<String, JavaFormalParameterModel>  getFormalParametersMap(final JavaTypeArgumentBoundTopLevelBlockOwnerModel x) throws TermWareException, EntityNotFoundException
    {
        return new ImmutableMappedMap<String,JavaFormalParameterModel,JavaFormalParameterModel>(
                x.getOrigin().getFormalParametersMap(),
                new Function<JavaFormalParameterModel,JavaFormalParameterModel>(){
            public JavaFormalParameterModel function(JavaFormalParameterModel y){
                return new JavaTypeSubstitutedFormalParameterModel(y,x);
            }
            
        }
                );
    }

   
 
}
