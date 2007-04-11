/*
 * JavaBlockModel.java
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.List;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

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
        
    /**
     * return model term (which is sequence of statement models)    
     */
    public Term getModelTerm() throws TermWareException, EntityNotFoundException;
    
}
