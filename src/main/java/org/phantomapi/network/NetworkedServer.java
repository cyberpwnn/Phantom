package org.phantomapi.network;

import org.bukkit.entity.Player;
import org.phantomapi.lang.GList;

/**
 * Represents a remote networked server on a bungeecord network
 * 
 * @author cyberpwn
 */
public interface NetworkedServer
{
	/**
	 * Get the bungeecord network
	 * 
	 * @return the network
	 */
	public Network getNetwork();
	
	/**
	 * Get the name of this server
	 * 
	 * @return the name of the server
	 */
	public String getName();
	
	/**
	 * Get the list of players on this remote server
	 * 
	 * @return the list of players
	 */
	public GList<String> getPlayers();
	
	/**
	 * Check if this server is a remote server or is the remote server actually
	 * this server
	 * 
	 * @return true if the server is not THIS server
	 */
	public Boolean isRemote();
	
	/**
	 * Send a player to this server
	 * 
	 * @param p
	 *            the player
	 */
	public void sendPlayer(Player p);
}
