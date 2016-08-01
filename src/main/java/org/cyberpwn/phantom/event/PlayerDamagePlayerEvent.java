package org.cyberpwn.phantom.event;

import org.bukkit.entity.Player;
import org.cyberpwn.phantom.event.structure.CombatEvent;

/**
 * Player Damage Player. This fires when a player has damaged another player.
 * Cancelling this will cancel the underlying event aswell
 * 
 * @author cyberpwn
 *
 */
public class PlayerDamagePlayerEvent extends CombatEvent
{
	private final Player damager;
	private final Player player;
	
	/**
	 * Create a damage event
	 * 
	 * @param damager
	 *            the player damager
	 * @param player
	 *            the player who took the damage
	 * @param damage
	 *            the damage
	 */
	public PlayerDamagePlayerEvent(Player damager, Player player, Double damage)
	{
		super(damage);
		
		this.damager = damager;
		this.player = player;
	}
	
	/**
	 * Get the damager
	 * 
	 * @return the damager
	 */
	public Player getDamager()
	{
		return damager;
	}
	
	/**
	 * Get the player who took the damage
	 * 
	 * @return the player who took the damage
	 */
	public Player getPlayer()
	{
		return player;
	}
}
