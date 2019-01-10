package com.volmit.phantom.api.lang;

public interface Operation extends Runnable
{
	public int getPriority();

	public String id();
}
