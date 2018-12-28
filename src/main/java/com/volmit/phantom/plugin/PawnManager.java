package com.volmit.phantom.plugin;

import com.volmit.phantom.lang.GList;
import com.volmit.phantom.pawn.Pawn;
import com.volmit.phantom.pawn.Start;
import com.volmit.phantom.pawn.Stop;
import com.volmit.phantom.pawn.Tick;

public class PawnManager
{
	private GList<Pawn> pawns;
	private GList<Pawn> ticked;
	private GList<Pawn> insert;
	
	public PawnManager()
	{
		pawns = new GList<Pawn>();
		ticked = new GList<Pawn>();
		insert = new GList<Pawn>();
	}
	
	public void start()
	{
		
	}
	
	public void tick()
	{
		while(!insert.isEmpty())
		{
			Pawn p = insert.pop();
			
			if(p.isStartable())
			{
				p.invoke(Start.class);
			}
			
			pawns.add(p);
			
			if(p.isTicked())
			{
				ticked.add(p);
			}
		}

		for(Pawn i : ticked)
		{
			i.invoke(Tick.class);
		}
	}
	
	public void stop()
	{
		for(Pawn i : pawns)
		{
			if(i.isStoppable())
			{
				i.invoke(Stop.class);
			}
		}
	}

	public void insert(Pawn pawn)
	{
		insert.add(pawn);
	}
}
