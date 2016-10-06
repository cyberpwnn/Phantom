package org.phantomapi.phast;

/**
 * A Node for waiting ticks
 * 
 * @author cyberpwn
 */
public class PhastWaitNode extends PhastNode
{
	public PhastWaitNode()
	{
		super("wait");
	}
	
	@Override
	public String phastHelp()
	{
		return "wait [ticks] - Pause Execution for ticks";
	}
	
	@Override
	public void on(String[] args)
	{
		
	}
}
