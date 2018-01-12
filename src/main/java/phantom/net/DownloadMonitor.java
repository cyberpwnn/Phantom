package phantom.net;

import org.phantomapi.Phantom;

/**
 * Represents a basic download monitor
 * 
 * @author cyberpwn
 */
public class DownloadMonitor implements IDownloadMonitor
{
	private double progress;
	private double bytesPerSecond;
	private double timeLeft;
	private long downloaded;
	private long elapsed;
	private long size;

	@Override
	public void onDownloadFailed(Exception e)
	{
		Phantom.kick(e);
	}

	@Override
	public void onDownloadProgress(double progress, double bytesPerSecond, long downloaded, double timeLeft, long elapsed)
	{
		this.progress = progress;
		this.bytesPerSecond = bytesPerSecond;
		this.downloaded = downloaded;
		this.timeLeft = timeLeft;
		this.elapsed = elapsed;
	}

	@Override
	public void onDownloadFinished(long elapsed)
	{
		this.elapsed = elapsed;
	}

	@Override
	public void onDownloadStarted(long size)
	{
		this.size = size;
	}

	/**
	 * Get the download progress (percent) from 0 to 1
	 * 
	 * @return the progress
	 */
	public double getProgress()
	{
		return progress;
	}

	/**
	 * Get the bytes per second rate
	 * 
	 * @return the download rate
	 */
	public double getBytesPerSecond()
	{
		return bytesPerSecond;
	}

	/**
	 * Estimated time left
	 * 
	 * @return estimated milliseconds left
	 */
	public double getTimeLeft()
	{
		return timeLeft;
	}

	/**
	 * Get the downloaded bytes
	 * 
	 * @return the size of downloaded bytes
	 */
	public long getDownloaded()
	{
		return downloaded;
	}

	/**
	 * Get the time in milliseconds elapsed
	 * 
	 * @return the elapsed time
	 */
	public long getElapsed()
	{
		return elapsed;
	}

	/**
	 * Get the total estimated download size
	 * 
	 * @return the download size
	 */
	public long getSize()
	{
		return size;
	}
}
