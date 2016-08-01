package org.cyberpwn.phantom.event;

import org.bukkit.entity.Player;
import org.cyberpwn.phantom.event.structure.KillEvent;

/**
 * Called when a player kills another player
 * 
 * @author cyberpwn
 *
 */
public class PlayerKillPlayerEvent extends KillEvent
{
	private final Player damager;
	private final Player player;
	
	/**
	 * Create a kill event
	 * 
	 * @param damager
	 *            the killer
	 * @param player
	 *            the player who died
	 */
	public PlayerKillPlayerEvent(Player damager, Player player)
	{
		this.damager = damager;
		this.player = player;
	}
	
	/**
	 * Get the killer
	 * 
	 * @return the killer
	 */
	public Player getDamager()
	{
		return damager;
	}
	
	/**
	 * Get the dead player
	 * 
	 * @return the dead player
	 */
	public Player getPlayer()
	{
		return player;
	}
}
