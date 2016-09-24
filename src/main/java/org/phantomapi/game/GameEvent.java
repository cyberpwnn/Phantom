package org.phantomapi.game;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Represents a game event annotation to signal that this should listen on game
 * events
 * 
 * @author cyberpwn
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface GameEvent
{
	
}
