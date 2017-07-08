package org.phantomapi.world;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.phantomapi.async.A;
import com.boydti.fawe.FaweAPI;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.regions.CuboidRegion;

/**
 * Lighting
 * 
 * @author cyberpwn
 */
public class L
{
	/**
	 * Relight the given chunk async
	 * 
	 * @param c
	 *            the chunk to relight
	 */
	public static void relight(Chunk c)
	{
		new A()
		{
			@SuppressWarnings("deprecation")
			@Override
			public void async()
			{
				FaweAPI.fixLighting(c.getWorld().getName(), new CuboidRegion(new Vector(c.getBlock(0, 0, 0).getX(), c.getBlock(0, 0, 0).getY(), c.getBlock(0, 0, 0).getZ()), new Vector(c.getBlock(15, 255, 15).getX(), c.getBlock(15, 255, 15).getY(), c.getBlock(15, 255, 15).getZ())));
			}
		};
	}
	
	/**
	 * Relight all loaded chunks in the given world async
	 * 
	 * @param world
	 *            the world
	 */
	public static void relight(World world)
	{
		World w = W.getAsyncWorld(world.getName());
		
		new A()
		{
			@Override
			public void async()
			{
				for(Chunk i : w.getLoadedChunks())
				{
					relight(i);
				}
			}
		};
	}
}
