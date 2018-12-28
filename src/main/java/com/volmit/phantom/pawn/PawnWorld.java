package com.volmit.phantom.pawn;

import com.volmit.phantom.lang.GList;

public interface PawnWorld
{
	public GList<Pawn> getPawns();
	
	public void addPawn(Pawn pawn);
	
	public void removePawn(Pawn pawn);
	
	public void clearPawns();
}
