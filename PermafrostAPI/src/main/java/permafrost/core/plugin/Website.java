package permafrost.core.plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Part of the plugin annotations framework.
 * <p>
 * Represents the website of the plugin.
 */

@Target(ElementType.TYPE)
public @interface Website
{
	
	public String value();
	
}