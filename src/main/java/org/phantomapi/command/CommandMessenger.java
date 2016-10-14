package org.phantomapi.command;

import org.phantomapi.text.TagProvider;

/**
 * Command messenger
 * 
 * @author cyberpwn
 */
public interface CommandMessenger extends TagProvider
{
	/**
	 * Get message
	 * 
	 * @return the message
	 */
	public String getMessageNoPermission();
	
	/**
	 * Get message
	 * 
	 * @return the message
	 */
	public String getMessageNotPlayer();
	
	/**
	 * Get message
	 * 
	 * @return the message
	 */
	public String getMessageNotConsole();
	
	/**
	 * Get message
	 * 
	 * @return the message
	 */
	public String getMessageInvalidArgument(String arg, String neededType);
	
	/**
	 * Get message
	 * @param expectedMax 
	 * 
	 * @return the message
	 */
	public String getMessageInvalidArguments(int given, int expected, int expectedMax);
	
	/**
	 * Get message
	 * 
	 * @return the message
	 */
	public String getMessageUnknownSubCommand(String given);	
}
