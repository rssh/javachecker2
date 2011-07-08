/*
 * JavaTopLevelBlockOwnerModelHelper.java
 */

package ua.gradsoft.javachecker.models;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.FileAndLine;
import ua.gradsoft.javachecker.JUtils;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Helper class for TopLevelBlockOwner
 * @author rssh
 */
public class JavaTopLevelBlockOwnerModelHelper {
    
    
    public static String getStringSignature(JavaTopLevelBlockOwnerModel executable)
    {    
      StringWriter swr = new StringWriter();
      PrintWriter pwr = new PrintWriter(swr);
      executable.printSignature(pwr);
      pwr.flush();
      return swr.toString();
    }

    
    
    /**
     * print list of type parameters in agle brackets throught comma without whitescapes if one exists,
     *otherwise - do nothing.
     *i. e.:
     *<code>
     *  <X,Y>
     *  <A extends B,C>
     *</cpode>
     *Note, that this method used not obly for cosmetic, but for identifying constructors and methods
     *within classes, in such cases. So, modify it with care. When you changing this class, you must change also
     *lass, which generate unique description of constructor or model from term.
     */
    public static void printTypeParametersSignature(PrintWriter out,JavaTopLevelBlockOwnerModel executable) {
        List<JavaTypeVariableAbstractModel> tvs=null;
        try{
            tvs=executable.getTypeParameters();
        }catch(TermWareException ex){
            out.print("<error:"+ex.getMessage()+">");
        }
        if (tvs!=null) {
            printTypeParametersSignature(out,tvs);
        }
    }
    
     public static void printTypeParametersSignature(PrintWriter out,List<JavaTypeVariableAbstractModel> tvs) {
            if (tvs.size()!=0) {
                out.print("<");
                boolean isFirst=true;
                for(JavaTypeVariableAbstractModel tv:tvs) {
                    if (isFirst){
                        isFirst=false;
                    }else{
                        out.print(",");
                    }
                    out.print(tv);
                }
                out.print(">");
            }
         
     }
    
    /**
     * print list of formal parameters types in curve brackets throught comma without whitescapes
     *otherwise - do nothing.
     *i. e.:
     *<code>
     *  ()
     *  (int,java.util.List<?>)
     *</code>
     *Note, that this method used not only for cosmetic, but for identifying constructors and methods
     *within classes, in such cases. So, modify it with care. When you changing this class, you must change also
     *lass, which generate unique description of constructor or model from term.
     */
     public static void printFormalParametersSignature(PrintWriter out,JavaTopLevelBlockOwnerModel executable) {
        printFormalParametersSignature(out,executable,false);
     }

    /**
     * print list of erased formal parameters types in curve brackets throught comma without whitescapes
     *otherwise - do nothing.
     *i. e.:
     *<code>
     *  ()
     *  (int,java.util.List)
     *</code>
     *Note, that this method used not only for cosmetic, but for identifying constructors and methods
     *within classes, in such cases. So, modify it with care. When you changing this class, you must change also
     *lass, which generate unique description of constructor or model from term.
     */
     public static void printErasedFormalParametersSignature(PrintWriter out,JavaTopLevelBlockOwnerModel executable) {
        printFormalParametersSignature(out,executable,true);
     }
     

     public static void printFormalParametersSignature(PrintWriter out,JavaTopLevelBlockOwnerModel executable, boolean erased) {
        List<JavaFormalParameterModel> fps=null;
        try{
            fps=executable.getFormalParametersList();
        }catch(TermWareException ex){
            out.print("(error:"+ex.getMessage()+")");
        }catch(EntityNotFoundException ex){
            out.print("(error:"+ex.getMessage()+")");
        }
        if (fps!=null){
            out.print("(");
            boolean isFirst=true;
            for(JavaFormalParameterModel fp:fps){
                if (isFirst){
                    isFirst=false;
                }else{
                    out.print(",");
                }
                try {
                    JavaTypeModel tm = fp.getType();
                    if (fp.getModifiers().isVarArgs()) {
                        if (!erased) {
                           out.print(tm.getReferencedType().getFullName());
                        }else{
                           out.print(tm.getReferencedType().getErasedFullName()); 
                        }                        
                        out.print("...");
                    }else{
                        if (!erased) {
                           out.print(tm.getFullName());
                        }else{
                           out.print(tm.getErasedFullName()); 
                        }
                    }
                }catch(TermWareException ex){
                    out.print("(error:"+ex.getMessage()+")");
                }catch(EntityNotFoundException ex){
                    out.print("(error:"+ex.getMessage()+")");
                }
            }
            out.print(")");
        }
        
    }

     public static void printFormalParametersSignature(PrintWriter out, Term formalParametersList, List<JavaTypeVariableAbstractModel> typeParameters,  JavaTypeModel where) throws TermWareException, EntityNotFoundException {
          Term l=formalParametersList;
          boolean isFirst=true;
          out.print("(");
          while(!l.isNil()) {
              Term fp=l.getSubtermAt(0);             
              l=l.getSubtermAt(1);
              if (isFirst) {
                  isFirst=true;
              }else{
                  out.print(",");
              }
              Term modifiers=fp.getSubtermAt(0);
              Term typeTerm=fp.getSubtermAt(1);
              JavaTypeModel fpType=null;
              try {
                  fpType=JavaResolver.resolveTypeToModel(typeTerm,where,typeParameters);
              }catch(EntityNotFoundException ex){
                  ex.setFileAndLine(JUtils.getFileAndLine(fp));
                  throw ex;
              }
              out.print(fpType.getFullName());
              int intModifiers = modifiers.getSubtermAt(0).getInt();
              if ((intModifiers & JavaModifiersModel.VARARGS)!=0) {
                  out.print("...");
              }
          }
          out.print(")");
     }
    
     public static List<JavaFormalParameterModel> buildFormalParametersList(Term formalParametersList,JavaTopLevelBlockOwnerModel executable) throws TermWareException, EntityNotFoundException
    {
      ArrayList<JavaFormalParameterModel> retval = new ArrayList<JavaFormalParameterModel>();      
      int index=0;
      while(!formalParametersList.isNil()) {
          Term c = formalParametersList.getSubtermAt(0);
          formalParametersList = formalParametersList.getSubtermAt(1);
          JavaTermFormalParameterModel pm = buildFormalParameter(c,executable,index);
          retval.add(pm);
          ++index;
      }
      return retval;
    }            
     
     
     
     public static Map<String, JavaFormalParameterModel> buildFormalParametersMap(Term formalParametersList,JavaTopLevelBlockOwnerModel executable) throws TermWareException, EntityNotFoundException
    {
      TreeMap<String,JavaFormalParameterModel> retval = new TreeMap<String,JavaFormalParameterModel>();      
      int index=0;
      while(!formalParametersList.isNil()) {
          Term c = formalParametersList.getSubtermAt(0);
          formalParametersList = formalParametersList.getSubtermAt(1);
          JavaTermFormalParameterModel pm = buildFormalParameter(c,executable,index);
          retval.put(pm.getName(),pm);
          ++index;
      }
      return retval;
    }            

    private static JavaTermFormalParameterModel buildFormalParameter(Term fp,JavaTopLevelBlockOwnerModel executable,int index) throws TermWareException, EntityNotFoundException
    {
          Term modifiers = fp.getSubtermAt(0);
          Term typeTerm = fp.getSubtermAt(1);
          JavaTypeModel tm = null;
          try {            
             tm = JavaResolver.resolveTypeToModel(typeTerm,executable.getTypeModel(),executable.getTypeParameters());
          }catch(EntityNotFoundException ex){
             if (ex.getFileAndLine()==FileAndLine.UNKNOWN) {  
                ex.setFileAndLine(JUtils.getFileAndLine(fp));              
             }
             //!!!
             //  this will cause endless loop
             //LOG.info("executable signature is "+JavaTopLevelBlockOwnerModelHelper.getStringSignature(executable));
             LOG.info("executable name is "+executable.getName());
             LOG.info("executable class is "+executable.getTypeModel().getFullName());
             throw ex;    
          }
          Term vdi=fp.getSubtermAt(2);  
          String name=null;
          //System.out.println("vdi is:"+TermHelper.termToString(vdi));
          if (vdi.getName().equals("VariableDeclaratorId")) {
            int nArray = vdi.getSubtermAt(1).getInt();
            name = vdi.getSubtermAt(0).getSubtermAt(0).getString();
            while(nArray > 0) {
              tm = new JavaArrayTypeModel(tm,null);
              --nArray;
            }
          }else if(vdi.getName().equals("Identifier")){
            name = vdi.getSubtermAt(0).getString();
          }else{
              throw new AssertException("Invalid VariableDeclaratorId in formal parameters:"+TermHelper.termToString(vdi));
          }
          if ((modifiers.getSubtermAt(0).getInt() &  JavaTermModifiersModel.VARARGS)!=0) {        
              tm = new JavaArrayTypeModel(tm,null);
          }
          JavaTermFormalParameterModel pm = new JavaTermFormalParameterModel(modifiers,name,tm,executable,index);        
          return pm;
    }
    
     /**
      * build list of type parameters from term.
      *Term must be NIL or TypeParameters
      */
     public static List<JavaTypeVariableAbstractModel>  buildTypeParameters(Term tpt, JavaTypeModel typeModel) throws TermWareException
    {         
        if (tpt.isNil()) {
            return Collections.emptyList();
        }
        if (!tpt.getName().equals("TypeParameters")) {
            throw new AssertException("TypeParameters required instead "+TermHelper.termToString(tpt));
        }
        Term tl=tpt.getSubtermAt(0);
        List<JavaTypeVariableAbstractModel> retval=new LinkedList<JavaTypeVariableAbstractModel>();
        while(!tl.isNil()) {
            Term ta=tl.getSubtermAt(0);
            tl=tl.getSubtermAt(1);
            if (!ta.getName().equals("TypeParameter")) {
                throw new AssertException("TypeParameter required instead "+TermHelper.termToString(ta));
            }
            JavaTermTypeVariableModel m=new JavaTermTypeVariableModel(ta,typeModel);
            retval.add(m);
        }
        return retval;
    }
     
    public static List<JavaTypeModel>  buildThrowsList(Term throwsNameList, JavaTopLevelBlockOwnerModel executable) throws TermWareException, EntityNotFoundException
    {       
      if (throwsNameList.isNil()) {
          return Collections.emptyList();
      }  
      List<JavaTypeModel> retval = new LinkedList<JavaTypeModel>();
      try{
      Term ct = throwsNameList.getSubtermAt(0);
      while(!ct.isNil()) {
          Term nameTerm = ct.getSubtermAt(0);
          ct = ct.getSubtermAt(1);
          JavaTypeModel tm = null;
          try {            
             tm = JavaResolver.resolveTypeToModel(nameTerm,executable.getTypeModel(),executable.getTypeParameters());
          }catch(EntityNotFoundException ex){
             if (ex.getFileAndLine()==FileAndLine.UNKNOWN) {  
                ex.setFileAndLine(JUtils.getFileAndLine(nameTerm));              
             }
             LOG.info("executable signature is "+JavaTopLevelBlockOwnerModelHelper.getStringSignature(executable));
             throw ex;    
          }
          retval.add(tm);
      }
      }catch(Exception ex){
          throw new AssertException("Can't get throws from "+TermHelper.termToString(throwsNameList),ex);
      }
      return retval;
    }
     
    
    private static final Logger LOG = Logger.getLogger(JavaTopLevelBlockOwnerModelHelper.class.getName());
}
