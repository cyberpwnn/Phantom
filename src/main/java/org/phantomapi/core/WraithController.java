package org.phantomapi.core;

import org.bukkit.event.EventHandler;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.construct.Ticked;
import org.phantomapi.event.ControllerStopEvent;
import org.phantomapi.lang.GList;
import org.phantomapi.util.C;
import org.phantomapi.wraith.Wraith;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

@Ticked(5)
public class WraithController extends Controller
{
	private GList<Wraith> wraiths;
	
	public WraithController(Controllable parentController)
	{
		super(parentController);
		
		wraiths = new GList<Wraith>();
	}
	
	@Override
	public void onStart()
	{
		
	}
	
	@Override
	public void onTick()
	{
		for(Wraith i : wraiths.copy())
		{
			i.tick();
		}
	}
	
	@EventHandler
	public void on(ControllerStopEvent e)
	{
		for(Wraith i : wraiths.copy())
		{
			if(i.getBase().equals(e.getControllable()))
			{
				i.despawn();
				i.destroy();
				unRegisterWraith(i);
			}
		}
	}
	
	@Override
	public void onStop()
	{
		for(Wraith i : wraiths.copy())
		{
			i.despawn();
			i.destroy();
			unRegisterWraith(i);
		}
		
		wraiths.clear();
		
		for(NPC i : new GList<NPC>(CitizensAPI.getNPCRegistry().iterator()))
		{
			if(i.getName().endsWith(C.BLACK.toString() + "^"))
			{
				s("Destroying " + i.getName());
				i.despawn();
				i.destroy();
			}
		}
	}
	
	public void registerWraith(Wraith wraith)
	{
		wraiths.add(wraith);
	}
	
	public void unRegisterWraith(Wraith wraith)
	{
		wraiths.remove(wraith);
	}
	
	public Wraith get(int entityID)
	{
		for(Wraith i : wraiths)
		{
			if(i.getEntityId() == entityID)
			{
				return i;
			}
		}
		
		return null;
	}
	
	public int getCount()
	{
		return wraiths.size();
	}
}
