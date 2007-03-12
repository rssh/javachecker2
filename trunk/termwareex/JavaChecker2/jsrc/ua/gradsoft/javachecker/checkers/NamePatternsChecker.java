/*
 * NamePatternsChecker.java
 *
 */

package ua.gradsoft.javachecker.checkers;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import ua.gradsoft.javachecker.ConfigException;
import ua.gradsoft.javachecker.ITermVisitor;
import ua.gradsoft.javachecker.JavaFacts;
import ua.gradsoft.javachecker.Main;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.javachecker.models.JavaEnumConstantModel;
import ua.gradsoft.javachecker.models.JavaMemberVariableModel;
import ua.gradsoft.javachecker.models.JavaMethodModel;
import ua.gradsoft.javachecker.models.JavaModelConstants;
import ua.gradsoft.javachecker.models.JavaTermEnumConstantModel;
import ua.gradsoft.javachecker.models.JavaTermEnumModel;
import ua.gradsoft.javachecker.models.JavaTermMemberVariableModel;
import ua.gradsoft.javachecker.models.JavaTermMethodModel;
import ua.gradsoft.javachecker.models.JavaTermTypeAbstractModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.javachecker.models.JavaTypeVariableAbstractModel;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHolder;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *
 * @author RSSH
 */
public class NamePatternsChecker implements JavaTypeModelProcessor {
    
    
    public void process(JavaTermTypeAbstractModel typeModel, JavaFacts facts) throws TermWareException {
        JavaTermTypeAbstractModel ttm = typeModel;
        if (ttm.isClass() || ttm.isInterface() ) {
            if (!ttm.isAnonimous()) {
                checkClassNamePatterns(ttm,facts);
            }
            if (ttm.hasTypeParameters()) {
                List<JavaTypeVariableAbstractModel> tvs = ttm.getTypeParameters();
                for(JavaTypeVariableAbstractModel v: tvs) {
                    checkTypeVariableNamePattern(v.getName(),facts,ttm.getTerm());
                }
            }
        }else if (ttm.isEnum()) {
            checkClassNamePatterns(ttm,facts);
            if (ttm instanceof JavaTermEnumModel) {
                checkEnumConstantNamePatterns((JavaTermEnumModel)ttm,facts);
            }
        }else if (ttm.isAnnotationType()) {
            checkClassNamePatterns(ttm,facts);
        }
        for(JavaMemberVariableModel v: ttm.getMemberVariableModels().values() ) {
            if (v instanceof JavaTermMemberVariableModel){
                checkMemberVariableNamePatterns((JavaTermMemberVariableModel)v,facts);
            }
        }
        try {
            for(List<JavaMethodModel> lm: ttm.getMethodModels().values()) {
                for(JavaMethodModel m: lm) {
                    if (m instanceof JavaTermMethodModel) {
                        JavaTermMethodModel mt=(JavaTermMethodModel)m;
                        checkMethodNamePatterns(mt,facts);
                        for(JavaTypeVariableAbstractModel v: mt.getTypeParameters()) {
                            checkTypeVariableNamePattern(v.getName(),facts,mt.getTerm());
                        }
                    }
                }
            }
        }catch(NotSupportedException ex){
            // this means, that target type has not method models. do nothing.
            ;
        }
    }
    
    public void configure(JavaFacts facts) throws ConfigException, TermWareException {
        facts.getViolations().addType("NonFinalPublicFileds","style","warn about non-final public field",true);
    }
    
    public void checkClassNamePatterns(JavaTermTypeAbstractModel tm, JavaFacts facts) throws TermWareException {
        if (!tm.getName().matches(facts.getClassNamePattern())) {
            facts.violationDiscovered("ClassNamePatterns","bad classname pattern",tm.getTerm());
        }
    }
    
    public void checkTypeVariableNamePattern(String name,JavaFacts facts, Term t) throws TermWareException {
        if (!name.matches(facts.getTypeArgumentNamePattern())) {
            facts.violationDiscovered("TypeArgumentNamePatterns","violation of type argument name pattern ",t );
        }
    }
    
    public void checkEnumConstantNamePatterns(JavaTermEnumModel tm,JavaFacts facts) throws TermWareException {
        for(Map.Entry<String,JavaEnumConstantModel> e: tm.getEnumConstantModels().entrySet()) {
            JavaEnumConstantModel en = e.getValue();
            if (!en.getName().matches(Main.getFacts().getEnumConstantNamePattern())) {
                Term t=null;
                if (en instanceof JavaTermEnumConstantModel) {
                    t = ((JavaTermEnumConstantModel)en).getIdentifierTerm();
                }else{
                    t = tm.getTerm();
                }
                facts.violationDiscovered("EnumConstantNamePatterns","enum constant does not match pattern",t);
            }
        }
    }
    
    
    public void checkMemberVariableNamePatterns(JavaTermMemberVariableModel v, JavaFacts facts) throws TermWareException {
        if (v.getModifiersModel().isFinal()) {
            if (!v.getName().matches(facts.getFinalFieldNamePattern())) {
                facts.violationDiscovered("VariablePatterns", "final field name pattern violation", v.getVariableDeclaratorTerm());
            }
        }else{
            if (!v.getName().matches(facts.getNonFinalFieldNamePattern())) {
                facts.violationDiscovered("VariablePatterns", "non-final field name pattern violation", v.getVariableDeclaratorTerm());
            }
        }
        if (facts.isCheckEnabled("NonFinalPublicFields")) {
            if (v.getModifiersModel().isPublic()) {
                if (!v.getModifiersModel().isFinal()) {
                    facts.violationDiscovered("NonFinalPublicFields","non-final public field discovered",v.getVariableDeclaratorTerm());
                }
            }
        }
    }
    
    /**
     * check method name patterns.
     */
    public void checkMethodNamePatterns(JavaTermMethodModel m, final JavaFacts facts) throws TermWareException {
        if (!m.getName().matches(facts.getMethodNamePattern())) {
            facts.violationDiscovered("MethodNamePatterns","bad name of method", m.getTerm());
        }
        boolean retval=true;
        TermHolder termHolder=new TermHolder(TermWare.getInstance().getTermFactory().createBoolean(retval));
        visitFormalParameterIdentifiers(m.getFormalParametersTerm(),
                new ITermVisitor() {
            public boolean doFirst(Term t, TermHolder result, HashSet<Term> trace) throws TermWareException {
                if (t.getName().equals("Identifier")) {
                    checkFormalParameterName(t,t.getSubtermAt(0).getName(),result);
                }else if (t.getName().equals("java_array_declarator")) {
                    checkFormalParameterName(t.getSubtermAt(0),t.getSubtermAt(0).getSubtermAt(0).getName(),result);
                }else{
                    throw new AssertException("first subterm of formal parameter must have name java_identifier or java_array_declarator");
                }
                return true;
            }
            public boolean doSecond(Term t, TermHolder result, HashSet<Term> trace) {
                return true;}
            
            private void checkFormalParameterName(Term t,String formalParameterName,TermHolder result) throws TermWareException {
                if (!formalParameterName.matches(facts.getLocalVariableNamePattern())) {
                    facts.violationDiscovered("MethodNamePatterns","bad formal parameter name",t);
                    result.setValue(TermWare.getInstance().getTermFactory().createBoolean(false));
                }
            }
            
        },termHolder,null);
        
        visitLocalVariableDeclarators(m.getMethodBody(),new ITermVisitor() {
            public boolean doFirst(Term t, TermHolder result, HashSet<Term> trace) throws TermWareException {
                Term nameTerm=t.getSubtermAt(0);
                String variableName=nameTerm.getString();
                if (!variableName.matches(facts.getLocalVariableNamePattern())) {
                    facts.violationDiscovered("VariablePatterns","violation of variable name conventions",t);
                    result.setValue(TermWare.getInstance().getTermFactory().createBoolean(false));
                }
                return true;
            }
            
            public boolean doSecond(Term t, TermHolder result, HashSet<Term> trace) {
                return true; }
        },termHolder,null);
        
    }
    
    
    
    public static void visitLocalVariableDeclarators(Term t,ITermVisitor visitor, TermHolder result, HashSet<Term> trace) throws TermWareException {
        if (t.isComplexTerm()) {
            if (t.getName().equals("LocalVariableDeclaration")) {
                Term type=t.getSubtermAt(JavaModelConstants.LOCALVARIABLEDECLARATION_TYPE_INDEX);
                Term variableDeclaratorsList=t.getSubtermAt(JavaModelConstants.LOCALVARIABLEDECLARATION_VARIABLEDECLARATORS_INDEX);
                Term current=variableDeclaratorsList;
                while(!current.isNil()) {
                    Term variableDeclarator=current.getSubtermAt(0);
                    Term variableDeclaratorId=variableDeclarator.getSubtermAt(0);
                    Term ident=variableDeclaratorId.getSubtermAt(0);
                    visitor.doFirst(ident,result,trace);
                    current=current.getSubtermAt(1);
                    // now vizit in initialization clause ?
                    //  (can be exists subblocks ?.  TODO: check syntax)
                    //Term iniclause=variableDeclarator.getSubtermAt(1);
                }
            }else{
                for(int i=0; i<t.getArity(); ++i) {
                    visitLocalVariableDeclarators(t.getSubtermAt(i),visitor,result,trace);
                }
            }
        }
    }
    
    
    private static void  visitFormalParameterIdentifiers(Term t, ITermVisitor visitor,TermHolder result,HashSet<Term> hs) throws TermWareException {
        //t=getFormalParametersTerm();
        while(!t.isNil()) {
            if (t.getName().equals("cons")) {
                Term formalParameter=t.getSubtermAt(0);
                Term variableDeclaratorId=formalParameter.getSubtermAt(JavaModelConstants.FORMAL_PARAMETER_VARIABLEDECLARATORID_INDEX);
                Term identifierTerm=variableDeclaratorId.getSubtermAt(0);
                visitor.doFirst(identifierTerm, result, hs);
                t=t.getSubtermAt(1);
            }else{
                throw new AssertException("formal parameters must be a list");
            }
        }
    }
    
    
    
    
}
