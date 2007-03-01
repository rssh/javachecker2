/*
 * JavaTermNestedTypeExpressionModel.java
 *
 * Created on 20 Февраль 2007 г., 4:00
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
 *Type, which is nested in another type.
 * @author RSSH
 */
public class JavaTermNestedTypeExpressionModel extends JavaTermTypeExpressionModel
{
    
    /** Creates a new instance of JavaTermNestedTypeExpressionModel */
    public JavaTermNestedTypeExpressionModel(JavaTypeModel tm,Term t, JavaTermStatementModel st, JavaTypeModel enclosedType) {
        super(t,st,enclosedType);
        type_=tm;
    }
          
    public JavaExpressionKind getKind()
    { return JavaExpressionKind.TYPE_FIELD; }
    
    public JavaTypeModel getType()
    { return type_; }
    
    public List<JavaExpressionModel> getSubExpressions()
    {
      return Collections.emptyList();  
    }
    
    /**
     * TypeRef(t_,type_)
     */
    public Term getModelTerm() throws TermWareException
    {
      Term retval=TermUtils.createTerm("TypeRef",t_,TermUtils.createJTerm(type_)); 
      return retval;
    }
    
    private JavaTypeModel type_;
}
