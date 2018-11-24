package com.volmit.phantom.pawn.handlers;

import com.volmit.phantom.pawn.ClassHandler;

public class ByteCH implements ClassHandler<Byte>
{
	@Override
	public Class<? extends Byte> getHandledClass()
	{
		return Byte.class;
	}

	@Override
	public String toString(Byte t)
	{
		return t.toString();
	}

	@Override
	public Byte fromString(String s)
	{
		return Byte.valueOf(s);
	}
}
