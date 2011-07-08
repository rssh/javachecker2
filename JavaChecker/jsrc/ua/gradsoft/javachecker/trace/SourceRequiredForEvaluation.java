package ua.gradsoft.javachecker.trace;

/**
 *Throwed, when known that source is required for evaluation.
 * @author rssh
 */
public class SourceRequiredForEvaluation extends EvaluationException
{

    public SourceRequiredForEvaluation()
    {
      super(1,"source required for evaluation");  
    }


}
