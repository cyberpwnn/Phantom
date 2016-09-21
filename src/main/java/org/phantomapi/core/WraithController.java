package org.phantomapi.core;

import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.lang.GList;
import org.phantomapi.wraith.Wraith;

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
	public void onStop()
	{
		for(Wraith i : wraiths.copy())
		{
			i.despawn();
			unRegisterWraith(i);
		}
		
		wraiths.clear();
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
}
