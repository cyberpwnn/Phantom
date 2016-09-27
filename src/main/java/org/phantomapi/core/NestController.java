package org.phantomapi.core;

import java.io.IOException;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.phantomapi.async.AsyncUtil;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.lang.GMap;
import org.phantomapi.nest.NestedBlock;
import org.phantomapi.nest.NestedChunk;
import org.phantomapi.nest.PhantomChunkNest;
import org.phantomapi.statistics.Monitorable;
import org.phantomapi.util.C;
import org.phantomapi.util.ExceptionUtil;
import org.phantomapi.util.F;
import org.phantomapi.world.W;

public class NestController extends Controller implements Monitorable
{
	private GMap<Chunk, PhantomChunkNest> nests;
	
	public NestController(Controllable parentController)
	{
		super(parentController);
		
		nests = new GMap<Chunk, PhantomChunkNest>();
	}
	
	@Override
	public void onStart()
	{
		
	}
	
	@Override
	public void onStop()
	{
		for(Chunk i : nests.k())
		{
			try
			{
				nests.get(i).save();
			}
			
			catch(IOException e)
			{
				ExceptionUtil.print(e);
			}
		}
	}
	
	public NestedChunk get(Chunk c)
	{
		AsyncUtil.enforceSync();
		c = W.toSync(c);
		
		if(!c.isLoaded())
		{
			return null;
		}
		
		if(!nests.containsKey(c))
		{
			nests.put(c, new PhantomChunkNest(c));
		}
		
		return nests.get(c);
	}
	
	public NestedBlock get(Block block)
	{
		AsyncUtil.enforceSync();
		block = W.toSync(block);
		
		if(!block.getChunk().isLoaded())
		{
			return null;
		}
		
		return get(block.getChunk()).getBlock(block);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void on(ChunkUnloadEvent e)
	{
		if(nests.containsKey(e.getChunk()))
		{
			try
			{
				nests.get(e.getChunk()).save();
				nests.remove(e.getChunk());
			}
			
			catch(IOException e1)
			{
				ExceptionUtil.print(e1);
			}
		}
	}

	@Override
	public String getMonitorableData()
	{
		long size = 0;
		long blocks = 0;
		
		for(Chunk i : nests.k())
		{
			for(NestedBlock j : nests.get(i).getBlocks())
			{
				blocks++;
				size += j.getData().byteSize();
			}
		}
		
		return "Chunks: " + C.LIGHT_PURPLE + F.f(nests.size()) + " " + F.fileSize(size) + C.DARK_GRAY + " Blocks: " + blocks;
	}
}
