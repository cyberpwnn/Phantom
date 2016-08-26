package org.phantomapi.placeholder;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.phantomapi.Phantom;
import org.phantomapi.nms.NMSX;
import me.clip.placeholderapi.external.EZPlaceholderHook;

public class PlaceholderHooker extends EZPlaceholderHook
{
	public PlaceholderHooker(Plugin plugin, String placeholderName)
	{
		super(plugin, placeholderName);
	}

	@Override
	public String onPlaceholderRequest(Player p, String q)
	{
		if(q.equalsIgnoreCase("server_count"))
		{
			return Phantom.instance().onlinePlayers().size() + "";
		}
		
		if(q.equalsIgnoreCase("network_count"))
		{
			return Phantom.getNetworkCount() + "";
		}
		
		for(String i : Phantom.getServers())
		{
			if(q.equalsIgnoreCase("server_" + i.toLowerCase() + "_count"))
			{
				return Phantom.getNetworkCount(i) + "";
			}
		}
		
		if(q.equalsIgnoreCase("ping"))
		{
			return NMSX.ping(p) + "ms";
		}
		
		return null;
	}
}
