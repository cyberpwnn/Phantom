package org.phantomapi.block;

import org.bukkit.Location;
import org.bukkit.World;

public class PhantomLocation extends Location
{
	public PhantomLocation(World world, double x, double y, double z, float yaw, float pitch)
	{
		super(world, x, y, z, yaw, pitch);
	}

	public PhantomLocation(World world, double x, double y, double z)
	{
		super(world, x, y, z);
	}
}
