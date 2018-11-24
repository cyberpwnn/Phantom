package com.volmit.phantom.pawn.handlers;

import com.volmit.phantom.pawn.ClassHandler;

public class StringCH implements ClassHandler<String>
{
	@Override
	public Class<? extends String> getHandledClass()
	{
		return String.class;
	}

	@Override
	public String toString(String t)
	{
		return t;
	}

	@Override
	public String fromString(String s)
	{
		return s;
	}
}
