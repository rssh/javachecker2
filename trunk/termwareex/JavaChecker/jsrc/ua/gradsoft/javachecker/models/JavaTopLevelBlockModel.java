/*
 * JavaBlockModel.java
 *
 * Created on середа, 6, грудня 2006, 2:42
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.List;
import java.util.Map;

/**
 *Model for top-level block of Java Code.
 * @author Ruslan Shevchenko
 */
public  interface JavaTopLevelBlockModel {
        
    
    public JavaTopLevelBlockOwnerModel  getOwnerModel();                       
            
    /**
     * return list of statements in block.
     */
    public List<JavaStatementModel> getStatements();
        
    
}
