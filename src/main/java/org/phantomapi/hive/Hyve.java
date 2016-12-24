package org.phantomapi.hive;

import java.io.File;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.phantomapi.clust.Configurable;

/**
 * Represents a hyve
 * 
 * @author cyberpwn
 */
public interface Hyve extends Configurable
{
	/**
	 * Get the type of this hyve
	 * 
	 * @return the hyve type
	 */
	public HyveType getType();
	
	/**
	 * Save changes to its destination
	 */
	public void save();
	
	/**
	 * Load any data and create defaults
	 */
	public void load();
	
	/**
	 * Delete all references of this hyve
	 */
	public void drop();
	
	/**
	 * Check if there is hyve data on the given object
	 * 
	 * @param block
	 *            the block
	 * @return true if data exists
	 */
	public static boolean exists(Block block)
	{
		File file = new File(block.getWorld().getWorldFolder(), "block");
		return new File(file, "hyve-block." + block.getX() + "." + block.getY() + "." + block.getZ() + ".hy").exists();
	}
	
	/**
	 * Check if there is hyve data on the given object
	 * 
	 * @param chunk
	 *            the chunk
	 * @return true if data exists
	 */
	public static boolean exists(Chunk chunk)
	{
		File file = new File(chunk.getWorld().getWorldFolder(), "chunk");
		return new File(file, "hyve-chunk." + chunk.getX() + "." + chunk.getZ() + ".hy").exists();
	}
}
