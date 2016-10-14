package org.phantomapi.event;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;

/**
 * Represents a player shooting another player
 * 
 * @author cyberpwn
 */
public class PlayerProjectileDamagePlayerEvent extends PlayerDamagePlayerEvent
{
	private final Projectile projectile;
	
	public PlayerProjectileDamagePlayerEvent(Player damager, Player player, Projectile projectile, Double damage)
	{
		super(damager, player, damage);
		
		this.projectile = projectile;
	}
	
	public Projectile getProjectile()
	{
		return projectile;
	}
}
