package phantom.pawn;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Defines a controller with a specified name.
 *
 * @author cyberpwn
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface Controller
{
	/**
	 * Get The name of the controller
	 * 
	 * @return the controller name.
	 */
	String value();
}
