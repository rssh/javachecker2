/*
 * JavaTermModifiersModel.java
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.lang.annotation.ElementType;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.JUtils;
import ua.gradsoft.javachecker.util.CachedMap;
import ua.gradsoft.javachecker.util.Function;
import ua.gradsoft.javachecker.util.ImmutableListAsMap;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Model for Java Modifiers
 * @author Ruslan Shevchenko
 */
public class JavaTermModifiersModel extends JavaModifiersModel 
{
    
    public JavaTermModifiersModel(int modifiers)
    {
        modifiers_=modifiers;
        annotationsList_=new LinkedList<JavaAnnotationInstanceModel>();
    }
            
    public JavaTermModifiersModel()
    {
        modifiers_=0;
        annotationsList_=new LinkedList<JavaAnnotationInstanceModel>();          
    }
    
    public JavaTermModifiersModel(Term modifiers, ElementType ownerType, Object owner) throws TermWareException
    {
      this(modifiers.getSubtermAt(0).getInt());
      Term al = modifiers.getSubtermAt(1);
      if (annotationsList_==null) {
          annotationsList_=new LinkedList<JavaAnnotationInstanceModel>();          
      }
      while(!al.isNil()) {
          Term ct = al.getSubtermAt(0);
          JavaTermAnnotationInstanceModel an = new JavaTermAnnotationInstanceModel(ct,ownerType,owner);
          String name = JUtils.getJavaNameLastComponentAsString(an.getNameTerm());
          //annotationsList_.put(name,an);
          annotationsList_.add(an);
          al=al.getSubtermAt(1);
      }
    }

     
      /**
       *@return internal int value
       */
     public int getIntValue()
     { return modifiers_; }
    
     List<JavaAnnotationInstanceModel> getAnnotationsList()
     { return annotationsList_; }
     
     Map<String,JavaAnnotationInstanceModel> getAnnotationsMap()
     { 

         Function<String,Integer> findAnnotationIndexByName=
                   new Function<String,Integer>(){
             public Integer function(String s) throws TermWareException {
              try {   
                 for(int i=0; i<annotationsList_.size(); ++i) {
                     JavaAnnotationInstanceModel am=annotationsList_.get(i);
                     String name=am.getAnnotationModel().getFullName();                    
                     if (name.equals(s)) {
                         return i;
                     }                                             
                 }
                 return null;
              }catch(EntityNotFoundException ex){
                  throw new AssertException(ex.getMessage(),ex);
              }
             }
         };            
         
         Function<Integer,String> findAnnotationNameByIndex=
                 new Function<Integer,String>(){
             public String function(Integer i) throws TermWareException {
                 try {
                   return annotationsList_.get(i).getAnnotationModel().getFullName();
                 }catch(EntityNotFoundException ex){
                     throw new AssertException(ex.getMessage(),ex);
                 }
             }
         };
         
         if (annotationsCache_==null) {
           annotationsCache_=new TreeMap<String,JavaAnnotationInstanceModel>();
         }
         
         return new CachedMap<String,JavaAnnotationInstanceModel>( 
                 annotationsCache_,
                 new ImmutableListAsMap<String,JavaAnnotationInstanceModel>(
                   annotationsList_,findAnnotationIndexByName,findAnnotationNameByIndex
                 )
                 );
     }
     
    /* 
     void  addAnnotation(String name, JavaAnnotationInstanceModel annotation) throws TermWareException
     {
         if (annotationsList_==null) {
             annotationsList_ = new TreeMap<String,JavaAnnotationInstanceModel>();
         }
         annotationsList_.put(name,annotation);
     }
     */
     
     /**
      * Modifiers(AnnotationsList, int)
      */
     public Term getModelTerm() throws TermWareException, EntityNotFoundException
     {
       Term anl = TermUtils.createNil();
       List<JavaAnnotationInstanceModel> annotationsList = getAnnotationsList();
       for(JavaAnnotationInstanceModel anim: annotationsList) {
           Term ct = anim.getModelTerm();
           anl=TermUtils.createTerm("cons",ct,anl);           
       }
       anl=TermUtils.reverseListTerm(anl);
       Term iv = TermUtils.createInt(getIntValue());
       return TermUtils.createTerm("Modifiers",iv,anl);       
     }
     
     
    private int modifiers_;
    private List <JavaAnnotationInstanceModel> annotationsList_;
    private Map<String,JavaAnnotationInstanceModel> annotationsCache_=null;
    
}
