package org.phantomapi.physics;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class VectorTrace
{
	private Location hit;
	
	public VectorTrace(Location l, Vector v)
	{
		Location c = l.clone();
		
		while(!canHit(c))
		{
			Vector grav = new Vector(0, -1, 0);
			v.add(grav);
			
			c.add(v);
		}
	}
	
	public boolean canHit(Location l)
	{
		return l.getBlock().getType().isSolid();
	}
	
	public Location getHit()
	{
		return hit;
	}
}
