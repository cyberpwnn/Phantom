package org.phantomapi.ext;

import java.util.UUID;
import org.bukkit.entity.Player;

/**
 * @author cyberpwn
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
			try
			{
				UUID id = player.getUniqueId();
				Class<?> vv = Class.forName("us.myles.ViaVersion.api.Via");
				Object api = vv.getMethod("getAPI").invoke(null);
				return (int) api.getClass().getMethod("getPlayerVersion", UUID.class).invoke(api, id);
			}
			
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		return -1;
	}
}
