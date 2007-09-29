/*
 * JavaClassModifiersModel.java
 *
 */

package ua.gradsoft.javachecker.models;

import java.util.List;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Modifiers for class.
 * @author rssh
 */
public class JavaClassModifiersModel extends JavaModifiersModel
{
    
    /** Creates a new instance of JavaClassModifiersModel */
    public JavaClassModifiersModel(List<JavaAnnotationInstanceModel> annotations,int value) {
        annotations_=annotations;
        value_=value;
    }
    
    public Term getModelTerm() throws TermWareException, EntityNotFoundException
    {
       Term anl = TermUtils.createNil();
       List<JavaAnnotationInstanceModel> annotationsList = annotations_;
       for(JavaAnnotationInstanceModel anim: annotationsList) {
           Term ct = anim.getModelTerm();
           anl=TermUtils.createTerm("cons",ct,anl);           
       }
       anl=TermUtils.reverseListTerm(anl);
       Term iv = TermUtils.createInt(getIntValue());
       return TermUtils.createTerm("Modifiers",iv,anl);               
    }
    
    public int getIntValue()
    { return value_; }
    
    private List<JavaAnnotationInstanceModel> annotations_;
    private int value_;
}
