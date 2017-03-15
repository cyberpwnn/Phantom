package org.phantomapi.core;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.phantomapi.block.BlockHandler;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.command.PhantomSender;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.event.ControllerStopEvent;
import org.phantomapi.lang.GList;
import org.phantomapi.nbt.NBTTagCompound;
import org.phantomapi.nbt.NBTUtil;
import org.phantomapi.nest.Nest;
import org.phantomapi.stack.Stack;
import org.phantomapi.sync.Task;
import org.phantomapi.text.MessageBuilder;
import org.phantomapi.util.C;
import org.phantomapi.util.F;
import org.phantomapi.util.FinalInteger;
import org.phantomapi.util.Probe;
import org.phantomapi.vfx.ParticleEffect;
import org.phantomapi.world.Blocks;

public class ProbeController extends Controller
{
	private GList<Probe> probes;
	
	public ProbeController(Controllable parentController)
	{
		super(parentController);
		
		probes = new GList<Probe>();
	}
	
	@Override
	public void onStart()
	{
		
	}
	
	@Override
	public void onStop()
	{
		
	}
	
	public DataCluster probe(Block block)
	{
		DataCluster cc = new DataCluster();
		
		for(Probe i : probes)
		{
			i.onProbe(block, cc);
		}
		
		return cc;
	}
	
	public void registerProbe(Probe probe)
	{
		probes.add(probe);
	}
	
	public void unRegisterProbe(Probe probe)
	{
		probes.remove(probe);
	}
	
	public ItemStack getProbe()
	{
		Stack s = new Stack(Material.TRIPWIRE_HOOK);
		s.setName(C.LIGHT_PURPLE + "Probe");
		s.setLore(new GList<String>().qadd(C.DARK_PURPLE + "Right click to probe"));
		return s.toItemStack();
	}
	
	@EventHandler
	public void on(ControllerStopEvent e)
	{
		if(e.getControllable() instanceof Probe)
		{
			unRegisterProbe((Probe) e.getControllable());
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGHEST)
	public void on(PlayerInteractEvent e)
	{
		if(!e.getPlayer().hasPermission("pha.god"))
		{
			return;
		}
		
		try
		{
			if(e.getAction().equals(Action.LEFT_CLICK_AIR) && e.getPlayer().isSneaking())
			{
				if(e.getItem().getType().equals(Material.TRIPWIRE_HOOK))
				{
					ItemStack is = e.getItem();
					ItemMeta im = is.getItemMeta();
					
					if(im.getDisplayName().equals(C.LIGHT_PURPLE + "Probe"))
					{
						Nest.giveMap(e.getPlayer());
					}
				}
				
				else if(e.getItem().getType().equals(Material.MAP))
				{
					ItemStack is = e.getItem();
					ItemMeta im = is.getItemMeta();
					
					if(im.getDisplayName().equals(C.LIGHT_PURPLE + "Probe"))
					{
						e.getPlayer().setItemInHand(getProbe());
					}
				}
			}
			
			if(e.getClickedBlock() != null && e.getItem() != null && e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
			{
				if(e.getItem().getType().equals(Material.TRIPWIRE_HOOK))
				{
					ItemStack is = e.getItem();
					ItemMeta im = is.getItemMeta();
					
					if(im.getDisplayName().equals(C.LIGHT_PURPLE + "Probe"))
					{
						DataCluster cc = probe(e.getClickedBlock());
						MessageBuilder mb = new MessageBuilder();
						PhantomSender s = new PhantomSender(e.getPlayer());
						mb.setTag(C.DARK_GRAY + "[" + C.LIGHT_PURPLE + "Probe" + C.DARK_GRAY + "]: " + C.GRAY, C.LIGHT_PURPLE + "Probed information");
						s.setMessageBuilder(mb);
						
						for(BlockHandler i : Blocks.getHandlers())
						{
							if(i.getProtector(e.getClickedBlock()) != null)
							{
								s.sendMessage(C.WHITE.toString() + "Protector: " + C.GRAY + i.getProtector() + " <> " + i.getProtector(e.getClickedBlock()));
							}
						}
						
						s.sendMessage(C.WHITE.toString() + "Block: " + C.GRAY + e.getClickedBlock().getType().toString() + ":" + e.getClickedBlock().getData());
						
						if(cc.size() == 0)
						{
							s.sendMessage("No Data for " + C.WHITE + e.getClickedBlock().getX() + " " + e.getClickedBlock().getY() + " " + e.getClickedBlock().getZ());
						}
						
						else
						{
							s.sendMessage(C.WHITE.toString() + cc.size() + C.GRAY + " Nodes, " + C.WHITE + F.fileSize(cc.byteSize()));
							
							for(String i : cc.toLines(false))
							{
								s.sendMessage(i);
							}
						}
						
						FinalInteger fi = new FinalInteger(0);
						
						new Task(0)
						{
							@Override
							public void run()
							{
								if(fi.get() > 5)
								{
									cancel();
									
									new Task(0)
									{
										@Override
										public void run()
										{
											if(fi.get() > 10)
											{
												cancel();
											}
											
											fi.add(1);
											
											for(double i = 0; i < 3; i += 0.1)
											{
												ParticleEffect.FIREWORKS_SPARK.display(0f, 1, Blocks.getCenter(e.getClickedBlock()).add(0, 0 + i, 0), 32);
												ParticleEffect.FIREWORKS_SPARK.display(0f, 1, Blocks.getCenter(e.getClickedBlock()).add(0, 0 - i, 0), 32);
												ParticleEffect.FIREWORKS_SPARK.display(0f, 1, Blocks.getCenter(e.getClickedBlock()).add(0 + i, 0, 0), 32);
												ParticleEffect.FIREWORKS_SPARK.display(0f, 1, Blocks.getCenter(e.getClickedBlock()).add(0 - i, 0, 0), 32);
												ParticleEffect.FIREWORKS_SPARK.display(0f, 1, Blocks.getCenter(e.getClickedBlock()).add(0, 0, 0 + i), 32);
												ParticleEffect.FIREWORKS_SPARK.display(0f, 1, Blocks.getCenter(e.getClickedBlock()).add(0, 0, 0 - i), 32);
											}
										}
									};
								}
								
								fi.add(1);
								ParticleEffect.SPELL_WITCH.display(0.6f, 12, Blocks.getCenter(e.getClickedBlock()), 32);
								ParticleEffect.SPELL_WITCH.display(0.6f, 12, Blocks.getCenter(e.getClickedBlock()).add(0, 0.5, 0), 32);
								ParticleEffect.SPELL_WITCH.display(0.6f, 12, Blocks.getCenter(e.getClickedBlock()).add(0, -0.5, 0), 32);
							}
						};
						
						e.setCancelled(true);
					}
				}
			}
		}
		
		catch(Exception ex)
		{
			
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void on(PlayerInteractAtEntityEvent e)
	{
		if(!e.getPlayer().hasPermission("pha.god"))
		{
			return;
		}
		
		ItemStack is = e.getPlayer().getInventory().getItemInMainHand();
		
		if(is!= null && is.getType().equals(Material.TRIPWIRE_HOOK))
		{
			ItemMeta im = is.getItemMeta();
			
			if(im.getDisplayName().equals(C.LIGHT_PURPLE + "Probe"))
			{
				Entity en = e.getRightClicked();
				
				if(en != null)
				{
					MessageBuilder mb = new MessageBuilder();
					PhantomSender s = new PhantomSender(e.getPlayer());
					mb.setTag(C.DARK_GRAY + "[" + C.LIGHT_PURPLE + "Probe" + C.DARK_GRAY + "]: " + C.GRAY, C.LIGHT_PURPLE + "Probed information");
					s.setMessageBuilder(mb);
					
					NBTTagCompound nbt = NBTUtil.getNBTTag(en);
					
					ParticleEffect.SPELL_WITCH.display(0.5f, 24, en.getLocation(), 32);
					
					s.sendMessage(C.WHITE + "Entity: " + C.GRAY + en.getType());
					
					for(Object i : nbt.c())
					{
						s.sendMessage(C.LIGHT_PURPLE + i.toString() + ": (" + C.GRAY + nbt.get(i.toString()).getTypeId() + ") " + C.WHITE + nbt.get(i.toString()).toString());
					}
				}
			}
		}
	}
}
