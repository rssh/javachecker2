/*
 * UnusedMethodsAndFieldsChecker.java
 *
 * Created on May 19, 2007, 4:47 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ua.gradsoft.javachecker.checkers;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.JavaFacts;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.javachecker.models.JavaConstructorModel;
import ua.gradsoft.javachecker.models.JavaExpressionModel;
import ua.gradsoft.javachecker.models.JavaInitializerModel;
import ua.gradsoft.javachecker.models.JavaMemberVariableModel;
import ua.gradsoft.javachecker.models.JavaMethodModel;
import ua.gradsoft.javachecker.models.JavaModifiersModel;
import ua.gradsoft.javachecker.models.JavaResolver;
import ua.gradsoft.javachecker.models.JavaStatementModel;
import ua.gradsoft.javachecker.models.JavaTermMemberVariableModel;
import ua.gradsoft.javachecker.models.JavaTermMethodModel;
import ua.gradsoft.javachecker.models.JavaTermTypeAbstractModel;
import ua.gradsoft.javachecker.models.JavaTopLevelBlockModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.javachecker.models.JavaTypeModelHelper;
import ua.gradsoft.javachecker.models.JavaVariableModel;
import ua.gradsoft.javachecker.models.TermUtils;
import ua.gradsoft.javachecker.models.expressions.JavaTermFieldExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermFunctionCallExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermIdentifierExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermMethodCallExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermSpecializedMethodCallExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermStaticFieldExpressionModel;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *TODO: implement
 * @author rssh
 */
public class UnusedMethodsAndFieldsChecker implements JavaTypeModelProcessor
{
  
   public void configure(JavaFacts facts)
   {}
    
    
   public void process(JavaTermTypeAbstractModel typeModel, JavaFacts facts) throws TermWareException, EntityNotFoundException 
   {     
     markUsage(typeModel,facts);  
     printUnused(typeModel,facts);
   }    
   
   public void markUsage(JavaTermTypeAbstractModel typeModel, JavaFacts facts) throws TermWareException, EntityNotFoundException
   {
     List<JavaInitializerModel> initializers = typeModel.getInitializerModels();
     for(JavaInitializerModel inm: initializers) {
         if (inm.isSupportBlockModel()) {
             try {
               JavaTopLevelBlockModel block = inm.getTopLevelBlockModel();
               markUsage(block);
             }catch(NotSupportedException ex){
                 // impossible ?
                 break;
             }             
         }
     }
     List<JavaConstructorModel> constructors = typeModel.getConstructorModels();
     for(JavaConstructorModel cn: constructors) {
         if (cn.isSupportBlockModel()) {
             try {
               JavaTopLevelBlockModel block = cn.getTopLevelBlockModel();
               markUsage(block);
             }catch(NotSupportedException ex){
                 // impossible ?
                 break;
             }
         }
     }
     Collection<List<JavaMethodModel>> mss = Collections.emptyList();
     try {
         mss=typeModel.getMethodModels().values();
     }catch(NotSupportedException ex){
         /* nothing */
         ;
     }
     for(List<JavaMethodModel> ms: mss) {
         for(JavaMethodModel m: ms) {
             if (m.isSupportBlockModel()) {
                 try {
                    JavaTopLevelBlockModel block = m.getTopLevelBlockModel();
                    if (block!=null) {
                      markUsage(block);
                    }
                 }catch(NotSupportedException ex){
                     throw new AssertException("isSupportBlockModel is true, but getTopLevelBlockModel is not supported",ex);
                 }                 
             }
         }
     }
     for(JavaMemberVariableModel mv: typeModel.getMemberVariableModels().values()) {
         if (mv.isSupportInitializerExpression()) {
             JavaExpressionModel e = mv.getInitializerExpression();
             markUsage(e);
         }
     }
     for(JavaTypeModel tm: typeModel.getNestedTypeModels().values()) {
         if (tm instanceof JavaTermTypeAbstractModel) {
           JavaTermTypeAbstractModel ttm = (JavaTermTypeAbstractModel)tm;  
           markUsage(ttm,facts);
         }
     }
   }
   
   public void markUsage(JavaTopLevelBlockModel block) throws TermWareException, EntityNotFoundException
   {
     if (block.getStatements()!=null) {  
       for(JavaStatementModel st: block.getStatements()) {
          markUsage(st);
       }
     }
   }
   
   public void markUsage(JavaStatementModel st) throws TermWareException, EntityNotFoundException
   {       
      //System.out.println("markUsage: "+st.getKind());
      for(JavaExpressionModel e: st.getExpressions()) {
          markUsage(e);
      }
      for(JavaStatementModel sti: st.getChildStatements()) {
          markUsage(sti);
      }
   }
   
   public void markUsage(JavaExpressionModel expr) throws TermWareException, EntityNotFoundException
   {
       switch(expr.getKind()) {
           case METHOD_CALL: 
           {
               JavaTermMethodCallExpressionModel e =(JavaTermMethodCallExpressionModel)expr;
               JavaMethodModel mm = e.getMethodModel();
               mm.setAttribute(this.getClass().getName(),TermUtils.createBoolean(true));
           }
           break;
           case FUNCTION_CALL:
           {
               JavaTermFunctionCallExpressionModel e = (JavaTermFunctionCallExpressionModel)expr;
               JavaMethodModel mm = e.getMethodModel();
               mm.setAttribute(this.getClass().getName(),TermUtils.createBoolean(true));
           }
           break;
           case SPECIALIZED_METHOD_CALL:
           {
               JavaTermSpecializedMethodCallExpressionModel e = (JavaTermSpecializedMethodCallExpressionModel)expr;
               JavaMethodModel mm = e.getMethodModel();
               mm.setAttribute(this.getClass().getName(),TermUtils.createBoolean(true));
           }
           break;                          
           case IDENTIFIER:
           {
               JavaTermIdentifierExpressionModel e = (JavaTermIdentifierExpressionModel)expr;
               if (!e.isType()) {
                   JavaVariableModel vm = e.getVariableModel();
                   vm.setAttribute(this.getClass().getName(),TermUtils.createBoolean(true));
               }
           }
           break;
           case FIELD:
           {
               JavaTermFieldExpressionModel e = (JavaTermFieldExpressionModel)expr;
               JavaMemberVariableModel vm = e.getFieldModel();
               vm.setAttribute(this.getClass().getName(),TermUtils.createBoolean(true));
           }
           break;
           case STATIC_FIELD:
           {
               JavaTermStaticFieldExpressionModel e = (JavaTermStaticFieldExpressionModel)expr;
               JavaMemberVariableModel vm = e.getFieldModel();
               vm.setAttribute(this.getClass().getName(),TermUtils.createBoolean(true));
           }
           break;
           default:
               break;
       }
       for(JavaExpressionModel e: expr.getSubExpressions()) {
           markUsage(e);
       }
   }
   
   public void printUnused(JavaTypeModel typeModel, JavaFacts facts) throws TermWareException, EntityNotFoundException
   {
       Collection<List<JavaMethodModel>> mmss = Collections.emptyList();
       try {
          mmss = typeModel.getMethodModels().values();
       }catch(NotSupportedException ex){
           ;
       }
             
       
       for(List<JavaMethodModel> mms: mmss) {
           for(JavaMethodModel mm: mms) {
               if (mm.getModifiers().isPrivate()) {                   
                 Term attr = mm.getAttribute(this.getClass().getName());               
                 if (attr.isNil()) {
                   if (mm instanceof JavaTermMethodModel) {
                       JavaTermMethodModel tmm = (JavaTermMethodModel)mm;
                       Term t = tmm.getTerm();
                       if (!isMethodOfSerializable(tmm)) {                                                  
                         facts.violationDiscovered("UnusedLocally","unused private method:"+mm.getName(),t);
                       }
                   }                     
                 }
               }
           }
       }
       
       Collection<JavaMemberVariableModel> vs = Collections.emptyList();
       try {
           vs=typeModel.getMemberVariableModels().values();
       }catch(NotSupportedException ex){
           ; /* do nothing */
       }
       
       for(JavaMemberVariableModel v: vs) {
           if (v.getModifiers().isPrivate()) {
               Term attr = v.getAttribute(this.getClass().getName());
               if (attr.isNil()) {
                   if (v instanceof JavaTermMemberVariableModel) {
                       JavaTermMemberVariableModel tv = (JavaTermMemberVariableModel)v;
                       if (!isFieldOfSerializable(tv)) {
                         Term t = tv.getVariableDeclaratorTerm();
                         facts.violationDiscovered("UnusedLocally","unused private field:"+v.getName(),t);
                       }
                   }
               }
           }
       }
       
   }
   
   private boolean isMethodOfSerializable(JavaTermMethodModel tmm) throws TermWareException, EntityNotFoundException
   {
      String name = tmm.getName();
      if (name.equals("writeObject")) {
          List<JavaTypeModel> fps = tmm.getFormalParametersTypes();
          if (fps.size()!=1) {
              return false;
          }
          JavaTypeModel fp = fps.get(0);
          if (JavaTypeModelHelper.same(fp,JavaResolver.resolveTypeModelByFullClassName("java.io.ObjectOutputStream"))){
              return JavaTypeModelHelper.subtypeOrSame(tmm.getTypeModel(),JavaResolver.resolveTypeModelByFullClassName("java.io.Serializable"));
          }                   
      }else if (name.equals("readObject")) {
          List<JavaTypeModel> fps = tmm.getFormalParametersTypes();
          if (fps.size()!=1) {
              return false;
          }
          JavaTypeModel fp = fps.get(0);
          if (JavaTypeModelHelper.same(fp,JavaResolver.resolveTypeModelByFullClassName("java.io.ObjectInputStream"))){
              return JavaTypeModelHelper.subtypeOrSame(tmm.getTypeModel(),JavaResolver.resolveTypeModelByFullClassName("java.io.Serializable"));
          }                             
      }else if (name.equals("readObjectNoData")) {
          List<JavaTypeModel> fps = tmm.getFormalParametersTypes();
          if (fps.size()!=0) {
              return false;
          }else{
              return JavaTypeModelHelper.subtypeOrSame(tmm.getTypeModel(),JavaResolver.resolveTypeModelByFullClassName("java.io.Serializable"));              
          }
      }
      return false;      
   }
   
   private boolean isFieldOfSerializable(JavaTermMemberVariableModel tv) throws TermWareException, EntityNotFoundException
   {
       if (tv.getName().equals("serialVersionUID")) {
           JavaModifiersModel m = tv.getModifiers();
           if (m.isFinal() && m.isStatic()) {
               return JavaTypeModelHelper.subtypeOrSame(tv.getOwner(),JavaResolver.resolveTypeModelByFullClassName("java.io.Serializable"));
           }
       }
       return false;
   }
   
   
}
