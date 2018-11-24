package com.volmit.phantom.pawn.handlers;

import com.volmit.phantom.pawn.ClassHandler;

public class LongCH implements ClassHandler<Long>
{
	@Override
	public Class<? extends Long> getHandledClass()
	{
		return Long.class;
	}

	@Override
	public String toString(Long t)
	{
		return t.toString();
	}

	@Override
	public Long fromString(String s)
	{
		return Long.valueOf(s);
	}
}
