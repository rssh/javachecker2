/*
 * JavaParserFactory.java
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.parsers.java5;

import java.io.Reader;
import ua.gradsoft.parsers.java5.jjt.JJTJavaParser;
import ua.gradsoft.termware.IParser;
import ua.gradsoft.termware.IParserFactory;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TermWareInstance;

/**
 *Adapter to TermWare IParserFactory interface
 * @author Ruslan Shevchenko
 */
public class JavaParserFactory implements IParserFactory
{
    
   
 /**
  * create parser object.
  *Term arg can be nil or atom option or list of options.
  *Options are:
  *<ul>
  *  <li>simplify</li>
  *</ul>
  **/                            
 public IParser  createParser(Reader in, String inFname, Term arg, TermWareInstance instance)  throws TermWareException
 {     
     JJTJavaParser parser = new JJTJavaParser(in);
     parser.setInFname(inFname);
     return new JavaParser(parser,arg,this);
 }

  public ASTTransformers  getASTTransformers()
  { return astTransformers_; }
 
  private ASTTransformers astTransformers_=new ASTTransformers();
 
}
