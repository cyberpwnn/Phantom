package org.phantomapi.core;

import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.phantomapi.async.AsyncUtil;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.construct.Ticked;
import org.phantomapi.event.PlayerMoveChunkEvent;
import org.phantomapi.lang.GMap;
import org.phantomapi.nest.NestedBlock;
import org.phantomapi.nest.NestedChunk;
import org.phantomapi.nest.PhantomChunkNest;
import org.phantomapi.statistics.Monitorable;
import org.phantomapi.util.C;
import org.phantomapi.util.ExceptionUtil;
import org.phantomapi.util.F;
import org.phantomapi.world.W;

@Ticked(20)
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
	
	public void onTick()
	{
		int chunks = 0;
		
		for(World i : Bukkit.getWorlds())
		{
			chunks += i.getLoadedChunks().length;
		}
		
		if(chunks != nests.size())
		{
			for(Chunk i : nests.k())
			{
				if(!i.isLoaded())
				{
					try
					{
						nests.get(i).save();
						nests.remove(i);
					}
					
					catch(IOException e1)
					{
						ExceptionUtil.print(e1);
					}
				}
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
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void on(ChunkLoadEvent e)
	{
		nests.put(e.getChunk(), new PhantomChunkNest(e.getChunk()));
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void on(PlayerMoveChunkEvent e)
	{
		if(!nests.containsKey(e.getToChunk()))
		{
			nests.put(e.getToChunk(), new PhantomChunkNest(e.getToChunk()));
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
