
package ua.gradsoft.javachecker.trace;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.models.JavaPlaceContext;
import ua.gradsoft.javachecker.annotations.Nullable;
import ua.gradsoft.javachecker.models.JavaFormalParameterModel;
import ua.gradsoft.javachecker.models.JavaInitializerModel;
import ua.gradsoft.javachecker.models.JavaMethodModel;
import ua.gradsoft.javachecker.models.JavaPlaceContextFactory;
import ua.gradsoft.javachecker.models.JavaStatementModel;
import ua.gradsoft.javachecker.models.JavaTopLevelBlockModel;
import ua.gradsoft.javachecker.models.JavaTopLevelBlockOwnerModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.javachecker.models.JavaTypeModelHelper;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TermWareRuntimeException;

/**
 *Trace context, which can keep state of variables.
 * @author rssh
 */
public class JavaTraceContext {


    /**
     * create 'zero' context
     * @param place
     */
    public JavaTraceContext(JavaPlaceContext place)
    {
      place_=place;
      if (place_.getTypeModel()!=null) {
          JavaTypeModel thisTypeModel = place_.getTypeModel();
          if (place_.getTopLeveBlockOwnerModel()!=null) {
              JavaTopLevelBlockOwnerModel bo = place_.getTopLeveBlockOwnerModel();
              if (bo instanceof JavaMethodModel) {
                  JavaMethodModel mm = (JavaMethodModel)bo;
                  if (mm.getModifiers().isStatic()) {
                      isStaticContext_=true;
                  }
              }else if (bo instanceof JavaInitializerModel) {
                  JavaInitializerModel im = (JavaInitializerModel)bo;
                  isStaticContext_=im.getModifiers().isStatic();
              }else{
                  //TODO:  write more.
              }
          }
      }
    }

    /**
     * New trace context, which created on method
     * @param methodModel
     * @param newThisObjectModel
     * @param arguments
     * @param prev
     */
    public JavaTraceContext(
                           JavaMethodModel methodModel,
                           JavaTraceObjectModel newThisObjectModel,
                           List<JavaTraceObjectModel> arguments,
                           JavaTraceContext prev
                           ) throws EvaluationException, EntityNotFoundException
    {
      try {
        if (methodModel.getModifiers().isStatic()) {
            isStaticContext_=true;
        }
        JavaPlaceContext newContext = null;
        methodModel = alignMethodCallToObject(methodModel,newThisObjectModel);
        if (methodModel.isSupportBlockModel()) {
            JavaTopLevelBlockModel blockModel = methodModel.getTopLevelBlockModel();
            if (blockModel!=null) {
              statements_ =
                          methodModel.getTopLevelBlockModel().getStatements();
              if (statements_.size()==0) {
                // empty method call
                newContext = JavaPlaceContextFactory.createNewMethodContext(methodModel);
              }else{
                newContext = JavaPlaceContextFactory.createNewStatementContext(statements_.get(0));
                statementIndex_=0;
              }
              buildFormalParameters(methodModel,arguments);
            }else{
              // method is not concrete (or we can't find implementation of
              // interface model.
              throw new NotKnowException();
            }
        }else{
           // this is class-like statement, not term.
           throw new SourceRequiredForEvaluation();
        }
        place_=newContext;
        prev_=prev;
        thisTraceObjectModel_=newThisObjectModel;
      }catch(TermWareException ex){
          throw new TermWareRuntimeException(ex);
      }
    }



    public JavaPlaceContext getPlace()
    { return place_; }

    @Nullable
    public JavaTraceObjectModel getLocalVariable(String name)
    {
      JavaTraceObjectModel retval=null;  
      if (localVariables_ !=null ) {
          retval=localVariables_.get(name);
      }       
      return retval;      
    }

    @Nullable
    public JavaTraceObjectModel getFormalParameter(String name)
    {
        JavaTraceObjectModel retval=null;
        if (formalParameters_!=null) {
            retval=formalParameters_.get(name);
        }
        return null;
    }

    protected JavaMethodModel  alignMethodCallToObject(JavaMethodModel methodModel, JavaTraceObjectModel om)
            throws TermWareException, EntityNotFoundException
    {
        JavaTypeModel targetType = om.getType();
        JavaTypeModel sourceType = methodModel.getTypeModel();
        if (JavaTypeModelHelper.same(sourceType, targetType)) {
            return methodModel;
        }

        if (JavaTypeModelHelper.subtypeOrSame(targetType, sourceType)) {
            // find overrided method model, if possible
            while(!JavaTypeModelHelper.same(sourceType, targetType) || targetType!=null) {
                List<JavaMethodModel>  ml = targetType.findMethodModels(methodModel.getName());
                for(JavaMethodModel m: ml) {
                    if (sameFormalParametersList(methodModel, m)) {
                        return m;
                    }
                }
                JavaTypeModel targetTypeSuper = targetType.getSuperClass();
                if (targetTypeSuper==null) {
                    break;
                }
                if (targetType.getErasedFullName().equals(targetTypeSuper.getErasedFullName())) {
                    break;
                }
                targetType = targetTypeSuper;
            }
        }
        // we does not find overrider, return origin
        return methodModel;
    }


    private boolean sameFormalParametersList(JavaMethodModel x, JavaMethodModel y)
                                                                 throws TermWareException, EntityNotFoundException
    {
      List<JavaFormalParameterModel> xl = x.getFormalParametersList();
      List<JavaFormalParameterModel> yl = y.getFormalParametersList();
      if (xl.size()!=yl.size()) {
          return false;
      }
      for(int i=0; i<xl.size(); ++i) {
          if (!JavaTypeModelHelper.same(xl.get(i).getType(), yl.get(i).getType())) {
              return false;
          }
      }
      return true;
    }

    private void buildFormalParameters(JavaMethodModel method, List<JavaTraceObjectModel> arguments) throws TermWareException, EntityNotFoundException, EvaluationException
    {
       List<JavaFormalParameterModel> params = method.getFormalParametersList();
       Map<String, JavaTraceObjectModel> retval = new TreeMap<String, JavaTraceObjectModel>();
       if (method.getModifiers().isVarArgs()) {
           // build varargs parametors.
           int lastParamIndex = (params.size()-1);
           if (arguments.size() > params.size()) {
               for(int i=0; i<lastParamIndex; ++i) {
                   retval.put(params.get(i).getName(), arguments.get(i));
               }
               JavaFormalParameterModel lastParam = params.get(lastParamIndex);
               JavaTraceArrayModel lastArg = new JavaTraceArrayModel(lastParam.getType().getReferencedType(),
                                                                     arguments.size()-params.size()+1);
               for(int i=lastParamIndex; i<arguments.size(); ++i) {
                   lastArg.set(i-lastParamIndex,arguments.get(i));
               }
               retval.put(lastParam.getName(), lastArg);
           }else if (arguments.size()==params.size()) {
               // check, that last arguemnt is same type.
               JavaTypeModel argType = arguments.get(lastParamIndex).getType();
               JavaTypeModel paramType = params.get(lastParamIndex).getType();
               if (argType.isArray() && paramType.isArray()
                       &&
                       JavaTypeModelHelper.subtypeOrSame(
                               argType.getReferencedType(),
                               paramType.getReferencedType()
                               )
                               ) {
                   retval.put(params.get(lastParamIndex).getName(), arguments.get(lastParamIndex));
               }else{
                   JavaTraceArrayModel lastArg = new JavaTraceArrayModel(params.get(lastParamIndex).getType().getReferencedType(),1);
                   lastArg.set(0,arguments.get(lastParamIndex));
                   retval.put(params.get(lastParamIndex).getName(),lastArg);
               }
           }else{
               // impossible, when we have no default parameters in java.
               throw new EvaluationException("number of parameters is bigger then number of actual arguments");
           }
       } else {
           for(int i=0; i<params.size(); ++i) {
               retval.put(params.get(i).getName(),arguments.get(i));
           }
       }
       formalParameters_=retval;
    }


    private JavaPlaceContext place_;
    List<JavaStatementModel>  statements_;
    int                       statementIndex_=0;
    private Map<String, JavaTraceObjectModel> localVariables_;
    private Map<String, JavaTraceObjectModel> formalParameters_;
    private JavaTraceObjectModel  thisTraceObjectModel_;
    private boolean               isStaticContext_=false;

    private JavaTraceContext prev_;
    private JavaTraceContextFrameType frameType;

}
