/*
 * JavaTermAnnotationInstanceModel.java
 *
 */

package ua.gradsoft.javachecker.models;

import java.lang.annotation.ElementType;
import java.util.Map;
import java.util.TreeMap;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.Main;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *
 * @author rssh
 */
public class JavaTermAnnotationInstanceModel extends JavaAnnotationInstanceModel {
    
    /** Creates a new instance of JavaTermAnnotationInstanceModel */
    public JavaTermAnnotationInstanceModel(ElementType elementType,JavaTypeModel typeTarget,Term t)  throws TermWareException
    {
        super(elementType,typeTarget);
        build(t,typeTarget);
    }
    
    
    public JavaTypeModel getAnnotationModel() throws TermWareException, EntityNotFoundException
    {
        if (annotationType_==null) {
            switch(elementType_) {
                case ANNOTATION_TYPE:
                    annotationType_=JavaResolver.resolveTypeToModel(nameTerm_,(JavaTypeModel)target_);
                    break;
                case CONSTRUCTOR:
                {
                    JavaConstructorModel cm = (JavaConstructorModel)target_;
                    annotationType_ = JavaResolver.resolveTypeToModel(nameTerm_,cm.getTypeModel());
                }
                break;
                case FIELD:
                {
                    JavaMemberVariableModel mvm = (JavaMemberVariableModel)target_;
                    annotationType_=JavaResolver.resolveTypeToModel(nameTerm_,mvm.getOwner());
                }
                break;
                case LOCAL_VARIABLE:
                {
                    JavaLocalVariableModel lvm = (JavaLocalVariableModel)target_;
                    annotationType_=JavaResolver.resolveTypeToModel(nameTerm_,lvm.getStatement());
                }
                break;
                case METHOD:
                {
                    JavaMethodModel mm = (JavaMethodModel)target_;
                    annotationType_=JavaResolver.resolveTypeToModel(nameTerm_,mm.getTypeModel());
                }
                break;
                case PACKAGE:
                {
                    JavaUnitModel unitModel = (JavaUnitModel)target_;
                    JavaPackageModel packageModel = Main.getFacts().getPackagesStore().findOrAddPackage(unitModel.getPackageName());
                    annotationType_=JavaResolver.resolveTypeToModel(nameTerm_,unitModel,packageModel,null,null,null);
                }
                break;
                case PARAMETER:
                {
                    JavaFormalParameterModel fm = (JavaFormalParameterModel)target_;
                    annotationType_=JavaResolver.resolveTypeToModel(nameTerm_,fm.getOwner().getTypeModel(),fm.getOwner().getTypeParameters());
                }
                break;
                case TYPE:
                    annotationType_=JavaResolver.resolveTypeToModel(nameTerm_,(JavaTypeModel)target_);
                    break;
                default:
                    throw new AssertException("Invalid ElementType:"+elementType_);
            }
        }
        return annotationType_;
    }
    
    
    public boolean hasElement(String element) {
        switch(kind_) {
            case MARKER:
                return false;
            case SINGLE_MEMBER:
                return element.equals("value");
            case NORMAL:
                return values_.containsKey(element);
            default:
                throw new RuntimeException("Internal error: invalid Annotation kind:"+kind_);
        }
    }
    
    public JavaExpressionModel  getElement(String element) throws NotSupportedException {
        JavaExpressionModel retval = values_.get(element);
        if (retval==null) {
            throw new NotSupportedException();
        }
        return retval;
    }
    
    public Map<String,JavaExpressionModel>  getElements()
    { return values_; }
    
    /**
     * AnnotationInstanceModel([pair(Identifier(name),value)],this)
     */
    public Term getModelTerm() throws TermWareException, EntityNotFoundException
    {
        Term l = TermUtils.createNil();
        
        for(Map.Entry<String,JavaExpressionModel> e:values_.entrySet()) {
            Term name = TermUtils.createIdentifier(e.getKey());
            Term value = e.getValue().getModelTerm();
            Term pair = TermUtils.createTerm("pair",name,value);
            l=TermUtils.createTerm("cons",pair,l);
        }
        l=TermUtils.reverseListTerm(l);
        
        Term tt = TermUtils.createJTerm(this);
        
        Term retval = TermUtils.createTerm("AnnotationInstanceModel",l,tt);
        return retval;
    }
    
    private void build(Term t, JavaTypeModel tm) throws TermWareException
    {
        values_=new TreeMap<String,JavaExpressionModel>();
        if (t.getName().equals("Annotation")) {
            Term t0 = t.getSubtermAt(0);
            if (t0.getName().equals("NormalAnnotation")) {
                buildNormalAnnotation(t0,tm);
            }else if (t0.getName().equals("SingleMemberAnnotation")) {
                buildSingleMemberAnnotation(t0,tm);
            }else if (t0.getName().equals("MarkerAnnotation")){
                buildMarkerAnnotation(t0,tm);
            }else{
                throw new InvalidJavaTermException("Invalid annotation type",t);
            }
        }else{
            throw new InvalidJavaTermException("Annotation required",t);
        }
        
    }
    
    
    private void buildNormalAnnotation(Term t, JavaTypeModel tm) throws TermWareException
    {
        kind_ = JavaAnnotationKind.NORMAL;
        nameTerm_ = t.getSubtermAt(0);
        if (t.getArity()==2) {
            Term memberValuePairs=t.getSubtermAt(1);
            Term l = memberValuePairs.getSubtermAt(0);
            while(!l.isNil()) {
                Term pair = l.getSubtermAt(0);
                l = l.getSubtermAt(1);
                Term identifier = pair.getSubtermAt(0);
                String name = identifier.getSubtermAt(0).getString();
                Term expressionTerm = pair.getSubtermAt(1);
                JavaTermExpressionModel expr = createExpressionModel(expressionTerm,tm,name);
                values_.put(name,expr);
            }
        }
    }
    
    private void buildSingleMemberAnnotation(Term t, JavaTypeModel tm) throws TermWareException
    {
        kind_=JavaAnnotationKind.SINGLE_MEMBER;     
        Term exprTerm = t.getSubtermAt(0);
        JavaExpressionModel expr = createExpressionModel(exprTerm,tm,"value");
        values_.put("value",expr);
    }
    
    private void buildMarkerAnnotation(Term t, JavaTypeModel tm) {
        kind_=JavaAnnotationKind.MARKER;
        nameTerm_=t.getSubtermAt(0);
    }
    
    private JavaTermExpressionModel  createExpressionModel(Term t, JavaTypeModel tm, String elementName) throws TermWareException
    {
        return JavaTermExpressionModel.create(t,null,tm,this,elementName);
    }
    
    private Term nameTerm_;
    private JavaTypeModel annotationType_=null;
    private TreeMap<String,JavaExpressionModel> values_;
    private JavaAnnotationKind kind_;
}
