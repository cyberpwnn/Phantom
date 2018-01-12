package phantom.net;

import org.phantomapi.Phantom;

public class UselessDownloadMonitor implements IDownloadMonitor
{

	@Override
	public void onDownloadFailed(Exception e)
	{
		Phantom.kick(e);
	}

	@Override
	public void onDownloadProgress(double progress, double bitsPerSecond, long downloaded, double timeLeft, long elapsed)
	{
		
	}

	@Override
	public void onDownloadFinished(long elapsed)
	{
		
	}

	@Override
	public void onDownloadStarted(long size)
	{
		
	}
}
