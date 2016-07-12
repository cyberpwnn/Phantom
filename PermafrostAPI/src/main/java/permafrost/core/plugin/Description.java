package permafrost.core.plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Part of the plugin annotations framework.
 * <p>
 * Represents a short description for the plugin.
 */

@Target(ElementType.TYPE)
public @interface Description
{
	public String value();
}
