package org.phantomapi.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.phantomapi.text.MessageBuilder;

/**
 * Command sender wrapper
 * 
 * @author cyberpwn
 */
public interface PhantomCommandSender extends CommandSender
{
	/**
	 * Is this sender a player?
	 * 
	 * @return true if it is
	 */
	public boolean isPlayer();
	
	/**
	 * Gets the player. Check if isPlayer() first
	 * 
	 * @return the player, or null if not a player
	 */
	public Player getPlayer();
	
	/**
	 * Is this sender a console?
	 * 
	 * @return true if its not a player
	 */
	public boolean isConsole();
	
	/**
	 * Set the message builder to override messaging
	 * 
	 * @param messageBuilder
	 *            the builder
	 */
	public void setMessageBuilder(MessageBuilder messageBuilder);
	
	/**
	 * Get the message builder to override messaging
	 * 
	 * @return the builder
	 */
	public MessageBuilder getMessageBuilder();
}
