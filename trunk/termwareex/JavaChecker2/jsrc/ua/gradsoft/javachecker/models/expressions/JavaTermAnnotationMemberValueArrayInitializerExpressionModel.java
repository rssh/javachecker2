/*
 * JavaTermAnnotationMemberValueArrayInitializerExpressionModel.java
 *
 * (C) Grad-Soft Ltd, Kiev, Ukraine
 *http://www.gradsoft.ua
 */

package ua.gradsoft.javachecker.models.expressions;

import java.util.LinkedList;
import java.util.List;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.javachecker.models.InvalidJavaTermException;
import ua.gradsoft.javachecker.models.JavaAnnotationInstanceModel;
import ua.gradsoft.javachecker.models.JavaExpressionKind;
import ua.gradsoft.javachecker.models.JavaExpressionModel;
import ua.gradsoft.javachecker.models.JavaMemberVariableModel;
import ua.gradsoft.javachecker.models.JavaPlaceContext;
import ua.gradsoft.javachecker.models.JavaTermExpressionModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.javachecker.models.TermUtils;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *MemberValueArryaInitializer inside annotation,
 * @author rssh
 */
public class JavaTermAnnotationMemberValueArrayInitializerExpressionModel extends JavaTermExpressionModel
{
    
    /**
     * Creates a new instance of JavaTermAnnotationMemberValueArrayInitializerExpressionModel
     */
    public JavaTermAnnotationMemberValueArrayInitializerExpressionModel(Term t,JavaTypeModel tm,JavaAnnotationInstanceModel enclosedAnnotation, String elementName)  throws TermWareException
    {
        super(t,null,tm);
        enclosedAnnotation_=enclosedAnnotation;    
        elementName_=elementName;
        build(t);
    }
    
    public JavaExpressionKind  getKind()
    { return JavaExpressionKind.ANNOTATION_MEMBER_VALUE_ARRAY_INITIALIZER; }
    
    public boolean isType()
    { return false; }
    
    public JavaTypeModel  getType() throws TermWareException, EntityNotFoundException
    {
      try {  
        JavaTypeModel tm = enclosedAnnotation_.getAnnotationModel();
        JavaMemberVariableModel mv = tm.getMemberVariableModels().get(elementName_);
        return mv.getType();
      }catch(NotSupportedException ex){
          throw new InvalidJavaTermException("Incorrect annotation element "+elementName_,t_,ex);
      }
    }
    
    public List<JavaExpressionModel>  getSubExpressions()
    { return subexpressions_; }
    
    public boolean isConstantExpression() throws TermWareException, EntityNotFoundException
    {
      return true;
    }
      
    
    /**
     * AnnotationMemberValueArrayInitalizerExpression(Identifier(name),[subexprs..],ctx)
     */
    public Term getModelTerm() throws TermWareException, EntityNotFoundException
    {
       Term name = TermUtils.createIdentifier(elementName_); 
       Term list = TermUtils.createNil();
       for(JavaExpressionModel e: getSubExpressions()) {
           list=TermUtils.createTerm("cons",e.getModelTerm(),list);
       }
       list = TermUtils.reverseListTerm(list);
       JavaPlaceContext ctx = createPlaceContext();
       Term jctx = TermUtils.createJTerm(ctx);
       return TermUtils.createTerm("AnnotationMemberValueArrayInitializerExpression",name,list,jctx); 
    }
    
    private void build(Term t) throws TermWareException
    {
        subexpressions_=new LinkedList<JavaExpressionModel>();
        if (!t.getName().equals("MemberValueArrayInitializer")) {
            throw new InvalidJavaTermException("MemberValueArrayInitializer required",t);
        }
        Term l = t.getSubtermAt(0);
          while(!l.isNil()) {
            Term ct = l.getSubtermAt(0);            
            l=l.getSubtermAt(1);
            JavaTermExpressionModel expr = JavaTermExpressionModel.create(ct.getSubtermAt(0),null,enclosedType_,enclosedAnnotation_,"["+elementName_);
            subexpressions_.add(expr);
          }
    }
    
    private List<JavaExpressionModel> subexpressions_;   
    private JavaAnnotationInstanceModel enclosedAnnotation_;
    private String elementName_;
   
}
