/*
 * JavaParser.java
 *
 * Copyright (c) 2006 - 2009 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.parsers.java5;

import ua.gradsoft.parsers.java5.jjt.JJTJavaParser;
import ua.gradsoft.parsers.java5.jjt.ParseException;
import ua.gradsoft.termware.IParser;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TermWareSymbols;
import ua.gradsoft.termware.exceptions.AssertException;
import ua.gradsoft.termware.exceptions.TermParseException;

/**
 *Adapter to IParser interface from TermWare framework.
 * @author Ruslan Shevchenko
 */
public class JavaParser implements IParser
{
    
    public Term  readTerm()  throws TermWareException
    {      
      try {            
          syntaxElement_.callParser(jjtJavaParser_);
      }catch(ParseException ex){
          throw new TermParseException(ex.getMessage());
      }
      Term t = jjtJavaParser_.getRootNode();
      simplify_=true;
      if (simplify_) {
          //System.err.println("simplify is set");
          t=owner_.getASTTransformers().simplifyBefore(t);          
          owner_.getASTTransformers().eraseJjtParents(t);
          t=owner_.getASTTransformers().transformSeqToList(t);
          t=owner_.getASTTransformers().insertEmptyTypeParametersExtendsAndImplementLists(t);
          t=owner_.getASTTransformers().simplifyAfter(t);          
      }
      eof_=true;
      return t;
    }
    
    public boolean  eof()
    { return eof_; }
    
    JavaParser(JJTJavaParser jjtJavaParser, Term args,JavaParserFactory owner) throws TermWareException
    {
      jjtJavaParser_=jjtJavaParser;              
      owner_=owner;
      eof_=false; 
      parseArgs(args);
    }
    
    private void parseArgs(Term args) throws TermWareException
    {
      if (args.isAtom()) {
        checkOptionString(args.getName());
      }else if (args.isString()) {
        checkOptionString(args.getAsString(TermWare.getInstance()));  
      }else if (args.isNil()) {
        return;  
      }else if (args.getNameIndex().equals(TermWareSymbols.CONS_INDEX)) {
          parseArgs(args.getSubtermAt(0));
          parseArgs(args.getSubtermAt(1));
      }else{
          throw new AssertException("Invalid Java Parser option:"+TermHelper.termToString(args));
      }        
    }
    
    private void checkOptionString(String option) throws TermWareException
    {        
        if (option.equals("simplify")) {
            simplify_=true;
        }else {
            try {
                syntaxElement_=JavaSyntaxElement.valueOf(option);
            }catch(IllegalArgumentException ex){
                throw new AssertException("Invalid Java Parser option:"+option);
            }            
        }
    }
    
    private boolean simplify_=false;
    
    
    private JJTJavaParser jjtJavaParser_;
    private JavaParserFactory     owner_;
    private JavaSyntaxElement     syntaxElement_=JavaSyntaxElement.CompilationUnit;
    private boolean eof_;
    
}
