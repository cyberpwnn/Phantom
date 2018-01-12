package org.phantomapi.service;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.phantomapi.Phantom;

import phantom.event.PlayerLookEvent;
import phantom.event.PlayerMovePositionEvent;
import phantom.pawn.Name;
import phantom.pawn.Register;
import phantom.pawn.Singular;
import phantom.pawn.Start;
import phantom.pawn.Stop;
import phantom.service.IService;
import phantom.util.metrics.Documented;

/**
 * Service for refiring events
 *
 * @author cyberpwn
 */
@Documented
@Register
@Name("SVC Event")
@Singular
public class EventSVC implements IService
{
	@Start
	public void start()
	{

	}

	@Stop
	public void stop()
	{
		
	}
	
	@EventHandler(ignoreCancelled = false, priority = EventPriority.MONITOR)
	public void on(PlayerMoveEvent e)
	{
		if(e.getFrom().getX() != e.getTo().getX() || e.getFrom().getY() != e.getTo().getY() || e.getFrom().getZ() != e.getTo().getZ())
		{
			PlayerMovePositionEvent pos = new PlayerMovePositionEvent(e.getFrom(), e.getTo());
			pos.setCancelled(e.isCancelled());
			Phantom.callEvent(pos);
			e.setCancelled(pos.isCancelled());
		}
		
		if(e.getFrom().getYaw() != e.getTo().getYaw() || e.getFrom().getPitch() != e.getTo().getPitch())
		{
			PlayerLookEvent look = new PlayerLookEvent(e.getFrom().getDirection(), e.getTo().getDirection());
			look.setCancelled(e.isCancelled());
			Phantom.callEvent(look);
			e.setCancelled(look.isCancelled());
		}
	}
}
