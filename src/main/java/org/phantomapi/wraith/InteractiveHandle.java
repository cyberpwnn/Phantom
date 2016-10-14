package org.phantomapi.wraith;

import org.bukkit.entity.Player;

/**
 * Interactive handle
 * 
 * @author cyberpwn
 */
public interface InteractiveHandle
{
	/**
	 * Called when a player interacts with this wraith
	 * 
	 * @param p
	 *            the player
	 */
	public void onInteract(Player p);
}
