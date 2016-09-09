package org.phantomapi;

import java.util.UUID;
import org.bukkit.entity.Player;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.lang.GMap;
import org.phantomapi.placeholder.PlaceholderHook;

public class PlaceholderController extends Controller
{
	private GMap<UUID, PlaceholderHook> hooks;
	
	public PlaceholderController(Controllable parentController)
	{
		super(parentController);
		
		hooks = new GMap<UUID, PlaceholderHook>();
	}

	@Override
	public void onStart()
	{
		
	}

	@Override
	public void onStop()
	{
		
	}
	
	public void unhook(UUID uuid)
	{
		hooks.remove(uuid);
	}
	
	public void hook(UUID uuid, PlaceholderHook hook)
	{
		hooks.put(uuid, hook);
	}
	
	public String handle(Player p, String q)
	{
		for(UUID i : hooks.k())
		{
			try
			{
				String v = hooks.get(i).onPlaceholderRequest(p, q);
				
				if(v != null)
				{
					return v;
				}
			}
			
			catch(Exception e)
			{
				w("Warning: Exception on placeholder request: " + q);
				e.printStackTrace();
			}
		}
		
		return null;
	}
}
