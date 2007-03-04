/*
 * CheckerComment.java
 *
 * Created 26, 02, 2004, 5:55
 */

package ua.gradsoft.javachecker;

import java.util.HashSet;
import java.util.regex.*;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;

/**
 *This class is a checker comment:
 *  i. e. expression, which developer put in comment
 *  before checked expression.
 *example:
 *<pre>
 *!checker: disable(all) !
 *class X {
 *  .....
 *}
 *</pre>
 * will disable all check in this class.
 *<pre>
 *!checker: disable(style)
 *</pre>
 *will disable style checks.
 * @author  Ruslan Shevchenko
 */
public class CheckerComment {
    
    /** Creates a new instance of CheckerComment 
    /*@param expression : checker expression.
     *  it's situated in comment between "!checker:"   "!"  tags.
     */
    private CheckerComment(String expression) throws InvalidCheckerCommentException
    {
        Term t=null;
        try {
            t=TermWare.getInstance().getTermFactory().createParsedTerm(expression);
        }catch(TermWareException ex){
            throw new InvalidCheckerCommentException(expression,ex.getMessage());
        }
        try {
          processCommentTerm(t);
        }catch(TermWareException ex){
              throw new InvalidCheckerCommentException(expression,ex.getMessage());
        }
        
    }
    
    public boolean isBean()
    { return bean_; }
    
    public boolean isDisable(String checkName)
    { return disabledChecks_.contains(checkName) || disabledChecks_.contains("All"); }
    
    
    /**
     * try to extract checker comment from comment.
     *@param Comment - comment to extract from.
     *@return Parsed checker comment, if it was found.
     *        null, otherwise
     *@exception InvalidCheckerCommentExcepton if we find checker comment, but expression inside is incorrect.
     */
    public static CheckerComment extract(String comment) throws InvalidCheckerCommentException
    {
       Matcher m=ASTERISKS_REMOVE_PATTERN_.matcher(comment);
       String s1=m.replaceAll("");
       Matcher m1=CHECKER_PATTERN_.matcher(s1);
       if (m1.matches()) {
           String checkerExpression=m1.group(1);
           return new CheckerComment(checkerExpression);
       }else{
         // disable check for files, generated by JavaCC
         //TODO: may be not all checks, but some specifics, such as style ?
         m1=JAVACC_PATTERN_.matcher(s1);
         if (m1.matches()) {
           return new CheckerComment("disable(All)");
         }
         // disable check for files, generated by Byacc/j
         m1=BYACC_PATTERN1_.matcher(s1);
         if (m1.matches()) {
           return new CheckerComment("disable(All)");
         }        
         m1=BYACC_PATTERN2_.matcher(s1);
         if (m1.matches()) {
           return new CheckerComment("disable(All)");
         }        
       }

       return null;
    }


    private void processCommentTerm(Term t) throws InvalidCheckerCommentException, TermWareException
    {
      if (t.isComplexTerm()) {
        if (t.getName().equals("disable")) {
            processDisable(t);
        }else if (t.getName().equals("cons")){
            if (t.getArity()!=2){
               throw new InvalidCheckerCommentException(t,"cons with arity !=2 in checker comment");
            }else{
               Term current=t;
               while(!current.isNil()) {
                  if (current.getArity()!=2) {
                      throw new InvalidCheckerCommentException(t,"cons with arity !=2 in checker comment");
                  }
                  processCommentTerm(current.getSubtermAt(0));
                  current=current.getSubtermAt(1);
               }
            }
        }else{
            throw new InvalidCheckerCommentException(t,"unknown name of checker term");
        }
      }else if(t.isAtom()){    
        if (t.getName().equals("bean")){
            bean_=true;   
        }else{
            throw new InvalidCheckerCommentException(t,"checker expression is unknown atom");
        }
      }else{
        throw new InvalidCheckerCommentException(t,"bad type for checker expression");
      }
    }

    /**
     * process "disable" comment
     */
    private void processDisable(Term t) throws InvalidCheckerCommentException, TermWareException
    {
     if (t.getArity()==0) {
          throw new InvalidCheckerCommentException(t,"arity of disable must be not nul");
     }
     for(int i=0; i<t.getArity(); ++i){
        Term st=t.getSubtermAt(i);
        if (st.isAtom()) {
           if (st.getName().equals("all")||st.getName().equals("All")) {
               disabledChecks_.add("All");
           }else if (st.getName().equals("style")||st.getName().equals("style")) {
               disabledChecks_.add("Style");
           }
        }else{
           throw new InvalidCheckerCommentException(t,"subterms of disable must be atoms");
        }
     }
    }
    
    private boolean bean_ = false;
    private HashSet<String> disabledChecks_ = new HashSet<String>();
    private static final Pattern ASTERISKS_REMOVE_PATTERN_ = Pattern.compile("\\p{Space}\\*",Pattern.MULTILINE);
    private static final Pattern CHECKER_PATTERN_ = Pattern.compile(".*!@checker:([^{!@}]*)!@.*",Pattern.DOTALL);
    private static final Pattern JAVACC_PATTERN_ = Pattern.compile(".*Generated By:JavaCC: Do not edit this line\\..*",Pattern.DOTALL);
    private static final Pattern BYACC_PATTERN1_  = Pattern.compile(".*Generated by Byacc.*",Pattern.DOTALL);
    private static final Pattern BYACC_PATTERN2_  = Pattern.compile(".*This file created by BYACC.*",Pattern.DOTALL);

  
}