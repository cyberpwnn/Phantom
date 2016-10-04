package org.phantomapi.core;

import java.io.File;
import java.io.IOException;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.phantomapi.Phantom;
import org.phantomapi.async.A;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.construct.Ticked;
import org.phantomapi.event.NestChunkLoadEvent;
import org.phantomapi.event.NestChunkUnloadEvent;
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
import org.phantomapi.sync.TaskLater;
import org.phantomapi.util.C;
import org.phantomapi.util.Chunks;
import org.phantomapi.util.ExceptionUtil;
import org.phantomapi.util.F;
import org.phantomapi.util.Probe;

/**
 * Loads nest chunks
 * 
 * @author cyberpwn
 */
@SyncStart
@Ticked(0)
public class NestController extends Controller implements Monitorable, Probe
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
			File file = NestUtil.getChunkFile(new GChunk(i));
			loading.add(i);
			
			if(file.exists())
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
							callEvent(new NestChunkLoadEvent(chunks.get(i)));
						}
					};
				}
				
				catch(Exception e)
				{
					ExceptionUtil.print(e);
					file.delete();
				}
			}
			
			else
			{
				loading.remove(i);
				chunks.put(i, new NestedChunk(new GChunk(i)));
				callEvent(new NestChunkLoadEvent(chunks.get(i)));
			}
		}
		
		Phantom.instance().getProbeController().registerProbe(this);
	}
	
	@Override
	public void onStop()
	{
		for(Chunk i : Chunks.getLoadedChunks())
		{
			File file = NestUtil.getChunkFile(new GChunk(i));
			
			NestedChunk c = chunks.get(i);
			
			if(c.size() == 0)
			{
				file.delete();
				continue;
			}
			
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
		
		new TaskLater()
		{
			@Override
			public void run()
			{
				Phantom.instance().getProbeController().unRegisterProbe(NestController.this);
			}
		};
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
										callEvent(new NestChunkLoadEvent(chunks.get(i)));
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
					callEvent(new NestChunkLoadEvent(chunks.get(i)));
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
		
		if(c == null)
		{
			f("Chunk " + chunk.getX() + "," + chunk.getZ() + " @" + chunk.getWorld().getName() + " unloaded invalid.");
			reload();
			return;
		}
		
		for(GLocation i : c.getBlocks().k())
		{
			if(c.getBlocks().get(i).size() == 0)
			{
				c.getBlocks().remove(i);
			}
		}
		
		if(c.size() == 0 && c.getBlocks().isEmpty())
		{
			callEvent(new NestChunkUnloadEvent(chunks.get(chunk)));
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
							callEvent(new NestChunkUnloadEvent(chunks.get(chunk)));
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
	
	@Override
	public DataCluster onProbe(Block block, DataCluster probeSet)
	{
		try
		{
			probeSet.add(chunks.get(block.getChunk()).getBlock(block).copy());
		}
		
		catch(Exception e)
		{
			
		}
		
		return probeSet;
	}
}
