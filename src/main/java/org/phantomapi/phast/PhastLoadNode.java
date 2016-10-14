package org.phantomapi.phast;

import org.phantomapi.util.PluginUtil;

/**
 * A Node for loading plugins
 * 
 * @author cyberpwn
 */
public class PhastLoadNode extends PhastNode
{
	public PhastLoadNode()
	{
		super("load");
	}
	
	@Override
	public String phastHelp()
	{
		return "load [plugin] - Loads a plugin";
	}
	
	@Override
	public void on(String[] args)
	{
		if(args.length == 1)
		{
			PluginUtil.load(args[0]);
		}
	}
}
