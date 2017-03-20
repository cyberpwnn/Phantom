package org.phantomapi.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import org.apache.commons.io.IOUtils;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.phantomapi.Phantom;
import org.phantomapi.async.A;
import org.phantomapi.blockmeta.ChunkMeta;
import org.phantomapi.clust.Configurable;
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
import org.phantomapi.util.C;
import org.phantomapi.util.Chunks;
import org.phantomapi.util.F;
import org.phantomapi.util.Probe;

/**
 * Loads nest chunks
 * 
 * @author cyberpwn
 */
@SyncStart
@Ticked(100)
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
					
					try
					{
						flush();
					}
					
					catch(Exception e)
					{
						cancel();
					}
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
		try
		{
			for(NestedChunk i : chunks.v())
			{
				save(i);
			}
		}
		
		catch(Exception e)
		{
			
		}
	}
	
	public void loadAll()
	{
		try
		{
			for(Chunk i : chunks.k())
			{
				load(i);
			}
		}
		
		catch(Exception e)
		{
			
		}
	}
	
	public void load(Chunk i)
	{
		if(loading.contains(i))
		{
			return;
		}
		
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
			saveMeta(c);
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
		GList<Chunk> c = Chunks.getLoadedChunks();
		GList<Chunk> m = chunks.k();
		
		new A()
		{
			@Override
			public void async()
			{
				for(Chunk i : m)
				{
					if(!c.contains(i))
					{
						chunks.remove(i);
					}
				}
			}
		};
	}
	
	public NestedChunk get(Chunk c)
	{
		return chunks.get(c);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void on(ChunkLoadEvent e)
	{
		
		try
		{
			load(e.getChunk());
		}
		
		catch(Exception ee)
		{
			return;
		}
		
		callEvent(new NestChunkLoadEvent(chunks.get(e.getChunk())));
		
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
	
	public void saveMeta(NestedChunk nc)
	{
		ChunkMeta cm = new ChunkMeta(nc.getChunk());
		cm.getConfiguration().clear();
		cm.getConfiguration().add(nc);
		
		for(GLocation i : nc.getBlocks().k())
		{
			cm.getBlock(i.toLocation().getBlock()).getConfiguration().add(nc.getBlocks().get(i));
		}
		
		new A()
		{
			@Override
			public void async()
			{
				try
				{
					write(cm.getFile(), cm);
				}
				
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		};
	}
	
	public void read(File file, Configurable cc) throws IOException
	{
		if(!file.exists())
		{
			file.getParentFile().mkdirs();
		}
		
		else
		{
			byte[] data = Files.readAllBytes(file.toPath());
			cc.getConfiguration().clear();
			cc.getConfiguration().addCompressed(data);
		}
	}
	
	public void write(File file, Configurable cc) throws IOException
	{
		if(cc.getConfiguration().keys().isEmpty())
		{
			return;
		}
		
		if(!file.exists())
		{
			file.getParentFile().mkdirs();
			file.createNewFile();
		}
		
		FileOutputStream fos = new FileOutputStream(file, false);
		IOUtils.write(cc.getConfiguration().compress(), fos);
		fos.close();
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
		try
		{
			if(chunks.containsKey(e.getChunk()))
			{
				save(chunks.get(e.getChunk()));
				callEvent(new NestChunkUnloadEvent(chunks.get(e.getChunk())));
				chunks.remove(e.getChunk());
			}
		}
		
		catch(Exception ee)
		{
			
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
