package com.volmit.phantom.plugin;

public class AbstractModuleException extends Exception
{
	private static final long serialVersionUID = -8657741846546164873L;

	public AbstractModuleException(String message)
	{
		super(message);
	}

	public AbstractModuleException(String message, Throwable cause)
	{
		super(message, cause);
	}
}