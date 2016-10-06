package org.phantomapi.phast;

import org.phantomapi.util.U;

/**
 * A Node for reloading the server
 * 
 * @author cyberpwn
 */
public class PhastReloadNode extends PhastNode
{
	public PhastReloadNode()
	{
		super("reload");
	}
	
	@Override
	public String phastHelp()
	{
		return "reload - Reloads the server";
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
