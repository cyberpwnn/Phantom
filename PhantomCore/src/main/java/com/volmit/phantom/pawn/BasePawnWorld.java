package com.volmit.phantom.pawn;

import com.volmit.phantom.lang.GList;

public class BasePawnWorld implements PawnWorld
{
	private GList<Pawn> pawns;
	
	@Override
	public GList<Pawn> getPawns()
	{
		return pawns;
	}

	@Override
	public void addPawn(Pawn pawn)
	{
		pawns.add(pawn);
	}

	@Override
	public void removePawn(Pawn pawn)
	{
		pawns.remove(pawn);
	}

	@Override
	public void clearPawns()
	{
		pawns.clear();
	}
}
