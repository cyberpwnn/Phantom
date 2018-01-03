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
public class PhantomController implements IPawn
{
	@Pawn
	private ServiceController serviceController;
	
	@Start
	public void onStart()
	{
		
	}
	
	@Stop
	public void onStop()
	{
		
	}
}
