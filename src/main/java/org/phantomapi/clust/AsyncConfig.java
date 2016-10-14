package org.phantomapi.clust;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Signals that this config can be async loaded and saved
 * 
 * @author cyberpwn
 */
@Retention(RUNTIME)
@Target({TYPE, METHOD})
public @interface AsyncConfig
{
	
}
