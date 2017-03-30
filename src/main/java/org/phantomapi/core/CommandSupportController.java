package org.phantomapi.core;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.phantomapi.clust.ConfigurableController;
import org.phantomapi.clust.Keyed;
import org.phantomapi.command.PhantomSender;
import org.phantomapi.construct.Controllable;
import org.phantomapi.lang.GList;
import org.phantomapi.text.MessageBuilder;
import org.phantomapi.text.TXT;
import org.phantomapi.text.TagProvider;
import org.phantomapi.util.C;
import org.phantomapi.util.F;
import org.phantomapi.util.P;

public class CommandSupportController extends ConfigurableController implements TagProvider
{
	@Keyed("handle.jump.permission")
	public String pJump = "phantom.jump";
	
	@Keyed("handle.jump.enabled")
	public boolean eJump = true;
	
	@Keyed("handle.teleport.permission")
	public String pTeleport = "phantom.jump";
	
	@Keyed("handle.teleport.enabled")
	public boolean eTeleport = true;
	
	@Keyed("command.tag")
	public String tag = TXT.makeTag(C.LIGHT_PURPLE, C.DARK_GRAY, C.GRAY, "Phantom");
	
	@Keyed("command.hover")
	public String hover = "&dPhantom";
	
	public CommandSupportController(Controllable parentController)
	{
		super(parentController, "command-support");
	}
	
	@EventHandler
	public void on(PlayerCommandPreprocessEvent e)
	{
		Player p = e.getPlayer();
		PhantomSender s = new PhantomSender(p);
		s.setMessageBuilder(new MessageBuilder(this));
		String message = e.getMessage();
		String roots = message;
		GList<String> rtz = new GList<String>(roots.split(" "));
		String command = rtz.pop();
		String[] args = rtz.toArray(new String[rtz.size()]);
		
		if(command.equalsIgnoreCase("/j") || command.equalsIgnoreCase("/jump") || command.equalsIgnoreCase("/jumpto"))
		{
			if(eJump && p.hasPermission(pJump))
			{
				s.sendMessage("Poof");
				Location l = P.targetBlock(p, 256);
				l.setYaw(p.getLocation().getYaw());
				l.setPitch(p.getLocation().getPitch());
				P.tp(p, l);
				e.setCancelled(true);
			}
		}
		
		else if(command.equalsIgnoreCase("/tp") || command.equalsIgnoreCase("/teleport"))
		{
			if(eTeleport && p.hasPermission(pTeleport))
			{
				if(args.length > 0)
				{
					if(args.length == 3)
					{
						try
						{
							Integer x = Integer.valueOf(args[0]);
							Integer y = Integer.valueOf(args[1]);
							Integer z = Integer.valueOf(args[2]);
							Location l = new Location(p.getWorld(), x, y, z);
							s.sendMessage("Teleporting to " + C.WHITE + args[0] + ", " + args[1] + ", " + args[2]);
							P.tp(p, l);
						}
						
						catch(NumberFormatException ex)
						{
							s.sendMessage(C.RED + "Invalid coordinates " + args[0] + ", " + args[1] + ", " + args[2]);
						}
						
						e.setCancelled(true);
					}
					
					else
					{
						Player tar = P.findPlayer(args[0]);
						
						if(tar != null)
						{
							s.sendMessage("Teleporting to " + C.WHITE + tar.getName());
							P.tp(p, tar.getLocation());
						}
						
						else
						{
							s.sendMessage(C.RED + "Cannot find player " + args[0]);
						}
						
						e.setCancelled(true);
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public void on(PlayerTeleportEvent e)
	{
		e.getPlayer().sendMessage("Teleport");
	}
	
	@Override
	public void onStart()
	{
		loadCluster(this);
	}
	
	@Override
	public void onStop()
	{
		
	}
	
	@Override
	public String getChatTag()
	{
		return F.color(tag);
	}
	
	@Override
	public String getChatTagHover()
	{
		return F.color(hover);
	}
}
