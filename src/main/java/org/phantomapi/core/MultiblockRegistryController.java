package org.phantomapi.core;

import java.io.File;
import java.io.IOException;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.event.MultiblockConstructEvent;
import org.phantomapi.event.MultiblockDestroyEvent;
import org.phantomapi.event.MultiblockInteractEvent;
import org.phantomapi.event.MultiblockLoadEvent;
import org.phantomapi.event.MultiblockUnloadEvent;
import org.phantomapi.event.NestChunkLoadEvent;
import org.phantomapi.event.NestChunkUnloadEvent;
import org.phantomapi.lang.GList;
import org.phantomapi.lang.GMap;
import org.phantomapi.lang.GSet;
import org.phantomapi.multiblock.Multiblock;
import org.phantomapi.multiblock.MultiblockInstance;
import org.phantomapi.multiblock.MultiblockStructure;
import org.phantomapi.multiblock.MultiblockUtils;
import org.phantomapi.nest.Nest;
import org.phantomapi.statistics.Monitorable;
import org.phantomapi.sync.TaskLater;
import org.phantomapi.util.C;
import org.phantomapi.util.ExceptionUtil;
import org.phantomapi.util.F;
import net.minecraft.server.v1_8_R3.Material;

/**
 * Manages multiblock registries
 * 
 * @author cyberpwn
 */
public class MultiblockRegistryController extends Controller implements Monitorable
{
	private GMap<String, MultiblockStructure> structures;
	private GMap<Integer, Multiblock> instances;
	private GMap<Chunk, Integer> instanceReference;
	private int v = 0;
	
	public MultiblockRegistryController(Controllable parentController)
	{
		super(parentController);
		
		this.structures = new GMap<String, MultiblockStructure>();
		this.instances = new GMap<Integer, Multiblock>();
		this.instanceReference = new GMap<Chunk, Integer>();
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
	public void on(NestChunkUnloadEvent e)
	{
		if(instanceReference.containsKey(e.getChunk()))
		{
			int i = instanceReference.get(e.getChunk());
			
			try
			{
				MultiblockUnloadEvent mbu = new MultiblockUnloadEvent(instances.get(i), e.getWorld());
				callEvent(mbu);
				MultiblockUtils.save(instances.get(i));
				instances.remove(i);
				
				for(Chunk j : instanceReference.k())
				{
					if(instanceReference.get(j) == i)
					{
						instanceReference.remove(j);
					}
				}
			}
			
			catch(IOException ex)
			{
				ExceptionUtil.print(ex);
			}
		}
	}
	
	@EventHandler
	public void on(NestChunkLoadEvent e)
	{
		if(Nest.getChunk(e.getChunk()) != null)
		{
			GSet<Integer> ids = new GSet<Integer>();
			
			for(String i : Nest.getChunk(e.getChunk()).keys())
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
				if(instances.containsKey(i))
				{
					continue;
				}
				
				try
				{
					File file = MultiblockUtils.getFile(e.getWorld(), i);
					
					if(file.exists())
					{
						try
						{
							Multiblock mb = MultiblockUtils.load(e.getWorld(), i);
							instances.put(i, mb);
							
							for(Chunk j : mb.getChunks())
							{
								if(j.isLoaded())
								{
									instanceReference.put(j, i);
								}
							}
							
							MultiblockLoadEvent mbl = new MultiblockLoadEvent(mb, e.getWorld());
							callEvent(mbl);
						}
						
						catch(Exception exx)
						{
							
						}
					}
				}
				
				catch(Exception ex)
				{
					
				}
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
				if(Nest.getChunk(e.getBlock().getChunk()) == null)
				{
					return;
				}
				
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
									if(j.isLoaded())
									{
										instanceReference.put(j, id);
									}
								}
								
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
	public void on(PlayerInteractEvent e)
	{
		try
		{
			if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
			{
				return;
			}
			
			if(((e.getItem() == null || e.getItem().getType().equals(Material.AIR))) || e.getPlayer().isSneaking())
			{
				for(Integer i : instances.k())
				{
					if(instances.get(i).getWorld().equals(e.getClickedBlock().getWorld()) && instances.get(i).contains(e.getClickedBlock().getLocation()))
					{
						MultiblockInteractEvent evt = new MultiblockInteractEvent(e.getPlayer(), instances.get(i), e.getPlayer().getWorld(), e.getAction());
						callEvent(evt);
						e.setCancelled(true);
						return;
					}
				}
			}
		}
		
		catch(Exception ex)
		{
			
		}
	}
	
	@EventHandler
	public void on(BlockBreakEvent e)
	{
		if(Nest.getChunk(e.getBlock().getChunk()) == null)
		{
			return;
		}
		
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
					
					for(Chunk j : instanceReference.k())
					{
						if(instanceReference.get(j) == i)
						{
							instanceReference.remove(j);
						}
					}
					
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
	
	@Override
	public String getMonitorableData()
	{
		return "Locks: " + C.LIGHT_PURPLE + F.f(v);
	}
	
	public GList<Multiblock> getMultiblocks()
	{
		return instances.v();
	}
	
	public GList<Multiblock> getMultiblocks(String type)
	{
		GList<Multiblock> bls = getMultiblocks();
		
		for(Multiblock i : bls.copy())
		{
			if(!type.equals(i.getType()))
			{
				bls.remove(i);
			}
		}
		
		return bls;
	}
}
