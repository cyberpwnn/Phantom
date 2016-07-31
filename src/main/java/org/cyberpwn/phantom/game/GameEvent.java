package org.cyberpwn.phantom.game;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Game Event tag for methods
 * 
 * @author cyberpwn
 *
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface GameEvent
{
	
}
