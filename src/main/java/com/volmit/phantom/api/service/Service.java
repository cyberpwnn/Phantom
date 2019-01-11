package com.volmit.phantom.api.service;

import com.volmit.phantom.api.lang.D;

public abstract class Service implements IService
{
	private D d;

	public Service()
	{
		this.d = new D(getClass().getSimpleName());
	}

	public void l(Object... l)
	{
		d.l(l);
	}

	public void w(Object... l)
	{
		d.w(l);
	}

	public void f(Object... l)
	{
		d.f(l);
	}
}
