package org.phantomapi.core;

import java.io.File;
import java.io.IOException;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.phantomapi.async.A;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.construct.Ticked;
import org.phantomapi.filesystem.Serializer;
import org.phantomapi.lang.GChunk;
import org.phantomapi.lang.GList;
import org.phantomapi.lang.GLocation;
import org.phantomapi.lang.GMap;
import org.phantomapi.lang.GSet;
import org.phantomapi.nest.NestUtil;
import org.phantomapi.nest.NestedBlock;
import org.phantomapi.nest.NestedChunk;
import org.phantomapi.statistics.Monitorable;
import org.phantomapi.sync.S;
import org.phantomapi.util.C;
import org.phantomapi.util.Chunks;
import org.phantomapi.util.ExceptionUtil;
import org.phantomapi.util.F;

/**
 * Loads nest chunks
 * 
 * @author cyberpwn
 */
@Ticked(0)
public class NestController extends Controller implements Monitorable
{
	private GMap<Chunk, NestedChunk> chunks;
	private GSet<Chunk> loading;
	private GSet<Chunk> queue;
	
	public NestController(Controllable parentController)
	{
		super(parentController);
		
		this.chunks = new GMap<Chunk, NestedChunk>();
		this.loading = new GSet<Chunk>();
		this.queue = new GSet<Chunk>();
	}
	
	@Override
	public void onStart()
	{
		for(Chunk i : Chunks.getLoadedChunks())
		{
			queue.add(i);
		}
	}
	
	@Override
	public void onStop()
	{
		for(Chunk i : Chunks.getLoadedChunks())
		{
			File file = NestUtil.getChunkFile(new GChunk(i));
			
			NestedChunk c = chunks.get(i);
			
			try
			{
				if(!file.exists())
				{
					file.getParentFile().mkdirs();
					file.createNewFile();
				}
				
				Serializer.serializeToFile(c, file);
			}
			
			catch(IOException ex)
			{
				ExceptionUtil.print(ex);
			}
		}
	}
	
	@Override
	public void onTick()
	{
		if(!queue.isEmpty())
		{
			for(Chunk i : new GList<Chunk>(queue.iterator()))
			{
				File file = NestUtil.getChunkFile(new GChunk(i));
				loading.add(i);
				
				if(file.exists())
				{
					new A()
					{
						@Override
						public void async()
						{
							try
							{
								NestedChunk nc = (NestedChunk) Serializer.deserializeFromFile(file);
								
								new S()
								{
									@Override
									public void sync()
									{
										loading.remove(i);
										chunks.put(i, nc);
									}
								};
							}
							
							catch(Exception e)
							{
								ExceptionUtil.print(e);
							}
						}
					};
				}
				
				else
				{
					loading.remove(i);
					chunks.put(i, new NestedChunk(new GChunk(i)));
				}
			}
			
			queue.clear();
		}
	}
	
	public NestedChunk get(Chunk c)
	{
		return chunks.get(c);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void on(ChunkLoadEvent e)
	{
		queue.add(e.getChunk());
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void on(ChunkUnloadEvent e)
	{
		File file = NestUtil.getChunkFile(new GChunk(e.getChunk()));
		
		Chunk chunk = e.getChunk();
		NestedChunk c = chunks.get(e.getChunk());
		
		for(GLocation i : c.getBlocks().k())
		{
			if(c.getBlocks().get(i).size() == 0)
			{
				c.getBlocks().remove(i);
			}
		}
		
		if(c.size() == 0 && c.getBlocks().isEmpty())
		{
			chunks.remove(chunk);
			file.delete();
			return;
		}
		
		new A()
		{
			@Override
			public void async()
			{
				try
				{
					if(!file.exists())
					{
						file.getParentFile().mkdirs();
						file.createNewFile();
					}
					
					Serializer.serializeToFile(c, file);
					
					new S()
					{
						@Override
						public void sync()
						{
							chunks.remove(chunk);
						}
					};
				}
				
				catch(IOException ex)
				{
					ExceptionUtil.print(ex);
				}
			}
		};
	}
	
	@Override
	public String getMonitorableData()
	{
		long size = 0;
		
		for(Chunk i : chunks.k())
		{
			size += chunks.get(i).byteSize();
			
			for(NestedBlock j : chunks.get(i).getBlocks().v())
			{
				size += j.byteSize();
			}
		}
		
		return "Chunks: " + C.LIGHT_PURPLE + F.f(chunks.size()) + C.DARK_GRAY + " Ramdisk: " + C.LIGHT_PURPLE + F.fileSize(size);
	}
}
