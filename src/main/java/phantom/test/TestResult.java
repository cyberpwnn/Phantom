package phantom.test;

import org.bukkit.command.CommandSender;

import phantom.text.C;
import phantom.text.ITagProvider;
import phantom.text.TXT;

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

	public void sendResults(CommandSender sender, ITagProvider provider)
	{
		provider.msg(sender, TXT.makeTag(finisher ? (failed ? C.RED : C.GREEN) : (failed ? C.RED : C.YELLOW), C.DARK_GRAY, C.DARK_GRAY, C.GRAY, finisher ? "Finished" : "Testing") + message);
	}
}
