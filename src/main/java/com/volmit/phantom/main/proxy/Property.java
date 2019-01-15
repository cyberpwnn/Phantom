package com.volmit.phantom.main.proxy;

public interface Property
{
	public String getName();

	public String getRealm();

	public String get(String context) throws Exception;

	public void set(String context, String s) throws Exception;
}
