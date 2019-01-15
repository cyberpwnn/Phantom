package com.volmit.phantom.api.intercom;

import java.io.Serializable;

public class Parcel<M extends Serializable>
{
	private M o;

	public Parcel()
	{

	}

	public Parcel(M o)
	{

	}

	public M getO()
	{
		return o;
	}

	public void setO(M o)
	{
		this.o = o;
	}
}
