package org.phantomapi.phast;

import org.phantomapi.util.U;

/**
 * A Node for thrashing the server
 * 
 * @author cyberpwn
 */
public class PhastThrashNode extends PhastNode
{
	public PhastThrashNode()
	{
		super("thrash");
	}
	
	@Override
	public String phastHelp()
	{
		return "thrash - Thrashes the server";
	}
	
	@Override
	public void on(String[] args)
	{
		if(args.length == 0)
		{
			U.thrash();
		}
	}
}
