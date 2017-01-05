package org.phantomapi.core;

import org.bukkit.entity.Player;
import org.phantomapi.clust.PD;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;

public class ZenithController extends Controller
{
	public ZenithController(Controllable parentController)
	{
		super(parentController);
	}
	
	@Override
	public void onStart()
	{
		
	}
	
	@Override
	public void onStop()
	{
		
	}
	
	public boolean isZenith(Player p)
	{
		return PD.get(p).getConfiguration().contains("p.zenith") && PD.get(p).getConfiguration().getBoolean("p.zenith");
	}
	
	public void setZenith(Player p, boolean tf)
	{
		PD.get(p).getConfiguration().set("p.zenith", tf);
	}
}
