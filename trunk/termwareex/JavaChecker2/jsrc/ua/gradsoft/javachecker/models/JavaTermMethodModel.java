/*
 * JavaMethodModel.java
 *
 * Created  23, 02, 2004, 10:00
 */

package ua.gradsoft.javachecker.models;

import java.lang.annotation.ElementType;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.FileAndLine;
import ua.gradsoft.javachecker.JUtils;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;



/**
 *Model for java method 
 *TODO: build block model
 * @author  Ruslan Shevchenko
 */
public class JavaTermMethodModel extends JavaMethodModel implements JavaTermTopLevelBlockOwnerModel
{
    
    
    /**
     * Creates a new instance of JavaMethodModel
     */
    public JavaTermMethodModel(Term modifiers, Term t, JavaTermTypeAbstractModel owner) throws TermWareException
    {
        super(owner);
        t_=t;
        if (t.getName().equals("MethodDeclaration")) {
            Term md=t.getSubtermAt(METHOD_DECLARATOR_INDEX);
            Term nameTerm=md.getSubtermAt(METHOD_DECLARATOR__IDENTIFIER_INDEX);
            methodName_=nameTerm.getSubtermAt(0).getName();
        }else{
            throw new AssertException("term must be MethodDeclaration:"+TermHelper.termToString(t));
        }        
        modifiers_=new JavaTermModifiersModel(modifiers,ElementType.METHOD,this);
        Term blockTerm = t.getSubtermAt(BLOCK_INDEX);        
        blockModel_= (blockTerm.isNil() ? null : new JavaTermTopLevelBlockModel(this,blockTerm.getSubtermAt(0)));
    }
    
           
    public  String getName()
    { return methodName_; }
    
    public  Term getTerm()
    { return t_; }
    
    public JavaTermModifiersModel getModifiers()
    { return modifiers_; }
    
    public JavaTypeModel getResultType() throws TermWareException
    { 
     //System.err.println("getResultType called");
     Term resultType = getResultTypeAsTerm();
     if (resultType.getName().equals("ResultType")) {
         resultType=resultType.getSubtermAt(0);
     }
     try {   
        return JavaResolver.resolveTypeToModel(resultType,getTypeModel(),getTypeParameters()); 
     }catch(EntityNotFoundException ex){
        if (ex.getFileAndLine().equals(FileAndLine.UNKNOWN)) { 
           ex.setFileAndLine(JUtils.getFileAndLine(resultType)); 
        }
        throw new AssertException(ex.getMessage()+" file "+ex.getFileAndLine().getFname()+", line "+ex.getFileAndLine().getLine(),ex);
     }
    }
    
    public  Term  getResultTypeAsTerm() throws TermWareException
    { return t_.getSubtermAt(RESULT_TYPE_TERM_INDEX); }
    
    public  Term  getFormalParametersTerm() throws TermWareException
    {        
        return t_.getSubtermAt(METHOD_DECLARATOR_INDEX).getSubtermAt(METHOD_DECLARATOR__FORMAL_PARAMETERS_INDEX).getSubtermAt(0);
    }

    
    public List<JavaFormalParameterModel> getFormalParametersList() throws TermWareException, EntityNotFoundException
    {      
      if (formalParametersList_==null) {
        Term formalParametersTerm = getFormalParametersTerm();
        formalParametersList_ = JavaTopLevelBlockOwnerModelHelper.buildFormalParametersList(formalParametersTerm,this);
      }
      return formalParametersList_;
    }                
        
    public Map<String, JavaFormalParameterModel> getFormalParametersMap() throws TermWareException, EntityNotFoundException
    {      
      if (formalParametersMap_==null) {
        Term formalParametersTerm = getFormalParametersTerm();
        formalParametersMap_ = JavaTopLevelBlockOwnerModelHelper.buildFormalParametersMap(formalParametersTerm,this);
      }
      return formalParametersMap_;
    }        
    
    public List<JavaTypeModel> getFormalParametersTypes() throws TermWareException, EntityNotFoundException
    {
      Map<String,JavaFormalParameterModel> fps=getFormalParametersMap();
      JavaTypeModel[] retval = new JavaTypeModel[fps.size()];
      for(Map.Entry<String, JavaFormalParameterModel> e:fps.entrySet())
      {
          retval[e.getValue().getIndex()]=e.getValue().getTypeModel();
      }
      return Arrays.asList(retval);
    }
    
    public List<JavaTypeVariableAbstractModel>  getTypeParameters() throws TermWareException
    {
        Term tpt = t_.getSubtermAt(TYPE_PARAMETERS_TERM_INDEX);
        return JavaTopLevelBlockOwnerModelHelper.buildTypeParameters(tpt,getTypeModel());
    }
    
    public  Term  getMethodBody() throws TermWareException
    { return t_.getSubtermAt(BLOCK_INDEX); }
        
    
    public JavaTermTypeAbstractModel getTermTypeAbstractModel()
    { return (JavaTermTypeAbstractModel)getTypeModel();  }
    
    
    public Map<String,JavaAnnotationInstanceModel> getAnnotationsMap()
    { return modifiers_.getAnnotationsMap(); }
    
    public boolean  hasDefaultValue()
    { return false; }
    
    public JavaExpressionModel  getDefaultValue() throws NotSupportedException
    {
       throw new NotSupportedException();
    }
    
    
    public boolean isSupportBlockModel()
    { return true; }

    /**
     * MethodModel(modifiers,typeParameters,ResultType,name,formalParameters,throws,block,context)
     */
    public Term getModelTerm() throws TermWareException, EntityNotFoundException
    {
      Term mt = modifiers_.getModelTerm();
      Term tpt = TermUtils.buildTypeParametersModelTerm(getTypeParameters(),t_.getSubtermAt(TYPE_PARAMETERS_TERM_INDEX));
      Term rt = TermUtils.createTerm("TypeRef",getResultTypeAsTerm(),TermUtils.createJTerm(getResultType()));
      Term identifier = t_.getSubtermAt(METHOD_DECLARATOR_INDEX).getSubtermAt(METHOD_DECLARATOR__IDENTIFIER_INDEX);
      Term ofp = t_.getSubtermAt(METHOD_DECLARATOR_INDEX).getSubtermAt(METHOD_DECLARATOR__FORMAL_PARAMETERS_INDEX);
      Term fp = ofp; /*buildFormalParametersModelTerm(formalParametersMap_,ofp);*/
      Term tht = t_.getSubtermAt(THROWS_SPECIFICATION_INDEX);
      Term blockModelTerm = TermUtils.createNil();
      if (blockModel_!=null) {
          blockModelTerm=blockModel_.getModelTerm();
      }
      JavaPlaceContext ctx = JavaPlaceContextFactory.createNewMethodContext(this);
      Term tctx = TermUtils.createJTerm(ctx);
      Term retval = TermUtils.createTerm("MethodModel",mt,tpt,rt,identifier,fp,tht,blockModelTerm,tctx);
      return retval;
    }
    
    /**
     *return top-level block of method or null if one is not defined.
     *(for abstract method)
     *@Nullable
     */    
    public JavaTopLevelBlockModel getTopLevelBlockModel()
    { return blockModel_; }
        
    
    private String methodName_;
    private Term t_; 
    private JavaTermTopLevelBlockModel blockModel_ =null;    
    private JavaTermModifiersModel   modifiers_=null;    
    
    // some cashed valuse
    private Map<String, JavaFormalParameterModel> formalParametersMap_=null;
    private List<JavaFormalParameterModel>       formalParametersList_=null;
    
    public static final int TYPE_PARAMETERS_TERM_INDEX=0;
    public static final int RESULT_TYPE_TERM_INDEX=1;
    public static final int METHOD_DECLARATOR_INDEX=2;    
    public static final int THROWS_SPECIFICATION_INDEX=3;
    public static final int BLOCK_INDEX=4;
        
    public static final int METHOD_DECLARATOR__IDENTIFIER_INDEX=0;
    public static final int METHOD_DECLARATOR__FORMAL_PARAMETERS_INDEX=1;
    
    
}
