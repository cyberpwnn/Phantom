package phantom.util.exception;

public class PawnException extends PhantomException
{
	private static final long serialVersionUID = -1074288530465350926L;

	public PawnException()
	{
		super();
	}

	public PawnException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public PawnException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public PawnException(String message)
	{
		super(message);
	}

	public PawnException(Throwable cause)
	{
		super(cause);
	}
}
