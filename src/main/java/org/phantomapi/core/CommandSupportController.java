package org.phantomapi.core;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.phantomapi.Phantom;
import org.phantomapi.clust.ConfigurableController;
import org.phantomapi.clust.Keyed;
import org.phantomapi.command.PhantomSender;
import org.phantomapi.construct.Controllable;
import org.phantomapi.text.MessageBuilder;
import org.phantomapi.util.P;

public class CommandSupportController extends ConfigurableController
{
	@Keyed("handle.jump.permission")
	public String pJump = "phantom.jump";
	
	@Keyed("handle.jump.enabled")
	public boolean eJump = true;
	
	public CommandSupportController(Controllable parentController)
	{
		super(parentController, "command-support");
	}
	
	@EventHandler
	public void on(PlayerCommandPreprocessEvent e)
	{
		String cmd = e.getMessage();
		Player p = e.getPlayer();
		PhantomSender s = new PhantomSender(p);
		s.setMessageBuilder(new MessageBuilder(Phantom.instance()));
		
		if(cmd.equalsIgnoreCase("/j") || cmd.equalsIgnoreCase("/jump") || cmd.equalsIgnoreCase("/jumpto"))
		{
			if(eJump && p.hasPermission(pJump))
			{
				s.sendMessage("Poof");
				Location l = P.targetBlock(p, 256);
				l.setYaw(p.getLocation().getYaw());
				l.setPitch(p.getLocation().getPitch());
				P.tp(p, l);
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
		
	}
	
	@Override
	public void onStop()
	{
		
	}
}
