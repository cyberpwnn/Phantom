package org.phantomapi.core;

import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.lang.GMap;
import org.phantomapi.lang.GSet;
import org.phantomapi.registry.Registrar;

public class RegistryController extends Controller
{
	private GMap<String, Registrar<?>> registers;
	
	public RegistryController(Controllable parentController)
	{
		super(parentController);
		
		registers = new GMap<String, Registrar<?>>();
	}
	
	@Override
	public void onStart()
	{
		
	}
	
	@Override
	public void onStop()
	{
		for(Registrar<?> i : registers.v())
		{
			i.unregisterAll();
		}
	}
	
	public void registerRegistrar(Registrar<?> registrar)
	{
		registers.put(registrar.getType(), registrar);
	}
	
	public void registerRegistrant(Controllable registrant)
	{
		for(String i : registers.k())
		{
			if(registers.get(i).isValid(registrant))
			{
				registers.get(i).register(registrant);
			}
		}
	}
	
	public void unregisterRegistrant(Controllable registrant)
	{
		for(String i : registers.k())
		{
			if(registers.get(i).isValid(registrant))
			{
				registers.get(i).unregister(registrant);
			}
		}
	}
	
	public GSet<Registrar<?>> getRegisters()
	{
		return registers.vset();
	}
}
