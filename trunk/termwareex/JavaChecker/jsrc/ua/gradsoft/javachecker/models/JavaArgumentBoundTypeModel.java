/*
 * JavaArgumentBoundTypeModel.java
 *
 * Copyright (c) 2004-2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.ArrayList;
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
        createResolvedTypeArguments(typeArguments);
    }
    
    /** Creates a new instance of JavaArgumentBoundTypeModel */
    public JavaArgumentBoundTypeModel(JavaTypeModel origin, List<JavaTypeModel> resolvedTypeArguments,JavaTypeModel where) throws TermWareException {
        super(origin.getPackageModel());
        origin_=origin;
        resolvedTypeArguments_=resolvedTypeArguments;
        where_=where;
        createTermTypeArguments(resolvedTypeArguments);
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
    
    public boolean isUnknown() {
        return origin_.isUnknown(); }
    
    public JavaTypeModel  getEnclosedType() throws NotSupportedException, TermWareException {
        return new JavaArgumentBoundTypeModel(origin_.getEnclosedType(),typeArguments_,where_);
    }
    
    public JavaTypeModel  getReferencedType() throws NotSupportedException, TermWareException {
        return new JavaArgumentBoundTypeModel(origin_.getReferencedType(),typeArguments_,where_);
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
    
    
    
    public  Map<String,List<JavaMethodAbstractModel> >   getMethodModels() throws NotSupportedException {
        if (boundMethodModels_!=null) {
            return boundMethodModels_;
        }
        Map<String,List<JavaMethodAbstractModel> > retval = new HashMap<String,List<JavaMethodAbstractModel> >();
        for(Map.Entry<String,List<JavaMethodAbstractModel> > me: origin_.getMethodModels().entrySet()) {
            LinkedList<JavaMethodAbstractModel> mappedList=new LinkedList<JavaMethodAbstractModel>();
            for(JavaMethodAbstractModel cm : me.getValue()) {
                JavaArgumentBoundToTypeMethodModel mm=new JavaArgumentBoundToTypeMethodModel(this,cm);
                mappedList.add(mm);
            }
            retval.put(me.getKey(),mappedList);
        }
        boundMethodModels_=retval;
        return retval;
    }
    
    
    public boolean hasMemberVariableModels() {
        return origin_.hasMemberVariableModels(); }
    
    public Map<String,JavaMemberVariableAbstractModel> getMemberVariableModels() throws NotSupportedException {
        if (boundMemberVariables_!=null) {
            return boundMemberVariables_;
        }
        Map<String,JavaMemberVariableAbstractModel> retval=new HashMap<String,JavaMemberVariableAbstractModel>();
        for(JavaMemberVariableAbstractModel c : origin_.getMemberVariableModels().values()) {
            JavaArgumentBoundMemberVariableModel mc=new JavaArgumentBoundMemberVariableModel(this,c);
            retval.put(mc.getName(),mc);
        }
        boundMemberVariables_=retval;
        return retval;
    }
    
    public boolean isNested() {
        return origin_.isNested();
    }
    
    public boolean hasNestedTypeModels() {
        return origin_.hasNestedTypeModels();
    }
    
    public Map<String,JavaTypeModel> getNestedTypeModels() throws NotSupportedException, TermWareException {
        Map<String,JavaTypeModel> retval = new TreeMap<String,JavaTypeModel>();
        for(JavaTypeModel otm: origin_.getNestedTypeModels().values()) {
            JavaTypeModel wrappedOtm=new JavaArgumentBoundTypeModel(otm,typeArguments_,where_);
            retval.put(wrappedOtm.getName(),wrappedOtm);
        }
        return retval;
    }
    
    public boolean hasTypeParameters() {
        return origin_.hasTypeParameters() && origin_.getTypeParameters().size() < resolvedTypeArguments_.size(); }
    
    /**
     * we thinks, that not all parameters can be bound, so
     */
    public List<JavaTypeVariableAbstractModel> getTypeParameters() {
        List<JavaTypeVariableAbstractModel> l=origin_.getTypeParameters();
        if (resolvedTypeArguments_.size() < l.size()) {
            return origin_.getTypeParameters().subList(resolvedTypeArguments_.size(),origin_.getTypeParameters().size());
        }else{
            return JavaModelConstants.TYPEVARIABLE_EMPTY_LIST;
        }
    }
    
    /**
     *@return type arguments, represented as term
     */
    public Term getTypeArguments() {
        return typeArguments_; }
    
    public List<JavaTypeModel> getResolvedTypeArguments() {
        return resolvedTypeArguments_;
    }
    
    public JavaTypeModel resolveTypeParameter(String name) throws EntityNotFoundException {
        int foundIndex=-1;
        Iterator<? extends JavaTypeVariableAbstractModel> it=origin_.getTypeParameters().iterator();
        while(it.hasNext()) {
            ++foundIndex;
            if (it.next().getName().equals(name)) {
                return resolvedTypeArguments_.get(foundIndex);
            }
        }
        throw new EntityNotFoundException("TypeParameter ",name, "in "+getName());
    }
    
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
            List<JavaTypeModel>  oldResolvedTypes = boundOtherType.resolvedTypeArguments_;
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
        resolvedTypeArguments_=new ArrayList<JavaTypeModel>();
        Term l=typeArguments_.getSubtermAt(0);
        while(!l.isNil()) {
            Term t=l.getSubtermAt(0);
            if (t.getName().equals("TypeArgument")) {
                if (t.getArity()==0) {
                    resolvedTypeArguments_.add(new JavaWildcardBoundsTypeModel(where_));
                }else if (t.getArity()==1) {
                    Term t1=t.getSubtermAt(0);
                    if (t1.getName().equals("WildcardBounds")) {
                        resolvedTypeArguments_.add(new JavaWildcardBoundsTypeModel(t1,where_));
                    }else{
                        try {
                            resolvedTypeArguments_.add(JavaResolver.resolveTypeToModel(t1,where_));
                        }catch(EntityNotFoundException ex){
                            resolvedTypeArguments_.add(JavaUnknownTypeModel.INSTANCE);
                        }
                    }
                }else{
                    try {
                        resolvedTypeArguments_.add(JavaResolver.resolveTypeToModel(t,where_));
                    }catch(EntityNotFoundException ex){
                        resolvedTypeArguments_.add(JavaUnknownTypeModel.INSTANCE);
                    }
                }
            }else{
                try {
                    resolvedTypeArguments_.add(JavaResolver.resolveTypeToModel(t,where_));
                }catch(EntityNotFoundException ex){
                    resolvedTypeArguments_.add(JavaUnknownTypeModel.INSTANCE);
                }
            }
        }
    }
    
    public void createTermTypeArguments(List<JavaTypeModel> resolvedTypes) throws TermWareException {
        Term l=TermWare.getInstance().getTermFactory().createNIL();
        for(int i=resolvedTypes.size(); i>0; --i) {
            Term t = resolvedTypes.get(i-1).getShortNameAsTerm();
            l=TermWare.getInstance().getTermFactory().createTerm("cons",l);
        }
        typeArguments_=TermWare.getInstance().getTermFactory().createTerm("TypeArguments",l);
    }
    
    //TODO:private void createTermTypeArguments()
    
    private JavaTypeModel origin_;
    private List<JavaTypeModel> resolvedTypeArguments_;
    private Map<String,List<JavaMethodAbstractModel> >   boundMethodModels_=null;
    private Map<String,JavaMemberVariableAbstractModel > boundMemberVariables_=null;
    private Term          typeArguments_;
    private JavaTypeModel where_;
}
