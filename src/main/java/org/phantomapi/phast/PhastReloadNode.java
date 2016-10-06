package org.phantomapi.phast;

import org.bukkit.Bukkit;
import org.phantomapi.Phantom;
import org.phantomapi.util.PluginUtil;

/**
 * A Node for reloading controllers
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
		return "reload [c/plugin] - Reloads a controller or plugin";
	}
	
	@Override
	public void on(String[] args)
	{
		if(args.length == 1)
		{
			try
			{
				Phantom.instance().getController(args[0]).reload();
			}
			
			catch(Exception e)
			{
				PluginUtil.reload(Bukkit.getPluginManager().getPlugin(args[0]));
			}
		}
	}
}
