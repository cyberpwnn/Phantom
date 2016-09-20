package org.phantomapi.event;

import org.bukkit.entity.Player;
import org.phantomapi.wraith.Wraith;

/**
 * Wraith clicked by player
 * 
 * @author cyberpwn
 */
public class WraithClickEvent extends WraithEvent
{
	private final Player player;
	
	public WraithClickEvent(Wraith wraith, Player player)
	{
		super(wraith);
		
		this.player = player;
	}

	public Player getPlayer()
	{
		return player;
	}
}
