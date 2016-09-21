package org.phantomapi.event;

import org.bukkit.entity.Player;
import org.phantomapi.wraith.Wraith;
import org.phantomapi.wraith.WraithInteraction;

/**
 * A Player interacted with a wraith.
 * 
 * @author cyberpwn
 */
public class WraithInteractEvent extends WraithEvent
{
	private final Player player;
	private final WraithInteraction type;
	
	public WraithInteractEvent(Wraith wraith, Player player, WraithInteraction type)
	{
		super(wraith);
		
		this.type = type;
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
	
	/**
	 * Get the type of interaction
	 * 
	 * @return the wraith interaction type
	 */
	public WraithInteraction getType()
	{
		return type;
	}
}
