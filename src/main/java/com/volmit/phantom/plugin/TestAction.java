package com.volmit.phantom.plugin;

import java.lang.reflect.Method;

public class TestAction extends ChronoAction
{
	public TestType type;

	public TestAction(Method method, ActionType type, boolean async, TestType testType)
	{
		super(method, type, async);
		this.type = testType;
	}

	public void invoke(Object o, Object v)
	{
		try
		{
			if(o == null)
			{
				throw new RuntimeException("Null Invoker");
			}

			method.setAccessible(true);
			method.invoke(o, v);
		}

		catch(Throwable e)
		{
			e.printStackTrace();
		}
	}
}
