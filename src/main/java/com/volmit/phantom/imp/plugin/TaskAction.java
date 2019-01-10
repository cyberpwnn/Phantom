package com.volmit.phantom.imp.plugin;

import java.lang.reflect.Method;

import com.volmit.phantom.api.sheduler.AR;
import com.volmit.phantom.api.sheduler.SR;

public class TaskAction extends ChronoAction
{
	private final int interval;
	protected SR sr;
	protected AR ar;

	public TaskAction(Method method, ActionType type, boolean async, int interval)
	{
		super(method, type, async);
		this.interval = interval;
	}

	public int getInterval()
	{
		return interval;
	}

	public SR getSr()
	{
		return sr;
	}

	public void setSr(SR sr)
	{
		this.sr = sr;
	}

	public AR getAr()
	{
		return ar;
	}

	public void setAr(AR ar)
	{
		this.ar = ar;
	}
}