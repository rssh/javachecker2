/*
 * JavaUnitModel.java
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.List;

/**
 *Abstract class for java unit. (Source file or class file or class entry in jar).
 */
public abstract class JavaUnitModel {

    public abstract String getPackageName();
    
    public abstract List<JavaTypeModel>  getTypeModels();
    
}
