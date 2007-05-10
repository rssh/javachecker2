package ann;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface A4C
{
  A3A xy() default @A3A(x=1,y=2);
  int z() default  3 ;
  A3C[] xys() default { @A3C(x=5,y=6), @A3C(x=7,y=8)  };
}
