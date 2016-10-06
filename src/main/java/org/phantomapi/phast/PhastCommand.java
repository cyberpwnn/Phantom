package org.phantomapi.phast;

/**
 * Can be controlled by phast
 * 
 * @author cyberpwn
 */
public interface PhastCommand
{
	/**
	 * Called when a Phast script invokes a command
	 * 
	 * @param command
	 *            the command
	 * @param args
	 *            the arguments
	 */
	public void phast(String command, String[] args);
	
	/**
	 * Helpful information on what this phast command set does
	 * 
	 * @return the help information
	 */
	public String phastHelp();
}
