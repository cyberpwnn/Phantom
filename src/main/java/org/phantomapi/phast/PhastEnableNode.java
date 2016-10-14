package org.phantomapi.phast;

import org.bukkit.Bukkit;
import org.phantomapi.util.PluginUtil;

/**
 * A Node for enabling plugins
 * 
 * @author cyberpwn
 */
public class PhastEnableNode extends PhastNode
{
	public PhastEnableNode()
	{
		super("enable");
	}
	
	@Override
	public String phastHelp()
	{
		return "enable [plugin] - Enables a plugin";
	}
	
	@Override
	public void on(String[] args)
	{
		if(args.length == 1)
		{
			PluginUtil.enable(Bukkit.getPluginManager().getPlugin(args[0]));
		}
	}
}
