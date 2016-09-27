package org.phantomapi.nest;

import java.io.IOException;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.clust.DataFile;
import org.phantomapi.lang.GMap;
import org.phantomapi.lang.GSet;

public class PhantomChunkNest implements NestedChunk
{
	private DataFile df;
	private GMap<Block, NestedBlock> nested;
	private Chunk chunk;
	
	public PhantomChunkNest(Chunk chunk)
	{
		this.df = new DataFile();
		this.nested = new GMap<Block, NestedBlock>();
		this.chunk = chunk;
	}
	
	public void load() throws IOException
	{
		nested = new GMap<Block, NestedBlock>();
		
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
			nested.put(block, nb);
		}
	}
	
	public void save() throws IOException
	{
		for(Block i : nested.k())
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
		
		for(Block i : nested.k())
		{
			df.add(nested.get(i).getData(), i.getX() + "_" + i.getY() + "_" + i.getZ() + ".");
		}
		
		df.save(NestUtil.getFile(chunk));
	}
	
	public NestedBlock getBlock(Block block)
	{
		if(!block.getChunk().equals(chunk))
		{
			return null;
		}
		
		if(!nested.containsKey(block))
		{
			nested.put(block, new PhantomBlockNest(block, new DataCluster()));
		}
		
		return nested.get(block);
	}

	public Chunk getChunk()
	{
		return chunk;
	}
}
