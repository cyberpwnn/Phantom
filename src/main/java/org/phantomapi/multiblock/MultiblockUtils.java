package org.phantomapi.multiblock;

import java.io.File;
import java.io.IOException;
import org.bukkit.World;
import org.phantomapi.filesystem.Serializer;

/**
 * Multiblock utilities
 * 
 * @author cyberpwn
 */
public class MultiblockUtils
{
	/**
	 * Get the next id
	 * 
	 * @param world
	 *            the world
	 * @return the next id
	 */
	public static int getNextID(World world)
	{
		File f = new File(world.getWorldFolder(), "multiblock");
		int lim = 1;
		
		if(f.exists() && f.isDirectory())
		{
			for(File i : f.listFiles())
			{
				try
				{
					Integer v = Integer.valueOf(i.getName().split("\\.")[0]);
					
					if(v > lim)
					{
						lim = v;
					}
				}
				
				catch(Exception e)
				{
					
				}
			}
		}
		
		return lim + 1;
	}
	
	/**
	 * Get the file for the given world and mbid
	 * 
	 * @param world
	 *            the world
	 * @param id
	 *            the mbid
	 * @return the file
	 */
	public static File getFile(World world, int id)
	{
		return new File(new File(world.getWorldFolder(), "multiblock"), id + ".mub");
	}
	
	/**
	 * Save the multiblock instance
	 * 
	 * @param multiblock
	 *            the multiblock
	 * @throws IOException
	 *             shit happens
	 */
	public static void save(Multiblock multiblock) throws IOException
	{
		File file = getFile(multiblock.getWorld(), multiblock.getId());
		
		if(!file.exists())
		{
			file.getParentFile().mkdirs();
			file.createNewFile();
		}
		
		Serializer.serializeToFile(multiblock, file);
	}
	
	/**
	 * Load the multiblock
	 * 
	 * @param world
	 *            the world
	 * @param id
	 *            the id
	 * @return the multiblock
	 * @throws IOException
	 *             shit happens
	 * @throws ClassNotFoundException
	 *             shit happens
	 */
	public static Multiblock load(World world, int id) throws IOException, ClassNotFoundException
	{
		File file = getFile(world, id);
		
		return (Multiblock) Serializer.deserializeFromFile(file);
	}
}
