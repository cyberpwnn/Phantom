package com.volmit.phantom.imp.plugin;

import java.lang.reflect.Method;

public class Actionable
{
	public enum ActionType
	{
		START,
		TEST,
		STOP,
		TICK;
	}

	protected final Method method;
	private final ActionType type;

	public Actionable(Method method, ActionType type)
	{
		if(method == null)
		{
			throw new RuntimeException("Null method");
		}

		this.method = method;
		this.type = type;
	}

	public void invoke(Object o)
	{
		try
		{
			if(o == null)
			{
				throw new RuntimeException("Null Invoker");
			}

			method.setAccessible(true);
			method.invoke(o);
		}

		catch(Throwable e)
		{
			e.printStackTrace();
		}
	}

	public ActionType getType()
	{
		return type;
	}

	public Method getMethod()
	{
		return method;
	}
}