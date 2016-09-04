package org.phantomapi.command;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Represents a firable command
 * 
 * @author cyberpwn
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface Command
{
	String value();
}
