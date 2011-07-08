/*
 * Configuration.java
 *
 * Created on понеділок, 1, січня 2007, 23:36
 *
 * Copyright (c) 2006-2007 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.jpe;

import java.util.HashMap;

/**
 *Class which store JPE Configuration.
 */
public class Configuration {
    
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
            }else if(args[i].equals("--value-pair")){
                if (i>=args.length-2) {
                    throw new JPEConfigurationException("option --value-pair must have two arguments");
                }                                                
                String key=args[++i];
                String value=args[++i];
                compileTimeProperties_.put(key,value);
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
    
    public String getOutputDir()
    {
        return outputDir_;
    }
    
    public String getJPEHome()
    {
        return jpeHome_;
    }
    
    public String getTransformationName()
    {
        return transformationName_;
    }
    
    private String inputDir_=null;
    private String outputDir_=null;
    private String compileTimeClass_=null;
    private String jpeHome_=null;
    private String transformationName_="JPE";
    private HashMap compileTimeProperties_=new HashMap();
    
}
