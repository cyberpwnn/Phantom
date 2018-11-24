package com.volmit.phantom.pawn.handlers;

import com.volmit.phantom.pawn.ClassHandler;

public class DoubleCH implements ClassHandler<Double>
{
	@Override
	public Class<? extends Double> getHandledClass()
	{
		return Double.class;
	}

	@Override
	public String toString(Double t)
	{
		return t.toString();
	}

	@Override
	public Double fromString(String s)
	{
		return Double.valueOf(s);
	}
}
