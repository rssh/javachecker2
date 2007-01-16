/*
 * JavaModelConstants.java
 *
 * Copyright (c) 2004-2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.LinkedList;
import java.util.List;

/**
 *Interface which define few usefull constants
 * @author Ruslan Shevchenko
 */
public class JavaModelConstants {
   
   public final static List<JavaStatementModel>            STATEMENT_EMPTY_LIST = new LinkedList<JavaStatementModel>();
   public final static List<JavaTypeVariableAbstractModel> TYPEVARIABLE_EMPTY_LIST = new LinkedList<JavaTypeVariableAbstractModel>();
   public final static List<JavaTypeModel>                 TYPEMODEL_EMPTY_LIST    = new LinkedList<JavaTypeModel>();
    
}
