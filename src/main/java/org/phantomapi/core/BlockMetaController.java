package org.phantomapi.core;

import org.bukkit.event.EventHandler;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.phantomapi.async.A;
import org.phantomapi.blockmeta.ChunkMeta;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.event.MetaChunkLoadEvent;
import org.phantomapi.event.MetaChunkUnloadEvent;
import org.phantomapi.lang.GChunk;
import org.phantomapi.lang.GMap;
import org.phantomapi.statistics.Monitorable;
import org.phantomapi.sync.S;
import org.phantomapi.util.C;
import org.phantomapi.util.F;

public class BlockMetaController extends Controller implements Monitorable
{
	private GMap<GChunk, ChunkMeta> metaCache;
	
	public BlockMetaController(Controllable parentController)
	{
		super(parentController);
		
		metaCache = new GMap<GChunk, ChunkMeta>();
	}
	
	@Override
	public void onStart()
	{
		
	}
	
	@Override
	public void onStop()
	{
		
	}
	
	@EventHandler
	public void on(ChunkLoadEvent e)
	{
		GChunk chunk = new GChunk(e.getChunk());
		
		new A()
		{
			@Override
			public void async()
			{
				ChunkMeta cm = new ChunkMeta(chunk);
				loadSqLite(cm, cm.getFile());
				
				new S()
				{
					@Override
					public void sync()
					{
						metaCache.put(chunk, cm);
						callEvent(new MetaChunkLoadEvent(chunk.toChunk(), cm));
					}
				};
			}
		};
	}
	
	@EventHandler
	public void on(ChunkUnloadEvent e)
	{
		GChunk chunk = new GChunk(e.getChunk());
		
		if(!metaCache.containsKey(chunk))
		{
			return;
		}
		
		ChunkMeta cm = metaCache.get(chunk);
		cm.getConfiguration().flushLinks();
		callEvent(new MetaChunkUnloadEvent(e.getChunk(), cm));
		cm.getConfiguration().flushLinks();
		
		metaCache.remove(chunk);
		
		new A()
		{
			@Override
			public void async()
			{
				saveSqLite(cm, cm.getFile());
			}
		};
	}
	
	@Override
	public String getMonitorableData()
	{
		return "Chunks: " + C.LIGHT_PURPLE + F.f(metaCache.size());
	}
}
