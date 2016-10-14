package org.phantomapi.phast;

/**
 * Nodes for helping commands
 * 
 * @author cyberpwn
 */
public abstract class PhastNode implements PhastCommand
{
	protected String node;
	
	/**
	 * Create a phast node
	 * 
	 * @param node
	 *            the node
	 */
	public PhastNode(String node)
	{
		this.node = node;
	}
	
	@Override
	public void phast(String command, String[] args)
	{
		if(command.equalsIgnoreCase(node))
		{
			on(args);
		}
	}
	
	public abstract void on(String[] args);
}
