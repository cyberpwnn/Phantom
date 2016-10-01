package org.phantomapi.nest;

import java.io.File;
import org.bukkit.Chunk;

/**
 * Nest Utilities
 * 
 * @author cyberpwn
 */
public class NestUtil
{
	/**
	 * Get the file name for a chunk nest
	 * 
	 * @param chunk
	 *            the chunk
	 * @return the file representation
	 */
	public static File getChunkFile(Chunk chunk)
	{
		return new File(new File(chunk.getWorld().getWorldFolder(), "nest"), "n." + chunk.getX() + "." + chunk.getZ() + ".nst");
	}
}
