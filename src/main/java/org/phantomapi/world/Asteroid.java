package org.phantomapi.world;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.phantomapi.nms.NMSX;
import org.phantomapi.sync.Task;
import org.phantomapi.vfx.ParticleEffect;

public class Asteroid
{
	private Location entry;
	private Vector direction;
	
	public Asteroid(Location entry, Vector direction)
	{
		this.entry = entry;
		this.direction = direction;
	}
	
	public void fire()
	{
		Entity e = NMSX.createFallingBlock(entry, new MaterialBlock(Material.OBSIDIAN));
		Location[] last = new Location[] {e.getLocation()};
		
		e.setVelocity(direction.clone());
		
		new Task(0)
		{
			@Override
			public void run()
			{
				if(e.isDead())
				{
					W.createNovaExplosion(last[0], 4, 2, 0.1, 1, 0.74);
					cancel();
				}
				
				else
				{
					last[0] = e.getLocation();
					ParticleEffect.CLOUD.display(0.1f, 1, last[0], 128);
				}
			}
		};
	}
}
