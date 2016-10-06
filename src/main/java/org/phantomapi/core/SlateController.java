package org.phantomapi.core;

import org.bukkit.entity.Player;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.construct.Ticked;
import org.phantomapi.lang.GMap;
import org.phantomapi.slate.Slate;

@Ticked(7)
public class SlateController extends Controller
{
	private GMap<Player, Slate> slates;
	
	public SlateController(Controllable parentController)
	{
		super(parentController);
		
		slates = new GMap<Player, Slate>();
	}

	@Override
	public void onStart()
	{
		
	}
	
	@Override
	public void onTick()
	{
		for(Player i : slates.k())
		{
			slates.get(i).update();
		}
	}

	@Override
	public void onStop()
	{
		
	}
	
	public void set(Player p, Slate slate)
	{
		slates.put(p, slate);
		slate.addViewer(p);
	}
	
	public void clear(Player p)
	{
		if(slates.containsKey(p))
		{
			slates.get(p).removeViewer(p);
		}
		
		slates.remove(p);
	}
}
