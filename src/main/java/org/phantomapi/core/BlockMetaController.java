package org.phantomapi.core;

import org.bukkit.event.EventHandler;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.phantomapi.async.A;
import org.phantomapi.blockmeta.ChunkMeta;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.lang.GChunk;
import org.phantomapi.lang.GMap;
import org.phantomapi.sync.S;

public class BlockMetaController extends Controller
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
						// Call load event
					}
				};
			}
		};
	}
	
	@EventHandler
	public void on(ChunkUnloadEvent e)
	{
		GChunk chunk = new GChunk(e.getChunk());
		ChunkMeta cm = metaCache.get(chunk);
		cm.getConfiguration().flushLinks();
		
		// Call unload event
		
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
}
