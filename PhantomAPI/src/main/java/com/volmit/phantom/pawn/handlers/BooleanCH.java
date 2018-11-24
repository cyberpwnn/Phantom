package com.volmit.phantom.pawn.handlers;

import com.volmit.phantom.pawn.ClassHandler;

public class BooleanCH implements ClassHandler<Boolean>
{
	@Override
	public Class<? extends Boolean> getHandledClass()
	{
		return Boolean.class;
	}

	@Override
	public String toString(Boolean t)
	{
		return t.toString();
	}

	@Override
	public Boolean fromString(String s)
	{
		return Boolean.valueOf(s);
	}
}
