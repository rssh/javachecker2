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

/**
 *Model for block of Java Code.
 *(yet not enabled)
 * @author Ruslan Shevchenko
 */
public  interface JavaBlockModel {
            
    public JavaTypeModel  getOwnerType();
    
    public enum Kind {
        STATIC,
        METHOD
    }
                
    public JavaMethodAbstractModel  getMethodModel();
    
    public boolean isTopLevelBlock();
    
    public JavaBlockModel  getParentBlock();
    
    public List<JavaBlockModel> getChildBlocks();
    
}
