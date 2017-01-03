package org.phantomapi.ext;

import org.phantomapi.factions.FactionConnector;

public class FactionsPluginConnector extends PluginConnector
{
	public FactionsPluginConnector()
	{
		super("Factions");
	}
	
	public FactionConnector getAPI()
	{
		return new FactionConnector(this);
	}
}
