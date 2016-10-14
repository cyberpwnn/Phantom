package org.phantomapi.construct;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Define a controller be ticked at an interval
 * 0 = 20 ticks per second
 * 10 = 2 ticks per second
 * @author cyberpwn
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Ticked
{
	public int value() default 0;
}
