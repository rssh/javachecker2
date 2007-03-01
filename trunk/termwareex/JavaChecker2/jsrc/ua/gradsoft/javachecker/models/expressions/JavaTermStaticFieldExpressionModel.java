/*
 * JavaTermStaticFieldExpressionModel.java
 *
 * Created on 20 ������� 2007 �., 3:54
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ua.gradsoft.javachecker.models.expressions;

import java.util.Collections;
import java.util.List;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.models.JavaExpressionKind;
import ua.gradsoft.javachecker.models.JavaExpressionModel;
import ua.gradsoft.javachecker.models.JavaMemberVariableModel;
import ua.gradsoft.javachecker.models.JavaPlaceContext;
import ua.gradsoft.javachecker.models.JavaResolver;
import ua.gradsoft.javachecker.models.JavaTermExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermStatementModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.javachecker.models.TermUtils;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Model, which represent static field of some class.
 *StaticField(class,identifier)
 * @author RSSH
 */
public class JavaTermStaticFieldExpressionModel extends JavaTermExpressionModel
{
    
    /** Creates a new instance of JavaTermStaticFieldExpressionModel */
    public JavaTermStaticFieldExpressionModel(JavaMemberVariableModel vm, Term t, JavaTermStatementModel st, JavaTypeModel enclosedType) throws TermWareException
    {
     super(t,st,enclosedType);
     memberVariable_=vm;
    }
    
    public JavaExpressionKind  getKind()
    { return JavaExpressionKind.STATIC_FIELD; }
    
    public boolean isType()
    { return false; }
    
    public JavaTypeModel getType() throws TermWareException
    { return memberVariable_.getTypeModel(); }
    
    public List<JavaExpressionModel> getSubExpressions()
    { return Collections.emptyList(); }
    
    /**
     * StaticFieldModel(typeRef,identifier,memberVariable,ctx)
     */
    public Term getModelTerm() throws TermWareException, EntityNotFoundException
    {
      Term typeTerm = t_.getSubtermAt(0);
      JavaPlaceContext ctx = this.createPlaceContext();
      JavaTypeModel tm = JavaResolver.resolveTypeTerm(typeTerm,ctx);
      Term typeRef = TermUtils.createTerm("TypeRef",typeTerm,TermUtils.createJTerm(tm));
      Term identifier = t_.getSubtermAt(1);
      Term mv = TermUtils.createJTerm(memberVariable_);
      Term tctx = TermUtils.createJTerm(ctx);
      Term retval = TermUtils.createTerm("StaticFieldModel",typeRef,identifier,mv,tctx);
      return retval;
    }
    
    public JavaMemberVariableModel  getMemberVariable()
    { return memberVariable_; }
    
    private JavaMemberVariableModel memberVariable_;
}
