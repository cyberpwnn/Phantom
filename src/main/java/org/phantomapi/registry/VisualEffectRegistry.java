package org.phantomapi.registry;

import org.bukkit.Location;
import org.phantomapi.vfx.VisualEffect;

/**
 * A Visual effect registry
 * 
 * @author cyberpwn
 */
public class VisualEffectRegistry extends RegistryBank<VisualEffect>
{
	/**
	 * Play the given visual effect if it exists
	 * 
	 * @param s
	 *            the key
	 * @param location
	 *            the location
	 */
	public void play(String s, Location location)
	{
		if(contains(s))
		{
			get(s).play(location);
		}
	}
}
