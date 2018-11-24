package com.volmit.phantom.pawn.handlers;

import com.volmit.phantom.pawn.ClassHandler;

public class IntegerCH implements ClassHandler<Integer>
{
	@Override
	public Class<? extends Integer> getHandledClass()
	{
		return Integer.class;
	}

	@Override
	public String toString(Integer t)
	{
		return t.toString();
	}

	@Override
	public Integer fromString(String s)
	{
		return Integer.valueOf(s);
	}
}
