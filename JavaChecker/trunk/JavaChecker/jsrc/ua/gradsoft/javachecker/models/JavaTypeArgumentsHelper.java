/*
 * JavaTypeArgumentsHelper.java
 *
 * Created on September 27, 2007, 1:09 AM
 *
 */

package ua.gradsoft.javachecker.models;

import java.util.ArrayList;
import java.util.List;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.JUtils;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;

/**
 *Helper for operations with type arguments.
 */
public class JavaTypeArgumentsHelper {
    
    
    public static ArrayList<JavaTypeModel> createResolvedTypeArguments(Term typeArguments, 
                                                                       JavaUnitModel unitModel, 
                                                                       JavaPackageModel packageModel, 
                                                                       JavaTypeModel where, 
                                                                       JavaStatementModel statement,  
                                                                       List<JavaTypeVariableAbstractModel> typeVariables, 
                                                                       List<JavaTypeVariableAbstractModel> originTypeParameters, 
                                                                       JavaTypeArgumentsSubstitution s)
                                                                                throws TermWareException, EntityNotFoundException
    {
        ArrayList<JavaTypeModel> resolvedTypeArguments=new ArrayList<JavaTypeModel>();
        //substitution_=new JavaTypeArgumentsSubstitution();
        Term l=typeArguments.getSubtermAt(0);
        JavaTypeModel resolvedTypeArgument=null;
        int tpIndex=0;
        int tpSize=originTypeParameters.size();
        List<JavaTypeVariableAbstractModel> allTypeVariables = new ArrayList<JavaTypeVariableAbstractModel>();
        //allTypeVariables.addAll(origin_.getTypeParameters());
        //if (where_!=null && where_!=origin_) {
        //    allTypeVariables.addAll(where_.getTypeParameters());
        //}
        if (typeVariables!=null) {
           allTypeVariables.addAll(typeVariables);
        }
                
        
        while(!l.isNil()) {
            Term t=l.getSubtermAt(0);
            l=l.getSubtermAt(1);
            if (t.getName().equals("TypeArgument")) {
                if (t.getArity()==0) {
                    resolvedTypeArgument = new JavaWildcardBoundsTypeModel(where);
                }else if (t.getArity()==1) {
                    Term t1=t.getSubtermAt(0);
                    if (t1.getName().equals("WildcardBounds")) {
                        resolvedTypeArgument = new JavaWildcardBoundsTypeModel(t1,where,allTypeVariables);
                    }else{
                      try {  
                        if (statement==null) {  
                           resolvedTypeArgument = JavaResolver.resolveTypeToModel(t1,unitModel,packageModel,where,allTypeVariables,null);
                        }else{
                           resolvedTypeArgument = JavaResolver.resolveTypeToModel(t1,statement); 
                        }
                      }catch(EntityNotFoundException ex){
                          ex.setFileAndLine(JUtils.getFileAndLine(t1));
                          throw ex;
                      }
                    }
                }else{
                   try { 
                      if (statement==null) { 
                         resolvedTypeArgument = JavaResolver.resolveTypeToModel(t,unitModel,packageModel,where,allTypeVariables,null);
                      }else{
                         resolvedTypeArgument = JavaResolver.resolveTypeToModel(t,statement); 
                      }
                   }catch(EntityNotFoundException ex){
                       ex.setFileAndLine(JUtils.getFileAndLine(t));
                       throw ex;
                   }
                }
            }else{
                try {
                    resolvedTypeArgument = JavaResolver.resolveTypeToModel(t,where,allTypeVariables);
                }catch(EntityNotFoundException ex){
                    System.out.println("can't resolve "+TermHelper.termToString(t));
                    ex.setFileAndLine(JUtils.getFileAndLine(t));
                    throw ex;
                    //resolvedTypeArgument = JavaUnknownTypeModel.INSTANCE;
                }
            }
            resolvedTypeArguments.add(resolvedTypeArgument);
            if (tpIndex < tpSize) {
              if (s!=null) {  
                s.put(originTypeParameters.get(tpIndex),resolvedTypeArgument);
              }
            }
            ++tpIndex;
        }
        return resolvedTypeArguments;
    }
}
