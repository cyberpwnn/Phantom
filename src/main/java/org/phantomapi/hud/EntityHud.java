package org.phantomapi.hud;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.phantomapi.physics.VectorMath;

public abstract class EntityHud extends BaseHud
{
	private Entity track;
	private double maxDist;
	
	public EntityHud(Player player, Entity track, double maxDist)
	{
		super(player);
		
		this.track = track;
		this.maxDist = maxDist;
	}
	
	public EntityHud(Player player, Entity track)
	{
		this(player, track, 5.6);
	}
	
	@Override
	public Location getBaseLocation()
	{
		Location host = track.getLocation();
		Vector left = VectorMath.angleLeft(player.getLocation().getDirection(), 90).clone().multiply(index);
		
		return host.clone().add(left);
	}
	
	@Override
	public void onUpdateInternal()
	{
		holo.setLocation(getBaseLocation());
		
		if(player.getLocation().distanceSquared(track.getLocation()) > maxDist * maxDist)
		{
			close();
		}
	}
}
