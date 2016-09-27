package org.phantomapi.nest;

import java.io.IOException;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.clust.DataFile;
import org.phantomapi.lang.GMap;
import org.phantomapi.lang.GSet;
import org.phantomapi.util.ExceptionUtil;
import org.phantomapi.world.W;

/**
 * Implementation of a NestedChunk
 * 
 * @author cyberpwn
 */
public class PhantomChunkNest implements NestedChunk
{
	private DataFile df;
	private GMap<Location, NestedBlock> nested;
	private Chunk chunk;
	
	/**
	 * Create a phantom Chunk Nest
	 * 
	 * @param chunk
	 *            the chunk
	 */
	public PhantomChunkNest(Chunk chunk)
	{
		this.df = new DataFile();
		this.nested = new GMap<Location, NestedBlock>();
		this.chunk = chunk;
		
		try
		{
			this.load();
		}
		
		catch(IOException e)
		{
			ExceptionUtil.print(e);
		}
	}
	
	/**
	 * Load this chunk data
	 * 
	 * @throws IOException
	 *             shit happens
	 */
	public void load() throws IOException
	{
		nested = new GMap<Location, NestedBlock>();
				
		if(!NestUtil.getFile(chunk).exists())
		{
			return;
		}
		
		df.load(NestUtil.getFile(chunk));
		
		GSet<String> blocks = new GSet<String>();
		
		for(String i : df.keys())
		{
			if(i.contains("."))
			{
				blocks.add(i.split("\\.")[0]);
			}
		}
		
		for(String i : blocks)
		{
			int x = Integer.valueOf(i.split("_")[0]);
			int y = Integer.valueOf(i.split("_")[1]);
			int z = Integer.valueOf(i.split("_")[2]);
			Block block = chunk.getBlock(x, y, z);
			NestedBlock nb = new PhantomBlockNest(block, df.crop(i));
			nested.put(block.getLocation(), nb);
		}
	}
	
	/**
	 * Saves this set of data into a nest file. Any blocks with no nest data
	 * will not be saved. If there is no nest data at all, any nest file will be
	 * remved, as it is saving changes
	 * 
	 * @throws IOException
	 *             shit happens
	 */
	public void save() throws IOException
	{
		for(Location i : nested.k())
		{
			if(nested.get(i).getData().getData().isEmpty())
			{
				nested.remove(i);
			}
		}
		
		if(nested.isEmpty())
		{
			NestUtil.getFile(chunk).delete();
			return;
		}
		
		for(Location i : nested.k())
		{
			df.add(nested.get(i).getData(), W.getChunkX(i.getBlock()) + "_" + i.getBlockY() + "_" + W.getChunkZ(i.getBlock()) + ".");
		}
		
		df.save(NestUtil.getFile(chunk));
	}
	
	public NestedBlock getBlock(Block block)
	{
		if(!nested.containsKey(block.getLocation()))
		{
			nested.put(block.getLocation(), new PhantomBlockNest(block, new DataCluster()));
		}
		
		return nested.get(block.getLocation());
	}
	
	public Chunk getChunk()
	{
		return chunk;
	}
}
