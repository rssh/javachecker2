/*
 * ParsePrintParseTest.java
 *
 * Created on четвер, 18, січня 2007, 16:34
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.jpe;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import junit.framework.TestCase;
import ua.gradsoft.termware.IParser;
import ua.gradsoft.termware.IParserFactory;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *
 * @author Ruslan Shevchenko
 */
public class ParsePrintParseTest extends TestCase
{
    
    public void testExample1() throws Exception
    {
        Configuration configuration = new Configuration();
        configuration.setJPEHome(".");
        configuration.setInputDir("testdata/example1");
        configuration.setOutputDir("output/id-example1");
        configuration.setTransformationName("ID");
        configuration.setCreateOutputDir(true);
        Main main = new Main();
        main.init(configuration);
        main.run();
        int nReadedFiles = checkAllSourcesAreParsed("output/id-example1");
        assertTrue("all files are read",nReadedFiles>0);
    }
    
    public void testExample2() throws Exception
    {
        Configuration configuration = new Configuration();
        configuration.setJPEHome(".");
        configuration.setInputDir("testdata/example2");
        configuration.setOutputDir("output/id-example2");
        configuration.setTransformationName("ID");
        configuration.setCreateOutputDir(true);
        Main main = new Main();
        main.init(configuration);
        main.run();
        int nReadedFiles = checkAllSourcesAreParsed("output/id-example2");
        assertTrue("all files are read",nReadedFiles>0);
    }
    
    
    
    private int checkAllSourcesAreParsed(String fname) throws Exception
    {
        File f = new File(fname);
        if (f.isDirectory()) {
            return checkSourcesAreParsedInDir(f);
        }else{
            if (f.getName().endsWith(".java")) {
                Term t = parseSource(f);
                return 1;
            }
        }
        return 0;
    }
    
    private int checkSourcesAreParsedInDir(File f) throws Exception
    {        
        if (f.isDirectory()) {
            int nProcessed=0;
            File[] sfs = f.listFiles();   
            for(File sf: sfs) {
                System.err.print(":"+sf.getName()+":");
            }
            for(File sf: sfs) {                
                if (sf.isDirectory()) {                    
                    nProcessed+=checkSourcesAreParsedInDir(sf);
                }else if(sf.getCanonicalPath().endsWith(".java")) {                    
                    Term t = parseSource(sf);
                    ++nProcessed;
                }else{                    
                    throw new AssertException("Non-Java file in output:"+f.getCanonicalPath());
                }
            }
            return nProcessed;
        }else{
            throw new RuntimeException("argument of checkSourcesAreParsedInDir must be directory");
        }        
    }
    
    private Term parseSource(File f) throws Exception
    {
        IParserFactory parserFactory = TermWare.getInstance().getParserFactory("Java");
        Reader reader=null;
        Term retval=null;
        try {
            reader=new BufferedReader(new FileReader(f));
            IParser parser=parserFactory.createParser(reader,f.getAbsolutePath(),TermWare.getInstance().getTermFactory().createNIL(),TermWare.getInstance());
            retval=parser.readTerm();
        }finally{
            if(reader!=null){
                reader.close();
            }
        }
        return retval;
    }
    
}
