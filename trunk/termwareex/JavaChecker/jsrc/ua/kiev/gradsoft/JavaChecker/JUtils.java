/*
 * Utils.java
 *
 * Created 18, 02, 2004, 13:46
 */

package ua.kiev.gradsoft.JavaChecker;

import java.io.*;

import ua.kiev.gradsoft.TermWare.*;
import ua.kiev.gradsoft.TermWare.exceptions.*;

/**
 *Utils
 * @author  Ruslan Shevchenko
 */
public final class JUtils {
    
    
    public static String getCompilationUnitPackageName(ITerm compilationUnit) throws TermWareException
    {
     if (compilationUnit.getSubtermAt(0).getName().equals("java_empty_package_declaration")) {
         return "default";
     }else if(compilationUnit.getSubtermAt(0).getName().equals("java_package_declaration")){
         return getJavaNameAsString(compilationUnit.getSubtermAt(0).getSubtermAt(0));         
     }else{
         throw new AssertException("Invalid term for compilation unit:"+TermHelper.termToString(compilationUnit));
     }
    }
    
    public static String getJavaIdentifierAsString(ITerm t) throws TermWareException
    {
        if (!t.getName().equals("java_identifier")) {
            throw new AssertException("term is not java_identifier:"+TermHelper.termToString(t));
        }
        return t.getSubtermAt(0).getString();
    }
    
    /**
     * return complex java name as string/
     **/
    public static String getJavaNameAsString(ITerm t) throws TermWareException
    { 
        if (!t.getName().equals("java_name")) {
            throw new AssertException("term is not java_name:"+TermHelper.termToString(t));
        }
        ITerm cur=t.getSubtermAt(0);
        StringBuffer sb=new StringBuffer();
        boolean emptySb=true;
        while(!cur.isNil()) {
            if (cur.isComplexTerm() && cur.getName().equals("cons")) {
                ITerm identifier=cur.getSubtermAt(0);
                ITerm rest=cur.getSubtermAt(1);
                if (!emptySb) {
                    sb.append(".");
                }
                sb.append(identifier.getSubtermAt(0).getString());
                emptySb=false;
                cur=rest;
            }else if (cur.isComplexTerm() && cur.getName().equals("java_identifier")){
                sb.append(cur.getSubtermAt(0).getString());
            }else{
                sb.append("<error>");
            }
        }
        return sb.toString();
    }

    /**
     * search java_identifier term in current subterm.
     *   (parser set attributes (file and line) to java_identifier terms).
     * nil if one is not found.
     **/
    public static ITerm findMarkedIdentifier(ITerm t) throws TermWareException
    {
     if (t.isComplexTerm()) {
       if (t.getName().equals("java_identifier")) {
         return t;
       }else{
         for(int i=0; i<t.getArity(); ++i){
           ITerm c=findMarkedIdentifier(t.getSubtermAt(i));
           if (!c.isNil()) {
             return c;
           }
         }
       }
     }
     return ITermFactory.createNil();
    }
    
    /**
     * get file and line marker from part of code.
     *if part of code does not contains markers - create FileAndLine as "unknown/-1"
     *@return FileAndLine marker for some identifier inside code.
     *@exception TermWareException if somethibg occurs.
     */
    public static FileAndLine getFileAndLine(ITerm partOfCode) throws TermWareException
    {
     ITerm markedIdentifier=JUtils.findMarkedIdentifier(partOfCode);
     ITerm fileTerm=TermHelper.getAttribute(markedIdentifier,"file");
     ITerm lineTerm=TermHelper.getAttribute(markedIdentifier,"line");
     String fname="unknown, entry is:";
     int    line=-1;
     if (!fileTerm.isNil()) {
         fname=fileTerm.getString();
     }else{
         fname+=TermHelper.termToString(partOfCode);
     }
     if (!lineTerm.isNil()) {
         if (lineTerm.isString()) {
           String sline=lineTerm.getString();
           try {
             line=Integer.parseInt(sline);
           }catch(NumberFormatException ex){
              throw new AssertException("internal error: line attribute can't be parsed as string");
           }
         }else if(lineTerm.isInt()) {
             line=lineTerm.getInt();
         }
     }
     return new FileAndLine(fname,line);
    }
    
    /**
     * if properties list contains java_static
     *@return true if properties list contains java_static.
     */
    public static boolean getIsStatic(ITerm propertiesList) throws TermWareException
    {
      return isFoundJavaAttribute(propertiesList,"java_static");
    }
    
    /**
     * if properties list contains java_final
     *@return true if properties list contains java_final
     */
    public static boolean getIsFinal(ITerm propertiesList) throws TermWareException
    {
      return isFoundJavaAttribute(propertiesList,"java_final");
    }
    
    /**
     * if properties list contains java_synchronized
     *@return true if properties list contains java_synchronized
     */
    public static boolean getIsSynchronized(ITerm propertiesList) throws TermWareException
    {
      return isFoundJavaAttribute(propertiesList,"java_synchronized");
    }
    
    
    public static boolean isFoundJavaAttribute(ITerm propertiesList,String attributeName) throws TermWareException
    {
      if (propertiesList.getName().equals("empty_list")) {
          return false;
      }
      while(!propertiesList.isNil()) {
          if (propertiesList.getName().equals("cons")) {
              ITerm frs=propertiesList.getSubtermAt(0);
              ITerm snd=propertiesList.getSubtermAt(1);
              if (frs.getName().equals(attributeName)) {
                  return true;
              }
              propertiesList=snd;
          }else{
              throw new AssertException("invalid list of java attributes");
          }
      }
      return false;
    }

    
    /**
     * generate name of setter method for variable <name>.
     *i. e. xxx_ -> setXxx()
     */
    public static String generateSetterName(String name)
    {
      StringBuffer sb=new StringBuffer();
      sb.append("set");
      char ch=name.charAt(0);
      boolean leadingUnderscore=false;
      if (ch=='_') {
          leadingUnderscore=true;
          if (name.length()==1) {
              return "set_";
          }
          ch=name.charAt(1);
      }
      char upCh=Character.toUpperCase(ch);
      sb.append(upCh);
      int beginOffset=1;
      if (leadingUnderscore) ++beginOffset;
      int endOffset=name.length();
      if (name.endsWith("_")) {
          endOffset--;
      }
      sb.append(name.substring(beginOffset,endOffset));
      return sb.toString();
    }
    
    
}
