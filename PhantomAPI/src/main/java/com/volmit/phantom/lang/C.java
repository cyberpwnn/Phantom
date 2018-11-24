package com.volmit.phantom.lang;

public class C<T>
{
	private T t;

	public C(T t)
	{
		s(t);
	}

	public C()
	{
		this(null);
	}

	public T g()
	{
		return t;
	}

	public void s(T t)
	{
		this.t = t;
	}
}
