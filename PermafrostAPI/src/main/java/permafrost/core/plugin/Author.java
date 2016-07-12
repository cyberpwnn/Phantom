package permafrost.core.plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Part of the plugin annotations framework.
 * <p>
 * Represents the author(s) of the plugin. Translates to {@code author} in
 * plugin.yml if a single author, otherwise {@code authors}
 */

@Target(ElementType.TYPE)
public @interface Author
{
	public String[] value();
}
