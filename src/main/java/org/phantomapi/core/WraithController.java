package org.phantomapi.core;

import org.bukkit.entity.Entity;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.wraith.WU;
import net.citizensnpcs.api.CitizensAPI;

public class WraithController extends Controller
{
	public WraithController(Controllable parentController)
	{
		super(parentController);
	}
	
	@Override
	public void onStart()
	{
		destroyNPCs();
	}
	
	@Override
	public void onStop()
	{
		destroyNPCs();
	}
	
	public void destroyNPCs()
	{
		for(Entity i : WU.getNPCs())
		{
			CitizensAPI.getNPCRegistry().getNPC(i).destroy();
		}
		
		CitizensAPI.getNPCRegistry().deregisterAll();
	}
}
