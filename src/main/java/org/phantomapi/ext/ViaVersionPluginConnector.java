package org.phantomapi.ext;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * 
 * @author cyberpwn
 *
 */
public class ViaVersionPluginConnector extends PluginConnector
{
	public ViaVersionPluginConnector()
	{
		super("ViaVersion");
	}
	
	public int getProtocol(Player player)
	{
		if(exists())
		{
			Plugin instance = Bukkit.getServer().getPluginManager().getPlugin(pluginName);
			
			try
			{
				Class<?> vv = Class.forName("us.myles.ViaVersion.ViaVersionPlugin");
				return (int) vv.getMethod("getPlayerVersion", Player.class).invoke(instance, player);
			}
			
			catch(Exception e)
			{
				
			}
		}
		
		return -1;
	}
}
