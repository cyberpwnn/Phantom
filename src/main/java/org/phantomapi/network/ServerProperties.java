package org.phantomapi.network;

public class ServerProperties
{
	private int maxPlayers;
	private int onlinePlayers;
	private double tps;
	private boolean whitelisted;
	
	public ServerProperties()
	{
		maxPlayers = 0;
		onlinePlayers = 0;
		tps = 0;
		whitelisted = false;
	}
	
	public int getMaxPlayers()
	{
		return maxPlayers;
	}
	
	public void setMaxPlayers(int maxPlayers)
	{
		this.maxPlayers = maxPlayers;
	}
	
	public int getOnlinePlayers()
	{
		return onlinePlayers;
	}
	
	public void setOnlinePlayers(int onlinePlayers)
	{
		this.onlinePlayers = onlinePlayers;
	}
	
	public double getTps()
	{
		return tps;
	}
	
	public void setTps(double tps)
	{
		this.tps = tps;
	}
	
	public boolean isWhitelisted()
	{
		return whitelisted;
	}
	
	public void setWhitelisted(boolean whitelisted)
	{
		this.whitelisted = whitelisted;
	}
}
