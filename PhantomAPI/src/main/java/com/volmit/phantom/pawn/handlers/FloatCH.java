package com.volmit.phantom.pawn.handlers;

import com.volmit.phantom.pawn.ClassHandler;

public class FloatCH implements ClassHandler<Float>
{
	@Override
	public Class<? extends Float> getHandledClass()
	{
		return Float.class;
	}

	@Override
	public String toString(Float t)
	{
		return t.toString();
	}

	@Override
	public Float fromString(String s)
	{
		return Float.valueOf(s);
	}
}
