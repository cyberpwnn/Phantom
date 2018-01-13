package org.phantomapi.pluginadapter;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import phantom.pawn.Name;
import phantom.pawn.Singular;
import phantom.pluginlink.PluginLink;

@Singular
@Name("LINK ProtocolLib")
public class ProtocolLibAdapter extends PluginLink
{
	private ProtocolManager manager;
	
	public ProtocolLibAdapter()
	{
		manager = ProtocolLibrary.getProtocolManager();
	}
	
	public ProtocolManager getManager()
	{
		return manager;
	}
}
