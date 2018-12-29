package com.volmit.phantom.lang;

public interface Operation extends Runnable
{
	public int getPriority();

	public String id();
}
