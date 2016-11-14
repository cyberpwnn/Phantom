package org.phantomapi.blockmeta;

import java.io.File;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.phantomapi.clust.ColdLoad;
import org.phantomapi.clust.LinkedDataCluster;
import org.phantomapi.clust.Tabled;
import org.phantomapi.lang.GChunk;
import org.phantomapi.util.Worlds;

@Tabled("meta")
@ColdLoad
public class ChunkMeta extends MetaObject
{
	private GChunk chunk;
	
	public ChunkMeta(GChunk chunk)
	{
		super(chunk.getX() + "." + chunk.getZ());
		
		this.chunk = chunk;
	}
	
	public int getMcaX()
	{
		return chunk.getX() >> 5;
	}
	
	public int getMcaZ()
	{
		return chunk.getZ() >> 5;
	}
	
	public File getFile()
	{
		return new File(new File(Worlds.getWorld(chunk.getWorld()).getWorldFolder(), "pmeta"), "mca." + getMcaX() + "." + getMcaZ() + ".p");
	}
	
	public ChunkMeta(Chunk chunk)
	{
		this(new GChunk(chunk));
	}
	
	public BlockMeta getBlock(Block block)
	{
		getConfiguration().flushLinks();
		
		String m = block.getX() + "_" + block.getY() + "_" + block.getZ();
		
		if(getConfiguration().contains(m + ".i"))
		{
			return new BlockMeta(getConfiguration().linkSplit(m));
		}
		
		getConfiguration().set(m + ".i", "i");
		
		return new BlockMeta(new LinkedDataCluster(getConfiguration(), m));
	}
}
