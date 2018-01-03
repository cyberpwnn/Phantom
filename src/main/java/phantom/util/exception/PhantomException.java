package phantom.util.exception;

public class PhantomException extends Exception
{
	private static final long serialVersionUID = 3014277493593740743L;

	public PhantomException()
	{
		super();
	}

	public PhantomException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public PhantomException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public PhantomException(String message)
	{
		super(message);
	}

	public PhantomException(Throwable cause)
	{
		super(cause);
	}
}
