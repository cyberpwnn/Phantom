package org.phantomapi.phast;

import org.phantomapi.util.U;

/**
 * A Node for reloading the server
 * 
 * @author cyberpwn
 */
public class PhastResetNode extends PhastNode
{
	public PhastResetNode()
	{
		super("reset");
	}
	
	@Override
	public String phastHelp()
	{
		return "reset - Reloads the server";
	}
	
	@Override
	public void on(String[] args)
	{
		if(args.length == 0)
		{
			U.reload();
		}
	}
}
