/*
 * JavaMemberVariableModel.java
 *
 * Created on понеділок, 23, лютого 2004, 10:59
 */

package ua.kiev.gradsoft.JavaChecker;

import java.util.*;

import ua.kiev.gradsoft.TermWare.*;
import ua.kiev.gradsoft.TermWare.exceptions.*;

/**
 *Model for java Member variable.
 * @author  Ruslan Shevchenko
 */
public class JavaMemberVariableModel {
    
    /** Creates a new instance of JavaMemberVariableModel */
    public JavaMemberVariableModel(ITerm t,JavaTypeModel owner,ITerm type,ITerm properties)  throws TermWareException
    {
        if (!t.getName().equals("java_variable_declarator")) {
            throw new AssertException("argument of JavaMemberVariableModel constructor must be java_variable_declarator");
        }
        ITerm varTerm=t.getSubtermAt(0);
        ITerm identifierTerm;
        if (varTerm.getName().equals("java_identifier")) {
           identifierTerm=varTerm;
           isArray_=false;
        }else if(varTerm.getName().equals("java_array_declarator")) {
            identifierTerm=varTerm.getSubtermAt(0);
            isArray_=true;
            arrayDimension_=varTerm.getSubtermAt(1).getInt();
        }else{
            throw new AssertException("invalid first term in java_variable_declarator");
        }
        name_=identifierTerm.getSubtermAt(0).getString();
        typeModel_=owner;
        type_=type;
        accessLevelModel_=new JavaAccessLevelModel(properties);
        isStatic_=JUtils.getIsStatic(properties);
        isFinal_=JUtils.getIsFinal(properties);
        t_=t;
    }
    
    public String getMemberVariableName()
    { return name_; }
    
    JavaFacts getFacts()
    { return typeModel_.getFacts(); }
    
    public boolean check() throws TermWareException
    {
      boolean retval=true;
      if (getFacts().isCheckVariablePatterns()) {
          if (isFinal_) {
              if (!name_.matches(getFacts().getFinalFieldNamePattern())) {
                  getFacts().finalFieldNamePatternViolationDiscovered(t_);
                  retval=false;
              }
          }else{
              if (!name_.matches(getFacts().getNonFinalFieldNamePattern())) {
                  getFacts().nonFinalFieldNamePatternViolationDiscovered(t_);
                  retval=false;
              }
          }
      }
      if (getFacts().isCheckNonFinalPublicFields()) {
          if (accessLevelModel_.isPublic()) {
              if (!isFinal_) {
                  getFacts().nonFinalPublicFieldDiscovered(t_);
                  retval=false;
              }
          }
      }
      if (getFacts().isCheckSynchronizeViolations()) {
          String setterName=JUtils.generateSetterName(name_);
          boolean synchronizedSetterFound=false;
          try {
              Collection cn=typeModel_.findMethodModels(setterName);
              Iterator it=cn.iterator();
              boolean unsynchronizedSetterFound=false;
              while(it.hasNext()) {
                  Object o=it.next();
                  JavaMethodModel methodModel=(JavaMethodModel)o;
                  if (methodModel.isSynchronized()) {
                      synchronizedSetterFound=true;
                  }else{
                      unsynchronizedSetterFound=true;
                  }
              }
          }catch(EntityNotFoundException ex){
              ; // all is ok, we just have no setter.
          }catch(NotSupportedException ex){
              throw new AssertException("impossible - method model properties is not supported");
          }
          if (synchronizedSetterFound) {
              retval &= Main.getSynchronizeViolationChecker().check(typeModel_,name_);
          }
      }
      return retval;
    }
    
    private ITerm   t_;
    private String  name_;
    private ITerm   type_;
    private JavaTypeModel typeModel_=null;
    private ITerm   propertiesList_;
    private JavaAccessLevelModel  accessLevelModel_;
    private boolean isStatic_;
    private boolean isFinal_;
    private boolean isArray_;
    private int     arrayDimension_;
    
}
