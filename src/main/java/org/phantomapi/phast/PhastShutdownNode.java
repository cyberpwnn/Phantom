package org.phantomapi.phast;

import org.phantomapi.util.U;

/**
 * A Node for shutting down the server
 * 
 * @author cyberpwn
 */
public class PhastShutdownNode extends PhastNode
{
	public PhastShutdownNode()
	{
		super("shutdown");
	}
	
	@Override
	public String phastHelp()
	{
		return "shutdown - Shuts down the server";
	}
	
	@Override
	public void on(String[] args)
	{
		if(args.length == 0)
		{
			U.stop();
		}
	}
}
