/*
 * JavaSyntaxElement.java
 *
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.parsers.java5;

import ua.gradsoft.parsers.java5.jjt.JJTJavaParser;
import ua.gradsoft.parsers.java5.jjt.ParseException;

/**
 *List of top-level syntax elements, which can
 *be separately parsed by us.
 * @author Ruslan Shevchenko
 */
public enum JavaSyntaxElement {
    
    CompilationUnit  
       { 
         public void callParser(JJTJavaParser jjtJavaParser)  throws ParseException {
             jjtJavaParser.CompilationUnit();             
         }
       },
       
    TypeDeclaration
       {
         public void callParser(JJTJavaParser jjtJavaParser)  throws ParseException {
             jjtJavaParser.TypeDeclaration();
         }           
       },       
       
    Block
       {
         public void callParser(JJTJavaParser jjtJavaParser)  throws ParseException {
             jjtJavaParser.Block();
         }                      
       },
       
    Statement
       {
         public void callParser(JJTJavaParser jjtJavaParser) throws ParseException{
             jjtJavaParser.Statement();
         }
       },    
       
    Expression
       {
         public void callParser(JJTJavaParser jjtJavaParser)  throws ParseException {
             jjtJavaParser.Expression();
         }           
       }
    ;  
            
    public abstract void callParser(JJTJavaParser jjtJavaParser) throws ParseException ;          
}
