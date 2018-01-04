package org.phantomapi.core;

import phantom.pawn.IPawn;
import phantom.pawn.Name;
import phantom.pawn.Pawn;
import phantom.pawn.Register;
import phantom.pawn.Singular;
import phantom.pawn.Start;
import phantom.pawn.Stop;

@Register
@Singular
@Name("Phantom Controller")
public class PhantomProvider implements IPawn
{
	@Pawn
	private ServiceProvider serviceController;

	@Start
	public void onStart()
	{

	}

	@Stop
	public void onStop()
	{

	}

	public ServiceProvider getServiceController()
	{
		return serviceController;
	}
}
