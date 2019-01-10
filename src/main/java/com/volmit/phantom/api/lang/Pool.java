package com.volmit.phantom.api.lang;

public interface Pool
{
	public void shove(Runnable op);

	public void shutDown();

	public void shutDownNow();
}
