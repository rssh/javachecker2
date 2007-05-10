/*
 * JavaTermAnnotationExpressionModel.java
 *
 */

package ua.gradsoft.javachecker.models.expressions;

import java.util.LinkedList;
import java.util.List;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.models.JavaAnnotationInstanceModel;
import ua.gradsoft.javachecker.models.JavaExpressionKind;
import ua.gradsoft.javachecker.models.JavaExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermExpressionModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Model for annotation, which is part of other annotation.
 * @author rssh
 */
public class JavaTermAnnotationExpressionModel extends JavaTermExpressionModel 
{
    
    /** Creates a new instance of JavaTermAnnotationExpressionModel */
    public JavaTermAnnotationExpressionModel(Term t, JavaAnnotationInstanceModel instanceModel, JavaTypeModel enclosedType, JavaAnnotationInstanceModel enclosedAnnotation) {
        super(t,null,enclosedType);
        instanceModel_=instanceModel;
    }

    public JavaExpressionKind  getKind()
    { return JavaExpressionKind.ANNOTATION; }
    
    public JavaTypeModel  getType() throws TermWareException, EntityNotFoundException
    { return instanceModel_.getAnnotationModel(); }
    
    public boolean isType()
    { return false; }
    
    public List<JavaExpressionModel> getSubExpressions() throws TermWareException
    { 
        List retval = new LinkedList<JavaExpressionModel>();
        retval.addAll(instanceModel_.getElements().values());
        return retval;
    }
    
    public Term getModelTerm() throws TermWareException, EntityNotFoundException
    { return instanceModel_.getModelTerm(); }
    
    private JavaAnnotationInstanceModel instanceModel_;
}
