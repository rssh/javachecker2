/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ua.gradsoft.models.php5;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *
 * @author rssh
 */
public class PhpLogical_Or_ExpressionModel extends PhpBinaryExpressionModel
{

    public PhpLogical_Or_ExpressionModel(Term t, PhpCompileEnvironment pce) throws TermWareException {
        super(t.getSubtermAt(0), t.getSubtermAt(2), PhpBinaryOperator.LOGICAL_OR, pce);
    }

}
