package org.phantomapi.core;

import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.ctn.Subroutine;
import org.phantomapi.lang.GSet;

public class CTNController extends Controller
{
	private GSet<Subroutine> subroutines;
	
	public CTNController(Controllable parentController)
	{
		super(parentController);
		
		subroutines = new GSet<Subroutine>();
	}
	
	public boolean isSubroutineRegistered(Subroutine s)
	{
		return subroutines.contains(s);
	}
	
	public void registerSubroutine(Subroutine s)
	{
		subroutines.add(s);
	}
	
	public void unregisterSubroutine(Subroutine s)
	{
		subroutines.remove(s);
	}
	
	@Override
	public void onStart()
	{
		
	}
	
	@Override
	public void onStop()
	{
		
	}
}
