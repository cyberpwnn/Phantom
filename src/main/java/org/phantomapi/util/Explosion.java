package org.phantomapi.util;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.phantomapi.Phantom;
import org.phantomapi.world.W;

public class Explosion implements Listener
{
	private float power;
	private boolean pushBlocks;
	
	public Explosion()
	{
		power = 3f;
		pushBlocks = false;
	}
	
	public Explosion power(float power)
	{
		this.power = power;
		return this;
	}
	
	public Explosion pushBlocks()
	{
		pushBlocks = true;
		return this;
	}
	
	public void boom(Location at)
	{
		at.getWorld().createExplosion(at, power);
		
		if(pushBlocks)
		{
			Phantom.instance().registerListener(this);
		}
	}
	
	@EventHandler
	public void on(EntityExplodeEvent e)
	{
		W.explodeBlocks(e);
		Phantom.instance().unRegisterListener(this);
	}
}
