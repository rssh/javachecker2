package ann;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface A2C
{
  String value() default  "qqq" ;
}
