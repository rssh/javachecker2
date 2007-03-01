/*
 * JavaTypeNameExpressionModel.java
 *
 * Created on 20 Февраль 2007 г., 2:20
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ua.gradsoft.javachecker.models.expressions;

import java.util.Collections;
import java.util.List;
import ua.gradsoft.javachecker.models.JavaExpressionKind;
import ua.gradsoft.javachecker.models.JavaExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermStatementModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.javachecker.models.TermUtils;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *
 * @author RSSH
 */
public class JavaTermTypeNameExpressionModel extends JavaTermTypeExpressionModel
{
    
    /** Creates a new instance of JavaTypeNameExpressionModel */
    public JavaTermTypeNameExpressionModel(JavaTypeModel type, Term t, JavaTermStatementModel st, JavaTypeModel enclosedType) {
        super(t,st,enclosedType);
        type_=type;
    }
    
    public JavaExpressionKind  getKind()
    { return JavaExpressionKind.TYPE_NAME; }
    
    public JavaTypeModel  getType()
    { return type_; }
    
    public List<JavaExpressionModel>  getSubExpressions()
    { return Collections.emptyList(); }
    
    
    /**
     * TypeRef(t,type_)
     */
    public Term getModelTerm() throws TermWareException
    {
       Term tt = TermUtils.createJTerm(type_);
       Term retval = TermUtils.createTerm("TypeRef",t_,tt);
       return retval;
    }
    
    
    private JavaTypeModel type_;
}
