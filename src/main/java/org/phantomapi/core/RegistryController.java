package org.phantomapi.core;

import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.lang.GSet;
import org.phantomapi.registry.Registrant;
import org.phantomapi.registry.Registrar;

public class RegistryController extends Controller
{
	private GSet<Registrar> registers;

	public RegistryController(Controllable parentController)
	{
		super(parentController);

		registers = new GSet<Registrar>();
	}

	@Override
	public void onStart()
	{

	}

	@Override
	public void onStop()
	{

	}

	public void register(Registrant registrant)
	{
		for(Registrar i : getRegisters())
		{
			if(i.getRegistrantClass().isAssignableFrom(registrant.getClass()))
			{
				i.register(registrant);
			}
		}
	}

	public void unregister(Registrant registrant)
	{
		for(Registrar i : getRegisters())
		{
			if(i.getRegistrantClass().isAssignableFrom(registrant.getClass()))
			{
				i.unregister(registrant);
			}
		}
	}

	public GSet<Registrar> getRegisters()
	{
		return registers;
	}
}
