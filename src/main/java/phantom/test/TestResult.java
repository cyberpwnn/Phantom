package phantom.test;

public class TestResult
{
	private String message;
	private boolean finisher;
	private boolean failed;

	public TestResult(String message)
	{
		this.message = message;
		this.finisher = false;
		this.failed = false;
	}

	public TestResult(String message, boolean failed)
	{
		this.message = message;
		this.finisher = false;
		this.failed = failed;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public boolean isFinisher()
	{
		return finisher;
	}

	public void setFinisher(boolean finisher)
	{
		this.finisher = finisher;
	}

	public boolean isFailed()
	{
		return failed;
	}

	public void setFailed(boolean failed)
	{
		this.failed = failed;
	}
}
