/*
 * JavaTermNameExpressionModel.java
 *
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ua.gradsoft.javachecker.models.expressions;

import java.util.Collections;
import java.util.List;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.JUtils;
import ua.gradsoft.javachecker.models.InvalidJavaTermException;
import ua.gradsoft.javachecker.models.JavaExpressionKind;
import ua.gradsoft.javachecker.models.JavaExpressionModel;
import ua.gradsoft.javachecker.models.JavaMemberVariableModel;
import ua.gradsoft.javachecker.models.JavaModifiersModel;
import ua.gradsoft.javachecker.models.JavaPlaceContext;
import ua.gradsoft.javachecker.models.JavaResolver;
import ua.gradsoft.javachecker.models.JavaTermExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermStatementModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.javachecker.models.JavaVariableModel;
import ua.gradsoft.javachecker.models.TermUtils;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;

/**
 *Name(IdentifierList)
 *reduced to one of:
 *<ul>
 * <li> Variable:
 *   Name([$x]) -> Variable($x)
 * </li>
 * <li> Field
 *<code>
 *   Name([$x:$y]) [ isMemberVariable($x) -> Field($x,RestField($y)) ]
 *</code>
 * where
 *<code>
 *   Field($x,RestField([$y:$z])) -> Field(Field($x,$y),RestField($z))
 *   Field($x,RestField([])) -> $x
 *</code>
 * </li>
 * <li> TypeField
 *   Name([$x:$y]) [ isType($x) ] -> TypeField($x,RestTypeField($y))
 *   TypeField($x,RestTypeField([$y:$z]))
 *                     [ isNameOdNestedClass($y,$x) ] -> TypeField(Name(cons($x,$y)),RestTypeField($z))
 *                    |
 *                     [ isNameOfStaticVariable($y,$x) ] -> Field(StaticField($x,$y),RestField($z))
 *                                                      |-> error.
 *
 *                                                      !-> PackageField($x,RestPackageField([$y:$z]))
 *   Name([$x:$y]) !-> PackageField($x,$y)
 *  PackageField($x,RestPackageField([$y:$z])
 *                     [ $z=resolveClassInPackage($y,$x) ] -> TypeField(TypeRef(Name($x,$y),$z),RestTypeField($z))
 *                     !-> PackageField(cons($x,$y),RestPackageField($z))
 *
 * </li>
 *</ul>
 * @author RSSH
 */
public class JavaTermNameExpressionModel extends JavaTermExpressionModel {
    
    /** Creates a new instance of JavaTermNameExpressionModel */
    public JavaTermNameExpressionModel(Term t, JavaTermStatementModel st, JavaTypeModel enclosedType) throws TermWareException {
        super(t,st,enclosedType);
    }
    
    public JavaExpressionKind  getKind() {
        return JavaExpressionKind.NAME; }
    
    public boolean isType() throws TermWareException, EntityNotFoundException {
        lazyInitProxy();
        return proxy_.isType();
    }
    
    public JavaTypeModel getType()  throws TermWareException, EntityNotFoundException {
        lazyInitProxy();
        return proxy_.getType();
    }
    
    
    public List<JavaExpressionModel> getSubExpressions() throws TermWareException, EntityNotFoundException {
        lazyInitProxy();
        return Collections.<JavaExpressionModel>singletonList(proxy_);
    }
    
    /**
     *@return model term of proxy
     */
    public Term getModelTerm() throws TermWareException, EntityNotFoundException {
        lazyInitProxy();
        return proxy_.getModelTerm();
    }
    
    private void lazyInitProxy() throws TermWareException, EntityNotFoundException {        
        Term list=t_.getSubtermAt(0);

        boolean debug=false;
        
        Term dbgCurr=list;
        String n1=null;
        String n2=null;
        String n3=null;
        if (dbgCurr.getArity()==2) {
            Term dbgT1=dbgCurr.getSubtermAt(0);
            n1=dbgT1.getSubtermAt(0).getString();
            dbgCurr=dbgCurr.getSubtermAt(1);
            if (dbgCurr.getArity()==2) {
                Term dbgT2=dbgCurr.getSubtermAt(0);
                n2=dbgT2.getSubtermAt(0).getString();
                dbgCurr=dbgCurr.getSubtermAt(1);
                if (dbgCurr.getArity()==2){
                    Term dbgT3=dbgCurr.getSubtermAt(0);
                    n3=dbgT3.getSubtermAt(0).getString();
                }
            }
        } 
        if (n1!=null && n2!=null && n3!=null) {
            if (n1.equals("p") && n2.equals("parent") && n3.equals("left")) {
                debug=true;
                System.out.println("our!!");
            }
        }
        
        
        // sink, is this one.
        if (list.getSubtermAt(1).isNil()) {
            proxy_ = new JavaTermIdentifierExpressionModel(list.getSubtermAt(0),getTermStatementModel(),getEnclosedType());
        }else{
            Term idt = list.getSubtermAt(0);
            String name = idt.getSubtermAt(0).getString();
            JavaPlaceContext ctx = createPlaceContext();
            // try to resolve as variable.
            try {
                JavaVariableModel vm = JavaResolver.resolveVariableByName(name,ctx);
                JavaTermExpressionModel current = new JavaTermIdentifierExpressionModel(list.getSubtermAt(0),getTermStatementModel(),getEnclosedType());
                buildField(vm,current,list.getSubtermAt(1),ctx,debug);
                return;
            }catch(EntityNotFoundException ex){
                // do nothing.
                ;
            }
            try {
                JavaTypeModel tm = JavaResolver.resolveTypeModelByName(name,ctx);
                JavaTermTypeExpressionModel current = new JavaTermTypeNameExpressionModel(tm,idt,getTermStatementModel(),getEnclosedType());
                buildTypeField(current,list.getSubtermAt(1),ctx);
                return;
            }catch(EntityNotFoundException ex){
                //do nothing
                ;
            }
            // get package.
            StringBuilder sb=new StringBuilder();
            sb.append(name);
            list=list.getSubtermAt(1);
            while(!list.isNil()) {
                Term idtc = list.getSubtermAt(0);
                String cname = idtc.getSubtermAt(0).getString();
                list=list.getSubtermAt(1);
                try {
                    String packageName=sb.toString();
                    JavaTypeModel tm = JavaResolver.resolveTypeModelFromPackage(cname,packageName);
                    Term packageNameTerm = TermUtils.createString(packageName);
                    Term nct = TermUtils.createTerm("FullTypeName",packageNameTerm,idtc);
                    JavaTermTypeExpressionModel current = new JavaTermTypeNameExpressionModel(tm,nct,getTermStatementModel(),getEnclosedType());
                    buildTypeField(current,list,ctx);
                    return;
                }catch(EntityNotFoundException ex){
                    if (debug) {
                        ex.printStackTrace();
                    }
                    ;
                }
                sb.append(".");
                sb.append(cname);
            }
        }
        EntityNotFoundException ex = new EntityNotFoundException("variable or type or field",TermHelper.termToString(t_)," in expression");
        ex.setFileAndLine(JUtils.getFileAndLine(t_));
        throw ex;
    }
    
    private void buildField(JavaVariableModel vm, JavaTermExpressionModel current, Term rest, JavaPlaceContext ctx, boolean debug)  throws TermWareException, EntityNotFoundException {
        
        if (debug) {
            System.out.println("buildFiled, rest="+TermHelper.termToString(rest)+", current="+TermHelper.termToString(current.getTerm()));
            
        }
        
        if (rest.isNil()) {
            proxy_=current;
        }else{
            Term idt = rest.getSubtermAt(0);
            String name = idt.getSubtermAt(0).getString();
            rest=rest.getSubtermAt(1);
            JavaTypeModel vmType = vm.getTypeModel();
            if (debug) {
                System.out.println("vmType="+vmType.getFullName());
            }
            JavaMemberVariableModel nextVm = JavaResolver.resolveMemberVariableByName(name,vmType);
            if (debug) {
                System.out.println("resolved next vm, kind="+nextVm.getKind());
                System.out.println("type="+nextVm.getKind());
            }
            
            Term fieldTerm = TermUtils.createTerm("Field",current.getTerm(),idt);
            JavaTermExpressionModel nextCurrent = new JavaTermFieldExpressionModel(current, fieldTerm, getTermStatementModel(),getEnclosedType());
            buildField(nextVm,nextCurrent,rest,ctx,debug);
        }
    }
    
    private void buildTypeField(JavaTermTypeExpressionModel current,Term rest,JavaPlaceContext ctx)  throws TermWareException, EntityNotFoundException {
        if (rest.isNil()) {
            proxy_=current;
        }else{
            Term idt = rest.getSubtermAt(0);
            String name = idt.getSubtermAt(0).getString();
            rest = rest.getSubtermAt(1);
            try {
                JavaMemberVariableModel mv = JavaResolver.resolveMemberVariableByName(name,current.getType());
               
                JavaModifiersModel mvm=mv.getModifiersModel();
                JavaTypeModel mvo = mv.getOwner();               
                if (mvm.isStatic()||mvo.isInterface()||mvo.isEnum()) {
                    Term nct = TermUtils.createTerm("StaticField",current.getTerm(),idt);
                    JavaTermExpressionModel nc = new JavaTermStaticFieldExpressionModel(mv,nct,getTermStatementModel(),getEnclosedType());
                    buildField(mv,nc,rest,ctx,false);
                    return;
                }else{
                    throw new InvalidJavaTermException("Field "+name+" must be static in "+current.getType().getName(),t_);
                }
            }catch(EntityNotFoundException ex){
                /* try next approach */
                ;
            }
            try {
                JavaTypeModel tm1 = JavaResolver.resolveTypeToModel(idt,current.getType());
                Term nct = TermUtils.createTerm("NestedType",current.getTerm(),idt);
                JavaTermTypeExpressionModel nc = new JavaTermNestedTypeExpressionModel(tm1,nct,getTermStatementModel(),getEnclosedType());
                buildTypeField(nc,rest,ctx);
                return;
            }catch(EntityNotFoundException ex){
                ;
            }
        }
    }
    
    private JavaTermExpressionModel proxy_=null;
}
