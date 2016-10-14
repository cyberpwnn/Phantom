package org.phantomapi.placeholder;

import java.util.UUID;
import org.bukkit.entity.Player;
import org.phantomapi.Phantom;

public abstract class PlaceholderHook
{
	private UUID hook;
	
	public PlaceholderHook()
	{
		hook = UUID.randomUUID();
	}
	
	public void hook()
	{
		Phantom.instance().getPlaceholderController().hook(hook, this);
	}
	
	public void unhook()
	{
		Phantom.instance().getPlaceholderController().unhook(hook);
	}
	
	public abstract String onPlaceholderRequest(Player p, String q);
}
