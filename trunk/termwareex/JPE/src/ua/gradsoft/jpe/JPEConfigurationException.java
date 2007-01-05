/*
 * JPEConfigurationException.java
 *
 * Created on понеділок, 1, січня 2007, 23:47
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.jpe;

/**
 *Exception which is throwing during incorrect configuration.
 * @author Ruslan Shevchenko
 */
public class JPEConfigurationException extends Exception
{
    JPEConfigurationException(String message)
    { super(message); }

    JPEConfigurationException(String message, Exception ex)
    { super(message,ex); }
    
}
