/*
 * EqualsHashCodeChecker.java
 *
 */

package ua.gradsoft.javachecker.checkers;

import java.util.List;
import ua.gradsoft.javachecker.ConfigException;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.JavaFacts;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.javachecker.models.JavaFormalParameterModel;
import ua.gradsoft.javachecker.models.JavaTermFormalParameterModel;
import ua.gradsoft.javachecker.models.JavaMethodModel;
import ua.gradsoft.javachecker.models.JavaTermTypeAbstractModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Check, that if method overload eqals, than hash-code must be implemented and vice-verse.
 * @author RSSH
 */
public class EqualsHashCodeChecker implements JavaTypeModelProcessor
{

    public void configure(JavaFacts facts) throws ConfigException, TermWareException {
    }

    
    public void process(JavaTermTypeAbstractModel typeModel, JavaFacts facts) throws TermWareException {
     try{   
       if (typeModel.hasMethodModels()) {
         if (existsEquals(typeModel)) {
             if (!existsHashCode(typeModel)) {
                 facts.violationDiscovered("EqualsHashCode","class implements equals but does not override hashCode",typeModel.getTerm());
             }
         }else{
             if (existsHashCode(typeModel)) {
                 facts.violationDiscovered("EqualsHashCode","class does not implements equals but override hashCode",typeModel.getTerm());                 
             }
         }
       }
     }catch(EntityNotFoundException ex){
         throw new AssertException(ex.getMessage(),ex);
     }
    }

    public boolean existsEquals(JavaTermTypeAbstractModel typeModel) throws TermWareException, EntityNotFoundException 
    {
        List<JavaMethodModel> eqModels=null;
        try {
          eqModels = typeModel.findMethodModels("equals");
        }catch(EntityNotFoundException ex){
            return false;
        }catch(NotSupportedException ex){
            return false;
        }
        for(JavaMethodModel m: eqModels) {
            List<JavaFormalParameterModel> fpl = m.getFormalParametersList();
            if (fpl.size()==1) {
                JavaFormalParameterModel fp0 = fpl.get(0);
                JavaTypeModel fp0t = fp0.getTypeModel();
                if (fp0t.getFullName().equals("java.lang.Object")) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean existsHashCode(JavaTermTypeAbstractModel typeModel) throws TermWareException, EntityNotFoundException
    {
       List<JavaMethodModel> hcModels = null;
       try {
           hcModels=typeModel.findMethodModels("hashCode");
       }catch(EntityNotFoundException ex){
           return false;
       }catch(NotSupportedException ex){
           return false;
       }
       for(JavaMethodModel m: hcModels) {
           List<JavaFormalParameterModel> fpl=m.getFormalParametersList();
           if (fpl.size()==0) {
               return true;
           }
       }
       return false;
    }
       
}
