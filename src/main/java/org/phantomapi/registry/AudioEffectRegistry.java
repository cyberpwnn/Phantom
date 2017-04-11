package org.phantomapi.registry;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.phantomapi.lang.Audible;

/**
 * A Audible effect registry
 * 
 * @author cyberpwn
 */
public class AudioEffectRegistry extends RegistryBank<Audible>
{
	/**
	 * Play the given audio effect if it exists
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
	
	/**
	 * Play the given audio effect if it exists
	 * 
	 * @param s
	 *            the key
	 * @param p
	 *            the player
	 */
	public void play(String s, Player p)
	{
		if(contains(s))
		{
			get(s).play(p);
		}
	}
	
	/**
	 * Play the audio effect if it exists
	 * 
	 * @param s
	 *            the key
	 * @param p
	 *            the player
	 * @param l
	 *            the relative location
	 */
	public void play(String s, Player p, Location l)
	{
		if(contains(s))
		{
			get(s).play(p);
		}
	}
}
