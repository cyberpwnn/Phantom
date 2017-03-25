package org.phantomapi.hud;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.phantomapi.physics.VectorMath;

public abstract class RootedHud extends BaseHud
{
	private Location track;
	private double maxDist;
	
	public RootedHud(Player player, Location track, double maxDist)
	{
		super(player);
		
		this.track = track;
		this.maxDist = maxDist;
	}
	
	public RootedHud(Player player, Location track)
	{
		this(player, track, 5.6);
	}
	
	@Override
	public Location getBaseLocation()
	{
		Location host = track.clone();
		Vector left = VectorMath.angleLeft(player.getLocation().getDirection(), 90).clone().multiply(index);
		
		return host.clone().add(left);
	}
	
	@Override
	public void onUpdateInternal()
	{
		holo.setLocation(getBaseLocation());
		
		if(player.getLocation().distanceSquared(track) > maxDist * maxDist)
		{
			close();
		}
	}
}
