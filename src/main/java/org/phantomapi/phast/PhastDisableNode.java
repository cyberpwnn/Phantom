package org.phantomapi.phast;

import org.bukkit.Bukkit;
import org.phantomapi.util.PluginUtil;

/**
 * A Node for disabling plugins
 * 
 * @author cyberpwn
 */
public class PhastDisableNode extends PhastNode
{
	public PhastDisableNode()
	{
		super("disable");
	}
	
	@Override
	public String phastHelp()
	{
		return "disable [plugin] - Disables a plugin";
	}
	
	@Override
	public void on(String[] args)
	{
		if(args.length == 1)
		{
			PluginUtil.disable(Bukkit.getPluginManager().getPlugin(args[0]));
		}
	}
}
