package org.phantomapi.core;

import phantom.pawn.IPawn;
import phantom.pawn.Registered;
import phantom.pawn.Start;
import phantom.pawn.Stop;
import phantom.pawn.Tick;

@Registered
public class SomePawn implements IPawn
{
	@Start
	public void onStart()
	{
		System.out.println("PAWN STARTED");
	}
	
	@Stop
	public void onStop()
	{
		System.out.println("PAWN STOPPED");
	}
	
	@Tick(5)
	public void onTick()
	{
		System.out.println("PAWN TICK");
	}
}
