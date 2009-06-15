
package ua.gradsoft.javachecker.checkers;

import ua.gradsoft.javachecker.ConfigException;
import ua.gradsoft.javachecker.JavaFacts;

/**
 *
 * *Dummy checker.
 * (actually, violations are detected by parser during sources,
 *  we create dummy checker for it, just to have checker with such name
 *  in a table of all checkers)
 * @author rssh
 */
public class DummyChecker extends AbstractChecker
{

    public DummyChecker(String name, String category, String description, boolean enabled)
    {
        super(name,category,description,enabled);
    }

    @Override
    public void configure(JavaFacts facts) throws ConfigException {
        /* do nothing */
    }

    @Override
    public CheckerType getCheckerType() {
        return CheckerType.DUMMY;
    }


}
