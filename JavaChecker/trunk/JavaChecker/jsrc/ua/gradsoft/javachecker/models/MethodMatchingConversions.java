/*
 * MethodMatchingConversions.java
 *
 *
 */

package ua.gradsoft.javachecker.models;

import java.util.List;
import ua.gradsoft.javachecker.util.Pair;

/**
 *Set of conversions.
 * @author RSSH
 */
final class MethodMatchingConversions implements Cloneable
{
    
    MethodMatchingConversions()
    {}
    
    MethodMatchingConversions(MethodMatchingConversions x)
    {
        this.nBoxing_=x.nBoxing_;
        this.nNarrows_=x.nNarrows_;
        this.nRows_=x.nRows_;
        this.nSupers_=x.nSupers_;
        this.nUnboxing_=x.nUnboxing_;
        this.substitution_.putAll(x.substitution_);
        this.varArg_=x.varArg_;
    }
    
    void assign(MethodMatchingConversions x)
    {
        this.nBoxing_=x.nBoxing_;
        this.nNarrows_=x.nNarrows_;
        this.nRows_=x.nRows_;
        this.nSupers_=x.nSupers_;
        this.nUnboxing_=x.nUnboxing_;
        this.substitution_=x.substitution_;
        this.varArg_=x.varArg_;
    }
    
    
    public JavaTypeArgumentsSubstitution getSubstitution()
    { return substitution_; }
    
    public boolean isVarArg()
    { return varArg_; }
    
    public void setVarArg(boolean value)
    { varArg_=value; }
    
    public int  getNBoxing()
    { return nBoxing_; }
    
    public void incrementNBoxing()
    { ++nBoxing_; }
    
    public int  getNUnboxing()
    { return nUnboxing_; }
    
    public void incrementNUnboxing()
    { ++nUnboxing_; }
    
    
    public int getNNarrows()
    { return nNarrows_; }
    
    public void incrementNNarrows()
    { ++nNarrows_; }
    
    public int getNRows()
    { return nRows_; }

    public void incrementNRows()
    { ++nRows_; }    
    
    public int getNSupers()
    { return nSupers_; }
    
    public void setNSupers(int nSupers)
    { nSupers_=nSupers; }
    
    public void incrementNSupers()
    { ++nSupers_; }

    
    public static Pair<JavaMethodModel,MethodMatchingConversions>  best(List<Pair<JavaMethodModel, MethodMatchingConversions>> candidates)
    {
        Pair<JavaMethodModel,MethodMatchingConversions> retval = bestPhase1(candidates);
        if (retval!=null) {
            return retval;
        }
        retval = bestPhase2(candidates);
        if (retval!=null) {
            return retval;
        }
        retval = bestPhase3(candidates);
        return retval;
    }
    
    public  boolean exactly()
    {
        return (!varArg_) && nBoxing_==0 && nUnboxing_==0 && nNarrows_==0;
    }
    
    private static Pair<JavaMethodModel,MethodMatchingConversions> bestPhase1(List<Pair<JavaMethodModel, MethodMatchingConversions>> candidates)
    {
       Pair<JavaMethodModel,MethodMatchingConversions> minSpecific=null; 
       for(Pair<JavaMethodModel, MethodMatchingConversions> candidate: candidates) {
           MethodMatchingConversions conversions = candidate.getSecond();
           if (conversions.getNBoxing()==0 && conversions.getNUnboxing()==0 && conversions.isVarArg()==false) {
               if (minSpecific==null) {
                   minSpecific=candidate;
               }else{
                   if (isMoreSpecific(candidate.getSecond(),minSpecific.getSecond())){
                       minSpecific=candidate;
                   }
               }               
           }
       }
       return minSpecific;
    }
    
    private static Pair<JavaMethodModel,MethodMatchingConversions> bestPhase2(List<Pair<JavaMethodModel, MethodMatchingConversions>> candidates)
    {
       Pair<JavaMethodModel,MethodMatchingConversions> minSpecific=null; 
       for(Pair<JavaMethodModel, MethodMatchingConversions> candidate: candidates) {
           MethodMatchingConversions conversions = candidate.getSecond();
           if (conversions.getNBoxing()==0 && conversions.getNUnboxing()==0) {
               if (minSpecific==null) {
                   minSpecific=candidate;
               }else{
                   if (isMoreSpecific(candidate.getSecond(),minSpecific.getSecond())) {
                       minSpecific=candidate;
                   }
               }               
           }
       }
       return minSpecific;
    }

    private static Pair<JavaMethodModel,MethodMatchingConversions> bestPhase3(List<Pair<JavaMethodModel, MethodMatchingConversions>> candidates)
    {
       Pair<JavaMethodModel,MethodMatchingConversions> minSpecific=null; 
       for(Pair<JavaMethodModel, MethodMatchingConversions> candidate: candidates) {
           MethodMatchingConversions conversions = candidate.getSecond();
           if (minSpecific==null) {
               minSpecific=candidate;
           }else {
               if (isMoreSpecific(candidate.getSecond(),minSpecific.getSecond())) {           
                       minSpecific=candidate;
               }
           }                  
       }
       return minSpecific;
    }
    
    
    private static boolean isMoreSpecific(MethodMatchingConversions x, MethodMatchingConversions y)
    {
        if (x.getNNarrows() < y.getNNarrows()) {
            return true;
        }
        if (x.getNRows() < y.getNRows()) {
            return true;
        }
        if (x.getNSupers() < y.getNSupers()) {
            return true;
        }
        return false;
    }
    
    
    private boolean varArg_=false;
    private int     nBoxing_=0;
    private int     nUnboxing_=0;
    private int     nNarrows_=0;
    private int     nRows_=0;
    private int     nSupers_=0;
    
    private JavaTypeArgumentsSubstitution substitution_=new JavaTypeArgumentsSubstitution();
}
