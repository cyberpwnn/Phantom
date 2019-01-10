package com.volmit.phantom.imp.plugin;

import java.lang.reflect.Method;

public class ChronoAction extends Actionable
{
	private final boolean async;

	public ChronoAction(Method method, ActionType type, boolean async)
	{
		super(method, type);
		this.async = async;
	}

	public boolean isAsync()
	{
		return async;
	}
}