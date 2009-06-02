package ua.gradsoft.javachecker.checkers;

import ua.gradsoft.javachecker.ConfigException;
import ua.gradsoft.javachecker.JavaFacts;

/**
 *Checker for InvalidCheckerComment
 * @author rssh
 */
public class InvalidCheckerCommentChecker extends AbstractChecker
{

    public InvalidCheckerCommentChecker()
    {
        super("InvalidCheckerCommand","comments","invalid checker comment",true);
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
