package org.phantomapi.event;

import org.bukkit.entity.Player;
import org.phantomapi.wraith.Wraith;

/**
 * A Player interacted with a wraith.
 * 
 * @author cyberpwn
 */
public class WraithInteractEvent extends WraithEvent
{
	private final Player player;
	
	public WraithInteractEvent(Wraith wraith, Player player)
	{
		super(wraith);
		
		this.player = player;
	}
	
	/**
	 * Get the interacting player
	 * 
	 * @return the player
	 */
	public Player getPlayer()
	{
		return player;
	}
}
