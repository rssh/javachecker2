/*
 * JavaArgumentBoundTypeModel.java
 *
 * Copyright (c) 2004-2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *
 * @author Ruslan Shevchenko
 */
public class JavaArgumentBoundTypeModel extends JavaTypeModel {
    
    /** Creates a new instance of JavaArgumentBoundTypeModel */
    public JavaArgumentBoundTypeModel(JavaTypeModel origin,Term typeArguments,JavaTypeModel where) throws TermWareException {
        super(origin.getPackageModel());
        origin_=origin;
        typeArguments_=typeArguments;
        where_=where;
        if (where_==null) {
            where_=origin;
        }              
        resolvedTypeArguments_=null;
        substitution_=null;
        //
        // Enum<E extends Enum<E> > cause endless loop during loading of Enum.
        // (so, resolvedTypeArguments and substituion will be lazy-initialized) 
        //
        //createResolvedTypeArguments(typeArguments);
        //createSubstitution();
    }
    
    /** Creates a new instance of JavaArgumentBoundTypeModel */
    public JavaArgumentBoundTypeModel(JavaTypeModel origin, List<JavaTypeModel> resolvedTypeArguments,JavaTypeModel where) throws TermWareException {
        super(origin.getPackageModel());
        origin_=origin;
        resolvedTypeArguments_=resolvedTypeArguments;
        createSubstitution();
        where_=where;
        if (where_==null) {
            where_=origin;
        }
        createTermTypeArguments(resolvedTypeArguments);
    }
    
    public JavaArgumentBoundTypeModel(JavaTypeModel origin,JavaTypeArgumentsSubstitution substitution, JavaTypeModel where) throws TermWareException
    {
      super(origin.getPackageModel());
      origin_=origin;
      substitution_=substitution;
      resolvedTypeArguments_=substitution_.substitute(origin_.getTypeParameters());
      where_=where;
      if (where_==null) {
          where_=origin;
      }      
      createTermTypeArguments(resolvedTypeArguments_);
    }
    
    
    public String getName() {
        String sTypeArguments=null;
        try{
            sTypeArguments=TermHelper.termToPrettyString(typeArguments_,"Java",TermWare.getInstance().getTermFactory().createNIL());
        }catch(TermWareException ex){
            sTypeArguments="<error>";
        }
        return origin_.getName()+sTypeArguments;
    }
    
    public Term getShortNameAsTerm() throws TermWareException {
        Term t = origin_.getShortNameAsTerm();
        Term retval=t;
        if (!t.getName().equals("ClassOrInterfaceType")) {
            Term l=TermWare.getInstance().getTermFactory().createNil();
            l=TermWare.getInstance().getTermFactory().createTerm("cons",getTypeArguments(),l);
            l=TermWare.getInstance().getTermFactory().createTerm("cons",t,l);
            retval=TermWare.getInstance().getTermFactory().createTerm("ClassOrInterfaceType",l);
        }else{
            retval=t.termClone();
            Term l=retval.getSubtermAt(0);
            TermUtils.addTermToList(l,getTypeArguments());
        }
        return retval;
    }
    
    /*
    public Term getFullNameAsTerm() throws TermWareException
    {
        Term t=origin_.getFullNameAsTerm();
        Term retval=t;
        Term fTATerm=TermWare.getInstance().getTermFactory().createNil();
        for(int i=getResolvedTypeArguments().size();i>0;--i) {
            fTATerm=TermWare.getInstance().getTermFactory().createTerm("cons",getResolvedTypeArguments().get(i-1).getFullNameAsTerm(),fTATerm);
        }
        fTATerm=TermWare.getInstance().getTermFactory().createTerm("TypeArguments",fTATerm);
        if (!t.getName().equals("ClassOrInterfaceType")) {
            Term nilTerm=TermUtils.createNil();
            retval=TermUtils.createTerm("ClassOrInterfaceType",
                                        TermUtils.createTerm("cons",t,
                                         TermUtils.createTerm("cons",fTATerm,nilTerm)));
        }else{
            retval=t.termClone();
            Term l=retval.getSubtermAt(0);
            l=TermUtils.addTermToList(l,fTATerm);
            retval.setSubtermAt(0,l);
        }
        return retval;
    }
     */
    
    public boolean isClass() {
        return origin_.isClass(); }
    
    public boolean isInterface() {
        return origin_.isInterface(); }
    
    public boolean isEnum() {
        return origin_.isEnum(); }
    
    public boolean isAnnotationType() {
        return origin_.isAnnotationType(); }
    
    public boolean isPrimitiveType() {
        return origin_.isPrimitiveType(); }
    
    public boolean isArray() {
        return origin_.isArray(); }
    
    public boolean isTypeArgument() {
        return origin_.isTypeArgument(); }
    
    public boolean isWildcardBounds() {
        return false; }
    
    public boolean isNull()
    { return false; }
    
    public boolean isUnknown() {
        return origin_.isUnknown(); }
    
    public JavaTypeModel  getEnclosedType() throws NotSupportedException, TermWareException {
        return getSubstitution().substitute(origin_.getEnclosedType());
    }
    
    public JavaTypeModel  getReferencedType() throws NotSupportedException, TermWareException {
        return getSubstitution().substitute(origin_.getReferencedType());
    }
    
    public  boolean canCheck() {
        return origin_.canCheck();
    }
    
    public boolean check() throws TermWareException {
        return origin_.check();
    }
    
    public boolean hasMethodModels() {
        return origin_.hasMethodModels();
    }
    
    
    
    public  Map<String, List<JavaMethodModel>>   getMethodModels() throws NotSupportedException {
        if (boundMethodModels_!=null) {
            return boundMethodModels_;
        }
        Map<String,List<JavaMethodModel> > retval = new HashMap<String,List<JavaMethodModel> >();
        for(Map.Entry<String, List<JavaMethodModel>> me: origin_.getMethodModels().entrySet()) {
            LinkedList<JavaMethodModel> mappedList=new LinkedList<JavaMethodModel>();
            for(JavaMethodModel cm : me.getValue()) {
                JavaClassArgumentBoundMethodModel mm=new JavaClassArgumentBoundMethodModel(this,cm);
                mappedList.add(mm);
            }
            retval.put(me.getKey(),mappedList);
        }
        boundMethodModels_=retval;
        return retval;
    }
    
    
    public boolean hasMemberVariableModels() {
        return origin_.hasMemberVariableModels(); }
    
    public Map<String, JavaMemberVariableModel> getMemberVariableModels() throws NotSupportedException {
        if (boundMemberVariables_!=null) {
            return boundMemberVariables_;
        }
        Map<String,JavaMemberVariableModel> retval=new HashMap<String,JavaMemberVariableModel>();
        for(JavaMemberVariableModel c : origin_.getMemberVariableModels().values()) {
            JavaArgumentBoundMemberVariableModel mc=new JavaArgumentBoundMemberVariableModel(this,c);
            retval.put(mc.getName(),mc);
        }
        boundMemberVariables_=retval;
        return retval;
    }

    
    public Map<String, JavaEnumConstantModel> getEnumConstantModels() throws NotSupportedException
    {
      return origin_.getEnumConstantModels();
    }    
    
    public boolean isNested() {
        return origin_.isNested();
    }
    
    public boolean hasNestedTypeModels() {
        return origin_.hasNestedTypeModels();
    }
    
    public Map<String,JavaTypeModel> getNestedTypeModels() throws NotSupportedException, TermWareException {
        //System.err.println("getNestedTypeModels for "+this.getName());
        Map<String,JavaTypeModel> retval = new TreeMap<String,JavaTypeModel>();
        for(JavaTypeModel otm: origin_.getNestedTypeModels().values()) {            
            JavaTypeModel wrappedOtm=getSubstitution().substitute(otm);
            retval.put(otm.getName(),wrappedOtm);
            //System.err.println("adding:"+otm.getName());
        }
        //System.err.println("ok, size="+retval.size());
        return retval;
    }
    
    public boolean hasTypeParameters() 
    {
      return false;
      // partially bounded type is another beast.
      //  return origin_.hasTypeParameters() && origin_.getTypeParameters().size() < getResolvedTypeArguments().size(); 
    }
    
    /**
     * all parameters of origin types are bound.
     */
    public List<JavaTypeVariableAbstractModel> getTypeParameters() throws TermWareException
    {
      return Collections.emptyList();
    }
    
    public JavaTypeModel getOrigin()
    { return origin_; }
    
    /**
     *@return type arguments, represented as term
     */
    public Term getTypeArguments() {
        return typeArguments_; }
    
    public List<JavaTypeModel> getResolvedTypeArguments() throws TermWareException
    {
        if (resolvedTypeArguments_==null) {
            createResolvedTypeArguments(typeArguments_);
        }
        return resolvedTypeArguments_;
    }
    
    public JavaTypeModel  getSuperClass() throws TermWareException, NotSupportedException
    {
      if (boundSuperClassModel_==null)  {
          boundSuperClassModel_=getSubstitution().substitute(origin_.getSuperClass());
      }
      return boundSuperClassModel_;
    }
    
    public List<JavaTypeModel> getSuperInterfaces() throws TermWareException, NotSupportedException
    {
      if (boundSuperInterfacesModels_==null) {
          boundSuperInterfacesModels_=getSubstitution().substitute(origin_.getSuperInterfaces());
      }
      return boundSuperInterfacesModels_;
    }    
    
    public boolean isLocal()
    { return origin_.isLocal(); }

    public boolean isAnonimous()
    { return origin_.isAnonimous(); }
    
   public JavaStatementModel  getEnclosedStatement()
   { return origin_.getEnclosedStatement(); }
    
    
    public JavaTypeModel resolveTypeParameter(String name) throws EntityNotFoundException, TermWareException 
    {
        int foundIndex=-1;
        Iterator<? extends JavaTypeVariableAbstractModel> it=origin_.getTypeParameters().iterator();
        while(it.hasNext()) {
            ++foundIndex;
            if (it.next().getName().equals(name)) {
                return getResolvedTypeArguments().get(foundIndex);
            }
        }
        throw new EntityNotFoundException("TypeParameter ",name, "in "+getName());
    }
    
    /**
     * substitute type parameters whith this.
     */
    public JavaTypeModel substituteTypeParameters(JavaTypeModel otherType) throws TermWareException {
        if (otherType.isTypeArgument()) {
            try {
                return resolveTypeParameter(otherType.getName());
            }catch(EntityNotFoundException ex){
                // this can be some other type parameter, not from our model,
                // so return unchanged.
                return otherType;
            }
        }else if(otherType instanceof JavaArgumentBoundTypeModel){
            JavaArgumentBoundTypeModel boundOtherType = (JavaArgumentBoundTypeModel)otherType;
            List<JavaTypeModel>  oldResolvedTypes = boundOtherType.getResolvedTypeArguments();
            List<JavaTypeModel> newResolvedTypes = new ArrayList<JavaTypeModel>(oldResolvedTypes.size());
            boolean changed=false;
            for(JavaTypeModel cur : oldResolvedTypes) {
                JavaTypeModel newCur=substituteTypeParameters(cur);
                newResolvedTypes.add(newCur);
                if (newCur!=cur) {
                    changed=true;
                }
            }
            if (changed) {
                return new JavaArgumentBoundTypeModel(boundOtherType.origin_,newResolvedTypes,boundOtherType.where_);
            }else{
                return otherType;
            }
        }else{
            return otherType;
        }
    }
    
    
    private void createResolvedTypeArguments(Term typeArguments) throws TermWareException {
        //System.err.println("createTypeArguments, origin_.getName()="+origin_.getName());
        resolvedTypeArguments_=new ArrayList<JavaTypeModel>();
        substitution_=new JavaTypeArgumentsSubstitution();
        Term l=typeArguments_.getSubtermAt(0);
        JavaTypeModel resolvedTypeArgument=null;
        int tpIndex=0;
        int tpSize=origin_.getTypeParameters().size();
        while(!l.isNil()) {
            Term t=l.getSubtermAt(0);
            l=l.getSubtermAt(1);
            if (t.getName().equals("TypeArgument")) {
                if (t.getArity()==0) {
                    resolvedTypeArgument = new JavaWildcardBoundsTypeModel(where_);
                }else if (t.getArity()==1) {
                    Term t1=t.getSubtermAt(0);
                    if (t1.getName().equals("WildcardBounds")) {
                        resolvedTypeArgument = new JavaWildcardBoundsTypeModel(t1,where_,origin_.getTypeParameters());
                    }else{
                        try {
                            resolvedTypeArgument = JavaResolver.resolveTypeToModel(t1,where_,origin_.getTypeParameters());
                        }catch(EntityNotFoundException ex){
                            resolvedTypeArgument = JavaUnknownTypeModel.INSTANCE;
                        }
                    }
                }else{
                    try {
                        resolvedTypeArgument = JavaResolver.resolveTypeToModel(t,where_,origin_.getTypeParameters());
                    }catch(EntityNotFoundException ex){
                        resolvedTypeArgument = JavaUnknownTypeModel.INSTANCE;
                    }
                }
            }else{
                try {
                    resolvedTypeArgument = JavaResolver.resolveTypeToModel(t,where_,origin_.getTypeParameters());
                }catch(EntityNotFoundException ex){
                    resolvedTypeArgument = JavaUnknownTypeModel.INSTANCE;
                }
            }
            resolvedTypeArguments_.add(resolvedTypeArgument);
            if (tpIndex < tpSize) {
              substitution_.put(origin_.getTypeParameters().get(tpIndex),resolvedTypeArgument);
            }
            ++tpIndex;
        }
        // recreate substitution.
        //  (add missing boundaries if needed)
        createSubstitution();
    }

    
    private void createSubstitution() throws TermWareException
    {
        // now try to align with number of type parameters.
        // (i. e. unbound type parameters are objects)
        // Map x is the same as Map<Object,Object> x        
        List typeParameters = origin_.getTypeParameters();
        List typeArguments = resolvedTypeArguments_;
        int tpSize=typeParameters.size();
        int taSize=resolvedTypeArguments_.size();
        if (taSize < tpSize) {
            JavaTypeModel objectModel = JavaResolver.resolveJavaLangObject();
            while(taSize < tpSize) {
                resolvedTypeArguments_.add(objectModel);
                ++taSize;
            }
        }
        substitution_=new JavaTypeArgumentsSubstitution(typeParameters,resolvedTypeArguments_);
    }
    
    public void createTermTypeArguments(List<JavaTypeModel> resolvedTypes) throws TermWareException {
        Term l=TermWare.getInstance().getTermFactory().createNIL();
        for(int i=resolvedTypes.size(); i>0; --i) {
            Term t = resolvedTypes.get(i-1).getShortNameAsTerm();
            l=TermWare.getInstance().getTermFactory().createTerm("cons",t,l);
        }
        typeArguments_=TermWare.getInstance().getTermFactory().createTerm("TypeArguments",l);
    }
    
    /**
     * TypeArgumentBoundTypeModel(ClassOrInterfaceType(originModel,list(typeModels)),placeContext)
     */
    public Term getModelTerm() throws TermWareException
    {
        Term originModelTerm=origin_.getModelTerm();
        Term argsModelTerms=JavaTypeModelHelper.createModelTermList(getResolvedTypeArguments());
        Term classOrInterfaceType=TermUtils.createTerm("ClassOrInterfaceType",originModelTerm,argsModelTerms);
        Term retval=TermUtils.createTerm("TypeArgumentBoundTypeModel",classOrInterfaceType,TermUtils.createJTerm(JavaPlaceContextFactory.createNewTypeContext(this)));
        return retval;
    }

    public JavaTypeArgumentsSubstitution  getSubstitution() throws TermWareException
    {
        //System.err.println("getSubstitution, origin.getName()="+origin_.getName());
        if (substitution_==null) {            
            if (resolvedTypeArguments_==null) {
               createResolvedTypeArguments(typeArguments_);
            }else{
               throw new AssertException("incorrect constucting ?, origin_="+origin_.getName());
            }
        }
        return substitution_;
    }
    
    //TODO:private void createTermTypeArguments()
    
    private JavaTypeModel origin_;   
    private List<JavaTypeModel> resolvedTypeArguments_;
    
    private JavaTypeModel  boundSuperClassModel_=null;
    private List<JavaTypeModel>  boundSuperInterfacesModels_=null;
    
    private Map<String, List<JavaMethodModel>>   boundMethodModels_=null;
    private Map<String, JavaMemberVariableModel> boundMemberVariables_=null;
    private Term          typeArguments_;
    private JavaTypeModel where_;
    private JavaTypeArgumentsSubstitution substitution_;
    
}
