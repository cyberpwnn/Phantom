package phantom.util.exception;

public class PawnActivationException extends PawnException
{
	private static final long serialVersionUID = -1074288885325350926L;

	public PawnActivationException()
	{
		super();
	}

	public PawnActivationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public PawnActivationException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public PawnActivationException(String message)
	{
		super(message);
	}

	public PawnActivationException(Throwable cause)
	{
		super(cause);
	}
}
