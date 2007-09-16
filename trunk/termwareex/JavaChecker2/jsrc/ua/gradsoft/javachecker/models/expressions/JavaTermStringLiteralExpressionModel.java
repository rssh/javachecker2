/*
 * JavaTermStringLiteralExpressionModel.java
 *
 *
 * Copyright (c) 2006-2007 GradSoft  Ukraine.
 * http://www.gradsoft.ua
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models.expressions;

import java.util.Collections;
import java.util.List;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.models.InvalidJavaTermException;
import ua.gradsoft.javachecker.models.JavaExpressionKind;
import ua.gradsoft.javachecker.models.JavaExpressionModel;
import ua.gradsoft.javachecker.models.JavaLiteralModel;
import ua.gradsoft.javachecker.models.JavaResolver;
import ua.gradsoft.javachecker.models.JavaTermExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermStatementModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *StringLiteral
 * @author Ruslan Shevchenko
 */
public class JavaTermStringLiteralExpressionModel extends JavaTermExpressionModel implements JavaLiteralModel, JavaObjectConstantExpressionModel
{
    
   public JavaTermStringLiteralExpressionModel(Term t,JavaTermStatementModel st,JavaTypeModel enclosedType) throws TermWareException
    {
       super(t,st,enclosedType);       
    }
    
    public JavaExpressionKind getKind()
    {
      return JavaExpressionKind.STRING_LITERAL;
    }
    
    
    public JavaTypeModel getType() throws TermWareException, EntityNotFoundException
    {
      return JavaResolver.resolveTypeModelByFullClassName("java.lang.String");
    }
    
    public boolean isType()
    { return false; }
    
    
    public List<JavaExpressionModel>  getSubExpressions()
    {
        return Collections.emptyList();
    }
    
    /**
     *return Literal String. (I. e. uninterpreted, with quotes and escapes inside)
     */
    public String getString()
    { return t_.getSubtermAt(0).getString(); }
                        
    /**
     * StringLiteral(String)
     */
    public Term getModelTerm()
    { return t_; }
    
    public boolean isConstantExpression() 
    {
        return true;
    }
    
    
    /**
     * return lexical translation of string inside literal
     */
    public Object getConstant() throws TermWareException
    {
      return evalStringLiteral(getString());  
    }
    
    public static String evalStringLiteral(String s) throws TermWareException
    {
      return evalStringLiteral(s,'\"',"String");  
    }
    
    static String evalStringLiteral(String s, char quote, String kind) throws TermWareException
    {
      char[] sarray = s.toCharArray();
      
      if (sarray[0]!=quote) {
          throw new AssertException("Invalid "+kind+" literal:"+s);
      }
      StringBuilder sb = new StringBuilder();
      for(int i=1; i<sarray.length-1; ++i) {
          switch(sarray[i]) {              
              case '\\': 
              {
                if (i+1==sarray.length-1) {
                    throw new AssertException("Invalid "+kind+" literal:"+s);
                }  
                int ch=sarray[i+1];
                switch(ch) {
                    case 'n':
                        sb.append('\n');
                        ++i;
                        break;
                    case 't':
                        sb.append('\t');
                        ++i;
                        break;
                    case 'b':
                        sb.append('\b');
                        ++i;
                        break;
                    case 'r':
                        sb.append('\r');
                        ++i;
                        break;
                    case '\'':
                        sb.append('\'');
                        ++i;
                        break;
                    case '\"':
                        sb.append('\"');
                        ++i;
                        break;
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9':
                    {
                        // read octal constant
                        ++i;                        
                        int sum=0;
                        int j=0;
                        while(Character.isDigit(ch) && j<3) {
                            sum=sum*8;
                            int q = Character.digit(ch,8);
                            sum=sum+q;
                            ++i;
                            if (i==sarray.length) {
                                throw new AssertException("Invalid "+kind+" literal:"+s);
                            }
                            ++j;
                            ch=sarray[i];
                        }
                        ch = (char)sum;
                        --i;
                    }
                    break;
                    case 'u':
                    {
                        // read unicode decimal constant
                        ++i;
                        if (i==sarray.length-1) {
                            throw new AssertException("Invalid "+kind+" literal:"+s);
                        }
                        ++i;
                        if (i==sarray.length-1) {
                            throw new AssertException("Invalid "+kind+" literal:"+s);
                        }                        
                        ch=sarray[i];
                        int sum=0;
                        int j=0;
                        while(Character.isDigit(ch) && j<4) {
                            sum=sum*16;
                            int q = Character.digit(ch,16);
                            sum+=q;
                            ++i;
                            if (i==sarray.length) {
                                throw new AssertException("Invalid "+kind+" literal:"+s);
                            }
                            ++j;
                            ch=sarray[i];
                        }
                        ch=(char)sum;
                        --i;                                                
                    }
                    break;
                    default:
                        throw new AssertException("Invalid literal - unexpected symbol after \\:"+s);                        
                }
              }
              break;
              default:
                  sb.append(sarray[i]);
                  break;
          }
      }
      return sb.toString();
    }
    
    
}
