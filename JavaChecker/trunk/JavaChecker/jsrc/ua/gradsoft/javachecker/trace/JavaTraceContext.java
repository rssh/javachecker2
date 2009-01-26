
package ua.gradsoft.javachecker.trace;

import java.util.Map;
import ua.gradsoft.javachecker.models.JavaPlaceContext;
import ua.gradsoft.javachecker.annotations.Nullable;

/**
 *Trace context, which can keep state of variables.
 * @author rssh
 */
public class JavaTraceContext {

    public JavaTraceContext(JavaPlaceContext place)
    {
      place_=place;
    }

    public JavaPlaceContext getPlace()
    { return place_; }

    @Nullable
    public JavaTraceObjectModel getLocalVariable(String name)
    {
      JavaTraceObjectModel retval=null;  
      if (localVariables_ !=null ) {
          retval=localVariables_.get(name);
      }       
      return retval;      
    }

    private JavaPlaceContext place_;
    private Map<String, JavaTraceObjectModel> localVariables_;
    private Map<String, JavaTraceObjectModel> formalParameters_;
    private JavaTraceObjectModel  thisTrace_;

    private JavaTraceContext prev_;
    private JavaTraceContextFrameType frameType;

}
