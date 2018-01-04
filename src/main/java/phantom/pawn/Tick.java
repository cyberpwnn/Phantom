package phantom.pawn;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import phantom.util.metrics.Documented;

/**
 * Indicates that the method should be ticked
 *
 * @author cyberpwn
 */
@Documented
@Retention(RUNTIME)
@Target(METHOD)
public @interface Tick
{
	int value();
}
