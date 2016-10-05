package org.phantomapi.core;

import java.io.File;
import java.io.IOException;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.util.Vector;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.event.MultiblockConstructEvent;
import org.phantomapi.event.MultiblockDestroyEvent;
import org.phantomapi.event.MultiblockLoadEvent;
import org.phantomapi.event.MultiblockUnloadEvent;
import org.phantomapi.event.NestChunkLoadEvent;
import org.phantomapi.lang.GMap;
import org.phantomapi.lang.GSet;
import org.phantomapi.multiblock.Multiblock;
import org.phantomapi.multiblock.MultiblockInstance;
import org.phantomapi.multiblock.MultiblockStructure;
import org.phantomapi.multiblock.MultiblockUtils;
import org.phantomapi.nest.Nest;
import org.phantomapi.sync.TaskLater;
import org.phantomapi.util.Chunks;
import org.phantomapi.util.ExceptionUtil;

/**
 * Manages multiblock registries
 * 
 * @author cyberpwn
 */
public class MultiblockRegistryController extends Controller
{
	private GMap<String, MultiblockStructure> structures;
	private GMap<Integer, Multiblock> instances;
	
	public MultiblockRegistryController(Controllable parentController)
	{
		super(parentController);
		
		this.structures = new GMap<String, MultiblockStructure>();
		this.instances = new GMap<Integer, Multiblock>();
	}
	
	@Override
	public void onStart()
	{
		
	}
	
	@Override
	public void onStop()
	{
		
	}
	
	public void registerStructure(MultiblockStructure mb)
	{
		structures.put(mb.getType(), mb);
	}
	
	public void unRegisterStructure(MultiblockStructure mb)
	{
		structures.remove(mb.getType());
	}
	
	@EventHandler
	public void on(ChunkUnloadEvent e)
	{
		for(Integer i : instances.k())
		{
			if(instances.get(i).getChunks().contains(e.getChunk()))
			{
				try
				{
					MultiblockUnloadEvent mbu = new MultiblockUnloadEvent(instances.get(i), e.getWorld());
					callEvent(mbu);
					Chunks.unload(instances.get(i).getChunks());
					MultiblockUtils.save(instances.get(i));
					instances.remove(i);
				}
				
				catch(IOException ex)
				{
					ExceptionUtil.print(ex);
				}
			}
		}
	}
	
	@EventHandler
	public void on(NestChunkLoadEvent e)
	{
		GSet<Integer> ids = new GSet<Integer>();
		
		for(String i : e.getNestedChunk().keys())
		{
			if(i.startsWith("mb") && i.contains("."))
			{
				try
				{
					String s = i.split("\\.")[1].replaceAll("i-", "");
					Integer v = Integer.valueOf(s);
					ids.add(v);
				}
				
				catch(Exception ex)
				{
					
				}
			}
		}
		
		for(Integer i : ids)
		{
			try
			{
				File file = MultiblockUtils.getFile(e.getWorld(), i);
				
				if(file.exists())
				{
					Multiblock mb = MultiblockUtils.load(e.getWorld(), i);
					instances.put(i, mb);
					MultiblockLoadEvent mbl = new MultiblockLoadEvent(mb, e.getWorld());
					callEvent(mbl);
				}
			}
			
			catch(Exception ex)
			{
				
			}
		}
	}
	
	@EventHandler
	public void on(BlockPlaceEvent e)
	{
		new TaskLater()
		{
			@Override
			public void run()
			{
				for(String i : structures.k())
				{
					GMap<Vector, Location> map = structures.get(i).match(e.getBlock().getLocation());
					
					if(map != null)
					{
						int id = MultiblockUtils.getNextID(e.getBlock().getWorld());
						Multiblock mb = new MultiblockInstance(id, i, map);
						
						try
						{
							MultiblockConstructEvent mbc = new MultiblockConstructEvent(e.getPlayer(), mb, mb.getWorld());
							callEvent(mbc);
							
							if(!e.isCancelled())
							{
								MultiblockUtils.save(mb);
								instances.put(id, mb);
								
								for(Chunk j : mb.getChunks())
								{
									Nest.getChunk(j).set("mb.i-" + mb.getId(), mb.getType());
								}
							}
						}
						
						catch(IOException ex)
						{
							ExceptionUtil.print(ex);
						}
						
						break;
					}
				}
			}
		};
	}
	
	@EventHandler
	public void on(BlockBreakEvent e)
	{
		for(Integer i : instances.k())
		{
			if(instances.get(i).getWorld().equals(e.getBlock().getWorld()) && instances.get(i).contains(e.getBlock().getLocation()))
			{
				MultiblockDestroyEvent mbd = new MultiblockDestroyEvent(e.getPlayer(), instances.get(i), e.getBlock().getWorld());
				callEvent(mbd);
				
				if(!mbd.isCancelled())
				{
					Multiblock mb = instances.get(i);
					instances.remove(i);
					MultiblockUtils.getFile(mb.getWorld(), i).delete();
					
					for(Chunk j : mb.getChunks())
					{
						Nest.getChunk(j).remove("mb.i-" + mb.getId());
					}
				}
				
				else
				{
					e.setCancelled(true);
				}
			}
		}
	}
}
