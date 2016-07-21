package org.cyberpwn.phantom.vfx;

import org.bukkit.Location;
import org.cyberpwn.phantom.lang.GList;

/**
 * An effect that can be played
 * @author cyberpwn
 *
 */
public class PhantomEffect implements VisualEffect
{
	@Override
	public GList<VisualEffect> getEffects()
	{
		return new GList<VisualEffect>();
	}

	@Override
	public void play(Location l)
	{
		
	}

	@Override
	public void addEffect(VisualEffect e)
	{
		
	}
}
