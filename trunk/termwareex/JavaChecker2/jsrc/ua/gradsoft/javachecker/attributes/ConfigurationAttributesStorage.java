/*
 * ConfigurationAttributesStorage.java
 *
 *
 */

package ua.gradsoft.javachecker.attributes;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.JUtils;
import ua.gradsoft.javachecker.JavaFacts;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.javachecker.models.InvalidJavaTermException;
import ua.gradsoft.javachecker.models.JavaConstructorModel;
import ua.gradsoft.javachecker.models.JavaMethodModel;
import ua.gradsoft.javachecker.models.JavaResolver;
import ua.gradsoft.javachecker.models.JavaTermAnnotationTypeModel;
import ua.gradsoft.javachecker.models.JavaTermClassOrInterfaceModel;
import ua.gradsoft.javachecker.models.JavaTermConstructorModel;
import ua.gradsoft.javachecker.models.JavaTermEnumModel;
import ua.gradsoft.javachecker.models.JavaTermMethodModel;
import ua.gradsoft.javachecker.models.JavaTopLevelBlockOwnerModelHelper;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.javachecker.models.JavaTypeVariableAbstractModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermStringLiteralExpressionModel;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Storage for configuration of use-supplied type attributes,
 *which are readed from configuration file.
 * @author rssh
 */
public class ConfigurationAttributesStorage {
    
    
    public ConfigurationAttributesStorage(JavaFacts facts)
    {
      facts_=facts;
      dirs_=new LinkedList<String>();
    }
    
    public List<String>  getPropertyDirs()
    { return dirs_; }
    
    public AttributesData readConfiguratedAttributes(JavaTypeModel tm) throws TermWareException, EntityNotFoundException {
        String packageName = tm.getPackageModel().getName();
        String className = tm.getName();
        AttributesData data = new AttributesData();
        for(String basedirname: dirs_) {
            String dirname = JUtils.createDirectoryNameFromPackageName(basedirname,packageName);
            String rfname = JUtils.createSourceFileNameFromClassName(className,".javacfg");
            String fname = dirname+File.separator+rfname;
            File f = new File(fname);
           // LOG.info("check for file "+f.getAbsolutePath());
            if (f.exists()) {
             //   LOG.info("exists, reading");
                Term cu = JUtils.readSourceFile(f);
                fillData(tm,cu,data,fname);
            }else{
              //  LOG.info("does not exists");
            }
        }
        return data;
    }
    
    
    
    private void fillData(JavaTypeModel typeModel,Term cu, AttributesData data,String fname)
    throws TermWareException,EntityNotFoundException {
        String packageName = JUtils.getCompilationUnitPackageName(cu);
        if (!packageName.equals(typeModel.getPackageModel().getName())) {
            throw new AssertException("package name in "+fname+" must be "+typeModel.getPackageModel().getName()+", have "+packageName);
        }
        for(int i=0; i<cu.getArity(); ++i) {
            Term ct = cu.getSubtermAt(i);
            if (ct.getName().equals("PackageDeclaration")) {
                // skip.
            }else if (ct.getName().equals("ImportDeclaration")) {
                // skip
            }else if (ct.getName().equals("TypeDeclaration")) {
                fillTypeData(typeModel,ct,data,null);
            }else{
                throw new InvalidJavaTermException("Packade, Import or TypeDeclarations needed.",ct);
            }
            
        }
    }
    
    
    private void fillTypeData(JavaTypeModel tm, Term td, AttributesData data, JavaTypeModel enclosed)
    throws TermWareException,EntityNotFoundException {
        Term modifiers = td.getSubtermAt(0);
        Term dcl = td.getSubtermAt(1);        
        if (dcl.getName().equals("ClassOrInterfaceDeclaration")) {
            fillClassOrInterfaceDeclarationData(tm,modifiers,dcl,data,enclosed,0);
        }else if(dcl.getName().equals("EnumDeclaration")) {
            fillEnumDeclarationData(tm,modifiers,dcl,data,enclosed,0);
        }else if(dcl.getName().equals("AnnotationTypeDeclaration")){
            fillAnnotationTypeDeclarationData(tm,modifiers,dcl,data,enclosed,0);
        }else{
            throw new InvalidJavaTermException("ClassOrInterfaceDeclaration|EnumDeclaration|AnnotationTypeDeclaration is required",dcl);
        }
    }
    
    private void fillClassOrInterfaceDeclarationData(JavaTypeModel tm, Term modifiers, Term dcl, AttributesData data, JavaTypeModel enclosed, int level)
    throws TermWareException, EntityNotFoundException {
        Term identifierTerm = dcl.getSubtermAt(JavaTermClassOrInterfaceModel.NAME_IDENTIFIER_TERM_INDEX);
        String name = identifierTerm.getSubtermAt(0).getString();
        JavaTypeModel tm1 = determinateTypeModel(tm,name,level);
        AttributesData tm1Data=null;
        if (level==0) {
            if (tm1==tm) {
                // (yes, by reference. This is not error)
                // all is ok, skipping
                tm1Data=data;
            }else{
                throw new AssertException("Checker properties for "+tm1.getFullName()+" is defined in file, where must be "+tm.getFullName());
            }
        }else{
            tm1Data = data.getOrCreateChild(name);
        }
        fillAttributesFromModifiers(modifiers,tm1Data);
        Term classOrInterfaceBody = dcl.getSubtermAt(JavaTermClassOrInterfaceModel.CLASS_OR_INTERFACE_BODY_INDEX);
        Term l = classOrInterfaceBody.getSubtermAt(0);
        while(!l.isNil()) {
            Term ct = l.getSubtermAt(0);
            l=l.getSubtermAt(1);
            fillClassOrInterfaceBodyDeclarationData(tm1,ct,tm1Data,enclosed,level);
        }
    }
    
    private void fillClassOrInterfaceBodyDeclarationData(JavaTypeModel tm, Term dcl, AttributesData data, JavaTypeModel enclosed, int level)
    throws TermWareException,EntityNotFoundException {
        if (dcl.getArity()==0) {
            return;
        }
        Term t0=dcl.getSubtermAt(0);
        if (t0.getName().equals("Initializer")){
            // useless, but - skip.
        }else{
            Term modifiers = t0;
            Term t1=dcl.getSubtermAt(1);
            if (t1.getName().equals("ClassOrInterfaceDeclaration")) {
                fillClassOrInterfaceDeclarationData(tm,modifiers,t1,data,tm,level+1);
            }else if (t1.getName().equals("EnumDeclaration")){
                fillEnumDeclarationData(tm,modifiers,t1,data,tm,level+1);
            }else if (t1.getName().equals("ConstructorDeclaration")){
                fillConstructorDeclarationData(tm,modifiers,t1,data,level+1);
            }else if (t1.getName().equals("FieldDeclaration")){
                fillFieldDeclarationData(tm,modifiers,t1,data,level+1);
            }else if (t1.getName().equals("MethodDeclaration")){
                fieldMethodDeclarationData(tm,modifiers,t1,data,level+1);
            }else{
                throw new InvalidJavaTermException("invalid context of ClassOrInterfaceBodyDeclaration",t1);
            }
        }
    }
    
    private void fillEnumDeclarationData(JavaTypeModel tm, Term modifiers, Term dcl, AttributesData data, JavaTypeModel enclosed, int level)
    throws TermWareException, EntityNotFoundException {
        Term identifier = dcl.getSubtermAt(JavaTermEnumModel.IDENTIFIER_TERM_INDEX);
        String name = identifier.getSubtermAt(0).getString();
        JavaTypeModel tm1 = determinateTypeModel(tm,name,level);
        AttributesData eData=null;
        if (level==0) {
            eData=data;
        }else{
            eData=data.getOrCreateChild(name);
        }
        fillAttributesFromModifiers(modifiers,eData);
        Term enumBody = dcl.getSubtermAt(JavaTermEnumModel.ENUMBODY_TERM_INDEX);
        Term l = enumBody.getSubtermAt(0);
        while(!l.isNil()) {
            Term ct = l.getSubtermAt(0);
            l=l.getSubtermAt(1);
            if (ct.getName().equals("EnumConstant")) {
                // skip,
            }else if (ct.getName().equals("ClassOrInterfaceBodyDeclaration")){
                fillClassOrInterfaceBodyDeclarationData(tm1,ct,eData,enclosed,level+1);
            }else{
                throw new InvalidJavaTermException("EnumConstant|ClassOrInterfaceBody required",ct);
            }
        }
    }
    
    
    private void fillAnnotationTypeDeclarationData(JavaTypeModel tm, Term modifiers, Term dcl, AttributesData data, JavaTypeModel enclosed, int level)
    throws TermWareException, EntityNotFoundException  {
        Term identifier=dcl.getSubtermAt(JavaTermAnnotationTypeModel.NAME_TERM_INDEX);
        String name = identifier.getSubtermAt(0).getString();
        JavaTypeModel tm1=determinateTypeModel(tm,name,level);
        AttributesData data1 = null;
        if (level==0) {
            data1=data;
        }else{
            data1=data.getOrCreateChild(name);
        }
        fillAttributesFromModifiers(modifiers,data1);
        Term annotationBody = dcl.getSubtermAt(JavaTermAnnotationTypeModel.BODY_TERM_INDEX);
        Term l = annotationBody.getSubtermAt(0);
        while(!l.isNil()) {
            Term ct=l.getSubtermAt(0);
            l=l.getSubtermAt(1);
            Term modifiers1=l.getSubtermAt(0);
            Term t1=l.getSubtermAt(1);
            if (t1.getName().equals("ClassOrInterfaceDeclaration")) {
                fillClassOrInterfaceDeclarationData(tm1,modifiers1,t1,data1,tm,level+1);
            }else if (t1.getName().equals("EnumDeclaration")){
                fillEnumDeclarationData(tm1,modifiers1,t1,data1,tm,level+1);
            }else if (t1.getName().equals("AnnotationTypeDeclaration")){
                fillAnnotationTypeDeclarationData(tm1,modifiers1,t1,data1,tm,level+1);
            }else if (t1.getName().equals("FieldDeclaration")) {
                fillFieldDeclarationData(tm1,modifiers1,t1,data1,level+1);
            }else{
                //do nothing, skip.
            }
        }
    }
    
    private void fillConstructorDeclarationData(JavaTypeModel tm, Term modifiers, Term dcl, AttributesData data, int level) throws TermWareException, EntityNotFoundException
    {
        Term tp = dcl.getSubtermAt(JavaTermConstructorModel.TYPE_PARAMETERS_TERM_INDEX);
        Term tidentifier = dcl.getSubtermAt(JavaTermConstructorModel.IDENTIFIER_TERM_INDEX);
        Term tfps = dcl.getSubtermAt(JavaTermConstructorModel.FORMAL_PARAMETERS_TERM_INDEX);
        
        //JavaTermConstructorModel executable = new JavaTermConstructorModel(modifiers,dcl,tm);
        StringWriter sSignatureWriter=new StringWriter();
        PrintWriter signatureWriter=new PrintWriter(sSignatureWriter);
        
        
        List<JavaTypeVariableAbstractModel> tpvs = JavaTopLevelBlockOwnerModelHelper.buildTypeParameters(tp,tm);
        JavaTopLevelBlockOwnerModelHelper.printTypeParametersSignature(signatureWriter,tpvs);
        signatureWriter.print(tm.getFullName());
        JavaTopLevelBlockOwnerModelHelper.printFormalParametersSignature(signatureWriter,tfps.getSubtermAt(0),tpvs,tm);
        signatureWriter.flush();
        
        String signature = sSignatureWriter.toString();
        
        
        JavaConstructorModel foundConstructor = null;
        for(JavaConstructorModel cm: tm.getConstructorModels()) {
            String cmSignature = JavaTopLevelBlockOwnerModelHelper.getStringSignature(cm);
            if (cmSignature.equals(signature)) {
                foundConstructor=cm;
                break;
            }
        }
        if (foundConstructor==null) {
            throw new InvalidJavaTermException("constructor with signature "+signature+" is not found in class "+tm.getFullName(),dcl);
        }
        AttributesData cnData = data.getChilds().get(signature);
        if (cnData==null) {
            cnData = new AttributesData();
            data.getChilds().put(signature,cnData);
        }
        fillAttributesFromModifiers(modifiers,cnData);
    }
    
    void fillFieldDeclarationData(JavaTypeModel tm, Term modifiers, Term dcl, AttributesData data,int level) throws TermWareException {
        // Samr modifiers for all fields.
        Term type = dcl.getSubtermAt(0);
        Term vdcll = dcl.getSubtermAt(1);
        while(!vdcll.isNil()) {
            Term vd = vdcll.getSubtermAt(0);
            vdcll = vdcll.getSubtermAt(1); 
            Term vdi=null;
            if (vd.getName().equals("VariableDeclaratorId")) {
                vdi=vd;
            }else if (vd.getName().equals("VariableDeclarator")) {
                vdi = vd.getSubtermAt(0);
            }else{
                throw new InvalidJavaTermException("VariableDeclarator|VariableDeclaratorId is required",dcl);
            }
            Term identifier=vdi.getSubtermAt(0);
            String fieldName = null;
            try {
               fieldName=identifier.getSubtermAt(0).getString();
            }catch(Exception ex){
                throw new InvalidJavaTermException("QQQ",vd);
            }
            AttributesData fData = data.getChilds().get(fieldName);
            if (fData==null) {
                fData = new AttributesData();
                data.getChilds().put(fieldName,fData);
            }
            fillAttributesFromModifiers(modifiers,fData);
        }
    }
    
    
    void fieldMethodDeclarationData(JavaTypeModel tm, Term modifiers, Term dcl, AttributesData data, int level)
    throws TermWareException, EntityNotFoundException {
        Term tpt = dcl.getSubtermAt(JavaTermMethodModel.TYPE_PARAMETERS_TERM_INDEX);
        Term mdt = dcl.getSubtermAt(JavaTermMethodModel.METHOD_DECLARATOR_INDEX);
        
        Term identifier = mdt.getSubtermAt(JavaTermMethodModel.METHOD_DECLARATOR__IDENTIFIER_INDEX);
        String name = identifier.getSubtermAt(0).getName();
        Term fpls = mdt.getSubtermAt(JavaTermMethodModel.METHOD_DECLARATOR__FORMAL_PARAMETERS_INDEX);
        
        
        StringWriter sw=new StringWriter();
        PrintWriter pwr=new PrintWriter(sw);
        
        List<JavaTypeVariableAbstractModel> tpvs = JavaTopLevelBlockOwnerModelHelper.buildTypeParameters(tpt,tm);
        JavaTopLevelBlockOwnerModelHelper.printTypeParametersSignature(pwr,tpvs);
        pwr.print(name);
        JavaTopLevelBlockOwnerModelHelper.printFormalParametersSignature(pwr,fpls.getSubtermAt(0),tpvs,tm);
        pwr.flush();
        
        
        String signature = sw.toString();
        pwr.close();
        
        List<JavaMethodModel>  methodModels=null;
        try{
            methodModels = tm.findMethodModels(name);
        }catch(EntityNotFoundException ex){
            ex.setFileAndLine(JUtils.getFileAndLine(dcl));
            throw ex;
        }catch(NotSupportedException ex){
            methodModels = Collections.emptyList();
        }
        
        JavaMethodModel foundMethod=null;
        for(JavaMethodModel m: methodModels) {
            String mSignature = JavaTopLevelBlockOwnerModelHelper.getStringSignature(m);
            if(signature.equals(mSignature)) {
                foundMethod=m;
                break;
            }
        }
        
        
        if (foundMethod==null) {
            throw new InvalidJavaTermException("Can't fimnd method with signature "+signature,dcl);
        }
        
        AttributesData mData = data.getChilds().get(signature);
        if (mData==null) {
            mData=new AttributesData();
            data.getChilds().put(signature,mData);
        }
        fillAttributesFromModifiers(modifiers,mData);
    }
    
    
    private JavaTypeModel determinateTypeModel(JavaTypeModel where, String name, int level) throws TermWareException, EntityNotFoundException {
        JavaTypeModel retval;
        if (level==0)  {
            if (where.getName().equals(name)){
                retval=where;
            }else{
                retval = JavaResolver.resolveTypeModelByName(name,where,null,null);
            }
        }else{
            retval = JavaResolver.resolveTypeModelByName(name,where,null,null);
        }
        return retval;
    }
    
    
    
    
    private void fillAttributesFromModifiers(Term modifiers, AttributesData attributes) throws TermWareException {
        // get checker proterty annotations
        if (modifiers.getArity()<2) {
            return;
        }else{
            Term annotationsList = modifiers.getSubtermAt(1);
            while(!annotationsList.isNil()) {
                Term annotation=annotationsList.getSubtermAt(0);
                annotationsList=annotationsList.getSubtermAt(1);
                Term nameTerm = annotation.getSubtermAt(0).getSubtermAt(0);                
                String name = null;
                if (nameTerm.getName().equals("Identifier")) {
                    name=nameTerm.getSubtermAt(0).getString();
                }else if (nameTerm.getName().equals("Name")) {
                    name=JUtils.getJavaNameLastComponentAsString(nameTerm);
                }else{
                    throw new InvalidJavaTermException("Name expected",nameTerm);
                }
                if (name.equals("CheckerProperties")
                    ||
                    name.equals("TypeCheckerProperties")
                    ||
                    name.equals("ConstructorCheckerProperties")
                    ||
                    name.equals("MethodCheckerProperties")    
                    ||
                    name.equals("FieldCheckerProperties")    
                ) {
                    fillAttributesFromCheckerPropertiesAnnotation(annotation.getSubtermAt(0),attributes);
                } else {
                    // TODO: erase
                    //System.out.println("skip annotation "+name);
                }
            }
        }
    }
    
    
    private void fillAttributesFromCheckerPropertiesAnnotation(Term annotation,AttributesData attributes) throws TermWareException {
        if (annotation.getName().equals("NormalAnnotation")) {
            if (annotation.getArity() > 0) {
                Term memberValuePairs = annotation.getSubtermAt(1);
                Term mvpList = memberValuePairs.getSubtermAt(0);
                while(!mvpList.isNil()) {
                    Term mvp=mvpList.getSubtermAt(0);
                    mvpList=mvpList.getSubtermAt(1);
                    Term mvpIdentifier = mvp.getSubtermAt(0);
                    String name = mvpIdentifier.getSubtermAt(0).getString();
                    if (!name.equals("value")) {
                        throw new InvalidJavaTermException("annotation name must ve 'value'",mvp);
                    }
                    Term mvpValue=mvp.getSubtermAt(1).getSubtermAt(0);
                    fillAttributesFromCheckerPropertiesMemberValue(mvpValue,attributes);
                }
            }
        }else if (annotation.getName().equals("SingleMemberAnnotation")){
            Term memberValue=annotation.getSubtermAt(1).getSubtermAt(0);
            fillAttributesFromCheckerPropertiesMemberValue(memberValue,attributes);
        }else if (annotation.getName().equals("MarkerAnnotation")){
            // strange, but if not correct - who care.
            throw new InvalidJavaTermException("MarkerAnnotation for checker properties is useless",annotation);
        }else{
            throw new InvalidJavaTermException("NormalAnnotation|SimgleMemberAnnotation|MarkerAnnotation expected",annotation);
        }
    }
    
    
    private void fillAttributesFromCheckerPropertiesMemberValue(Term mv,AttributesData attributes) throws TermWareException {
        if (mv.getName().equals("MemberValueArrayInitializer")) {
            Term l = mv.getSubtermAt(0);
            while(!l.isNil()) {
                Term tname = l.getSubtermAt(0).getSubtermAt(0);
                l=l.getSubtermAt(1);
                if (l.isNil()) {
                    throw new InvalidJavaTermException("odd number of arguments is requred in @CheckerProperties",mv);
                }
                Term tvalue = l.getSubtermAt(0).getSubtermAt(0);
                l=l.getSubtermAt(1);
                if (!tname.getName().equals("StringLiteral")) {
                    throw new InvalidJavaTermException("String literal is required",tname);
                }else if(!tvalue.getName().equals("StringLiteral")){
                    throw new InvalidJavaTermException("String literal is required",tvalue);
                }
                String name = tname.getSubtermAt(0).getString();
                name=JavaTermStringLiteralExpressionModel.evalStringLiteral(name);
                String value = tvalue.getSubtermAt(0).getString();
                value=JavaTermStringLiteralExpressionModel.evalStringLiteral(value);
                Term obj = TermWare.getInstance().getTermFactory().createParsedTerm(value);
                //LOG.info("put: "+name+","+TermHelper.termToString(obj));
                attributes.getGeneralAttributes().put(name,obj);
            }
        } else {
            throw new InvalidJavaTermException("MemberValueArrayInitializer expected",mv);
        }
    }
    
    
    
    /**
     *pointer to factsl
     */
    private JavaFacts  facts_;
    
    /**
     * set of directories, read files from
     */
    private List<String>  dirs_;
    
    private static final Logger LOG = Logger.getLogger(ConfigurationAttributesStorage.class.getName());
    
}
