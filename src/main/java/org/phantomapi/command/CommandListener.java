package org.phantomapi.command;

import org.phantomapi.lang.GList;

/**
 * A Basic command listener
 * 
 * @author cyberpwn
 */
public interface CommandListener extends CommandMessenger
{
	/**
	 * A new command has been called
	 * 
	 * @param sender
	 *            the sender
	 * @param command
	 *            the command
	 * @return returns true if the command was handled. Returns false if the
	 *         command was not handled
	 */
	public boolean onCommand(PhantomCommandSender sender, PhantomCommand command);
	
	/**
	 * Get the command name
	 * 
	 * @return the name
	 */
	public String getCommandName();
	
	/**
	 * Get the command alias names
	 * 
	 * @return the names
	 */
	public GList<String> getCommandAliases();
}
