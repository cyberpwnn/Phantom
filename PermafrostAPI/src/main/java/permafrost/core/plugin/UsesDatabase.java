package permafrost.core.plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Part of the plugin annotations framework.
 * <p>
 * Denotes this plugin as using Bukkit's bundled database system.
 */

@Target(ElementType.TYPE)
public @interface UsesDatabase
{
}
