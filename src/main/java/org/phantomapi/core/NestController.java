package org.phantomapi.core;

import java.io.File;
import java.io.IOException;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldSaveEvent;
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
import org.phantomapi.nest.NestScrub;
import org.phantomapi.nest.NestUtil;
import org.phantomapi.nest.NestedBlock;
import org.phantomapi.nest.NestedChunk;
import org.phantomapi.statistics.Monitorable;
import org.phantomapi.sync.Task;
import org.phantomapi.sync.TaskLater;
import org.phantomapi.util.C;
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
	private GList<NestScrub> scrubs;
	private boolean flush;
	
	public NestController(Controllable parentController)
	{
		super(parentController);
		
		chunks = new GMap<Chunk, NestedChunk>();
		loading = new GSet<Chunk>();
		scrubs = new GList<NestScrub>();
		flush = false;
	}
	
	@Override
	public void onStart()
	{
		loadAll();
		
		Phantom.instance().getProbeController().registerProbe(this);
		
		new Task(100)
		{
			@Override
			public void run()
			{
				if(flush)
				{
					flush = false;
					flush();
				}
			}
		};
	}
	
	@Override
	public void onStop()
	{
		saveAll();
		
		Phantom.instance().getProbeController().unRegisterProbe(NestController.this);
	}
	
	public void saveAll()
	{
		for(NestedChunk i : chunks.v())
		{
			save(i);
		}
	}
	
	public void loadAll()
	{
		for(Chunk i : chunks.k())
		{
			load(i);
		}
	}
	
	public void load(Chunk i)
	{
		File file = NestUtil.getChunkFile(new GChunk(i));
		loading.add(i);
		
		if(file.exists())
		{
			try
			{
				NestedChunk nc = (NestedChunk) Serializer.deserializeFromFile(file);
				loading.remove(i);
				chunks.put(i, nc);
			}
			
			catch(Exception e)
			{
				f("Failed to load nest chunk @ " + i.getX() + " / " + i.getZ() + " / " + i.getWorld().getName());
				file.delete();
				loading.remove(i);
				chunks.put(i, new NestedChunk(new GChunk(i)));
			}
		}
		
		else
		{
			loading.remove(i);
			chunks.put(i, new NestedChunk(new GChunk(i)));
		}
		
		scrub(chunks.get(i));
	}
	
	public void save(NestedChunk c)
	{
		if(c == null)
		{
			return;
		}
		
		Chunk i = c.getChunk().toChunk();
		File file = NestUtil.getChunkFile(new GChunk(i));
		
		if(c.size() == 0)
		{
			file.delete();
			return;
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
			f("Failed to save nest chunk @ " + i.getX() + " / " + i.getZ() + " / " + i.getWorld().getName());
		}
	}
	
	public void flush()
	{
		for(NestedChunk i : chunks.v())
		{
			Chunk chunk = i.getChunk().toChunk();
			File file = NestUtil.getChunkFile(i.getChunk());
			
			for(GLocation j : i.getBlocks().k())
			{
				if(i.getBlocks().get(j).size() == 0)
				{
					i.getBlocks().remove(j);
				}
			}
			
			if(i.size() == 0 && i.getBlocks().isEmpty())
			{
				chunks.remove(chunk);
				file.delete();
				continue;
			}
		}
		
		new A()
		{
			@Override
			public void async()
			{
				for(NestedChunk i : chunks.v())
				{
					try
					{
						File file = NestUtil.getChunkFile(i.getChunk());
						
						if(!file.exists())
						{
							file.getParentFile().mkdirs();
							file.createNewFile();
						}
						
						Serializer.serializeToFile(i, file);
					}
					
					catch(IOException ex)
					{
						f("Failed to save nest chunk @ " + i.getChunk().getX() + " / " + i.getChunk().getZ() + " / " + i.getChunk().getWorld());
					}
				}
			}
		};
	}
	
	@Override
	public void onTick()
	{
		
	}
	
	public NestedChunk get(Chunk c)
	{
		return chunks.get(c);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void on(ChunkLoadEvent e)
	{
		new TaskLater()
		{
			@Override
			public void run()
			{
				load(e.getChunk());
				callEvent(new NestChunkLoadEvent(chunks.get(e.getChunk())));
			}
		};
	}
	
	public void scrub(NestedChunk c)
	{
		for(NestScrub j : scrubs)
		{
			try
			{
				j.onScan(c);
			}
			
			catch(Exception e)
			{
				f("Failed to scrub nest chunk @ " + c.getChunk().getX() + " / " + c.getChunk().getZ() + " / " + c.getChunk().getWorld());
			}
		}
		
		for(GLocation i : c.getBlocks().k())
		{
			for(NestScrub j : scrubs)
			{
				try
				{
					j.onScan(c.getBlocks().get(i));
				}
				
				catch(Exception e)
				{
					f("Failed to scrub nest chunk @ " + c.getChunk().getX() + " / " + c.getChunk().getZ() + " / " + c.getChunk().getWorld());
				}
			}
		}
	}
	
	@EventHandler
	public void on(WorldSaveEvent e)
	{
		requestFlush();
	}
	
	public void requestFlush()
	{
		flush = true;
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void on(ChunkUnloadEvent e)
	{
		if(chunks.containsKey(e.getChunk()))
		{
			save(chunks.get(e.getChunk()));
			callEvent(new NestChunkUnloadEvent(chunks.get(e.getChunk())));
		}
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
			probeSet.add(chunks.get(block.getChunk()));
		}
		
		catch(Exception e)
		{
			
		}
		
		return probeSet;
	}
	
	public void registerScrub(NestScrub i)
	{
		scrubs.add(i);
	}
	
	public void unregisterScrub(NestScrub i)
	{
		scrubs.remove(i);
	}
}
