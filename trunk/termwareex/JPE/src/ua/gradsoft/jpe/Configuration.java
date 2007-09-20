/*
 * Configuration.java
 *
 *
 * Copyright (c) 2006-2007 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.jpe;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *Class which store JPE Configuration.
 */
public class Configuration implements Serializable {
    
    public void init(String[] args) throws JPEConfigurationException
    {
        String hashFileName=null;
        for(int i=0;i<args.length; ++i) {
            if (args[i].equals("--input-dir")) {
                if (i==args.length-1) {
                    throw new JPEConfigurationException("option --input-dir must have argument");
                }
                inputDir_=args[i+1];
                ++i;
            }else if(args[i].equals("--include-dir")) {
                if (i==args.length-1) {
                    throw new JPEConfigurationException("option --input-dir must have argument");
                }
                includeDirs_.add(args[i+1]);
                ++i;                
            }else if(args[i].equals("--output-dir")) {
                if (i==args.length-1) {
                    throw new JPEConfigurationException("option --output-dir must have argument");
                }                
                outputDir_=args[i+1];
                ++i;
            }else if(args[i].equals("--ct-class")) {
                if (i==args.length-1) {
                    throw new JPEConfigurationException("option --ct-class must have argument");
                }                                
                compileTimeClass_=args[i+1];
                ++i;
            }else if(args[i].equals("--input-file")) {
                if (i==args.length-1) {
                    throw new JPEConfigurationException("option --input-file must have argument");
                }                                                
                hashFileName=args[i+1];
                ++i;
            }else if(args[i].equals("--jpehome")) {
                if (i==args.length-1) {
                    throw new JPEConfigurationException("option --jpehome must have argument");
                }
                jpeHome_=args[i+1];
                ++i;
            }else if(args[i].equals("--transformation")) {
                if (i==args.length-1) {
                    throw new JPEConfigurationException("option --transformation must have argument");
                }
                transformationName_=args[i+1];
                ++i;           
            }else if(args[i].equals("--dump")){
                dump_=true;
            }else if(args[i].equals("--value-pair")){
                if (i>=args.length-2) {
                    throw new JPEConfigurationException("option --value-pair must have two arguments");
                }                                                
                String key=args[++i];
                String value=args[++i];
                compileTimeProperties_.put(key,value);
            }else if(args[i].equals("--create-output-dir")){
                createOutputDir_=true;
            }else{
                throw new JPEConfigurationException("Unknown option:"+args[i]);
            }
        }
        if (hashFileName!=null) {
            readHashFile(hashFileName);
        }
        if (inputDir_==null) {
            throw new JPEConfigurationException("option --input-dir is mandatory");            
        }
        if (outputDir_==null) {
            throw new JPEConfigurationException("option --output-dir is mandatory");            
        }
        if (jpeHome_==null) {
            throw new JPEConfigurationException("option --jpe-home is mandatory");            
        }

    }
    
    private void readHashFile(String hashFileName) throws JPEConfigurationException
    {
        throw new JPEConfigurationException("readHashFile is not implemented yet");
    }
    
    public String getInputDir()
    {
        return inputDir_;
    }
    
    public void  setInputDir(String inputDir)
    {
       inputDir_ = inputDir; 
    }        
    
    public String getOutputDir()
    {
        return outputDir_;
    }
    
    public void  setOutputDir(String outputDir)
    {
        outputDir_=outputDir;
    }
    
    public String getJPEHome()
    {
        return jpeHome_;
    }
    
    public void  setJPEHome(String jpeHome)
    {
       jpeHome_=jpeHome; 
    }
    
    public String getTransformationName()
    {
        return transformationName_;
    }
    
    public void setTransformationName(String transformationName)
    { 
        transformationName_=transformationName; 
    }
    
    public boolean isDump()
    {
        return dump_;
    }
    
    public void setDump(boolean dump)
    {
        dump_=dump;
    }
    
    public boolean isCreateOutputDir()
    {
        return createOutputDir_;
    }
    
    public void setCreateOutputDir(boolean createOutputDir)
    {
       createOutputDir_=createOutputDir; 
    }
    
    public void addIncludeDir(String includeDir)
    {
        includeDirs_.add(includeDir);
    }
    
    public List<String>  getIncludeDirs()
    {
        return includeDirs_;
    }
    
    public void  setIncludeDirs(List<String> includeDirs)
    {
        includeDirs_=includeDirs;
    }
    
    public Map<String,String>  getCompileTimeProperties()
    {
       return compileTimeProperties_; 
    }
    
    private boolean dump_=false;
    private boolean createOutputDir_=false;    
    private String inputDir_=null;
    private String outputDir_=null;
    private List<String>  includeDirs_=new LinkedList<String>();
    private String compileTimeClass_=null;
    private String jpeHome_=null;
    private String transformationName_="JPE";
    private HashMap<String,String> compileTimeProperties_=new HashMap<String,String>();
    
}
