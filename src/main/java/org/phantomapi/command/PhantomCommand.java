package org.phantomapi.command;

/**
 * A command object/event
 * 
 * @author cyberpwn
 */
public class PhantomCommand
{
	private String name;
	private final String[] args;
	
	/**
	 * Create a phantom command holder
	 * 
	 * @param name
	 *            the command
	 * @param args
	 *            the args
	 */
	public PhantomCommand(String name, String[] args)
	{
		this.name = name;
		this.args = args;
	}
	
	/**
	 * get the command name
	 * 
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * get the command args
	 * 
	 * @return the args
	 */
	public String[] getArgs()
	{
		return args;
	}
	
	/**
	 * Used for really nasty command situations
	 */
	public void setName(String name)
	{
		this.name = name;
	}
}
