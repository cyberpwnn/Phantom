package org.phantomapi.core;

import java.io.File;
import java.io.IOException;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.phantomapi.Phantom;
import org.phantomapi.async.A;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.clust.JSONDataInput;
import org.phantomapi.clust.JSONDataOutput;
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
import org.phantomapi.sync.S;
import org.phantomapi.sync.Task;
import org.phantomapi.sync.TaskLater;
import org.phantomapi.util.C;
import org.phantomapi.util.Chunks;
import org.phantomapi.util.F;
import org.phantomapi.util.Probe;
import org.phantomapi.world.W;

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
							
							new TaskLater(1)
							{
								@Override
								public void run()
								{
									if(chunks.get(i) == null)
									{
										return;
									}
									
									callEvent(new NestChunkLoadEvent(chunks.get(i)));
								}
							};
						}
					};
				}
				
				catch(Exception e)
				{
					f("Failed to load nest chunk @ " + i.getX() + " / " + i.getZ() + " / " + i.getWorld().getName());
					file.delete();
					loading.remove(i);
					chunks.put(i, new NestedChunk(new GChunk(i)));
					callEvent(new NestChunkLoadEvent(chunks.get(i)));
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
				f("Failed to save nest chunk @ " + i.getX() + " / " + i.getZ() + " / " + i.getWorld().getName());
			}
		}
		
		Phantom.instance().getProbeController().unRegisterProbe(NestController.this);
	}
	
	public NestedChunk load(GChunk c)
	{
		NestedChunk nc = new NestedChunk(c);
		DataCluster cc = new DataCluster();
		
		try
		{
			new JSONDataInput().load(cc, NestUtil.getChunkAuxFile(c));
		}
		
		catch(IOException e)
		{
			return null;
		}
		
		GSet<String> hashes = new GSet<String>();
		DataCluster blocks = cc.crop("b");
		
		for(String i : blocks.keys())
		{
			if(i.contains("."))
			{
				hashes.add(i.split("\\.")[0]);
			}
		}
		
		for(String i : hashes)
		{
			int x = Integer.valueOf(i.split("\\.")[0]);
			int y = Integer.valueOf(i.split("\\.")[1]);
			int z = Integer.valueOf(i.split("\\.")[2]);
			Location l = new Location(W.getAsyncWorld(c.getWorld()), x, y, z);
			GLocation loc = new GLocation(l);
			NestedBlock block = new NestedBlock(loc);
			DataCluster bd = blocks.crop(i);
			block.add(bd);
			nc.getBlocks().put(loc, block);
		}
		
		DataCluster chunk = cc.crop("c");
		nc.add(chunk);
		
		return nc;
	}
	
	public void flush()
	{
		v("Flushing");
		
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
				
				v("Flushed");
			}
		};
	}
	
	public void save(NestedChunk c)
	{
		File file = NestUtil.getChunkAuxFile(c.getChunk());
		DataCluster cc = new DataCluster();
		cc.add(c, "c");
		
		for(GLocation i : c.getBlocks().k())
		{
			NestedBlock b = c.getBlocks().get(i);
			String key = "b." + i.getBlockX() + "." + i.getBlockY() + "." + i.getBlockZ() + ".";
			cc.add(b, key);
		}
		
		try
		{
			new JSONDataOutput().save(cc, file);
		}
		
		catch(IOException e)
		{
			e.printStackTrace();
		}
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
				Chunk i = e.getChunk();
				File file = NestUtil.getChunkFile(new GChunk(i));
				loading.add(i);
				
				if(file.exists())
				{
					new A()
					{
						@Override
						public void async()
						{
							NestedChunk nc = null;
							
							try
							{
								nc = (NestedChunk) Serializer.deserializeFromFile(file);
							}
							
							catch(Exception e)
							{
								f("Failed to load nest chunk @ " + i.getX() + " / " + i.getZ() + " > " + i.getWorld().getName());
							}
							
							if(nc != null)
							{
								NestedChunk ff = nc;
								
								new S()
								{
									@Override
									public void sync()
									{
										loading.remove(i);
										chunks.put(i, ff);
										
										if(chunks.get(i) == null)
										{
											return;
										}
										
										scrub(chunks.get(i));
										callEvent(new NestChunkLoadEvent(chunks.get(i)));
									}
								};
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
		File file = NestUtil.getChunkFile(new GChunk(e.getChunk()));
		
		if(!chunks.containsKey(e.getChunk()))
		{
			return;
		}
		
		Chunk chunk = e.getChunk();
		NestedChunk c = chunks.get(e.getChunk());
		
		if(c == null)
		{
			f("Chunk " + chunk.getX() + "," + chunk.getZ() + " @" + chunk.getWorld().getName() + " unloaded invalid.");
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
		
		callEvent(new NestChunkUnloadEvent(chunks.get(chunk)));
		chunks.remove(chunk);
		
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
				}
				
				catch(IOException ex)
				{
					f("Failed to save nest chunk @ " + c.getChunk().getX() + " / " + c.getChunk().getZ() + " / " + c.getChunk().getWorld());
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
