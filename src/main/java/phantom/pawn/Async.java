package phantom.pawn;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Denotes the type it is to be async
 *
 * @author cyberpwn
 */
@Retention(RUNTIME)
@Target({ TYPE, FIELD, METHOD })
public @interface Async
{

}
