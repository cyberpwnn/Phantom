package org.phantomapi.refract;

public class RefractException extends Exception
{
	private static final long serialVersionUID = 2688902193967547399L;

	public RefractException()
	{
		super();
	}

	public RefractException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public RefractException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public RefractException(String message)
	{
		super(message);
	}

	public RefractException(Throwable cause)
	{
		super(cause);
	}
}
