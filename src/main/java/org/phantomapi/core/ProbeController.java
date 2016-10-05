package org.phantomapi.core;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.command.PhantomSender;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.lang.GList;
import org.phantomapi.stack.Stack;
import org.phantomapi.text.MessageBuilder;
import org.phantomapi.util.C;
import org.phantomapi.util.F;
import org.phantomapi.util.Probe;

public class ProbeController extends Controller
{
	private GList<Probe> probes;
	
	public ProbeController(Controllable parentController)
	{
		super(parentController);
		
		this.probes = new GList<Probe>();
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
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void on(PlayerInteractEvent e)
	{
		try
		{
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
						
						e.setCancelled(true);
					}
				}
			}
		}
		
		catch(Exception ex)
		{
			
		}
	}
}
