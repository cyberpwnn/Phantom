package com.volmit.phantom.plugin;

import java.io.File;
import com.volmit.phantom.lang.GList;
import com.volmit.phantom.pawn.Pawn;
import com.volmit.phantom.pawn.Start;
import com.volmit.phantom.pawn.Stop;
import com.volmit.phantom.pawn.Tick;

public class PawnManager
{
	private GList<Pawn> pawns;
	private GList<Pawn> insert;
	
	public PawnManager()
	{
		pawns = new GList<Pawn>();
		insert = new GList<Pawn>();
	}
	
	public void start()
	{
		
	}
	
	public void tick()
	{
		while(!insert.isEmpty())
		{
			Phantom.TASK_MANAGER.sync(() -> insert.pop().invoke(Start.class));
		}

		
	}
	
	public void stop()
	{
		
	}

	public void insert(Pawn pawn)
	{
		insert.add(pawn);
	}
}
