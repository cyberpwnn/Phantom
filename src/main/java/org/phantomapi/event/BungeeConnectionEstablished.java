package org.phantomapi.event;

import org.phantomapi.lang.GList;

public class BungeeConnectionEstablished extends BungeecordEvent
{
	private final String serverName;
	private final GList<String> servers;
	
	public BungeeConnectionEstablished(String serverName, GList<String> servers)
	{
		this.serverName = serverName;
		this.servers = servers;
	}

	public String getServerName()
	{
		return serverName;
	}

	public GList<String> getServers()
	{
		return servers;
	}
}
