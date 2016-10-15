package org.phantomapi.core;

import java.io.File;
import java.io.IOException;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.construct.Ticked;
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
import org.phantomapi.sync.ExecutiveRunnable;
import org.phantomapi.sync.TaskLater;
import org.phantomapi.util.C;
import org.phantomapi.util.Chunks;
import org.phantomapi.util.ExceptionUtil;
import org.phantomapi.util.F;

/**
 * Manages multiblock registries
 * 
 * @author cyberpwn
 */
@Ticked(100)
public class MultiblockRegistryController extends Controller implements Monitorable
{
	private GMap<String, MultiblockStructure> structures;
	private GMap<Integer, Multiblock> instances;
	private GMap<Chunk, Integer> instanceReference;
	private int v = 0;
	
	public MultiblockRegistryController(Controllable parentController)
	{
		super(parentController);
		
		structures = new GMap<String, MultiblockStructure>();
		instances = new GMap<Integer, Multiblock>();
		instanceReference = new GMap<Chunk, Integer>();
	}
	
	@Override
	public void onStart()
	{
		
	}
	
	@Override
	public void onStop()
	{
		
	}
	
	@Override
	public void onTick()
	{
		validate();
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
				
				for(Chunk j : instanceReference.k())
				{
					if(instanceReference.get(j) == i)
					{
						instanceReference.remove(j);
					}
				}
				
				Multiblock mb = instances.get(i);
				instances.remove(i);
				Chunks.unload(mb.getChunks());
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
								if(!j.isLoaded())
								{
									j.load();
								}
								
								instanceReference.put(j, i);
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
							MultiblockConstructEvent mbc = new MultiblockConstructEvent(e.getPlayer(), mb, mb.getWorld(), e.getBlock());
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
			
			if(e.getClickedBlock() != null && (e.getClickedBlock().getType().equals(Material.CHEST) || e.getClickedBlock().getType().equals(Material.TRAPPED_CHEST)))
			{
				return;
			}
			
			if(e.getItem() == null || e.getItem().getType().equals(Material.AIR) || e.getPlayer().isSneaking())
			{
				for(Integer i : instances.k())
				{
					if(instances.get(i).getWorld().equals(e.getClickedBlock().getWorld()) && instances.get(i).contains(e.getClickedBlock().getLocation()))
					{
						MultiblockInteractEvent evt = new MultiblockInteractEvent(e.getPlayer(), instances.get(i), e.getPlayer().getWorld(), e.getAction(), e.getClickedBlock());
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
				MultiblockDestroyEvent mbd = new MultiblockDestroyEvent(e.getPlayer(), instances.get(i), e.getBlock().getWorld(), e.getBlock());
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
	
	public void validate()
	{
		instances.v().schedule(new ExecutiveRunnable<Multiblock>()
		{
			@Override
			public void run()
			{
				Multiblock next = next();
				
				if(!next.update())
				{
					MultiblockDestroyEvent mbd = new MultiblockDestroyEvent(null, next, next.getWorld(), null);
					callEvent(mbd);
					f("Destroying invalid multiblock: " + next.getType() + "." + next.getId());
					Multiblock mb = next;
					instances.remove(mb.getId());
					MultiblockUtils.getFile(mb.getWorld(), mb.getId()).delete();
					
					for(Chunk j : instanceReference.k())
					{
						if(instanceReference.get(j) == mb.getId())
						{
							instanceReference.remove(j);
						}
					}
					
					for(Chunk j : mb.getChunks())
					{
						Nest.getChunk(j).remove("mb.i-" + mb.getId());
					}
				}
			}
		});
	}
	
	public GMap<String, MultiblockStructure> getStructures()
	{
		return structures;
	}
	
	public GMap<Integer, Multiblock> getInstances()
	{
		return instances;
	}
	
	public GMap<Chunk, Integer> getInstanceReference()
	{
		return instanceReference;
	}
	
	public int getV()
	{
		return v;
	}
}
