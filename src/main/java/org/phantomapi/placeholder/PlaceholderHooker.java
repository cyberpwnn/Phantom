package org.phantomapi.placeholder;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.phantomapi.Phantom;
import org.phantomapi.nms.NMSX;
import org.phantomapi.util.F;
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
		try
		{
			if(q.equalsIgnoreCase("server_count"))
			{
				return Phantom.instance().onlinePlayers().size() + "";
			}
			
			if(q.equalsIgnoreCase("network_count"))
			{
				return (Phantom.getNetworkCount() - Phantom.instance().getWraithController().getCount()) + "";
			}
			
			if(q.equalsIgnoreCase("ping"))
			{
				return NMSX.ping(p) + "ms";
			}
			
			if(q.equalsIgnoreCase("bad_decimal"))
			{
				return "" + Math.random() * 1234.574;
			}
			
			if(q.equalsIgnoreCase("bad_percent"))
			{
				return "" + Math.random();
			}
			
			if(q.equalsIgnoreCase("bad_number"))
			{
				return "" + (int) (Math.random() * 1234534374);
			}
			
			if(q.contains("filter-") && q.contains("@") && q.split("@").length == 2)
			{
				String placeholder = q.split("@")[1];
				String result = PlaceholderUtil.handle(p, placeholder);
				Double v = Double.valueOf(result);
				
				if(q.startsWith("filter-dec") || q.startsWith("filter-per"))
				{
					Integer num = Integer.valueOf(q.split("@")[0].substring(10));
					
					if(q.startsWith("filter-dec"))
					{
						return F.f(v, num);
					}
					
					if(q.startsWith("filter-per"))
					{
						return F.pc(v, num);
					}
				}
				
				else if(q.startsWith("filter-num"))
				{
					return F.f(v);
				}
			}
			
			try
			{
				for(String i : Phantom.getServers())
				{
					if(q.equalsIgnoreCase("server_" + i.toLowerCase() + "_count"))
					{
						return Phantom.getNetworkCount(i) + "";
					}
				}
			}
			
			catch(Exception ee)
			{
				
			}
		}
		
		catch(Exception e)
		{
			
		}
		
		return Phantom.instance().getPlaceholderController().handle(p, q);
	}
}
