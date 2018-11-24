package com.volmit.phantom.pawn.handlers;

import com.volmit.phantom.pawn.ClassHandler;

public class ShortCH implements ClassHandler<Short>
{
	@Override
	public Class<? extends Short> getHandledClass()
	{
		return Short.class;
	}

	@Override
	public String toString(Short t)
	{
		return t.toString();
	}

	@Override
	public Short fromString(String s)
	{
		return Short.valueOf(s);
	}
}
