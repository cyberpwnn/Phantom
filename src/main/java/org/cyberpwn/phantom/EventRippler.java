package org.cyberpwn.phantom;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.cyberpwn.phantom.construct.Controllable;
import org.cyberpwn.phantom.construct.Controller;
import org.cyberpwn.phantom.event.PlayerDamagePlayerEvent;
import org.cyberpwn.phantom.event.PlayerJumpEvent;
import org.cyberpwn.phantom.event.PlayerKillPlayerEvent;
import org.cyberpwn.phantom.event.PlayerMoveBlockEvent;
import org.cyberpwn.phantom.event.PlayerMoveChunkEvent;
import org.cyberpwn.phantom.event.PlayerMoveLookEvent;
import org.cyberpwn.phantom.event.PlayerMovePositionEvent;

/**
 * Ripple fire events for more specific events
 * 
 * @author cyberpwn
 */
public class EventRippler extends Controller
{
	public EventRippler(Controllable parentController)
	{
		super(parentController);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void on(EntityDamageByEntityEvent e)
	{
		try
		{
			if(e.getDamager().getType().equals(EntityType.PLAYER))
			{
				if(e.getEntity().getType().equals(EntityType.PLAYER))
				{
					PlayerDamagePlayerEvent ev = new PlayerDamagePlayerEvent((Player) e.getDamager(), (Player) e.getEntity(), (Double) e.getDamage());
					callEvent(ev);
					e.setCancelled(ev.isCancelled());
				}
			}
		}
		
		catch(Exception ex)
		{
			
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void on(EntityDeathEvent e)
	{
		try
		{
			if(e.getEntity().getType().equals(EntityType.PLAYER))
			{
				if(e.getEntity().getKiller().getType().equals(EntityType.PLAYER))
				{
					PlayerKillPlayerEvent ev = new PlayerKillPlayerEvent((Player) e.getEntity().getKiller(), (Player) e.getEntity());
					callEvent(ev);
				}
			}
		}
		
		catch(Exception ex)
		{
			
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void on(PlayerMoveEvent e)
	{
		try
		{
			if(!e.getPlayer().isFlying() && e.getTo().clone().subtract(e.getFrom().clone()).toVector().getY() > 0.419999986)
			{
				if(!e.getFrom().getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.AIR) || !e.getFrom().getBlock().getRelative(BlockFace.DOWN).getRelative(BlockFace.NORTH).getType().equals(Material.AIR) || !e.getFrom().getBlock().getRelative(BlockFace.DOWN).getRelative(BlockFace.WEST).getType().equals(Material.AIR) || !e.getFrom().getBlock().getRelative(BlockFace.DOWN).getRelative(BlockFace.EAST).getType().equals(Material.AIR) || !e.getFrom().getBlock().getRelative(BlockFace.DOWN).getRelative(BlockFace.SOUTH).getType().equals(Material.AIR))
				{
					PlayerJumpEvent ev = new PlayerJumpEvent(e.getPlayer());
					callEvent(ev);
					e.setCancelled(ev.isCancelled());
				}
			}
			
			Location f = new Location(e.getFrom().getWorld(), e.getFrom().getX(), e.getFrom().getY(), e.getFrom().getZ());
			Location t = new Location(e.getTo().getWorld(), e.getTo().getX(), e.getTo().getY(), e.getTo().getZ());
			
			if(!t.equals(f) && !e.isCancelled())
			{
				PlayerMovePositionEvent pmpe = new PlayerMovePositionEvent(e.getPlayer(), e.getFrom(), e.getTo());
				callEvent(pmpe);
				e.setCancelled(pmpe.isCancelled());
				
				if(!t.getBlock().equals(f.getBlock()) && !e.isCancelled())
				{
					PlayerMoveBlockEvent pmbe = new PlayerMoveBlockEvent(e.getPlayer(), e.getFrom(), e.getTo());
					callEvent(pmbe);
					e.setCancelled(pmbe.isCancelled());
					
					if(!t.getChunk().equals(f.getChunk()) && !e.isCancelled())
					{
						PlayerMoveChunkEvent pmce = new PlayerMoveChunkEvent(e.getPlayer(), e.getFrom(), e.getTo());
						callEvent(pmce);
						e.setCancelled(pmce.isCancelled());
					}
				}
			}
			
			else if((e.getFrom().getYaw() != e.getTo().getYaw() || e.getFrom().getPitch() != e.getTo().getPitch()) && !e.isCancelled())
			{
				PlayerMoveLookEvent pmle = new PlayerMoveLookEvent(e.getPlayer(), e.getFrom(), e.getTo());
				callEvent(pmle);
				e.setCancelled(pmle.isCancelled());
			}
		}
		
		catch(Exception ex)
		{
			
		}
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
