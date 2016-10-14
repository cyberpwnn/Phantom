package org.phantomapi.nest;

import java.io.File;
import org.phantomapi.lang.GChunk;
import org.phantomapi.util.Worlds;

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
	 *            the gchunk
	 * @return the file representation
	 */
	public static File getChunkFile(GChunk chunk)
	{
		return new File(new File(Worlds.getWorld(chunk.getWorld()).getWorldFolder(), "nest"), "n." + chunk.getX() + "." + chunk.getZ() + ".n");
	}
}
