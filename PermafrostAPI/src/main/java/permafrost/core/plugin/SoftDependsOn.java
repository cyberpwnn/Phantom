package permafrost.core.plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Part of the plugin annotations framework.
 * <p>
 * Represents the plugin's soft dependencies.
 */

@Target(ElementType.TYPE)
public @interface SoftDependsOn
{
	
	public String[] value();
	
}
