package org.cyberpwn.phantom.ext;

import org.bukkit.Bukkit;

/**
 * 
 * @author cyberpwn
 *
 */
public class PluginConnector implements PluginConnection
{
	protected String pluginName;
	
	public PluginConnector(String pluginName)
	{
		this.pluginName = pluginName;
	}
	
	@Override
	public String getPluginName()
	{
		return pluginName;
	}

	@Override
	public Boolean exists()
	{
		return Bukkit.getServer().getPluginManager().getPlugin(pluginName) != null;
	}
}
