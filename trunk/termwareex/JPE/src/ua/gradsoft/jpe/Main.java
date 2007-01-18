/*
 * Main.java
 *
 * Created on понеділок, 1, січня 2007, 23:34
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.jpe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.JUtils;
import ua.gradsoft.parsers.java5.JavaParserFactory;
import ua.gradsoft.printers.java5.JavaPrinterFactory;
import ua.gradsoft.termware.IEnv;
import ua.gradsoft.termware.IFacts;
import ua.gradsoft.termware.ITermRewritingStrategy;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.envs.SystemEnv;
import ua.gradsoft.termware.exceptions.ExternalException;
import ua.gradsoft.termware.strategies.FirstTopStrategy;

/**
 *
 * @author Ruslan Shevchenko
 */
public class Main {
    
    public static void main(String args[]) {
        Main main=new Main();
        try{
            main.init(args);
            main.run();
        }catch(JPEConfigurationException ex){
            System.err.print("exception during initialization");
            ex.printStackTrace();
        }catch(JPEProcessingException ex){
            System.err.print("exception during processing");
            ex.printStackTrace();
        }
    }
    
    public void init(String[] args) throws JPEConfigurationException {
        configuration_.init(args);
        init(configuration_);
    }
    
    public void init(Configuration configuration) throws JPEConfigurationException {        
        configuration_=configuration;
        IEnv env=new SystemEnv();
        TermWare.getInstance().init();
        TermWare.getInstance().addParserFactory("Java",new JavaParserFactory());
        TermWare.getInstance().addPrinterFactory("Java",new JavaPrinterFactory());
        TermWare.getInstance().setEnv(env);
        try {
            TermWare.getInstance().getTermLoader().addSearchPath(configuration_.getJPEHome()+File.separator+"systems");
        }catch(ExternalException ex){
            throw new JPEConfigurationException("exception during init",ex);
        }
        startTransformer_ = new JPEStartTransformer(configuration_.getTransformationName());
        try {           
//            IFacts facts = new JPEFacts(env,configuration_);
//            ITermRewritingStrategy strategy=new FirstTopStrategy();
//            jpeSystem_=new TermSystem(strategy,facts,TermWare.getInstance());
//            jpeSystem_.addRule("JPE(CompilationUnit$[$x:$y])->CompilationUnit$[JPE($x),JPE($y)]");
//            jpeSystem_.addRule("JPE(NIL)->NIL");
            jpeSystem_=TermWare.getInstance().getOrCreateDomain("JPE").resolveSystem(configuration_.getTransformationName());
            IFacts facts = jpeSystem_.getFacts();           
            if (facts instanceof JPEFacts) {
                ((JPEFacts)facts).setConfiguration(configuration_);
            }
        }catch(TermWareException ex){
            throw new JPEConfigurationException("exception during init",ex);
        }   
    }
    
    public void run() throws JPEConfigurationException, JPEProcessingException {
        transformDirs(configuration_.getInputDir(),configuration_.getOutputDir());
    }
    
    private void transformDirs(String inputDir,String outputDir) throws JPEConfigurationException, JPEProcessingException {
        String packageDir="";
        File inputDirFile=new File(inputDir);
        if (inputDirFile.exists()) {
            if (inputDirFile.isDirectory()) {
                /* all is ok */
            }else{
                throw new JPEConfigurationException("input dir "+inputDir+" is not directory");
            }
        }else{
            throw new JPEConfigurationException("input dir "+inputDir+" does not exists");
        }
        File outputDirFile=new File(outputDir);
        if (outputDirFile.exists()) {
            if (outputDirFile.isDirectory()) {
                /* all ok */
            }else{
                throw new JPEConfigurationException("output dir "+outputDir+" is not directory");
            }
        }else{
            if (configuration_.isCreateOutputDir()) {
                outputDirFile.mkdirs();
            }else{
                throw new JPEConfigurationException("output dir "+outputDir+" does not exists");
            }
        }
        if (inputDirFile.getAbsolutePath().equals(outputDirFile.getAbsolutePath())) {
            throw new JPEConfigurationException("input and output direcotries must be different ");
        }
        transformDirs(packageDir,inputDir,outputDir,inputDirFile);
    }
    
    private void transformDirs(String packageDir,String sourceDir,String destinationDir,File d) throws JPEProcessingException{
        File[] files=d.listFiles();
        for(int i=0; i<files.length; ++i) {
            if (files[i].isDirectory()) {
                String nextPackageDir = (packageDir.length()==0 ? files[i].getName() : packageDir+"."+files[i].getName());
                transformDirs(nextPackageDir, sourceDir, destinationDir,files[i]);
            }else if(files[i].getName().endsWith(".java")) {
                transformFile(packageDir, sourceDir, destinationDir, files[i]);
            }
        }
    }
    
    private void transformFile(String packageDir, String sourceDir, String destinationDir, File f) throws JPEProcessingException {
        System.out.println("transform file:"+f.getAbsolutePath());
        Reader reader = null;
        try {
            reader = new FileReader(f);
        }catch(FileNotFoundException ex){
            throw new JPEProcessingException("error during opening file "+f.getAbsolutePath(),ex);
        }
        Term nil=TermWare.getInstance().getTermFactory().createNIL();
        Term source=null;
        try {
            source=TermWare.getInstance().getParserFactory("Java").createParser(reader,f.getAbsolutePath(),nil,TermWare.getInstance()).readTerm();
        }catch(TermWareException ex){
            throw new JPEProcessingException("exception during reading file "+f.getAbsolutePath(),ex);
        }
        TermSystem jpeSystem=getJPESystem();        
        Term transformedSource=null;
        try {
            Term startSource=startTransformer_.transform(source);
            System.err.println("startSource="+TermHelper.termToString(startSource));
            transformedSource=jpeSystem.reduce(startSource);
            System.err.println("transformedSource="+TermHelper.termToString(transformedSource));
        }catch(TermWareException ex){
            throw new JPEProcessingException("exception during transforming file "+f.getAbsolutePath(),ex);
        }
        if (!transformedSource.getName().equals("CompilationUnit")) {
            transformedSource.println(System.err);
            throw new JPEProcessingException("transformed source is not compilation unit.");
        }
        if (transformedSource.getArity()!=0) {
            if (configuration_.isDump()) {
                transformedSource.println(System.out);
            }
            String packageName = "";
            try {
              packageName=JUtils.getCompilationUnitPackageName(transformedSource);
            }catch(TermWareException ex){
              throw new JPEProcessingException("exception during getting package name for results for "+f.getAbsolutePath(),ex);  
            }
            String firstClassName = null;
            try {
              firstClassName = JUtils.getFirstTypeDefinitionName(transformedSource);
            }catch(TermWareException ex){
              throw new JPEProcessingException("exception during getting type declaration name for results for "+f.getAbsolutePath(),ex);    
            }catch(EntityNotFoundException ex){
                // file without type declaration, i. e. empty.
                // just do nothing.
                return;
            }            
            String sTransformed = null;
            try {
                sTransformed=TermHelper.termToPrettyString(transformedSource,"Java",TermWare.getInstance().getTermFactory().createNIL());
            }catch(TermWareException ex){
                transformedSource.println(System.err);
                throw new JPEProcessingException("exception during printing resuts for "+f.getAbsolutePath(),ex);
            }
            FileWriter writer = null;
            String destinationPackageDir=JUtils.createDirectoryNameFromPackageName(destinationDir,packageName);
            File destinationDirFile = new File(destinationPackageDir);
            if (!destinationDirFile.exists()) {
                destinationDirFile.mkdirs();
            }
            String outputFname=destinationPackageDir+File.separator+JUtils.createSourceFileNameFromClassName(firstClassName);
            try {
                writer=new FileWriter(outputFname);
                writer.append(sTransformed);
            }catch(IOException ex){
                throw new JPEProcessingException("exception during  "+f.getAbsolutePath(),ex);
            }finally{
                if (writer!=null){
                    try {
                        writer.close();
                    }catch(IOException ex){
                        System.err.println("exception during closing file "+outputFname);
                    }
                }
            }
        }
    }
    
    private TermSystem     getJPESystem() {
        return jpeSystem_; }
    
    private JPEStartTransformer  startTransformer_=null;
    private TermSystem    jpeSystem_=null;
    private Configuration configuration_=new Configuration();
    
}
