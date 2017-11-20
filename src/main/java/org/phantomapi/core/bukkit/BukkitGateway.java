package org.phantomapi.core.bukkit;

import org.bukkit.Bukkit;
import org.phantomapi.core.IGateway;

import phantom.protocol.Protocol;

public class BukkitGateway implements IGateway
{
	@Override
	public Protocol getProtocolVersion()
	{
		for(Protocol i : Protocol.values())
		{
			if(Bukkit.getVersion().startsWith(i.getVersionString()))
			{
				return i;
			}
		}

		return Protocol.UNKNOWN;
	}
}
