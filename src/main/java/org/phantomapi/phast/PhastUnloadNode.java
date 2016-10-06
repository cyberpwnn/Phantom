package org.phantomapi.phast;

import org.bukkit.Bukkit;
import org.phantomapi.util.PluginUtil;

/**
 * A Node for unloading plugins
 * 
 * @author cyberpwn
 */
public class PhastUnloadNode extends PhastNode
{
	public PhastUnloadNode()
	{
		super("unload");
	}
	
	@Override
	public String phastHelp()
	{
		return "unload [plugin] - Unloads a plugin";
	}
	
	@Override
	public void on(String[] args)
	{
		if(args.length == 1)
		{
			PluginUtil.unload(Bukkit.getPluginManager().getPlugin(args[0]));
		}
	}
}
