/*
 * JavaIdentifierExpressionModel.java
 *
 */

package ua.gradsoft.javachecker.models.expressions;

import ua.gradsoft.javachecker.models.JavaExpressionModel;

/**
 *Interface for expression, which consists from identifiers.
 * @author rssh
 */
public interface JavaIdentifierExpressionModel extends JavaExpressionModel
{

    public String getIdentifier();
    
}
