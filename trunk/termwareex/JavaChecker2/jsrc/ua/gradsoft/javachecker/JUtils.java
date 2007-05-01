/*
 * Utils.java
 *
 * Created 18, 02, 2004, 13:46
 */

package ua.gradsoft.javachecker;

import java.io.File;
import java.io.Reader;
import java.util.logging.Logger;
import ua.gradsoft.termware.IParser;
import ua.gradsoft.termware.IParserFactory;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;


/**
 *Utils
 * @author  Ruslan Shevchenko
 */
public final class JUtils {
    

     public static Term readSourceFile(File f) throws TermWareException
     {
      if (Main.isShowFiles() && !Main.isQOption()) {
        logger_.info("reading file:"+f.getAbsolutePath());   
      }
      Term parserOption=TermWare.getInstance().getTermFactory().createAtom("simplify");
      Term source=TermWare.getInstance().load(f.getAbsolutePath(),TermWare.getInstance().getParserFactory("Java"),parserOption);
      return source;
     }

    
    public static String getCompilationUnitPackageName(Term compilationUnit) throws TermWareException
    {
     if(compilationUnit.getSubtermAt(0).getName().equals("PackageDeclaration")){
         Term packageTerm = compilationUnit.getSubtermAt(0);
         return getJavaNameAsString(packageTerm.getSubtermAt(0));         
     }else{
         return "default";
     }
    }
    
    /**
     * get from term (which must represent compilation unit) name of first declared type.
     */ 
    public static String getFirstTypeDefinitionName(Term compilationUnit) throws TermWareException, EntityNotFoundException
    {
      String retval=null;  
      for(int i=0; i<compilationUnit.getArity(); ++i)  {
          Term dcl=compilationUnit.getSubtermAt(i);
          if (dcl.getName().equals("TypeDeclaration")) {
              Term ct=dcl.getSubtermAt(1);
              if (ct.getName().equals("ClassOrInterfaceDeclaration")) {
                  retval=ct.getSubtermAt(1).getSubtermAt(0).getString();
              }else if(ct.getName().equals("EnumDeclaration")){
                  retval=ct.getSubtermAt(0).getSubtermAt(0).getString();
              }else if(ct.getName().equals("AnnotationTypeDeclaration")){
                  retval=ct.getSubtermAt(0).getSubtermAt(0).getString();
              }else{
                  throw new AssertException("Invalid contest of type declaration");
              }
          }
          if (retval!=null) {
              break;
          }
      }
      if (retval==null) {
          throw new EntityNotFoundException("*","type declaration"," in compilation unit");
      }
      return retval;
    }
    
    /***
     * create directory name from package name.
     *@param srcDir -- root directory of source.
     *@param packageName -- name of package.
     */
    public static String createDirectoryNameFromPackageName(String srcDir,String packageName)
    {
        String transformedPackageName=packageName.replace('.',File.separatorChar);
        String retval=null;
        if (srcDir.endsWith(File.separator)) {
            retval=srcDir+transformedPackageName;
        }else if(srcDir.endsWith("/")) {
            retval=srcDir+transformedPackageName;
        }else{
            retval=srcDir+File.separator+transformedPackageName;
        }
        return retval;
    }

    public static String createSourceFileNameFromClassName(String className)
    {
      return createSourceFileNameFromClassName(className,".java");  
    }
    
    public static String createSourceFileNameFromClassName(String className, String ext)
    {
      String sourceName=className;  
      int dIndex=className.indexOf('$');
      if (dIndex!=-1) {
          sourceName=sourceName.substring(0,dIndex);
      }  
      sourceName=sourceName+ext;
      return sourceName;
    }
    
    public static String getJavaIdentifierAsString(Term t) throws TermWareException
    {
        if (!t.getName().equals("Identifier")) {
            throw new AssertException("term is not java_identifier:"+TermHelper.termToString(t));
        }
        return t.getSubtermAt(0).getString();
    }
    
    /**
     * return complex java name as string/
     **/
    public static String getJavaNameAsString(Term t) throws TermWareException
    { return getJavaNameAsString(t,-1); }           
    
    /**
     *return first <code> lastIndex </code> components of list t as complext java name as string
     */
    public static String getJavaNameAsString(Term t, int lastIndex) throws TermWareException        
    { 
        if (!t.getName().equals("Name")) {
            throw new AssertException("term is not java_name:"+TermHelper.termToString(t));
        }
        Term cur=t.getSubtermAt(0);
        StringBuffer sb=new StringBuffer();
        boolean emptySb=true;
        int curIndex=0;
        while(!cur.isNil() && (lastIndex==-1 || curIndex<lastIndex)) {
            if (cur.isComplexTerm() && cur.getName().equals("cons")) {                
                Term identifier=cur.getSubtermAt(0);
                Term rest=cur.getSubtermAt(1);
                if (!emptySb) {
                    sb.append(".");
                }
                sb.append(identifier.getSubtermAt(0).getName());
                emptySb=false;
                cur=rest;
            }else if (cur.isComplexTerm() && cur.getName().equals("Identifier")){
                sb.append(cur.getSubtermAt(0).getName());
            }else{
                sb.append("<error>");
            }
            ++curIndex;
        }
        return sb.toString();
    }
    
    public static String getJavaNameLastComponentAsString(Term t) throws TermWareException
    {
        if (!t.getName().equals("Name")) {
            throw new AssertException("term is not java_name:"+TermHelper.termToString(t));
        }
        String retval=null;
        Term l=t.getSubtermAt(0);
        while(!l.isNil()) {
            Term identifier = l.getSubtermAt(0);
            retval=identifier.getSubtermAt(0).getString();
            l=l.getSubtermAt(1);
        }
        return retval;
    }

    /**
     * search java_identifier term in current subterm.
     *   (parser set attributes (file and line) to java_identifier terms).
     * nil if one is not found.
     **/
    public static Term findMarkedIdentifier(Term t) throws TermWareException
    {
     if (t.isComplexTerm()) {
       if (t.getName().equals("Identifier")) {
         return t;
       }else{
         for(int i=0; i<t.getArity(); ++i){
           Term c=findMarkedIdentifier(t.getSubtermAt(i));
           if (!c.isNil()) {
             return c;
           }
         }
       }
     }
     return TermWare.getInstance().getTermFactory().createNIL(); 
    }
    
    /**
     * get file and line marker from part of code.
     * if part of code does not contains markers - create FL as "unknown/-1"
     * 
     * @returnFLe marker for some identifier inside code.
     * @exception TermWareException if somethibg occurs.
     */
    public static FileAndLine getFileAndLine(Term partOfCode) throws TermWareException
    {
     if (partOfCode.isJavaObject()) {
         Object o = partOfCode.getJavaObject();
         if (o instanceof FileAndLine) {
             return (FileAndLine)o;
         }
     }   
     Term markedIdentifier=JUtils.findMarkedIdentifier(partOfCode);
     Term fileTerm=TermHelper.getAttribute(markedIdentifier,"file");
     Term lineTerm=TermHelper.getAttribute(markedIdentifier,"line");
     String fname="unknown, entry is:";
     int    line=-1;
     if (fileTerm.isNil() && lineTerm.isNil()) {
         return FileAndLine.UNKNOWN;
     }
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
     *Read java language element.
     *@param reader -- reader.
     *@param languageElementName -- name of Java Language Element. Must be one of names
     *of ua.gradsoft.parsers.java.JavaSyntaxElement enum. (See TermWareJPP project for details)
     */
    public static Term parseJavaLanguageElement(Reader reader,String languageElementName) throws TermWareException
    {      
      Term optionTerm = TermWare.getInstance().getTermFactory().createAtom(languageElementName);
      IParserFactory parserFactory = TermWare.getInstance().getParserFactory("Java");
      IParser parser = parserFactory.createParser(reader,"inline",optionTerm,TermWare.getInstance());
      Term t = parser.readTerm();
      return t;
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
    
    private static Logger logger_ = Logger.getLogger(JUtils.class.getName());
    
}
