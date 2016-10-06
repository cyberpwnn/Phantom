package org.phantomapi.callable;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Callable description
 * 
 * @author cyberpwn
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface Description
{
	public String value();
}
