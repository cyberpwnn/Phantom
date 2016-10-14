package org.phantomapi.event;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;

/**
 * Arrow shot by player hit another or the same player
 * 
 * @author cyberpwn
 */
public class PlayerArrowDamagePlayerEvent extends PlayerProjectileDamagePlayerEvent
{
	private final Arrow arrow;
	
	public PlayerArrowDamagePlayerEvent(Player damager, Player player, Arrow arrow, Double damage)
	{
		super(damager, player, arrow, damage);
		
		this.arrow = arrow;
	}
	
	public Arrow getArrow()
	{
		return arrow;
	}
}
